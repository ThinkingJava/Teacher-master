package com.aswifter.material.arragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aswifter.material.R;
import com.aswifter.material.Utils;
import com.aswifter.material.widget.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/12/10
 */
public class ArragmentFragment extends Fragment {
    private Context context;
    ArragmentService arragementService;
    public ArragmentFragment(Context context) {
        this.context=context;
    }

    private List<List<String>>listdata;   //listview中数据
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFabButton;
    private List<Map<String,String>> dataMap;
    private int count=0;
    private static final int ANIM_DURATION_FAB = 400;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), onItemClickListener));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mAdapter = new MyAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        setUpFAB(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFabButton.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));
     //   doSearch(getString(R.string.default_search_keyword));
        setData();
    }
    private void setData(){
        new LongTimeTask().execute();  //开启线程
    }

    private class LongTimeTask extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object[] params) {
            arragementService=new ArragmentService();
            listdata= arragementService.getListdata(context);
            dataMap=arragementService.getSelectDeptname(context);
      //      Log.d("Arragment-------------",dataMap.toString());
            return listdata;
        }

        @Override
        protected void onPostExecute(Object o)
        {
            //更新UI的操作，这里面的内容是在UI线程里面执行的
            if(o!=null) {
                mProgressBar.setVisibility(View.GONE);
                startFABAnimation();
                mAdapter.updateItems(listdata, true);
            }else{
                Toast.makeText(getActivity(),"出错了",Toast.LENGTH_LONG).show();
            }
        }

    }

//    private void doSearch(String keyword) {
//        mProgressBar.setVisibility(View.VISIBLE);
//        mAdapter.clearItems();
//
//        Book.searchBooks(keyword, new Book.IBookResponse<List<Book>>() {
//            @Override
//            public void onData(List<Book> books) {
//                mProgressBar.setVisibility(View.GONE);
//                startFABAnimation();
//                mAdapter.updateItems(books, true);
//            }
//        });
//    }


    private void setUpFAB(View view) {
        mFabButton = (FloatingActionButton) view.findViewById(R.id.fab_normal);
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new MaterialDialog.Builder(getActivity())
//                        .title(R.string.search)
//                                //.inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
//                        .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
//                            @Override
//                            public void onInput(MaterialDialog dialog, CharSequence input) {
//                                // Do something
//                                if (!TextUtils.isEmpty(input)) {
//                                    doSearch(input.toString());
//                                }
//                            }
//                        }).show();
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.show();
                Window window = alertDialog.getWindow();
                window.setContentView(R.layout.dialog_main_info);
                 final List<String> mListdata=new ArrayList<String>();
                final List<String> mListkey=new ArrayList<String>();

                for(int i=0;i<dataMap.size();i++) {
                    Map<String,String> mMap=dataMap.get(i);
                    for (Map.Entry<String, String> entry : mMap.entrySet()) {
                        mListkey.add(entry.getKey());
                        mListdata.add(entry.getValue());

                    }
                }
           //     String[] mItems={"1","2","3","4"};
           //     String[] mValues={"5","6","7","8"};
                Spinner tv_type = (Spinner) window.findViewById(R.id.type_item);
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, mListdata);
                tv_type.setAdapter(adapter);
                tv_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        count=position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Button tv_submit= (Button) window.findViewById(R.id.submit_search);
                tv_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setUpdateData(mListkey.get(count), mListdata.get(count));
                        alertDialog.dismiss();
                    }
                });

            }
        });
    }
    private void setUpdateData(String key,String value){
        ArragmentService arragementService=new ArragmentService();
        listdata= arragementService.getUpdateDeptname(context,key);
        mAdapter.clearItems();
        mProgressBar.setVisibility(View.GONE);
        startFABAnimation();
        mAdapter.updateItems(listdata, true);
    }

    private void startFABAnimation() {
        mFabButton.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500)
                .setDuration(ANIM_DURATION_FAB)
                .start();
    }


    private RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
//            Book book = mAdapter.getBook(position);
//            Intent intent = new Intent(getActivity(), BookDetailActivity.class);
//            intent.putExtra("book", book);
//
//            ActivityOptionsCompat options =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
//                            view.findViewById(R.id.ivBook), getString(R.string.transition_book_img));
//
//            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

        }
    };

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private final int mBackground;
        private List<List<String>> mBooks = new ArrayList<List<String>>();
        private final TypedValue mTypedValue = new TypedValue();

        private static final int ANIMATED_ITEMS_COUNT = 4;

        private boolean animateItems = false;
        private int lastAnimatedPosition = -1;

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(Context context) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public ImageView ivBook;
            public TextView tvTitle;
            public TextView tvDesc;

            public int position;

            public ViewHolder(View v) {
                super(v);
           //     ivBook = (ImageView) v.findViewById(R.id.ivBook);
                tvTitle = (TextView) v.findViewById(R.id.tvTitle);
                tvDesc = (TextView) v.findViewById(R.id.tvDesc);
            }
        }


        private void runEnterAnimation(View view, int position) {
            if (!animateItems || position >= ANIMATED_ITEMS_COUNT - 1) {
                return;
            }

            if (position > lastAnimatedPosition) {
                lastAnimatedPosition = position;
                view.setTranslationY(Utils.getScreenHeight(getActivity()));
                view.animate()
                        .translationY(0)
                        .setStartDelay(100 * position)
                        .setInterpolator(new DecelerateInterpolator(3.f))
                        .setDuration(700)
                        .start();
            }
        }


        public void updateItems(List<List<String>> books, boolean animated) {
            animateItems = animated;
            lastAnimatedPosition = -1;
            mBooks.addAll(books);
            notifyDataSetChanged();
        }

        public void clearItems() {
            mBooks.clear();
            notifyDataSetChanged();
        }


        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.arragment_item, parent, false);
            //v.setBackgroundResource(mBackground);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            runEnterAnimation(holder.itemView, position);
            List<String> book = mBooks.get(position);
            holder.tvTitle.setText(book.get(0));
            String desc = "类型: " + book.get(1) + "\n课程号: " + book.get(2)
                    + "\n课程名: " + book.get(3) + "\n排课: " + book.get(4) ;
            holder.tvDesc.setText(desc);
//            Glide.with(holder.ivBook.getContext())
//                    .load(book.getImage())
//                    .fitCenter()
//                    .into(holder.ivBook);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mBooks.size();
        }


        public List<String> getBook(int pos) {
            return mBooks.get(pos);
        }
    }
}
