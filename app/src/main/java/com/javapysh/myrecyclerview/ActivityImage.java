package com.javapysh.myrecyclerview;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ActivityImage extends AppCompatActivity {

    String myImages;
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        imageView=findViewById(R.id.imageView2);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            myImages = extras.getString("image");
        }

        Picasso.get().load(myImages).placeholder(R.color.neutral)
                .error(R.drawable.error2).into(imageView);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
