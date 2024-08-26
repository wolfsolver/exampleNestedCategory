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

    DataProviderComplex(Context c) {
        initialize(c);
    }

    public static DataProviderComplex getInstance() {
        return instance;
    }

    public static DataProviderComplex getInstance(Context c) {
        if (instance == null ) {
            instance = new DataProviderComplex(c);
        }
        return instance;
    }

    private void initialize(Context c) {
        String jsonStringList = Utils.loadRaw(c, R.raw.data);

        try {
            JSONArray jsonArrayStringList = new JSONArray(jsonStringList);
            mItems = parseJSONArray(jsonArrayStringList);
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }

    }

    private List<BaseItemComplex> parseJSONArray(JSONArray jsonArray) {
        int length = jsonArray.length();
        List<BaseItemComplex> items = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            try {
                JSONObject itemObject = jsonArray.getJSONObject(i);
                BaseItemComplex item = new BaseItemComplex(itemObject.getString("title"), itemObject.getInt("value"));

                if (itemObject.has("childrens")) {
                    item.setChildren(parseJSONArray(itemObject.getJSONArray("childrens")));
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
