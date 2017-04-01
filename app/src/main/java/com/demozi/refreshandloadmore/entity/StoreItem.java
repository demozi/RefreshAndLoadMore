package com.demozi.refreshandloadmore.entity;

import java.util.List;

/**
 * Created by wujian on 2017/3/30.
 */

public class StoreItem extends SimpleResult {
    public List<DatasBean> datas;

    public static class DatasBean {
        public String store_id;
        public String store_name;
        public String store_avatar;
        public String city_name;
        public String district_name;
        public String lng;
        public String lat;
        public String store_credit;
        public String whole_discount;
        public NoteInfoBean note_info;
        public String store_address;
        public String per_consumption;
        public String store_commis_rates;
        public String zh_pay_status;
        public String is_agg;
        public String distance;
        public String dis_goods_count;
        public String evaluate_count;
        public String class_name;
        public MjActivityBean mj_activity;
        public List<GoodsInfo> goods_info;

        public static class NoteInfoBean {
            public String is_wifi;
            public String is_stopcart;
            public String is_cash;
        }

        public static class MjActivityBean {
            public boolean first_order;
            public boolean give;
            public boolean sub;
        }

        public static class GoodsInfo {
            public String id;
            public String goods_image;
            public String goods_name;
            public String goods_price;
            public String goods_marketprice;
            public String goods_salenum;
            public String store_name;
            public String limit_count;
            public String limit_storage;
            public String goods_storage;
            public String evaluate_count;
            public String rebate;
        }
    }

    /*public List<DataEntity> data;

    public static class DataEntity {

        public ArrayList<GoodsInfo> goods_info;

    }

    public static class GoodsInfo {
        public String id;
        public String goods_image;
        public String goods_name;
        public String goods_price;
        public String goods_marketprice;
        public String goods_salenum;
        public String store_name;
        public String limit_count;
        public String limit_storage;
        public String goods_storage;
        public String evaluate_count;
        public String rebate;
    }*/


}
