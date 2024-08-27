package com.example.myapplication.datacomplex;

import android.content.Context;

import com.example.myapplication.DataActivity;
import com.example.myapplication.R;
import com.example.myapplication.Utils;
import com.example.myapplication.data.BaseItem;
import com.example.myapplication.data.GroupItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataProviderComplex {

    private static List<BaseItemComplex> mItems;
    private static DataProviderComplex instance;
    private static boolean mWithSelf = false;
    private static Context mContext;

    DataProviderComplex(Context c, boolean withSelf) {
        mContext = c;
        mWithSelf = withSelf;
        initialize(c, withSelf);
    }

    public static DataProviderComplex getInstance() {
        return instance;
    }

    public static DataProviderComplex getInstance(boolean withSelf) {
        return getInstance(mContext, withSelf);
    }

    public static DataProviderComplex getInstance(Context c) {
        return getInstance(c, false);
    }

    public static DataProviderComplex getInstance(Context c, boolean withSelf) {
        if (instance == null || withSelf != mWithSelf) {
            instance = new DataProviderComplex(c, withSelf);
        }
        return instance;
    }


    private void initialize(Context c, boolean withSelf) {
        String jsonStringList = Utils.loadRaw(c, R.raw.data);

        try {
            JSONArray jsonArrayStringList = new JSONArray(jsonStringList);
            mItems = parseJSONArray(jsonArrayStringList, withSelf);
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }

    }
    private void initialize(Context c) {
        initialize(c, false);
    }

    private List<BaseItemComplex> parseJSONArray(JSONArray jsonArray, boolean withSelf) {
        int length = jsonArray.length();
        List<BaseItemComplex> items = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            try {
                JSONObject itemObject = jsonArray.getJSONObject(i);
                BaseItemComplex item = new BaseItemComplex(itemObject.getString("title"), itemObject.getDouble("value"));

                if (itemObject.has("childrens")) {
                    item.setChildren(parseJSONArray(itemObject.getJSONArray("childrens"), withSelf));
                    if (withSelf && item.hasChildren()) {
                            item.addChildren(0,(new BaseItemComplex("_"+item.getName(), item.getValue())));
                        item.setValue(0.0);
                        item.setValue(item.getValueWithChildrens());
                    }
                }

                items.add(item);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return items;
    }

    public static List<BaseItemComplex> getSubItems(BaseItemComplex item) {
        return item.getChildren();
    }


    public static List<BaseItemComplex> getSubItems() {
        return mItems;
    }


    public static boolean isExpandable(BaseItemComplex item) {
        return item.hasChildren();
    }

}
