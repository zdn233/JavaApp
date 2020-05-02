package com.example.ragdoll;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

public class SecondActivity extends AppCompatActivity{

    Button reset_second;
    Button about_second;
    Button newitem_second;
    DrawAnimal drawanimal;
    AlertDialog.Builder dlgAlert_second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        ViewGroup view_group = (ViewGroup) findViewById(R.id.main_region_second);
        drawanimal = new DrawAnimal(this.getBaseContext());
        view_group.addView(drawanimal);


        reset_second = (Button) findViewById(R.id.Reset_second);
        about_second = (Button) findViewById(R.id.About_second);
        newitem_second = (Button) findViewById(R.id.NewItem_second);


        dlgAlert_second = new AlertDialog.Builder(this);
        dlgAlert_second.setMessage("Ragdoll\nName: Dainan Zhang\nStudentID: 20729582");
        dlgAlert_second.setCancelable(true);


        reset_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawanimal.reset();
            }
        });


        about_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgAlert_second.create().show();
            }
        });

        final Intent intent_second = new Intent(this, MainActivity.class);

        newitem_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent_second);
            }
        });
    }
}
