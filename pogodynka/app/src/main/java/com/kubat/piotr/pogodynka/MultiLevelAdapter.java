package com.kubat.piotr.pogodynka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.kubat.piotr.pogodynka.R;
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
            vh = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.info_item, null);
            vh.txt_info = (TextView)convertView.findViewById(R.id.txt_info);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }

        final Model item = _contentList.get(i);

        vh.txt_info.setText(item.getName());
        vh.txt_info.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        if(item instanceof Continent) {
            vh.txt_info.setPadding(10, 10,10,10);
            vh.txt_info.setBackgroundColor(convertView.getResources().getColor(R.color.c1));

        }
        if(item instanceof Country) {
            vh.txt_info.setPadding(40, 10,10,10);
            vh.txt_info.setBackgroundColor(convertView.getResources().getColor(R.color.c2));

        }
        if(item instanceof City) {
            vh.txt_info.setPadding(70, 10,10,10);
            vh.txt_info.setBackgroundColor(convertView.getResources().getColor(R.color.c3));
        }
        if(item instanceof ExpandableModel) {
            boolean isExpanded = ((ExpandableModel) item).isExpanded();
            if(isExpanded) {
                vh.txt_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_expand_less_white_24dp, 0, 0, 0);
            } else {
                vh.txt_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_expand_more_white_24dp, 0, 0, 0);
            }
        }
        if(item instanceof ExpandableModel) {
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
                        notifyDataSetChanged();
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
                        notifyDataSetChanged();
                    }
                }
            });
        } else if(item instanceof Model) {
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

    private OnModelClickedListener onModelClickedListener;

    public void setOnModelClicked(OnModelClickedListener onModelClickedListener) {
        this.onModelClickedListener = onModelClickedListener;
    }

    public interface OnModelClickedListener {

        void onModelClicked(Model model);

    }

    static class ViewHolder {
        public TextView txt_info = null;
    }
}
