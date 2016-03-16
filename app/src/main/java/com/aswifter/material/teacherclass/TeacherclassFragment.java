package com.aswifter.material.teacherclass;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.aswifter.material.R;
import com.aswifter.material.Utils;
import com.aswifter.material.widget.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yangchenghuan
 * 类描述：
 * 创建日期：2015/12/13
 */
public class TeacherclassFragment extends Fragment {
    private Context context;
    public TeacherclassFragment(Context context) {
        this.context=context;
    }

    private List<Student>listdata;   //listview中数据
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFabButton;

    private List<List<String>> listselect;
   private  List<String> number;
    private List<Bitmap> bm;
    private int count=0;
    private static final int ANIM_DURATION_FAB = 400;
    TeacherclassService arragementService;
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

     //   Log.d("Arragment-------------", mImageList.toString());
           new LongTimeTask().execute();  //开启线程
    }

    public class LongTimeTask extends AsyncTask
    {

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

                mProgressBar.setVisibility(View.GONE);
                startFABAnimation();
                mAdapter.updateItems(listdata, true);


        }


        @Override
        protected Object doInBackground(Object[] params) {
            try {
                arragementService = new TeacherclassService();
                listselect = arragementService.getListdata(context);
                listdata = arragementService.getStudentData();
                Log.d("data11111", listdata.toString());
                bm = arragementService.getImage();
                return listdata;
            } catch (Exception e) {
                return null;
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
        mFabButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(v,"容量:"+listselect.get(count+1).get(6)+" \n 人数:"+ listselect.get(count+1).get(7), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }
        });
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
               number=new ArrayList<String>();
                 for(int i=1;i<listselect.size();i++){
                     number.add(listselect.get(i).get(5));
            //         Log.d("----ou00--",listselect.get(i).get(0));
                 }
                //     String[] mItems={"1","2","3","4"};
                //     String[] mValues={"5","6","7","8"};
                Spinner tv_type = (Spinner) window.findViewById(R.id.type_item);
                final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,number );
                tv_type.setAdapter(adapter);
                tv_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        count=position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                      count=1;
                    }
                });
                Button tv_submit= (Button) window.findViewById(R.id.submit_search);

                tv_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.clear();
           //             Log.d("-----", "第几个" + count);
                        arragementService.getUpdateStudent(context, listselect.get(count).get(0));
                        listdata.clear();
                        mAdapter.clearItems();
                        listdata=arragementService.getStudentData();
                        mAdapter.updateItems(listdata, true);
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
        private List<Student> mBooks = new ArrayList<Student>();
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
                     ivBook = (ImageView) v.findViewById(R.id.ivBook);
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


        public void updateItems(List<Student> books, boolean animated) {
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
                    .inflate(R.layout.book_item, parent, false);
            //v.setBackgroundResource(mBackground);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            runEnterAnimation(holder.itemView, position);
            Student book = mBooks.get(position);
         //   holder.tvTitle.setText(book.get(0));
            String desc = "学生: " + book.getText() ;

            holder.tvDesc.setText(desc);
            if(book.getBitmap()!=null)
            holder.ivBook.setImageBitmap(book.getBitmap());
//            try {
//                URL url=new URL(mImageList.get(position));
//                URLConnection conn= null;
//                conn = url.openConnection();
//                conn.connect();
//                bm= BitmapFactory.decodeStream(conn.getInputStream());
//                holder.ivBook.setImageBitmap(bm);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }



//
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


        public Student getBook(int pos) {
            return mBooks.get(pos);
        }
    }
}
