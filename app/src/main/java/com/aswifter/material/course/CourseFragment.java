package com.aswifter.material.course;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aswifter.material.R;
import com.aswifter.material.view.XListView;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/12/26
 */
public class CourseFragment extends Fragment {
    private Context context;
    private XListView listView;
    private Handler mHandler;
    private MyBaseAdapter adapter;
    private ProgressBar mProgressBar;
    private int loadnumber = 10;
    List<List<String>> myList;
    List<String> URLList;
    Map<String, String> map2;
    //   private List<List<String>> teacherClassID;
    List<String> mItems=null;
    private int count=0;

    private static final int ANIM_DURATION_FAB = 400;
    CourseService service;
    public CourseFragment(Context context) {
        this.context=context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, null);
        //    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView = (XListView) view.findViewById(R.id.listview);
        listView.setXListViewListener(xListViewListener);
        listView.setPullLoadEnable(true);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

     //   setUpFAB(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    //    mFabButton.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));
        setData();
    }




    private void setData(){
        mProgressBar.setVisibility(View.VISIBLE);
        new LongTimeTask().execute();
        mHandler = new Handler();
    }

    private class LongTimeTask extends AsyncTask {
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o!=null){
                mProgressBar.setVisibility(View.GONE);

                adapter=new MyBaseAdapter();
                listView.setAdapter(adapter);
            }else{
                Toast.makeText(getActivity(), "出错了", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            service=new CourseService();

            myList= service.getListdata(context);
            URLList=service.getCourse();
            return myList;
        }
    }


    private XListView.IXListViewListener xListViewListener = new XListView.IXListViewListener() {

        @Override
        public void onRefresh() {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
//					refreshListview();
//					onLoad();
                }
            },2000);
        }

        @Override
        public void onLoadMore() {
            mHandler.postDelayed(new Runnable() {// 这里只是个显示界面的例子而已,正在做的时候应该用post方法
                @Override
                public void run() {
                    getmoreList();
                    adapter.notifyDataSetChanged();
                    onLoad();
                }
            }, 2000);

        }

    };
    private void getmoreList(){
        if(loadnumber<myList.size()&&loadnumber<0){
            loadnumber=0;
        }else if(loadnumber+10<myList.size()){
            loadnumber=loadnumber+10;
        }else {
            loadnumber=myList.size()%loadnumber+loadnumber;
        }
    }

    private void onLoad() {// 显示拉出来时候的一些信息

        listView.stopRefresh();
        listView.stopLoadMore();
        listView.setRefreshTime(new Date().toLocaleString());
        Log.i("======", "onLoad");
    }



    class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return loadnumber;
        }

        @Override
        public Object getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            List<String> list = myList.get(position);
            View view = View.inflate(getActivity(), R.layout.layout_item, null);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout);
            layout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.d("hhhhhhhhhhhhhhh", URLList.get(position));
                    Intent intent = new Intent(getActivity(), CoursesActivity.class);
                    //      intent.putExtra("cookies",(Serializable)response2.cookies());
                    intent.putExtra("href", URLList.get(position));
                    startActivity(intent);
                }
            });
            TextView text1 = (TextView) view.findViewById(R.id.text1);
            TextView text2 = (TextView) view.findViewById(R.id.text2);
            TextView text3 = (TextView) view.findViewById(R.id.text3);
            TextView text4 = (TextView) view.findViewById(R.id.text4);
            TextView text5 = (TextView) view.findViewById(R.id.text5);


                text1.setText("教学系:" + list.get(0));
                text2.setText("教师编号:" + list.get(1));
                text3.setText("姓名:" + list.get(2));
                text4.setText("总课时:" + list.get(3));
                text5.setText("平均课时:" + list.get(4));



            return view;
        }
    }

}
