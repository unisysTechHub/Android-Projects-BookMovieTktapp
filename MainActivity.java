package com.uisys.bookmovieticket;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements ListMoviesFragment.OnFragmentInteractionListener{

    TabLayout tabLayout;
    ViewPager viewPager;
    DatabaseReference databaseReference;
    int currentPosition=0;
    boolean configurationChanged=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        SharedPreferences sharedPreferences = getSharedPreferences("Firebase",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("addChild"+"English",false);
        editor.putBoolean("addChild"+"Hindi",false);
        editor.putBoolean("addChild"+"Telugu",false);
        editor.commit();
      FirebaseDatabase.getInstance().getReference("/English")
               .addChildEventListener(new FirebasedatabaseChildEventListener(getApplicationContext(), "English"));

        FirebaseDatabase.getInstance().getReference("/Hindi").
                addChildEventListener(new FirebasedatabaseChildEventListener(getApplicationContext(), "Hindi"));

        FirebaseDatabase.getInstance().getReference("/Telugu").
                addChildEventListener(new FirebasedatabaseChildEventListener(getApplicationContext(), "Telugu"));

        if(savedInstanceState != null) {

           currentPosition=savedInstanceState.getInt("position");
           configurationChanged=true;

       }





        FirebaseDatabase.getInstance().getReference("/English").addValueEventListener(valueEventListener);
//          FirebaseDatabase.getInstance().getReference().
  //              child("Telugu").child("Dwaraka")
    //            .setValue("90");




        tabLayout= (TabLayout) findViewById(R.id.tabLayout);
        viewPager= (ViewPager) findViewById(R.id.viewPager);

        //TabLayout tabs
        TabLayout.Tab tab1 = tabLayout.newTab().setText("English");
        tabLayout.addTab(tab1,0);
        TabLayout.Tab tab2 = tabLayout.newTab().setText("Hindi");
        tabLayout.addTab(tab2,1);
        TabLayout.Tab tab3 = tabLayout.newTab().setText("Telugu");
        tabLayout.addTab(tab3,2);

        //viewPager
        viewPager.setAdapter(new MoviesListFragmentStatePagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                     viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    class MoviesListFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        public MoviesListFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(configurationChanged) {
                position = currentPosition;
                configurationChanged=false;
            }

            Fragment fragment=null;
                         switch (position)
                         {

                             case 0:
                                 //English
                                 fragment=ListMoviesFragment.newInstance("English",null);

                                 break;

                             case 1:
                                 //Hindi
                                 fragment=ListMoviesFragment.newInstance("Hindi",null);

                                 break;

                             case 2:
                                 //Telugu
                                 fragment=ListMoviesFragment.newInstance("Telugu",null);

                                 break;


                         }

                         currentPosition=position;


            return fragment;
        }

        @Override
        public int getCount() {
            return tabLayout.getTabCount();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

             outState.putInt("position",currentPosition);


              }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        databaseReferenceEnglish.removeEventListener(fbChildEventListenerEnglish);
  //      databaseReferenceHindi.removeEventListener(fbChildEventListenerHindi);
   //     databaseReferenceTelugu.removeEventListener(fbChildEventListenerTelugu);
    }
}
