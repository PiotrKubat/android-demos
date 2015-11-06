package com.kubat.piotr.pogodynka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kubat.piotr.pogodynka.ccc.City;
import com.kubat.piotr.pogodynka.ccc.Continent;
import com.kubat.piotr.pogodynka.ccc.Country;
import com.kubat.piotr.pogodynka.ccc.ExpandableModel;
import com.kubat.piotr.pogodynka.ccc.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by piotrk on 26.10.15.
 */
public class MultiLevelAdapter extends BaseAdapter {

    // lista zawierająca wszystkie widoczne elementy ma liście
    private List<Model> _contentList;

    private LayoutInflater layoutInflater;

    public MultiLevelAdapter(Context context, Continent[] list) {
        super();
        _contentList = new ArrayList<Model>();
        _contentList.addAll(Arrays.asList(list));
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return _contentList.size();
    }

    @Override
    public Object getItem(int i) {
        if(i < 0 || i > (_contentList.size() - 1))
            throw new IndexOutOfBoundsException("indeks poza zakresem");
        return _contentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder vh = null;
        if(convertView == null) {
            vh = new ViewHolder(); // obiekt przechowujący referencje do elementów listview
            convertView = layoutInflater.inflate(R.layout.info_item, null); // pobranie custom view do wyświetlenia elementu listy
            vh.txt_info = (TextView)convertView.findViewById(R.id.txt_info);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }

        final Model item = _contentList.get(i); // pobranie elemntu listy

        vh.txt_info.setText(item.getName());
        vh.txt_info.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        // ustawiebia wizualne elementów listview
        if(item instanceof Continent) {
            vh.txt_info.setPadding(10, 10,10,10);
            vh.txt_info.setBackgroundColor(convertView.getResources().getColor(R.color.level1));

        }
        if(item instanceof Country) {
            vh.txt_info.setPadding(40, 10,10,10);
            vh.txt_info.setBackgroundColor(convertView.getResources().getColor(R.color.level2));

        }
        if(item instanceof City) {
            vh.txt_info.setPadding(70, 10,10,10);
            vh.txt_info.setBackgroundColor(convertView.getResources().getColor(R.color.level3));
        }
        // dla elementów zawierających podlisty ustawiamy ikonę oraz obsługę pokazania/chowania podlisty
        if(item instanceof ExpandableModel) {
            boolean isExpanded = ((ExpandableModel) item).isExpanded();
            if(isExpanded) {
                vh.txt_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_expand_less_white_24dp, 0, 0, 0);
            } else {
                vh.txt_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_expand_more_white_24dp, 0, 0, 0);
            }
            //przy kliknięciu na element listy pokazujemy/chowamy podlistę
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item instanceof Continent) {
                        Continent c = (Continent) item;
                        if (!c.isExpanded()) {
                            for (int it = 0; it < c.getCountries().length; it++) {
                                _contentList.add(i + it + 1, c.getCountries()[it]);
                            }
                            c.setExpanded(true);
                        } else {
                            for (int it = 0; it < c.getCountries().length; it++) {
                                Country ct = (Country) c.getCountries()[it];
                                if (ct.isExpanded()) {
                                    for (int itc = 0; itc < ct.getCities().length; itc++) {

                                        _contentList.remove(ct.getCities()[itc]);
                                    }
                                    ct.setExpanded(false);
                                }
                                _contentList.remove(ct);
                            }
                            c.setExpanded(false);
                        }
                        notifyDataSetChanged(); // odświeżenie adaptera
                    } else if (item instanceof Country) {
                        Country c = (Country) item;
                        if (!c.isExpanded()) {
                            for (int it = 0; it < c.getCities().length; it++) {
                                _contentList.add(i + it + 1, c.getCities()[it]);
                            }
                            c.setExpanded(true);
                        } else {
                            for (int it = 0; it < c.getCities().length; it++) {
                                _contentList.remove(i + 1);
                            }
                            c.setExpanded(false);
                        }
                        notifyDataSetChanged(); // odświeżenie adaptera
                    }
                }
            });
        } else {
            // dla miast wywołujemy zdarzenie wyboru miasta przekazując w nim referencje do niego
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onModelClickedListener != null) {
                        onModelClickedListener.onModelClicked((Model) item);
                    }
                }
            });
        }
        return convertView;
    }

    private OnModelClickedListener onModelClickedListener; // referencja do listenera zdarzenia wyboru miasta

    public void setOnModelClicked(OnModelClickedListener onModelClickedListener) {
        this.onModelClickedListener = onModelClickedListener;
    }

    public interface OnModelClickedListener {

        void onModelClicked(Model model);

    }

    // klasa do przechowywania referencji do elementów widoku elementu listy
    // wykorzystywana aby zwiększyć wydajność renderowania całej listy (unikamy wywoływania funkcji findViewById()
    static class ViewHolder {
        public TextView txt_info = null;
    }
}
