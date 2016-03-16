package com.aswifter.material.attend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aswifter.material.R;
import com.aswifter.material.Utils;
import com.aswifter.material.app.App;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentDetailActivity extends AppCompatActivity {
    Map<String, String> cookies;
    Map<String, String> map2;
    private  static String pathString;
    private TextView textView;
    private Button rollback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        textView=(TextView)this.findViewById(R.id.text);
        rollback=(Button)this.findViewById(R.id.rollback);
        Intent intent=getIntent();
        map2=(Map<String,String>)intent.getSerializableExtra("data");
        App application=(App)getApplication();
        cookies=application.getCookies();
        pathString=intent.getStringExtra("href");
        setData();
        rollback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
    private void setData(){

        try {
            Document document = Jsoup.connect(Utils.DOMAIN + pathString).
                    timeout(3000)
                    .data(map2)
                    .cookies(cookies)//这个就是上面获取的cookies
                    .method(Connection.Method.POST)
                    .post();
            Elements tr1 = document.getElementsByClass("table").select("tr");
            //   	text.setText(""+tr1.toString());
            List<List<String>>  myList = new ArrayList<List<String>>();


//	    	List<String> ahrefList=new ArrayList<String>();
            for (int i = 1; i < tr1.size(); i++) {
                Elements td1 = tr1.get(i).select("td");
//	            Elements href = tr1.select("a[href]");
                List<String> list = new ArrayList<String>();
//	            for (Element link : href) {
//	    	       ahrefList.add(link.attr("abs:href"));
//	    	    }
                for (int j = 0; j < td1.size(); j++) {

                    String text = td1.get(j).text();
                    text = text.trim();
                    System.out.println(text);
                    list.add(text);
                    textView.append(text + "\t");
                }
                myList.add(list);
                textView.append("\n");
            }
        }catch (Exception e){

        }
    }


}