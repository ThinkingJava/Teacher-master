package com.aswifter.material.service;

import android.app.Application;
import android.util.Log;

import com.aswifter.material.Utils;
import com.aswifter.material.app.App;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/12/8
 */
public class LoginService {
    private Application context;
    public  LoginService(Application context){
        this.context=context;
    }
    public  String login(String username, String password,String code) {

        try {
            Map<String, String> map = new HashMap<String, String>();

            map.put("rand",code);
            map.put("username",username);
            map.put("password", password);
            Connection.Response response = Jsoup.connect(Utils.LOGIN)
                    .data(map)
                    .method(Connection.Method.POST)
                    .timeout(20000)
                    .execute();
            if(response.statusCode() == 200) {
                Map<String, String> map1 = new HashMap<String, String>();
                map1.put("j_password", password);
                map1.put("j_username", "guest");

                Connection conn = Jsoup.connect(Utils.LOGIN_CHECK);
                conn.header("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
                conn.header("Accept-Encoding", "gzip, deflate");
                conn.header("Accept-Language", "zh-CN");
                conn.header("Cache-Control", "no-cache");
                conn.header("Connection", "Keep-Alive");
                conn.header("Content-Type", "application/x-www-form-urlencoded");
                conn.header("Host", "class.sise.com.cn:7001");
                conn.header("Referer", "。。。");
                conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; Touch; rv:11.0) like Gecko");

                //提交表单的代码

                Connection.Response response2 = conn
                        .ignoreContentType(true)
                        .data(map1)
                        .method(Connection.Method.POST)
                        .timeout(3000)
                        .execute();
                Log.d("LogService","---"+response2.cookies());
                App application=(App)getApplication();
                application.setCookies(response2.cookies());   //获取登录cookies
                return "3";

            }
        }catch(Exception e){
            e.printStackTrace();
            return "2";

        }
        return "1";
    }

    public Application getApplication() {
        return this.context;
    }

}


