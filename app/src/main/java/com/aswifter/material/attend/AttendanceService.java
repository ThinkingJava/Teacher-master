package com.aswifter.material.attend;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/12/8
 */

import android.content.Context;
import android.util.Log;

import com.aswifter.material.Utils;
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

public class AttendanceService {
    private List<String> listhref;
    Map<String, String> map2;
    Map<String, String> cookies;
    Map<String, String> mMap = null;


    private void setCookie(Context context) {
        App application = (App) context;
        cookies = application.getCookies();
        //  Log.d("setCookie","11"+cookies);
    }

    /**
     * 获取url参数
     *
     * @param strURL
     * @return
     */
    public static String UrlPage(String strURL) {
        String strPage = null;
        String[] arrSplit = null;


        arrSplit = strURL.split("[?]");
        if (strURL.length() > 0) {
            if (arrSplit.length > 1) {
                if (arrSplit[arrSplit.length - 1] != null) {
                    strPage = arrSplit[arrSplit.length - 1];
                }
            }
        }

        return strPage;
    }

    /**
     * 获取url参数
     *
     * @param URL
     * @return map
     */

    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        String strUrlParam = UrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        //每个键值为一组 www.2cto.com
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }


    public List<List<String>> getListdata(Context context) {
        setCookie(context);
        App app=(App)context;

        List<List<String>> myList = new ArrayList<List<String>>();
        List<String> classIdList = new ArrayList<String>();  //获取班级id


        try {

            Document document = Jsoup.connect("。。。").cookies(cookies).get();

            Elements mlinks = document.select("select[name=yearSemester] option");

            Map<String, String> testmap = new HashMap<String, String>();

            testmap.put("yearSemester", mlinks.get(1).attr("value"));
            Log.d("11111111111", testmap.toString());

            Document mDocument = Jsoup.connect("。。。")
                    .cookies(cookies)
                    .get();
            Elements tr = mDocument.getElementsByClass("table1").select("tr");
            System.out.println("大小" + tr.size());
            for (int i = 1; i < tr.size(); i++) {

                Element td = tr.get(i).select("td").get(1);

                if (td.text().equals(app.getID())) {

                    Element td1=tr.get(i).select("td").get(2);
                    app.setName(td1.text());
                    Log.d("11122",app.getID()+"+"+td1.text());
                    Elements links = td.select("a[href]");
                    String ahref = links.attr("abs:href");
                    mMap = URLRequest(ahref);
                }
            }
            Map<String, String> mMap1 = new HashMap<>();
            mMap1.put("teacherID", mMap.get("teacherid"));
            mMap1.put("teachingClassID", "0");
            mMap1.put("term", "1");
            mMap1.put("year", "2015");
            Document classdata = Jsoup.connect("。。。")   //获取页面中的课程
                    .cookies(cookies)
                    .data(mMap1)
                    .post();
            Elements elements = classdata.select("select[name=teachingClassID]").select("option");
            //  System.out.println("大小"+elements.size()+":"+elements.toString());
            map2 = new HashMap<String, String>();    //获取课程
            for (int i = 1; i < elements.size(); i++) {

                map2.put( elements.get(i).text(),elements.get(i).attr("value"));
            }

            for (String key : map2.keySet()) {
                classIdList.add(map2.get(key));
             //   System.out.println("key= " + key + " and value= " + map2.get(key));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("AttendanceService---", map2.toString());
        Document document = null;
        Map<String, String> mMap2 = new HashMap<String, String>();
        mMap2.put("teacherID", mMap.get("teacherid"));
        mMap2.put("teachingClassID", classIdList.get(0));
        mMap2.put("term", "1");
        mMap2.put("year", "2015");
        Log.d("AttendanceService", mMap2.toString());
        try {
            document = Jsoup.connect(Utils.ATTEND).
                    timeout(3000)
                    .data(mMap2)
                    .cookies(cookies)//这个就是上面获取的cookies
                    .method(Connection.Method.POST)
                    .post();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //      Log.d("----------<<<<<<",document.text().toString());
        Elements tr1 = document.getElementsByClass("table").select("tr");
        //   	text.setText(""+tr1.toString());

        listhref = new ArrayList<String>();

//			    	List<String> ahrefList=new ArrayList<String>();
        for (int i = 1; i < tr1.size(); i++) {
            Elements href = tr1.get(i).select("a[onClick]");
            String hreString = href.attr("onClick");
            String a[] = hreString.split("'");
            listhref.add(a[1]);

            Elements td1 = tr1.get(i).select("td");
//			            Elements href = tr1.select("a[href]");
            List<String> list = new ArrayList<String>();
//			            for (Element link : href) {
//			    	       ahrefList.add(link.attr("abs:href"));
//			    	    }
            for (int j = 0; j < td1.size(); j++) {

                String text = td1.get(j).text();
                text = text.trim();
                //       System.out.println(text);
                list.add(text);
            }
            myList.add(list);

        }
        return myList;
    }

    public List<String> getURLList() {
        return listhref;
    }

    public Map<String, String> getData() {
        return map2;
    }


    public Map<String,String> getclassName(){    //获取班级
        return map2;
    }

    public List<List<String>> getListupdatedata(Context context, String value) {
        setCookie(context);
        List<List<String>> myList = new ArrayList<List<String>>();
        Map<String, String> mMap2 = new HashMap<String, String>();
        mMap2.put("teacherID", mMap.get("teacherid"));
        mMap2.put("teachingClassID", value);
        mMap2.put("term", "1");
        mMap2.put("year", "2015");
      Log.d("getListUpdate",mMap2.toString());
        Document document = null;
        try {
            document = Jsoup.connect(Utils.ATTEND).
                    timeout(3000)
                    .data(mMap2)
                    .cookies(cookies)   //这个就是上面获取的cookies
                    .method(Connection.Method.POST)
                    .post();
        } catch (IOException e) {
            e.printStackTrace();
        }
    //    Log.d("----------<<<<<<", document.text().toString());
        Elements tr1 = document.getElementsByClass("table").select("tr");
        //   	text.setText(""+tr1.toString());

        listhref=new ArrayList<>();

//			    	List<String> ahrefList=new ArrayList<String>();
        for (int i = 1; i < tr1.size(); i++) {
            Elements href = tr1.get(i).select("a[onClick]");
            String hreString = href.attr("onClick");
            String a[] = hreString.split("'");
            listhref.add(a[1]);

            Elements td1 = tr1.get(i).select("td");
//			            Elements href = tr1.select("a[href]");
            List<String> list = new ArrayList<String>();
//			            for (Element link : href) {
//			    	       ahrefList.add(link.attr("abs:href"));
//			    	    }
            for (int j = 0; j < td1.size(); j++) {

                String text = td1.get(j).text();
                text = text.trim();
                //       System.out.println(text);
                list.add(text);
            }
            myList.add(list);

        }
        return myList;
    }


}