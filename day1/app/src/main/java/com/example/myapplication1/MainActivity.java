package com.example.myapplication1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication1.R.id.button;
import static com.example.myapplication1.R.id.imageView;
//import static com.example.myapplication1.R.id.button;

public class MainActivity extends AppCompatActivity {

   // Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pickImage = findViewById(R.id.button);

       pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //general share
              //  Intent myIntent = new Intent(Intent.ACTION_SEND);
               // myIntent.setType("text/plain");
              //  String body = "Your body here";
               // String sub = "Your Subject";
                //myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
              //  myIntent.putExtra(Intent.EXTRA_TEXT,body);
             //   startActivity(Intent.createChooser(myIntent, "Share Using"));

                //multi image share
                if(ActivityCompat.checkSelfPermission(MainActivity.this,
                         Manifest.permission.READ_EXTERNAL_STORAGE)
                            !=PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            100);

                    return;


                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

    }
    //multiple image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ImageView imageView = findViewById(R.id.imageView);
            List<Bitmap> bitmaps = new ArrayList<>();
            ClipData clipData = data.getClipData();

            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    try {
                        InputStream is = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            } else {

                Uri imageUri = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    bitmaps.add(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (Bitmap b : bitmaps) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                imageView.setImageBitmap(b);
                            }
                        });
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                }
            }).start();


        }
    }
}