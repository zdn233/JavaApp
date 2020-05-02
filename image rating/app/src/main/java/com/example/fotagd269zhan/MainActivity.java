package com.example.fotagd269zhan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mainActivity";


    //vars
    private ArrayList<String> myURLs = new ArrayList<String>();
    private ArrayList<Float> myRatings = new ArrayList<Float>();
    RecyclerView myRecyclerView;
    ProductAdapter myAdapter;
    //ratings to filter
    float myRatingstofilter = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button myClearbutton = findViewById(R.id.clear_button);
        final float inital = 0;
        for (int i = 0; i < 10; ++i) {
            myRatings.add(inital);
        }
        final RatingBar myfilterRatingBar = findViewById(R.id.filterRatingBar);
        myClearbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myfilterRatingBar.setRating(inital);
                myURLs.clear();
                for (int i = 0; i < 10; ++i) {
                    myRatings.set(i, inital);
                }
                initRecyclerView();
            }
        });
        Button myLoadbutton = findViewById(R.id.load_button);
        myLoadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myfilterRatingBar.setRating(inital);
                myURLs.clear();
                for (int i = 0; i < 10; ++i) {
                    myRatings.set(i, inital);
                }
                InitImageBitmaps();
            }
        });
        myfilterRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                myRatings = myAdapter.myRatings;
                myURLs.clear();
                myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/bunny.jpg");
                myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/chinchilla.jpg");
                myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/doggo.jpg");
                myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/fox.jpg");
                myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/hamster.jpg");
                myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/husky.jpg");
                myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/kitten.png");
                myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/loris.jpg");
                myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/puppy.jpg");
                myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/sleepy.png");
                myRatingstofilter = myfilterRatingBar.getRating();
                int track = 0;
                for (int i = 0; i < 10; ++i) {
                    if (myRatings.get(i) < myfilterRatingBar.getRating()) {
                        myURLs.remove(i - track);
                        track++;
                    }
                }
                initRecyclerView();
            }
        });
        initRecyclerView();
    }


    private void InitImageBitmaps() {

        myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/bunny.jpg");
        myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/chinchilla.jpg");
        myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/doggo.jpg");
        myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/fox.jpg");
        myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/hamster.jpg");
        myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/husky.jpg");
        myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/kitten.png");
        myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/loris.jpg");
        myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/puppy.jpg");
        myURLs.add("https://www.student.cs.uwaterloo.ca/~cs349/s19/assignments/images/sleepy.png");

        initRecyclerView();
    }


    private void initRecyclerView() {
        myRecyclerView = findViewById(R.id.recycler_view);
        myAdapter = new ProductAdapter(myURLs, myRatings, this);
        myRecyclerView.setAdapter(myAdapter);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            myRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            myRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            initRecyclerView();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            initRecyclerView();
        }
    }
}
