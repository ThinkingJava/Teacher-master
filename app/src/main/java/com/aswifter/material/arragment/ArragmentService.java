package com.aswifter.material.arragment;

import android.content.Context;
import android.util.Log;

import com.aswifter.material.app.App;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/12/10
 */
public class ArragmentService {
    public List<List<String>> getListdata(Context context){
        try{
            App application=(App)context;
            Document document = Jsoup.connect("。。。").
                    timeout(3000)
                            //       .data(map2)
                    .cookies(application.getCookies())//这个就是上面获取的cookies
                    .method(Connection.Method.GET)
                    .get();
            Log.d("----------<<<<<<", document.text().toString());
            Elements tr1=   document.getElementsByClass("table1").select("tr");
            //   	text.setText(""+tr1.toString());
            List<List<String>>  myList=new ArrayList<List<String>>();


//		    	List<String> ahrefList=new ArrayList<String>();
            for(int i = 1;i<tr1.size();i++){
                Elements td1 = tr1.get(i).select("td");
//		            Elements href = tr1.select("a[href]");
                List<String> list=new ArrayList<String>();
//		            for (Element link : href) {
//		    	       ahrefList.add(link.attr("abs:href"));
//		    	    }
                for(int j = 0;j<td1.size();j++){

                    String text = td1.get(j).text();
                    text=text.trim();
        //            System.out.println(text);
                    list.add(text);
                }
                myList.add(list);

            }

            return myList;
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public List<Map<String,String>> getSelectDeptname(Context context){
        App application=(App)context;
        List<Map<String,String>> mMap=new ArrayList<Map<String,String>>();
        try {
            Document document = Jsoup.connect("。。。").
                    timeout(3000)
                            //       .data(map2)
                    .cookies(application.getCookies())//这个就是上面获取的cookies
                    .method(Connection.Method.GET)
                    .get();
            Elements deptnameOptions = document.select("select[name=deptname] option");
            for(Element option : deptnameOptions){
                //对value进行映射
                Map<String,String> map=new HashMap<String,String>();
                map.put(option.attr("value"),option.text());
                mMap.add(map);
            }

             return mMap;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<List<String>> getUpdateDeptname(Context context,String depart){
        App application=(App)context;
        try{
        Document document = Jsoup.connect("。。。"+depart).
                timeout(3000)
                        //       .data(map2)
                .cookies(application.getCookies())//这个就是上面获取的cookies
                .method(Connection.Method.GET)
                .get();
        Log.d("----------<<<<<<", document.text().toString());
        Elements tr1=   document.getElementsByClass("table1").select("tr");
        //   	text.setText(""+tr1.toString());
        List<List<String>>  myList=new ArrayList<List<String>>();


//		    	List<String> ahrefList=new ArrayList<String>();
        for(int i = 1;i<tr1.size();i++){
            Elements td1 = tr1.get(i).select("td");
//		            Elements href = tr1.select("a[href]");
            List<String> list=new ArrayList<String>();
//		            for (Element link : href) {
//		    	       ahrefList.add(link.attr("abs:href"));
//		    	    }
            for(int j = 0;j<td1.size();j++){

                String text = td1.get(j).text();
                text=text.trim();
              //  System.out.println(text);
                list.add(text);
            }
            myList.add(list);

        }

        return myList;
    }catch(Exception e){
        e.printStackTrace();
    }

    return null;
    }

}
