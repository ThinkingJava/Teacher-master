package com.aswifter.material;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aswifter.material.arragment.ArragmentFragment;
import com.aswifter.material.attend.AttendFragment;
import com.aswifter.material.course.CourseFragment;
import com.aswifter.material.teacherclass.TeacherclassFragment;
import com.aswifter.material.widget.BackHandledFragment;

public class MainActivity extends AppCompatActivity implements BackHandledFragment.BackHandlerInterface {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private BackHandledFragment selectedFragment;
    private NavigationView mNavigationView;

    private static final int ANIM_DURATION_TOOLBAR = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(mNavigationView);

        //profile Image
        setUpProfileImage();


      //  switchToBook();
        switchToAttend();
    }





  private void switchToAttend(){

       getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new AttendFragment(getApplication())).commit();
      mToolbar.setTitle(R.string.navigation_attend);

  }

    private void switchToArragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new ArragmentFragment(getApplication())).commit();
        mToolbar.setTitle(R.string.mavigation_arragment);
    }

    private void switchToTeacherclass(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new TeacherclassFragment(getApplication())).commit();
        mToolbar.setTitle(R.string.navigation_teacherclass);
    }
   private void switchToTeachercourse(){
       getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new CourseFragment(getApplication())).commit();
       mToolbar.setTitle(R.string.navigation_teachercourse);
   }
    private void setUpProfileImage() {

        findViewById(R.id.profile_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  switchToAttend();
                new LongTimeTask().execute();
                mDrawerLayout.closeDrawers();
                mNavigationView.getMenu().getItem(1).setChecked(true);

            }
        });
    }
    private class LongTimeTask extends AsyncTask {
        @Override
        protected String doInBackground(Object[] params) {
      TeacherName teacherName=new TeacherName();
         String text=  teacherName.getteachername(getApplicationContext());
            return text;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o!=null){
                TextView textView=(TextView)MainActivity.this.findViewById(R.id.person1);
                textView.setText("教师:"+o);
            }

        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            case R.id.navigation_item_attend:
                               switchToAttend();
                              break;
                            case R.id.navigation_item_arragment:
                                switchToArragment();
                                break;
                            case R.id.navigation_item_teacherclass:
                                switchToTeacherclass();
                                break;
                            case  R.id.navigation_item_course:
                               switchToTeachercourse();
                               break;
                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSelectedFragment(BackHandledFragment backHandledFragment) {
        this.selectedFragment = backHandledFragment;
    }


    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (selectedFragment == null || !selectedFragment.onBackPressed()) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                doExitApp();
            }
        }

    }
}
