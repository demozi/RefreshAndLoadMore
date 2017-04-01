package com.demozi.refreshandloadmore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demozi.refreshandloadmore.base.BasePtrLoadMoreListAdapter;
import com.demozi.refreshandloadmore.entity.StoreItem;

/**
 * Created by wujian on 2017/3/29.
 */

public class AppAdapter extends BasePtrLoadMoreListAdapter<StoreItem.DatasBean> {


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appinfo_list, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        StoreItem.DatasBean item = mDataList.get(position);

        viewHolder.title_txt.setText(item.store_name);

        return convertView;
    }



    static class ViewHolder {

        TextView title_txt;

        public ViewHolder(View view) {
            title_txt = (TextView) view.findViewById(R.id.title_txt);
        }
    }

}
