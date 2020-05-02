package com.example.fotagd269zhan;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class PopupActivity extends AppCompatActivity {

    private static final String TAG = "Popup Activity";

    //vars
    ImageView PopupImage;
    Bitmap Popupbitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_layout);
        getMyIntent();
        PopupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void getMyIntent() {
        if(getIntent().hasExtra("myImageURL")) {
            String imageURLinPopup = getIntent().getStringExtra("myImageURL");
            loadImage(imageURLinPopup);
        }
    }

    private void loadImage(String imageURLinPopup) {
        PopupImage = findViewById(R.id.myImagePopup);
        new downloadImageTask(PopupImage, Popupbitmap).execute(imageURLinPopup);
    }
}
