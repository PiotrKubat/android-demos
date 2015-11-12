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
import java.util.Objects;

/**
 * Created by piotrk on 26.10.15.
 */
public class ContinentCountryCityAdapter extends BaseAdapter {

    // lista zawierająca wszystkie widoczne elementy ma liście
    private List<Model> contentList;

    private LayoutInflater layoutInflater;

    public ContinentCountryCityAdapter(Context context, Continent[] list) {
        super();
        contentList = new ArrayList<Model>();
        contentList.addAll(Arrays.asList(list));
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return contentList.size();
    }

    @Override
    public Object getItem(int i) {
        if(i < 0 || i > (contentList.size() - 1))
            throw new IndexOutOfBoundsException("indeks poza zakresem");
        return contentList.get(i);
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

        final Model item = contentList.get(i); // pobranie elemntu listy

        vh.txt_info.setText(item.getName());
        vh.txt_info.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        if(item instanceof ExpandableModel) {
            boolean isExpanded = ((ExpandableModel) item).isExpanded();
            if (isExpanded) {
                vh.txt_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_expand_less_white_24dp, 0, 0, 0);
            } else {
                vh.txt_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_expand_more_white_24dp, 0, 0, 0);
            }
        }

        if(item instanceof Continent) {
            return getContinentView(convertView, (Continent)item);
        }
        if(item instanceof Country) {
            return getCountryView(convertView, (Country) item);
        }
        if(item instanceof City) {
            return getCityView(convertView, (City)item);
        }
        return convertView;
    }

    private View getContinentView(final View view, final Continent continent) {
        ViewHolder vh = (ViewHolder)view.getTag();
        vh.txt_info.setPadding(10, 10,10,10);
        vh.txt_info.setBackgroundColor(view.getResources().getColor(R.color.level1));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!continent.isExpanded()) {
                    expandContinent(continent);
                } else {
                    collapseContinent(continent);
                }
                notifyDataSetChanged(); // odświeżenie adaptera
            }
        });

        return  view;
    }

    private void expandContinent(Continent continent) {
        int i = contentList.indexOf(continent);
        for (int it = 0; it < continent.getCountries().length; it++) {
            contentList.add(i + it + 1, continent.getCountries()[it]);
        }
        continent.setExpanded(true);
    }

    private void collapseContinent(Continent continent) {
        for (int it = 0; it < continent.getCountries().length; it++) {
            Country ct = (Country) continent.getCountries()[it];
            collapseCountry(ct);
            contentList.remove(ct);
        }
        continent.setExpanded(false);
    }

    private View getCountryView(final View view, final Country country) {
        ViewHolder vh = (ViewHolder)view.getTag();
        vh.txt_info.setPadding(40, 10,10,10);
        vh.txt_info.setBackgroundColor(view.getResources().getColor(R.color.level2));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!country.isExpanded()) {
                    expandCountry(country);
                } else {
                    collapseCountry(country);
                }
                notifyDataSetChanged(); // odświeżenie adaptera
            }

        });
        return  view;
    }

    private void expandCountry(Country country) {
        int i = contentList.indexOf(country);
        for (int it = 0; it < country.getCities().length; it++) {
            contentList.add(i + it + 1, country.getCities()[it]);
        }
        country.setExpanded(true);
    }

    private void collapseCountry(Country country) {
        int i = contentList.indexOf(country);
        for (int it = 0; it < country.getCities().length; it++) {
            contentList.remove(country.getCities()[it]);
        }
        country.setExpanded(false);
    }

    private View getCityView(final View view, final City city) {
        ViewHolder vh = (ViewHolder)view.getTag();
        vh.txt_info.setPadding(70, 10,10,10);
        vh.txt_info.setBackgroundColor(view.getResources().getColor(R.color.level3));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onModelClickedListener != null) {
                    onModelClickedListener.onModelClicked((Model) city);
                }
            }
        });
        return  view;
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
