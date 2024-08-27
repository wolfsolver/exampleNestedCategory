/******************************************************************************
 *
 *  2016 (C) Copyright Open-RnD Sp. z o.o.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.datacomplex.BaseItemComplex;
import com.example.myapplication.datacomplex.DataProviderComplex;
import com.example.myapplication.multilevellistview.ItemInfo;
import com.example.myapplication.multilevellistview.MultiLevelListAdapter;
import com.example.myapplication.multilevellistview.MultiLevelListView;
import com.example.myapplication.multilevellistview.NestType;
import com.example.myapplication.multilevellistview.OnItemClickListener;
import com.example.myapplication.views.LevelBeamView;

import java.text.NumberFormat;
import java.util.List;

public class DataActivityComplex extends Activity {

    private MultiLevelListView mListView;
    private Switch mReportMode;
    private Switch mApplicationMode;

//    private boolean mAlwaysExpandend;

    static private Context mContext;
    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplication();

        DataProviderComplex.getInstance(getContext());

        confViews();
    }

    private void confViews() {
        setContentView(R.layout.data_activity);

        mListView = (MultiLevelListView) findViewById(R.id.listView);
        mReportMode = (Switch) findViewById(R.id.reportMode);
        mApplicationMode = (Switch) findViewById(R.id.applicationMode);

        ListAdapter listAdapter = new ListAdapter();

        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);

        mReportMode.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mApplicationMode.setOnCheckedChangeListener(mOnCheckedChangeListener);

        listAdapter.setDataItems(DataProviderComplex.getInstance(mApplicationMode.isChecked()).getSubItems());
    }

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == R.id.reportMode) {
                mReportMode.setChecked(isChecked);
                mListView.notifyDataSetChanged();
            } else if (buttonView.getId() == R.id.applicationMode) {
                if (mApplicationMode.isChecked()) {
                    mApplicationMode.setText("Simplified mode");
                } else {
                    mApplicationMode.setText("Normal mode");
                }
                ListAdapter listAdapter = new ListAdapter();
                mListView.setAdapter(listAdapter);
                listAdapter.setDataItems(DataProviderComplex.getInstance(mApplicationMode.isChecked()).getSubItems());
                mApplicationMode.setChecked(isChecked);
                mListView.notifyDataSetChanged();
            }
        }
    };


    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        private void showItemDescription(Object object, ItemInfo itemInfo) {
            StringBuilder builder = new StringBuilder("\"");
            builder.append(((BaseItemComplex) object).getName());
            builder.append("\" clicked!\n");
            builder.append(getItemInfoDsc(itemInfo));

            Toast.makeText(DataActivityComplex.this, builder.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }

        @Override
        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
//            showItemDescription(item, itemInfo);
        }
    };

    private class ListAdapter extends MultiLevelListAdapter {

        private class ViewHolder {
            TextView nameView;
            TextView infoView;
            ImageView arrowView;
            LevelBeamView levelBeamView;
            TextView valueView;
            ImageView selectView;
        }

        @Override
        public List<?> getSubObjects(Object object) {
            List<BaseItemComplex> list = DataProviderComplex.getInstance().getSubItems((BaseItemComplex) object);
            return list;
        }

        @Override
        protected Object getParent(Object object) {
            assert 1 == 1;
            return null;
        }

        @Override
        protected View getViewForObject(Object object, View convertView, ItemInfo itemInfo, int pos) {
            return getViewForObject(object, convertView, itemInfo);
        }

        @Override
        public boolean isExpandable(Object object) {
            return ((BaseItemComplex) object).hasChildren();
//            return DataProviderComplex.isExpandable((BaseItemComplex) object);
        }

        public View getViewForObject(Object object, View convertView, ItemInfo itemInfo) {
            BaseItemComplex item = (BaseItemComplex) object;
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(DataActivityComplex.this).inflate(R.layout.data_item, null);
                viewHolder.infoView = (TextView) convertView.findViewById(R.id.dataItemInfo);
                viewHolder.nameView = (TextView) convertView.findViewById(R.id.dataItemName);
                viewHolder.arrowView = (ImageView) convertView.findViewById(R.id.dataItemArrow);
                viewHolder.levelBeamView = (LevelBeamView) convertView.findViewById(R.id.dataItemLevelBeam);
                viewHolder.valueView = (TextView) convertView.findViewById(R.id.dataItemValue);
                viewHolder.selectView = (ImageView) convertView.findViewById(R.id.dataItemSelect);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.nameView.setText(item.getName());
            viewHolder.infoView.setText(getItemInfoDsc(itemInfo));

            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String moneyString = formatter.format(item.getValue());
            viewHolder.valueView.setText(moneyString);

            viewHolder.selectView.setTag(R.string.tag_node,item);
            viewHolder.selectView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    BaseItemComplex item = (BaseItemComplex) v.getTag(R.string.tag_node);
                    Toast.makeText(DataActivityComplex.this, "Item Select"+item.getName(), Toast.LENGTH_SHORT).show();
                }
            });

            if (itemInfo.isExpandable()) {
                viewHolder.arrowView.setVisibility(View.VISIBLE);
                viewHolder.arrowView.setImageResource(itemInfo.isExpanded() ?
                        R.drawable.arrow_up : R.drawable.arrow_down);
            } else {
                viewHolder.arrowView.setVisibility(View.GONE);
                viewHolder.selectView.setVisibility(View.GONE);
            }

            viewHolder.levelBeamView.setLevel(itemInfo.getLevel());

            if (mReportMode.isChecked())  {
                viewHolder.valueView.setVisibility(View.VISIBLE);
                viewHolder.selectView.setVisibility(View.GONE);
            } else {
                viewHolder.valueView.setVisibility(View.GONE);
                if (mApplicationMode.isChecked()) {
                    viewHolder.selectView.setVisibility(View.GONE);
                } else if (itemInfo.isExpandable()) {
                    viewHolder.selectView.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.selectView.setVisibility(View.GONE);
                }
            }

            return convertView;
        }
    }

    private String getItemInfoDsc(ItemInfo itemInfo) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("level[%d], idx in level[%d/%d]",
                itemInfo.getLevel() + 1, /*Indexing starts from 0*/
                itemInfo.getIdxInLevel() + 1 /*Indexing starts from 0*/,
                itemInfo.getLevelSize()));

        if (itemInfo.isExpandable()) {
            builder.append(String.format(", expanded[%b]", itemInfo.isExpanded()));
        }
        return builder.toString();
    }
}