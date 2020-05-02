package com.example.ragdoll;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.graphics.Point;
import android.graphics.Matrix;
import android.util.Log;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    Button reset;
    Button about;
    Button newDollModel;
    DrawDoll drawdoll;
    AlertDialog.Builder dlgAlert;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup view_group = (ViewGroup) findViewById(R.id.main_region);
        drawdoll = new DrawDoll(this.getBaseContext());
        view_group.addView(drawdoll);



        reset = (Button)findViewById(R.id.Reset);
        about = (Button)findViewById(R.id.About);
        newDollModel = (Button)findViewById(R.id.NewItem);


        dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Ragdoll\nName: Dainan Zhang\nStudentID: 20729582");
        dlgAlert.setCancelable(true);



        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                drawdoll.reset();
            }
        });


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dlgAlert.create().show();
            }
        });


        final Intent intent = new Intent(this, SecondActivity.class);

        newDollModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

    }

}
