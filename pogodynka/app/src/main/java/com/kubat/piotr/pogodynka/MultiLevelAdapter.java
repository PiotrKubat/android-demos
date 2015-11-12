package com.kubat.piotr.pogodynka;

import android.content.Context;
import android.graphics.AvoidXfermode;
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
public class MultiLevelAdapter extends BaseAdapter {

    // lista zawierająca wszystkie widoczne elementy ma liście
    private List<ModelNode> contentList;

    private LayoutInflater layoutInflater;

    public MultiLevelAdapter(Context context, Continent[] list) {
        super();
        layoutInflater = LayoutInflater.from(context);
        init(list);

    }

    private void init(Continent[] list) {
        contentList = new ArrayList<ModelNode>();
        for (Continent c: list) {
            ModelNode node = convertToModelNode(c);
            contentList.add(node);
        }
    }

    private ModelNode convertToModelNode(Model model) {
        if(model == null) return null;
        ModelNode node = new ModelNode();
        node.setName(model.getName());
        node.setTag(model);
        node.setIsExpandable(model instanceof ExpandableModel);

        if(model instanceof Continent) {
            Continent c = (Continent) model;
            for (Country ct: c.getCountries()) {
                node.addChildNode(convertToModelNode(ct));
            }
        }
        else if(model instanceof Country) {
            Country c = (Country) model;
            for (City ct: c.getCities()) {
                node.addChildNode(convertToModelNode(ct));
            }
        }

        return node;
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

        final ModelNode item = contentList.get(i); // pobranie elemntu listy

        vh.txt_info.setText(item.getName());
        vh.txt_info.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        if(item.isExpandable()) {
            boolean isExpanded = item.isExpanded();
            if (isExpanded) {
                vh.txt_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_expand_less_white_24dp, 0, 0, 0);
            } else {
                vh.txt_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_expand_more_white_24dp, 0, 0, 0);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!item.isExpanded()) {
                        expandNode(item);
                    } else {
                        collapseNode(item);
                    }
                    notifyDataSetChanged(); // odświeżenie adaptera
                }
            });
        }

        final Model model = (Model)item.getTag();

        if(model instanceof Continent) {
            return getContinentView(convertView);
        }
        if(model instanceof Country) {
            return getCountryView(convertView);
        }
        if(model instanceof City) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onModelClickedListener != null) {
                        onModelClickedListener.onModelClicked(model);
                    }
                }
            });
            return getCityView(convertView);
        }

        return convertView;
    }

    private View getContinentView(final View view) {
        ViewHolder vh = (ViewHolder)view.getTag();
        vh.txt_info.setPadding(10, 10, 10, 10);
        vh.txt_info.setBackgroundColor(view.getResources().getColor(R.color.level1));
        return  view;
    }

    private void expandNode(ModelNode node) {
        if(!node.isExpandable()) return;

        if(node.getParent() == null) {
            collapseAll();
        } else {
            collapseAllSubNodes(node.getParent());
        }

        int i = contentList.indexOf(node);
        for (int it = 0; it < node.size(); it++) {
            contentList.add(i + it + 1, node.getChildNodes().get(it));
        }
        node.setIsExpanded(true);
    }

    private void collapseNode(ModelNode node) {
        if(!node.isExpandable()) return;

        for (int it = 0; it < node.size(); it++) {
            ModelNode subNode = node.getChildNodes().get(it);
            collapseNode(subNode);
            contentList.remove(subNode);
        }
        node.setIsExpanded(false);
    }

    private void collapseAll() {
        List<ModelNode> nodesToCollapse = new ArrayList<ModelNode>();
        for (ModelNode node: contentList) {
            if(node.isExpanded() && node.getParent() == null) {

                nodesToCollapse.add(node);
            }
        }
        for (ModelNode node: nodesToCollapse) {
            collapseNode(node);
        }
    }

    private void collapseAllSubNodes(ModelNode parentNode) {
        for (ModelNode node: parentNode.getChildNodes()) {
            if(node.isExpanded()) {
                collapseNode(node);
            }
        }
    }

    private View getCountryView(final View view) {
        ViewHolder vh = (ViewHolder)view.getTag();
        vh.txt_info.setPadding(40, 10, 10, 10);
        vh.txt_info.setBackgroundColor(view.getResources().getColor(R.color.level2));
        return  view;
    }

    private View getCityView(final View view) {
        ViewHolder vh = (ViewHolder)view.getTag();
        vh.txt_info.setPadding(120, 10, 10, 10);
        vh.txt_info.setBackgroundColor(view.getResources().getColor(R.color.level3));
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

    private class ModelNode {

        private ModelNode parent;

        private List<ModelNode> childNodes;

        private String name;

        private Object tag;

        private boolean isExpanded;

        public ModelNode getParent() {
            return parent;
        }

        public void setParent(ModelNode parent) {
            this.parent = parent;
        }

        public List<ModelNode> getChildNodes() {
            return childNodes;
        }

        public void addChildNode(ModelNode childNode) {
            if(childNodes == null)
                childNodes = new ArrayList<ModelNode>();
            childNode.setParent(this);
            this.childNodes.add(childNode);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setIsExpanded(boolean isExpanded) {
            this.isExpanded = isExpanded;
        }

        private boolean isExpandable;

        public boolean isExpandable() {
            return isExpandable;
        }

        public void setIsExpandable(boolean isExpandable) {
            this.isExpandable = isExpandable;
        }

        public int size() {
            return childNodes != null ? childNodes.size() : 0;
        }
    }
}
