package com.aswifter.material.attend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aswifter.material.R;
import com.aswifter.material.view.XListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/12/8
 */
public class AttendFragment extends Fragment {
    private Context context;
    private XListView listView;
    private Handler mHandler;
    private MyBaseAdapter adapter;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFabButton;
    private int loadnumber = 10;
    List<List<String>> myList;
    List<String> URLList;
    Map<String, String> map2;
 //   private List<List<String>> teacherClassID;
     List<String> mItems=null;
    private int count=0;

    private static final int ANIM_DURATION_FAB = 400;
    AttendanceService service;
    public AttendFragment(){

    }
    public AttendFragment(Context context) {
        this.context=context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attend, null);
    //    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView = (XListView) view.findViewById(R.id.listview);
        listView.setXListViewListener(xListViewListener);
        listView.setPullLoadEnable(true);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        setUpFAB(view);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFabButton.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));
                setData();
    }

    private void setUpFAB(View view) {
        mFabButton = (FloatingActionButton) view.findViewById(R.id.fab_normal);

        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.show();
                Window window = alertDialog.getWindow();
                window.setContentView(R.layout.dialog_main_info);

                Spinner tv_type = (Spinner) window.findViewById(R.id.type_item);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mItems);
                tv_type.setAdapter(adapter);
                tv_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        count = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Button tv_submit = (Button) window.findViewById(R.id.submit_search);
                tv_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String value=mItems.get(count);

                        setUpdate(map2.get(value));
                        alertDialog.dismiss();
                    }
                });

            }
        });
    }

    private void startFABAnimation() {
        mFabButton.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500)
                .setDuration(ANIM_DURATION_FAB)
                .start();
    }
    private void setData(){
        mProgressBar.setVisibility(View.VISIBLE);
        new LongTimeTask().execute();
        mHandler = new Handler();
            }

    private class LongTimeTask extends AsyncTask{
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o!=null){
                mProgressBar.setVisibility(View.GONE);
                startFABAnimation();
                adapter=new MyBaseAdapter();
                listView.setAdapter(adapter);
            }else{
                Toast.makeText(getActivity(), "出错了", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            service=new AttendanceService();

            myList= service.getListdata(context);
            // Log.d("AttendFragment",myList.toString());
            URLList=service.getURLList();
            map2=service.getclassName();
            mItems=new ArrayList<>();
            //     teacherClassID=service.getitem();
            for(String key:map2.keySet()){
                mItems.add(key);    //
            }
            return myList;
        }
    }



    private void setUpdate(String checkvalue) {
        myList.clear();
        myList = service.getListupdatedata(context, checkvalue);
        URLList = service.getURLList();
        adapter.notifyDataSetChanged();

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
                        Intent intent = new Intent(getActivity(), StudentDetailActivity.class);
                        intent.putExtra("data", (Serializable) map2);
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

                if (list != null) {
                    text1.setText("" + list.get(0));
                    text2.setText("" + list.get(1));
                    text3.setText("" + list.get(2));
                    text4.setText("" + list.get(3));
                    text5.setText("" + list.get(4));

                } else {
                    text1.setText("暂无数据");
                }

                return view;
            }
        }

    }
