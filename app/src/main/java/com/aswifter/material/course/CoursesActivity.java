package com.aswifter.material.course;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aswifter.material.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class CoursesActivity extends AppCompatActivity  {
    private LinearLayout linearLayout = null;
    private JSONObject jsonObject = null;
    private JSONObject moreJsonObject = null;
 //   private TextView weekNum = null;
    private int weekNumI = 0;
    private String weekS;
    private String schoolyear;
    private int seed = 0;
    public static final String[] DAY_OF_WEEK = { "星期一", "星期二", "星期三", "星期四",
            "星期五", "星期六", "星期日" };
    private void setWeekS() {
        if (weekNumI == 0) {
            weekS = "0";
        } else {
            weekS = new StringBuilder("0").append(weekNumI).append(",")
                    .toString();
        }
    }
    private static final int[] dayID = { R.id.day01, R.id.day02, R.id.day03,
            R.id.day04, R.id.day05, R.id.day06, R.id.day07 };

    private static final int[] textViewBackground = {
            R.drawable.textview_border_course_1,
            R.drawable.textview_border_course_2,
            R.drawable.textview_border_course_3,
            R.drawable.textview_border_course_4,
            R.drawable.textview_border_course_5,
            R.drawable.textview_border_course_6, };

    private void drawCourses() {
        for (int i = 0; i < 7; i++) {
            drawCourses(dayID[i], DAY_OF_WEEK[i]);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        initData();
    }

    private void initData() {
        Intent intent=getIntent();
     String href=    intent.getStringExtra("href");
        Log.d("CourseActivity", href);
        CourseJsonService courseJsonService=new CourseJsonService();
        ImageView imageView=(ImageView)findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoursesActivity.this.finish();  //窗口关闭
            }
        });
        try {
         JSONObject jsonOb=   courseJsonService.getSchedules(getApplication(), href);
            courseJsonService.makeNewCoursesInfo(getApplication(),jsonOb);
           jsonObject= courseJsonService.courseObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
      moreJsonObject=courseJsonService.morecourseObject();
  //      weekNum = (TextView) findViewById(R.id.weekNum);
        try {
            schoolyear = jsonObject.getString("schoolyear");
            weekNumI = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
                    - jsonObject.getInt("dValue");
            setWeekS();
        } catch (JSONException e) {
            //       e.printStackTrace();
            Toast.makeText(CoursesActivity.this, "初始化错误", Toast.LENGTH_LONG).show();
        }
   //     weekNum.setText(new StringBuilder("第 ").append(weekNumI).append(" 周"));
        drawCourses();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void drawCourses(int dayID, String dayName) {
        linearLayout = (LinearLayout) findViewById(dayID);
        try {
            JSONArray day = jsonObject.getJSONArray(dayName);
            // Log.d("day", day.toString());

            JSONArray moreDay = moreJsonObject.getJSONArray(dayName);
            // Log.d("moreDay", moreDay.toString());
            for (int i = 0; i < 8; i++) {

                final TextView textView = new TextView(this);
                JSONArray time = day.getJSONArray(i);
                /***************************
                 *
                 *
                 *
                 * ********************************/
                // Log.d("iiiiiiii", time.toString());
                JSONArray mTime = moreDay.getJSONArray(i);
                if (time.length() > 0) {
                    // Log.d("iiiiiiii", "是的");
                    for (int j = 0; j < time.length(); j++) {
                        final JSONObject temp = mTime.getJSONObject(j);
                        // Log.d("iiiiiiii", temp.toString());
                        // Log.d("week", weekS);
                        // Log.d("boolean", weekS
                        // + " "
                        // + temp.getString("week").replace('[', '0')
                        // .replace(",", ",0").replace(']', ','));
                        if (temp.getString("week").replace('[', '0')
                                .replace(",", ",0").replace(']', ',')
                                .indexOf(weekS) != -1) {

                            // Log.d("tttt", time.get(j).toString());
                            textView.setText(time.get(j).toString());
                            textView.setTextSize(11f);
                            textView.setTextColor(Color.BLACK);
                            textView.setBackground(getResources().getDrawable(
                                    textViewBackground[seed++]));
                            if (seed > 5) {
                                seed = 0;
                            }
                            textView.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
//                                    Intent intent = new Intent(
//                                            CoursesActivity.this,
//                                            CoursesDetailActivity.class);
//                                    intent.putExtra("data", temp.toString());
//                                    startActivity(intent);
                                }
                            });
                            break;
                        }
                    }
                } else {
                    textView.setBackground(getResources().getDrawable(
                            R.drawable.textview_border_course));
                }

                textView.setHeight(160);
                // textView.setHeight(120);

                linearLayout.addView(textView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
