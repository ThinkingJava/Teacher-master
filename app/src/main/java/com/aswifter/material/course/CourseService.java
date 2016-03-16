package com.aswifter.material.course;

import android.content.Context;

import com.aswifter.material.app.App;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/12/26
 */
public class CourseService {
    private static List<List<String>> myList;   //教师数据
    List<String> courseList;    //教师课表链接
    public List<List<String>> getListdata(Context context) {

         courseList=new ArrayList<>();
        App app=(App)context;
        try {
            Document document=Jsoup.connect("。。。")
                    .cookies(app.getCookies())
                    .get();
            Elements tr1=   document.getElementsByClass("table1").select("tr[height=25]");
       //     Log.d("||course||",""+tr1.size());
            myList=new ArrayList<List<String>>();
            courseList=new ArrayList<>();
            for(int i = 1;i<tr1.size();i++){
                Elements td1 = tr1.get(i).select("td");
                List<String> list=new ArrayList<String>();
                for(int j = 0;j<td1.size();j++){

                    String text = td1.get(j).text();
                    text=text.trim();
                    if(j==1){
                        courseList.add(td1.get(2).select("a[href]").attr("abs:href"));
                    }
                    list.add(text);
                }
                myList.add(list);

            }
    //    System.out.println("教师课表链接"+courseList.toString());
        return myList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public  List<String> getCourse(){
        return courseList;
    }
}
