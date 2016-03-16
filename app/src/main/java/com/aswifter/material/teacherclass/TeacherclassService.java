package com.aswifter.material.teacherclass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.aswifter.material.app.App;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/12/13
 */
public class TeacherclassService {
    List<String> mlist;
    List<List<String>>  myList;
    List<String> imageList;
    public List<List<String>> getListdata(Context context) {
        App application=(App)context;
        myList=new ArrayList<List<String>>();
        try {
            Document document= Jsoup.connect("。。。" + application.getID()+"&teachingname=&deptname=-1")
                    .cookies(application.getCookies())
                    .get();

            Element link=  document.select("a[href]").get(0);
            String hrefString=link.attr("abs:href");
         //   System.out.println(hrefString);

       //     System.out.println("----------<<<<<<"+document.text().toString());
            Document doc1=Jsoup.connect(hrefString)
                    .cookies(application.getCookies())
                    .get();

            Elements tr1=   doc1.getElementsByClass("table1").select("tr");
            //   	text.setText(""+tr1.toString());

//
//
			    	List<String> ahrefList=new ArrayList<String>();

            Elements href1 = tr1.select("a[href]");
            for(int s=0;s<href1.size();s++){
                if(s%2!=0){
                    ahrefList.add(href1.get(s).attr("abs:href"));
                }
            }
           ahrefList.add(href1.get(1).attr("abs:href"));
            for(int i = 0;i<tr1.size();i++){
                Elements td1 = tr1.get(i).select("td");

                List<String> list=new ArrayList<String>();
                list.add(ahrefList.get(i));

                for(int j = 0;j<td1.size();j++){

                    String text = td1.get(j).text();
                    text=text.trim();
                    //        System.out.println(text);
                    list.add(text);
                }
                myList.add(list);
            }
    //        Log.d("ooooooooooo",myList.toString());
        List<String> mList=myList.get(0);
        Document mDocument=Jsoup.connect(mList.get(0))
                .cookies(application.getCookies())
                .get();
        Elements tr=   mDocument.select("table").get(2).select("tr");
        mlist=new ArrayList<String>();
            imageList=new ArrayList<String>();
        for(int i = 1;i<tr.size();i++){
            Elements td1 = tr.get(i).select("td");

            Elements href = td1.select("img[src]");
            for(int j = 0;j<td1.size();j++){

                imageList.add(href.get(j).attr("abs:src"));
                String text = td1.get(j).text();
                //        System.out.println(text);

                mlist.add(text.trim());
            }


        }
  //      System.out.println("1111111111"+mlist.toString());
        return myList;

    } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<String> getImageList(){
   //     Log.d("图片链接",imageList.size()+imageList.toString());
        return imageList;
    }
    public List<Bitmap> getImage(){

        List<Bitmap> Image=new ArrayList<Bitmap>();
        try {
            for (int i = 0; i < getImageList().size(); i++) {
                Image.add(loadImageFromUrl(imageList.get(i)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    //    Log.d("多少",""+Image);
        return Image;
    }

    public  Bitmap loadImageFromUrl(String url) throws Exception  {
     //   Log.d("picture",url);
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpGet getRequest = new HttpGet(url);

        HttpResponse response = client.execute(getRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK)  {
            Log.e("PicShow", "Request URL failed, error code =" + statusCode);
        }

        HttpEntity entity = response.getEntity();
        if (entity == null) {
            Log.e("PicShow", "HttpEntity is null");
        }
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            is = entity.getContent();
            byte[] buf = new byte[1024];
            int readBytes = -1;
            while ((readBytes = is.read(buf)) != -1) {
                baos.write(buf, 0, readBytes);
            }
        } finally {
            if (baos != null) {
                baos.close();
            }
            if (is != null) {
                is.close();
            }
        }
        byte[] imageArray = baos.toByteArray();
        return BitmapFactory.decodeByteArray(
                imageArray, 0, imageArray.length);
    }



 public   List<Student> getStudentData(){
     List<Student> mStudent=new ArrayList<Student>();
     List<Bitmap> mBitmap=getImage();
      for(int i=0;i<imageList.size();i++){
          Student student=new Student();
          student.setText(mlist.get(i));
          student.setBitmap(mBitmap.get(i));
          mStudent.add(student);
      }
     return mStudent;
    }

    public List<Student> getUpdateStudent(Context context,String url){
        Log.d("11111111111Service",url);
        List<Student> mStudent=new ArrayList<Student>();
        App application=(App)context;
        Document mDocument= null;
        try {
            mDocument = Jsoup.connect(url)
                    .cookies(application.getCookies())
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements tr=   mDocument.select("table").get(2).select("tr");
        mlist.clear();
        imageList.clear();
        for(int i = 1;i<tr.size();i++){
            Elements td1 = tr.get(i).select("td");

            Elements href = td1.select("img[src]");
            for(int j = 0;j<td1.size();j++){

                imageList.add(href.get(j).attr("abs:src"));
                String text = td1.get(j).text();
                //        System.out.println(text);

                mlist.add(text.trim());
            }


        }


        return null;
    }

    private List<List<String>> getSelect(){   //获取图片链接
        return myList;
    }
}
