package com.aswifter.material.course;

import android.content.Context;
import android.util.Log;

import com.aswifter.material.app.App;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/12/26
 */
public class CourseJsonService {

    JSONObject targetJsonObject;
    JSONObject  moreInfoTargetJsonObject;
    /**
     * 获取课程表信息
     *
     * @param context
     *            对象
     * @param url
     *           链接
     * @return 保存课程信息的二维数组
     */
    public JSONObject getSchedules(Context context,String url) {
        App app=(App)context;
        try {
            Document document= Jsoup.connect(url).cookies(app.getCookies()).get();


                Element body = document.body();
           //   Log.d("coursejsonservice",body.toString());
                // 获得学年范围：eg: 2012 2013 2014
//                String schoolyear = body.getElementsByTag("select").get(0)
//                        .getElementsByTag("option").html().replace("\n", " ");
                // Log.d("schoolyear", schoolyear);
                // Log.d("loginAction", body.html());
				/*
				 * 计算当前周跟上课周的差值开始
				 */
//                String week ="0";
//                Pattern p = Pattern.compile("\\d+");
//                Matcher m = p.matcher(week);
//                int weekNum = 0;
//                if (m.find()) {
//                    weekNum = Integer.valueOf(m.group());
//                }

                Log.d(" weekNum", String.valueOf(0));

                Calendar calendar = Calendar.getInstance();
                int nowWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                int dValue = nowWeek - 0;
				/*
				 * 周数差值计算结束
				 */

                Elements trs = body.getElementsByTag("table").get(4)
                        .getElementsByTag("tr");
                // tdCount ==> 表格列数 trCount ==> 表格行数
                int tdCount = trs.get(0).children().size();
                int trCount = trs.size();
                JSONObject all = new JSONObject();
                for (int i = 0; i < tdCount; i++) {
                    JSONArray everyDay = new JSONArray();
                    for (int j = 0; j < trCount - 1; j++) {
                        String data = trs.get(j + 1).child(i).html();
                        // Log.d("data", data);
                        JSONArray dataArray = new JSONArray();
                        if (data.indexOf(",") != -1) {
                            String[] spli = data.split(", ");
                            for (String string : spli) {
                                dataArray.put(string.replace("<strong>", "")
                                        .replace("</strong>", "")
                                        .replace("<br>", ""));
                            }
                            everyDay.put(dataArray);
                        } else {

                            dataArray.put(data.replace("<strong>", "")
                                    .replace("</strong>", "")
                                    .replace("<br>", ""));
                            everyDay.put(dataArray);
                        }

                    }
                    all.put(trs.get(0).child(i).html().replace("<strong>", "")
                            .replace("</strong>", ""), everyDay);
                }
                all.put("dValue", dValue);
                all.put("schoolyear", "2015");
                 Log.d("CourseJsonService", all.toString());
                return all;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void makeNewCoursesInfo(Context context, JSONObject jsonObject)
            throws JSONException {
        //      Log.d("jsonObject", jsonObject.toString());
        Map<String, CoursesInfo> coursesInfos = getCourseObjectArray(context,
                jsonObject);
        //      Log.d("--------------",coursesInfos.toString());
        targetJsonObject = new JSONObject();
         moreInfoTargetJsonObject = new JSONObject();
        JSONArray timeInfo = jsonObject.getJSONArray("&nbsp;");
        // 19:00 - 20:20
        for (String string : CoursesActivity.DAY_OF_WEEK) {
            JSONArray dayInfo = jsonObject.getJSONArray(string);
            JSONArray array = new JSONArray();
            JSONArray moreInfoArray = new JSONArray();
            for (int i = 0; i < 8; i++) {
                JSONArray courseArray = dayInfo.getJSONArray(i);
                JSONArray sameTime = new JSONArray();
                JSONArray moreSameTime = new JSONArray();
                for (int j = 0; j < courseArray.length(); j++) {
                    String src = courseArray.get(j).toString();

                    if (src.equals("&nbsp;")) {
                        // array.put(sameTime);
                        // moreInfoArray.put(moreSameTime);
                    } else {
                        // Log.d("ssrrcc", src);
                        // name + room
                        //Android网络编程(IE 潘正军 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17周 )
                        // [B201])
                        String[] split = src.substring(src.indexOf("(")).split(
                                " ");
                        String courseName = split[0].substring(1);
                        // src.substring(src.indexOf("("));
                        String classRoom = src.substring(src.indexOf("[") + 1,
                                src.indexOf("]"));
                        /******************************************************************/
                        String time = timeInfo.getString(i);
                        // Log.d("time", time);
                        int length = time.length();
                        String startTime = time.substring(length - 15,
                                length - 10);
                        Log.d("startTime", startTime);
                        //	String flag = courseName + classRoom + startTime;
                        String flag = courseName + classRoom+startTime;
                        //	 Log.d("coursesInfos", coursesInfos.toString());
                        //	 Log.d("coursesInfos", flag);
                        CoursesInfo info = coursesInfos.get(flag);
                        Log.d("11111", ""+flag);
                        StringBuilder builder = new StringBuilder();
                        builder.append(info.getCourseName());
                        builder.append("\n");
                        builder.append(info.getTeacherName());
                        builder.append("\n@ ");
                        builder.append(info.getClassRoom());
                        sameTime.put(builder.toString());
                        // array.put(builder.toString());

                        JSONObject tempObject = new JSONObject();
                        tempObject.put("className", info.getClassName());
                        tempObject.put("classRoom", info.getClassRoom());
                        tempObject.put("courseName", info.getCourseName());
                        tempObject.put("startTime", info.getStartTime());
                        tempObject.put("teacherName", info.getTeacherName());
                        JSONArray week = new JSONArray();
                        for (Integer tmp : info.getWeek()) {
                            week.put(tmp);
                        }
                        tempObject.put("week", week);
                        moreSameTime.put(tempObject);
                        // moreInfoArray.put(tempObject);
                    }
                }
                array.put(sameTime);
                moreInfoArray.put(moreSameTime);

            }
            targetJsonObject.put(string, array);
            moreInfoTargetJsonObject.put(string, moreInfoArray);
        }
        targetJsonObject.put("dValue", jsonObject.get("dValue"));
        targetJsonObject.put("schoolyear", jsonObject.get("schoolyear"));
//		moreInfoTargetJsonObject
//				.put("schoolyear", jsonObject.get("schoolyear"));
        moreInfoTargetJsonObject.put("dValue", jsonObject.get("dValue"));

//        UserUtil.saveData(context, targetJsonObject, UserUtil.COURSE_OBJECT_FILE_NAME);
//        UserUtil.saveData(context, moreInfoTargetJsonObject,
//                UserUtil.MORE_COURSE_OBJECT_FILE_NAME);

    }
    /*
    *课程
     */
      public JSONObject courseObject(){
          Log.d("targetJsonObject",targetJsonObject.toString());
          return targetJsonObject;
      }
    /*
    *更多
     */
    public JSONObject morecourseObject(){
        Log.d("moreInfoTargetJson",targetJsonObject.toString());
        return moreInfoTargetJsonObject;
    }
    /**
     * 获取CourseObjectArray
     *
     * @param context
     * @return
     * @throws JSONException
     */
    private Map<String, CoursesInfo> getCourseObjectArray(Context context,
                                                          JSONObject jsonObject) throws JSONException {
        Set<String> set = new HashSet<String>();
        JSONArray timeInfo = jsonObject.getJSONArray("&nbsp;");
        for (String string : CoursesActivity.DAY_OF_WEEK) {
            JSONArray dayInfo = jsonObject.getJSONArray(string);
            for (int i = 0; i < 8; i++) {
                JSONArray coursesArray = dayInfo.getJSONArray(i);
                // Log.d("coursesArray", coursesArray.toString());
                if (!coursesArray.toString().equals("[\"&nbsp;\"]")) {
                    // 放到set里面是为了去重
                    // Log.d("dayInfo", dayInfo.get(i).toString());
                    for (int j = 0; j < coursesArray.length(); j++) {
                        set.add(timeInfo.getJSONArray(i).getString(0) + "#"
                                + coursesArray.getString(j));
                    }

                }
            }
        }
        Map<String, CoursesInfo> map = new HashMap<String, CoursesInfo>();
        CoursesInfo info = null;
        for (String string : set) {
            //    Log.d("dayInfo", string);
            info = getCourseObject(string);
            //    Log.d("info.toString()", info.toString());
            map.put(info.getFlag(), info);
        }
        return map;
    }
    /*
      把对象改为json
     */
    public CoursesInfo getCourseObject(String src) {
        // String[] strings = src.split("#");// [0]==>timeInfo:JSONArray;
        // [1]==>courseInfo:JSONArray

        // JSONObject tTime =

        //    Log.d("******", src);
        CoursesInfo coursesInfo = new CoursesInfo();
        String time = src.substring(src.indexOf(">") + 1, src.indexOf("#"))
                .substring(0,5);
        String courseName = src.substring(src.indexOf("#") + 1,
                src.indexOf("("));
        String otherInfo = src.substring(src.indexOf("("));
        String[] split = otherInfo.split(" ");
        String className = split[0].substring(split[0].indexOf("(") + 1);
        String teacherName = split[1];
        int splitLength = split.length;
        String classRoom = split[splitLength - 1].substring(
                split[splitLength - 1].indexOf("[") + 1,
                split[splitLength - 1].length() - 2);
        Integer[] week = new Integer[splitLength - 3];
        for (int i = 0; i < split.length - 4; i++) {
            week[i] = Integer.valueOf(split[i + 2]);
        }
        week[splitLength - 4] = Integer.valueOf(split[splitLength - 2]
                .substring(0, split[splitLength - 2].length() - 1));
        coursesInfo.setClassName(className);
        coursesInfo.setClassRoom(classRoom);
        coursesInfo.setFlag(className + classRoom + time);
        coursesInfo.setCourseName(courseName);
        coursesInfo.setStartTime(time);
        coursesInfo.setTeacherName(teacherName);
        coursesInfo.setWeek(week);
        return coursesInfo;
    }
}
