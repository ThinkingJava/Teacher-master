package com.aswifter.material;

import android.content.Context;
import android.util.Log;

import com.aswifter.material.app.App;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/12/28
 */
public class TeacherName {

    public String getteachername(Context context){
        App app=(App)context;
        Log.d("1------1",app.getName());
        return  app.getName();
    }

}
