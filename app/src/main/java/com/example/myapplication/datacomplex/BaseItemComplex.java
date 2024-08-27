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

package com.example.myapplication.datacomplex;

import java.util.ArrayList;
import java.util.List;

public class BaseItemComplex {
    private String mName;
    private Double mvalue;
    private List<BaseItemComplex> mChildrens;

    public BaseItemComplex(String name) {
        mName = name;
        mvalue = 0.0;
        mChildrens = new ArrayList<>();
    }

    public BaseItemComplex(String name, Double value) {
        mName = name;
        mvalue = value;
        mChildrens = new ArrayList<>();
    }

    public BaseItemComplex(String name, Double value, List<BaseItemComplex> childrens) {
        mName = name;
        mvalue = value;
        mChildrens = childrens;
    }

    public String getName() {
        return mName;
    }

    public Double getValue() { return mvalue; }
    public void setValue(Double value) { mvalue = value ;}

    public Double getValueWithChildrens(){
        Double total = mvalue ;
        if (hasChildren()) {
            for (BaseItemComplex item : mChildrens) {
                total = total + item.getValueWithChildrens();
            }
        }
        return total;
    }

    public List<BaseItemComplex> getChildren() { return mChildrens; }
    public void setChildren(List<BaseItemComplex> childrens) { mChildrens = childrens; }
    public void addChildren(BaseItemComplex item) {
      mChildrens.add(item);
    }
    public void addChildren(int i, BaseItemComplex item) {
        mChildrens.add(i,item);
    }

    public boolean hasChildren() { return mChildrens.size() > 0; }
}
