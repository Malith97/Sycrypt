package com.synnlabz.sycryptr;

import android.app.ActionBar;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.synnlabz.sycryptr.account.AddAccount;
import com.synnlabz.sycryptr.database.DatabaseHelper;
import com.synnlabz.sycryptr.other.Model;
import com.synnlabz.sycryptr.other.Settings;
import com.synnlabz.sycryptr.adapters.Adapter;

import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


public class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener{
    private static final String TAG = "MainActivity";
    private FloatingActionButton fab;
    private SharedPreferences settings;
    public static final String THEME_Key = "app_theme";
    public static final String APP_PREFERENCES="sycryptr_settings";
    private int theme;
    private String filter = "";

    DatabaseHelper databaseHelper;
    ActionBar actionBar;
    private RecyclerView mRecyclerView;
    private ArrayList<Model> arrayList;
    private com.synnlabz.sycryptr.adapters.Adapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        theme = settings.getInt(THEME_Key, R.style.AppTheme);
        setTheme(theme);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        setupNavigation(savedInstanceState, toolbar);
        // init recyclerView
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        showAllRecord();

        // init fab Button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View reveal = findViewById(R.id.reveal_background);
                int centerX = (fab.getLeft() + fab.getRight()) / 2;
                int centerY = (fab.getTop() + fab.getBottom()) / 2;
                int startRadius = 0;
                // get the final radius for the clipping circle
                int endRadius = Math.max(reveal.getWidth(), reveal.getHeight());
                SupportAnimator anim = ViewAnimationUtils.createCircularReveal(reveal, centerX, centerY, startRadius, endRadius);

                reveal.setVisibility(View.VISIBLE);
                anim.start();

                anim.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        Intent intent = new Intent(MainActivity.this, AddAccount.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
//                        reveal.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel() {

                    }

                    @Override
                    public void onAnimationRepeat() {

                    }
                });

            }
        });
    }

    private void showAllRecord() {
        databaseHelper = new DatabaseHelper(this);
        Adapter adapter = new Adapter(MainActivity.this,databaseHelper.getAllData(),mRecyclerView);
        mRecyclerView.setAdapter(adapter);
    }

    private void showFilterData(int type){
        databaseHelper = new DatabaseHelper(this);
        Adapter adapter = new Adapter(MainActivity.this,databaseHelper.getFilterData(type),mRecyclerView);
        mRecyclerView.setAdapter(adapter);
    }


    private void setupNavigation(Bundle savedInstanceState, Toolbar toolbar) {
        // Navigation menu items
        List<IDrawerItem> iDrawerItems = new ArrayList<>();
        iDrawerItems.add(new PrimaryDrawerItem().withName("Recent").withIcon(R.drawable.ic_all).withTypeface(Typeface.createFromAsset(getAssets(),"bold.ttf")));
        iDrawerItems.add(new PrimaryDrawerItem().withName("Social").withIcon(R.drawable.ic_social).withTypeface(Typeface.createFromAsset(getAssets(),"bold.ttf")));
        iDrawerItems.add(new PrimaryDrawerItem().withName("Website").withIcon(R.drawable.ic_web).withTypeface(Typeface.createFromAsset(getAssets(),"bold.ttf")));
        iDrawerItems.add(new PrimaryDrawerItem().withName("Cards").withIcon(R.drawable.ic_card).withTypeface(Typeface.createFromAsset(getAssets(),"bold.ttf")));
        iDrawerItems.add(new PrimaryDrawerItem().withName("Mail").withIcon(R.drawable.ic_mail).withTypeface(Typeface.createFromAsset(getAssets(),"bold.ttf")));
        iDrawerItems.add(new PrimaryDrawerItem().withName("Other").withIcon(R.drawable.ic_other).withTypeface(Typeface.createFromAsset(getAssets(),"bold.ttf")));

        // sticky DrawItems ; footer menu items

        List<IDrawerItem> stockyItems = new ArrayList<>();

        SwitchDrawerItem switchDrawerItem = new SwitchDrawerItem()
                .withName("Dark Theme")
                .withTypeface(Typeface.createFromAsset(getAssets(),"bold.ttf"))
                .withChecked(theme == R.style.AppTheme_Dark)
                .withIcon(R.drawable.ic_menu_slideshow)
                .withOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                        // TODO: 02/10/2018 change to darck theme and save it to settings
                        if (isChecked) {
                            settings.edit().putInt(THEME_Key, R.style.AppTheme_Dark).apply();
                        } else {
                            settings.edit().putInt(THEME_Key, R.style.AppTheme).apply();
                        }

                        // recreate app or the activity // if it's not working follow this steps
                        // MainActivity.this.recreate();

                        // this lines means wi want to close the app and open it again to change theme
                        TaskStackBuilder.create(MainActivity.this)
                                .addNextIntent(new Intent(MainActivity.this, MainActivity.class))
                                .addNextIntent(getIntent()).startActivities();
                    }
                });

        PrimaryDrawerItem primaryDrawerItem = new PrimaryDrawerItem()
                .withName("Settings")
                .withTypeface(Typeface.createFromAsset(getAssets(),"bold.ttf"))
                .withIcon(R.drawable.ic_settings)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent intent = new Intent(MainActivity.this, Settings.class);
                        startActivity(intent);
                        finish();
                        return true;
                    }
                });

        stockyItems.add(primaryDrawerItem);
        stockyItems.add(switchDrawerItem);

        // navigation menu header
        AccountHeader header = new AccountHeaderBuilder().withActivity(this)
                .addProfiles(new ProfileDrawerItem()
                        .withEmail("SynnLabz@gmail.com")
                        .withName("Malith Ileperuma")
                        .withIcon(R.drawable.propic))
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.drawable.headerback)
                .withSelectionListEnabledForSingleProfile(false) // we need just one profile
                .build();

        // Navigation drawer
        new DrawerBuilder()
                .withActivity(this) // activity main
                .withToolbar(toolbar) // toolbar
                .withSavedInstance(savedInstanceState) // saveInstance of activity
                .withDrawerItems(iDrawerItems) // menu items
                .withTranslucentNavigationBar(true)
                .withStickyDrawerItems(stockyItems) // footer items
                .withAccountHeader(header) // header of navigation
                .withOnDrawerItemClickListener(this) // listener for menu items click
                .build();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        showAllRecord();
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        if(position==1){
            mRecyclerView.setAdapter(null);
            showAllRecord();
            Toast.makeText(this, "Recent", Toast.LENGTH_SHORT).show();
        }else if(position==2){;
            mRecyclerView.setAdapter(null);
            showFilterData(1);
            Toast.makeText(this, "Social", Toast.LENGTH_SHORT).show();
        }else if(position==3){
            mRecyclerView.setAdapter(null);
            showFilterData(2);
            Toast.makeText(this, "Website", Toast.LENGTH_SHORT).show();
        }else if(position==4){
            mRecyclerView.setAdapter(null);
            showFilterData(3);
            Toast.makeText(this, "Cards", Toast.LENGTH_SHORT).show();
        }else if(position==5){
            mRecyclerView.setAdapter(null);
            showFilterData(4);
            Toast.makeText(this, "Mail", Toast.LENGTH_SHORT).show();
        }else if(position==6){
            mRecyclerView.setAdapter(null);
            showFilterData(5);
            Toast.makeText(this, "Other", Toast.LENGTH_SHORT).show();
        }else {
            mRecyclerView.setAdapter(null);
            showAllRecord();
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
        //nothing
    }
}



