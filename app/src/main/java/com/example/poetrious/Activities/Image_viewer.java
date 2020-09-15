package com.example.poetrious.Activities;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.poetrious.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Objects;

public class Image_viewer extends AppCompatActivity {
ImageView imageView;
PhotoView photoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.image_viewer);
        imageView=findViewById(R.id.imageView3);
        photoView=findViewById(R.id.viewer);
        photoView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        photoView.setAdjustViewBounds(true);
        Bundle bundle = getIntent().getExtras();
        final String PATH = Objects.requireNonNull(bundle).getString("path");
        Log.e("Path",PATH+"   ");
        Glide.with(Image_viewer.this).load(PATH).into(photoView);
      //  photoView.setImageURI(Uri.parse(PATH));
imageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        PopupMenu Meenu = new PopupMenu(Image_viewer.this,imageView);

        Meenu.inflate(R.menu.save_img);
        Meenu.show();
        Meenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

                                    DownloadManager.Request request;
                request = new DownloadManager.Request(Uri.parse(PATH));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    downloadManager.enqueue(request);
                return true;
            }
        });
    }
});

    }
}
