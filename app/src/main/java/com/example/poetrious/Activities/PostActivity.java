package com.example.poetrious.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.poetrious.ImageFilePath;
import com.example.poetrious.ImageUtil;
import com.example.poetrious.Models.Post_List;
import com.example.poetrious.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class PostActivity extends AppCompatActivity {
    String realPath;
    String realpath_del;
    final Bitmap[] bitmap = {null};
    String converted;
    private static final int PICK_IMAGE = 100;
    Uri takenPicUri;
    TextView post;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ImageView post_img;
    private int REQUEST_GET_SINGLE_FILE = 1;
    private StorageReference mStorageRef;
    String uri_check;
    Uri selectedImageUri = null;
    int pageno;
    EditText Img_des;
    Uri imageUri;
    Bitmap thumb_bitmap;
    Uri downloadUri;
    public String upload_uri;
    Bitmap takenpic;
    final int min = 1;
    final int max = 800;
    Uri fileProvider;
    String pic_trig;
    public ImageButton gallery, camera;
    File photoFile =new File(String.valueOf(getBaseContext()),"");
     ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_data);
        Img_des = findViewById(R.id.img_description);
        post = findViewById(R.id.do_post);
        post_img = findViewById(R.id.post_img);
        gallery = findViewById(R.id.select_gallery_img);
        camera=findViewById(R.id.select_camera_img);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        post.setTextColor(Color.parseColor("#C9D6DC"));
        post.setEnabled(false);


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
                pic_trig="gallery";
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PostActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PostActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                } else {


                    final int random = new Random().nextInt((max - min) + 1) + min;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
pic_trig="camera";

                    photoFile = getPhotoFileUri(random+".jpg");
                     fileProvider = FileProvider.getUriForFile(PostActivity.this, "com.mydomain.fileproviderr", photoFile);
                   intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                    startActivityForResult(intent, 12);

                }
            }
        });



        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final DatabaseReference myRef = database.getReference("User_Post").child(userid).push();
                final DatabaseReference myReff = database.getReference("Post");
                final String push_idd = myReff.push().getKey();
                final String push_id = myReff.push().getKey();
                if (post_img.getDrawable() != null && Img_des.getText().toString().trim().length() < 1) {
                    Toast.makeText(PostActivity.this, "please be patient ! post is uploading ", Toast.LENGTH_SHORT).show();
                    post.setEnabled(false);
                    post.setTextColor(Color.parseColor("#C9D6DC"));
                    progressDialog
                            = new ProgressDialog(PostActivity.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    getdownloadurl();

                } else if (post_img.getDrawable() == null && Img_des.getText().toString().length() > 0) {


                    DatabaseReference ref = database.getReference("Users_info");

                    ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            Post_List post_list = dataSnapshot.getValue(Post_List.class);


                            Name_usr = post_list.getUser_Name();
                            Pic_pub = post_list.getProfile_img_Download_link();

                            HashMap<String, String> hashMap = new HashMap<>();



                            hashMap.put("Name_usr", Name_usr);
                            hashMap.put("pub_pic", Pic_pub);
                            hashMap.put("post_id",push_idd);
                            hashMap.put("Description", Img_des.getText().toString().trim());
                            hashMap.put("post_time", String.valueOf(savetime()));
                            hashMap.put("usr_id", userid);


Log.e("timestamp",String.valueOf(savetime())+"");




                            //    Log.d("EEEE",+);

                            //    Toast.makeText(PostActivity.this, "Post Upload+Img+dec ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PostActivity.this, MainActivity.class));
                            finish();
                            myRef.setValue(hashMap);
                            assert push_idd != null;
                            myReff.child(push_idd).setValue(hashMap);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });











                    post.setEnabled(false);
                    post.setTextColor(Color.parseColor("#C9D6DC"));
                } else {
                 //   Toast.makeText(PostActivity.this, "please be patient ! post is uploading ", Toast.LENGTH_SHORT).show();
              progressDialog
                            = new ProgressDialog(PostActivity.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    post.setEnabled(false);
                    post.setTextColor(Color.parseColor("#C9D6DC"));


                    Log.d("newvalue", Name_usr + Pic_pub);
                    getImageurl();

                }


                //   Toast.makeText(PostActivity.this, "Post Upload ", Toast.LENGTH_SHORT).show();
                //  startActivity(new Intent(PostActivity.this,MainActivity.class));

            }
        });

        Img_des.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0 && post_img.getDrawable() == null) {


                    post.setEnabled(false);
                    post.setTextColor(Color.parseColor("#C9D6DC"));

                } else if (s.toString().trim().length() > 0) {


                    post.setEnabled(true);
                    post.setTextColor(Color.parseColor("#FFFFFF"));


                    //Do your stuff

                } else if (s.toString().trim().length() == 0 && post_img.getDrawable() != null) {
                    post.setEnabled(true);
                    post.setTextColor(Color.parseColor("#FFFFFF"));


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
   public   Long savetime()
    {
        Long timestamp=System.currentTimeMillis()/1000;
        return  timestamp;
    }
//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }
//
//    @Override
//    public void onDataPass(String data) {
//        Text_f_justText=data;
//
//    }


    //    @Override
//    public void onDataPass(String data, String uri) {
//             Text_f_TI=data;
//             uri_check=uri;
//      //  Log.d("MSGGG",Text_f_TI+Uri_f_TI);
//    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(PostActivity.this, MainActivity.class));
        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
         takenPicUri  = Uri.fromFile(photoFile.getAbsoluteFile());
      takenpic      = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
                    selectedImageUri = Objects.requireNonNull(data).getData();
                    realPath   = ImageFilePath.getPath(PostActivity.this, data.getData());

                    // Get the path from the Uri
                    final String path = getPathFromURI(selectedImageUri);
                    if (path != null) {
                        File f = new File(path);
                        selectedImageUri = Uri.fromFile(f);

                    }
//                    File filpath=new  File(selectedImageUri.getPath());
//                    try
//                    {
//                       thumb_bitmap=new Compressor(this)
//                               .setMaxWidth(200)
//                               .setMaxHeight(200)
//                               .setQuality(50)
//                               .compressToBitmap(filpath);
//                    }
//                    catch(IOException e)
//                    {
//                        e.printStackTrace();
//
//                    }
                    // Set the image in ImageView
                    uri_check = selectedImageUri.toString();
                    post_img.setImageURI(Uri.parse(uri_check));
                    Log.i("imgurl", uri_check);
                    post.setEnabled(true);

                    post.setTextColor(Color.parseColor("#FFFFFF"));


// set Fragmentclass Arguments


                }

        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
        if( requestCode == 12)
        {
            //  data.getExtras()
        //    Bitmap photo = (Bitmap) data.getExtras().get("data");
            realPath   = ImageFilePath.getPath(PostActivity.this, takenPicUri);

            post_img.setImageURI(takenPicUri);
        }
        else
        {
            Toast.makeText(PostActivity.this, "Picture NOt taken", Toast.LENGTH_LONG);
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        cursor = getContentResolver().query(contentUri, proj, null, null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public void getdownloadurl() {
        final StorageReference ref;
        UploadTask uploadTask = null;
        if(pic_trig.equals("gallery"))
        {
            final int random = new Random().nextInt((max - min) + 1) + min;
            bitmap[0] = ImageUtil.getInstant().getCompressedBitmap(String.valueOf(realPath));
            converted  = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap[0], random + " - " + System.currentTimeMillis(), null);
            //   String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap[0], "Title", null);
            realpath_del=  ImageFilePath.getPath(PostActivity.this, Uri.parse(converted));
            ref  = mStorageRef.child("images/" + converted + ".jpg");
            uploadTask = ref.putFile(Uri.parse(converted));
        }
        else
        {
            final int random = new Random().nextInt((max - min) + 1) + min;
            bitmap[0] = ImageUtil.getInstant().getCompressedBitmap(String.valueOf(realPath));
            converted  = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap[0], random + " - " + System.currentTimeMillis(), null);
            //   String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap[0], "Title", null);
            realpath_del=  ImageFilePath.getPath(PostActivity.this, Uri.parse(converted));
            ref = mStorageRef.child("images/" + converted + ".jpg");
            uploadTask = ref.putFile(Uri.parse(converted));
        }


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult();
                    progressDialog.dismiss();
                    File fdelete = new File(realpath_del);
                    deleteFileFromMediaStore(getContentResolver(),fdelete);
                 //   Toast.makeText(PostActivity.this, "Data has Saved ", Toast.LENGTH_SHORT).show();
                    Log.d("Imageurl", String.valueOf(downloadUri));
                    final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                final    DatabaseReference myRef = database.getReference("User_Post").child(userid).push();
              final      DatabaseReference myReff = database.getReference("Post");
                    final String push_id = myReff.push().getKey();

//                    if (post_img.getDrawable() != null && Img_des.getText().toString().length() > 0) {
//
//                        HashMap<String, String> hashMap = new HashMap<>();
//                        getdownloadurl();
//                        hashMap.put("Description", Img_des.getText().toString().trim());
//                        hashMap.put("img_post", upload_uri);
//                        hashMap.put("usr_id", userid);
//                        Toast.makeText(PostActivity.this, "Post Upload+Img+dec ", Toast.LENGTH_SHORT).show();
//
//                        myRef.setValue(hashMap);
//                        myReff.setValue(hashMap);
//                    }

                    DatabaseReference ref = database.getReference("Users_info");

                    ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            Post_List post_list = dataSnapshot.getValue(Post_List.class);


                            Name_usr = post_list.getUser_Name();
                            Pic_pub = post_list.getProfile_img_Download_link();

                            HashMap<String, String> hashMap = new HashMap<>();

                            hashMap.put("Name_usr", Name_usr);
                            hashMap.put("pub_pic", Pic_pub);



                            upload_uri = String.valueOf(downloadUri);


                            hashMap.put("img_post", upload_uri);
                            hashMap.put("post_time", String.valueOf(savetime()));
                            hashMap.put("usr_id", userid);
                            hashMap.put("post_id",push_id);

                                Log.d("EEEE",String.valueOf(savetime())+"");

                            //    Toast.makeText(PostActivity.this, "Post Upload+Img+dec ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PostActivity.this, MainActivity.class));
                            finish();

                            myRef.setValue(hashMap);
                            myReff.child(push_id).setValue(hashMap);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                    Log.d("onlyimg", upload_uri + "");

                } else {
                    // Handle failures
                    // ...

                    Log.d("down",
                            "failed");
                }
            }
        });
    }

    static String Name_usr, Pic_pub;

    public void getImageurl() {

        final StorageReference ref;
        UploadTask uploadTask = null;
        if(pic_trig.equals("gallery"))
        {
            final int random = new Random().nextInt((max - min) + 1) + min;
            bitmap[0] = ImageUtil.getInstant().getCompressedBitmap(String.valueOf(realPath));
            converted  = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap[0], random + " - " + System.currentTimeMillis(), null);
            //   String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap[0], "Title", null);
            realpath_del=  ImageFilePath.getPath(PostActivity.this, Uri.parse(converted));
            ref  = mStorageRef.child("images/" + converted + ".jpg");
             uploadTask = ref.putFile(Uri.parse(converted));
        }
        else
        {
            final int random = new Random().nextInt((max - min) + 1) + min;
            bitmap[0] = ImageUtil.getInstant().getCompressedBitmap(String.valueOf(realPath));
            converted  = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap[0], random + " - " + System.currentTimeMillis(), null);
            //   String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap[0], "Title", null);
            realpath_del=  ImageFilePath.getPath(PostActivity.this, Uri.parse(converted));
             ref = mStorageRef.child("images/" + converted + ".jpg");
            uploadTask = ref.putFile(Uri.parse(converted));
        }



        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult();
                    progressDialog.dismiss();
                    File fdelete = new File(realpath_del);
                    deleteFileFromMediaStore(getContentResolver(),fdelete);
                    Toast.makeText(PostActivity.this, "Data has Saved ", Toast.LENGTH_SHORT).show();
                    Log.d("Imageurl", String.valueOf(downloadUri));
                    final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    final DatabaseReference myRef = database.getReference("User_Post").child(userid).push();
                    final DatabaseReference myReff = database.getReference("Post");

                    final String push_id = myReff.push().getKey();
                    DatabaseReference ref = database.getReference("Users_info");

                    ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            Post_List post_list = dataSnapshot.getValue(Post_List.class);


                            Name_usr = post_list.getUser_Name();
                            Pic_pub = post_list.getProfile_img_Download_link();

                            HashMap<String, String> hashMap = new HashMap<>();

                            hashMap.put("Name_usr", Name_usr);
                            hashMap.put("pub_pic", Pic_pub);



                            upload_uri = String.valueOf(downloadUri);


                            hashMap.put("Description", Img_des.getText().toString().trim());
                            hashMap.put("img_post", upload_uri);

                            hashMap.put("usr_id", userid);
                            hashMap.put("post_id",push_id);
                            hashMap.put("post_time", String.valueOf(savetime()));

                            //    Log.d("EEEE",+);

                            //    Toast.makeText(PostActivity.this, "Post Upload+Img+dec ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PostActivity.this, MainActivity.class));
                            finish();

                            myRef.setValue(hashMap);
                            myReff.child(push_id).setValue(hashMap);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    Log.d("onlyimg", upload_uri + "");

                } else {
                    // Handle failures
                    // ...

                    Log.d("down",
                            "failed");
                }
            }
        });
    }
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "poetrious");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("TAG", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }
    public static void deleteFileFromMediaStore(final ContentResolver contentResolver, final File file) {
        String canonicalPath;
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            canonicalPath = file.getAbsolutePath();
        }
        final Uri uri = MediaStore.Files.getContentUri("external");
        final int result = contentResolver.delete(uri,
                MediaStore.Files.FileColumns.DATA + "=?", new String[]{canonicalPath});
        if (result == 0) {
            final String absolutePath = file.getAbsolutePath();
            if (!absolutePath.equals(canonicalPath)) {
                contentResolver.delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
            }
        }
    }
}

