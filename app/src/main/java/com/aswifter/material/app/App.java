package com.aswifter.material.app;

import android.app.Application;

import java.util.Map;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/12/8
 */
public class App extends Application{
    private Map<String,String> cookies;
    private String ID;
    private String Name;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
