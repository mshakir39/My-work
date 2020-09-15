package com.example.poetrious.Activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.poetrious.ImageFilePath;
import com.example.poetrious.ImageUtil;
import com.example.poetrious.Models.Post_List;
import com.example.poetrious.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class EditProfile extends AppCompatActivity {
    ImageView imageView, upload_Img;
    TextView change_pic;
    ImageView post_img;
    EditText name, bio;
    String realPath;
    String realpath_del;
    String converted;
    final Bitmap[] bitmap = {null};
    Uri conv_uri;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users_info");
    String user_id;
    String current_name;
    File photoFile =new File(String.valueOf(getBaseContext()),"");
    private static final int PICK_IMAGE = 100;

    FirebaseStorage storage;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    Uri downloadUri;
    String value;
    private int REQUEST_GET_SINGLE_FILE = 1;
    Uri selectedImageUri = null;
    Button Logout, Edit;
    ImageView imageVieww, back;
    final int min = 1;
    final int max = 800;
    DatabaseReference reff;


    RecyclerView recyclerView;
    List<Post_List> lists;
    Uri fileProvider;
    String pic_trig;
    RecyclerView.Adapter adapter;
    Dialog dialog;
    Uri takenPicUri;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditProfile.this, Drawer.class));
        Animatoo.animateSlideRight(EditProfile.this);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        dialog = new Dialog(this);
        imageView = findViewById(R.id.profile_image);
        imageVieww = findViewById(R.id.imageView2);
        storage = FirebaseStorage.getInstance();
        name = findViewById(R.id.editTextTextPersonName);
        bio = findViewById(R.id.editTextTextPersonName2);
        back = findViewById(R.id.go_back);

        storageReference = storage.getReference();
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        change_pic = findViewById(R.id.textView);
        DatabaseReference update = FirebaseDatabase.getInstance()
                .getReference("Users_info").child(user_id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfile.this, Drawer.class));
                finish();

            }
        });
        update.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                current_name = String.valueOf(snapshot.child("User_Name").getValue(String.class));
                name.setText(current_name);
                Log.e("NAME56", current_name + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        imageVieww.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().length() < 1) {
                    Toast.makeText(EditProfile.this, "Can't Update Name! ", Toast.LENGTH_SHORT).show();
                } else {

                    DatabaseReference updateData = FirebaseDatabase.getInstance()
                            .getReference("Users_info").child(user_id);

                    updateData.child("User_Name").setValue(name.getText().toString().trim());
                    post_name_change();
                    progressDialog = new ProgressDialog(EditProfile.this);

                    progressDialog.setTitle("Saving...");
                    progressDialog.setMessage("Please Wait !");
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            progressDialog.dismiss();
                            startActivity(new Intent(EditProfile.this, Drawer.class));
                            Animatoo.animateSlideRight(EditProfile.this);
                            finish();

                        }
                    }, 3000);
                }


            }
        });


        myRef.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("User_Name").getValue(String.class);
                 value = dataSnapshot.child("Profile_img_Download_link").getValue(String.class);
                Uri myUri = Uri.parse(value);
                //         Uri name_uri=Uri.parse(name);


                Picasso.with(EditProfile.this).load(myUri).into(imageView);
                //    prof_name.setText(name);
                //     Log.d("myUri", myUri.toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        change_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setContentView(R.layout.picker_popup);

                TextView close = dialog.findViewById(R.id.dialog_close);
                upload_Img = dialog.findViewById(R.id.upload_img);
                Button upl = dialog.findViewById(R.id.upload);
                ImageView cam, gallery;
                cam = dialog.findViewById(R.id.cam_prof);
                gallery = dialog.findViewById(R.id.gallery_prof);
cam.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (ActivityCompat.checkSelfPermission(EditProfile.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {


            final int random = new Random().nextInt((max - min) + 1) + min;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            pic_trig="camera";

            photoFile = getPhotoFileUri(random+".jpg");
            fileProvider = FileProvider.getUriForFile(EditProfile.this, "com.mydomain.fileproviderr", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
            startActivityForResult(intent, 12);

        }
    }
});

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("image/*");
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GET_SINGLE_FILE);

                        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityForResult(gallery, PICK_IMAGE);
                        pic_trig="gallery";
                    }
                });


                upl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (converted == null&&takenPicUri==null) {
                            Toast.makeText(EditProfile.this, "Pick Image First !", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(EditProfile.this, "Will Take Time to Upload !", Toast.LENGTH_SHORT).show();
                            //  delete_img();
                            StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(value);
                            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // File deleted successfully
                                    Log.d("TAG", "onSuccess: deleted file");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Uh-oh, an error occurred!
                                    Log.d("TAG", "onFailure: did not delete file");
                                }
                            });
                            getdownloadurl();

                        }

                    }

                });


                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }

                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takenPicUri  = Uri.fromFile(photoFile.getAbsoluteFile());
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            selectedImageUri = data.getData();
          realPath   = ImageFilePath.getPath(EditProfile.this, data.getData());

            Bitmap conv;
//            try {
//   bitmap[0] = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImageUri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//           bitmap .compress(Bitmap.CompressFormat.JPEG, 20, out);
//            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
          //  String path = MediaStore.Images.Media.insertImage(getContentResolver(), reduzed, "Title", null);
//            conv = Bitmap.createScaledBitmap(bitmap, 160, 160, true);
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            conv.compress(Bitmap.CompressFormat.JPEG, 20, bytes);

           // conv_uri= Uri.parse(selectedImageUri);
//            new Handler().postDelayed(new Runnable(){
//                @Override
//                public void run() {
//
//
//
//                }
//            }, 3000);

            upload_Img.setImageURI(Uri.parse(String.valueOf(selectedImageUri)));
            // final String path = getPathFromURI(selectedImageUri);

          //  Log.e("URIIIIIIIIII", filePath + "");
        }
        if( requestCode == 12)
        {
            //  data.getExtras()
            //    Bitmap photo = (Bitmap) data.getExtras().get("data");
            realPath   = ImageFilePath.getPath(EditProfile.this,takenPicUri);

            upload_Img.setImageURI(takenPicUri);
        }

    }

    String Name_usr, Pic_pub;

    public void getdownloadurl() {
        final StorageReference ref;
        UploadTask uploadTask = null;
        if(pic_trig.equals("gallery"))
        {
            final int random = new Random().nextInt((max - min) + 1) + min;
            bitmap[0] = ImageUtil.getInstant().getCompressedBitmap(String.valueOf(realPath));
            converted  = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap[0], random + " - " + System.currentTimeMillis(), null);
            //   String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap[0], "Title", null);
            realpath_del=  ImageFilePath.getPath(EditProfile.this, Uri.parse(converted));

            Log.e("PAth",realPath+"||}|}|}|}|}"+realpath_del);
            ref  = storageReference.child("images/" + converted + ".jpg");
            uploadTask = ref.putFile(Uri.parse(converted));
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
        }
        else
        {
            final int random = new Random().nextInt((max - min) + 1) + min;
            bitmap[0] = ImageUtil.getInstant().getCompressedBitmap(String.valueOf(realPath));
            converted  = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap[0], random + " - " + System.currentTimeMillis(), null);
            //   String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap[0], "Title", null);
            realpath_del=  ImageFilePath.getPath(EditProfile.this, Uri.parse(converted));

            Log.e("PAth",realPath+"||}|}|}|}|}"+realpath_del);
            ref = storageReference.child("images/" + converted + ".jpg");
            uploadTask = ref.putFile(Uri.parse(converted));
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
        }


        Task<Uri> urlTask = uploadTask.continueWithTask(new
                                                                Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                    Log.i("URI", String.valueOf(downloadUri));
                    Log.d("URI", String.valueOf(downloadUri));


                    progressDialog.dismiss();
                    dialog.dismiss();
                    File fdelete = new File(realpath_del);
                deleteFileFromMediaStore(getContentResolver(),fdelete);
                    final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Log.e("Delete?",realPath+"   ||||   "+converted);
                    Log.i("URI", userid);


                    reff = database.getReference("Users_info").child(userid);
                    DatabaseReference updateData = FirebaseDatabase.getInstance()
                            .getReference("Users_info").child(userid);
                    String link = String.valueOf(downloadUri);
                    updateData.child("Profile_img_Download_link").setValue(link);


                    Log.i("URI3rd", "3rd" + downloadUri);
                    post_img_change();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });


    }

    public void post_name_change() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("User_Post");
        databaseReference1.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                    String key = dataSnapshot1.getKey();


                    assert key != null;
                    DatabaseReference updateData = FirebaseDatabase.getInstance()
                            .getReference("User_Post").child(userid).child(key);
                    //   String link = String.valueOf(downloadUri);
                    updateData.child("Name_usr").setValue(name.getText().toString().trim());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //     lists.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String key = dataSnapshot1.getKey();
                    String u = dataSnapshot1.child("usr_id").getValue(String.class);

                    if (Objects.equals(u, userid)) {
                        assert key != null;
                        DatabaseReference updateData = FirebaseDatabase.getInstance()
                                .getReference("Post").child(key);
                        //   String link = String.valueOf(downloadUri);
                        updateData.child("Name_usr").setValue(name.getText().toString().trim());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void post_img_change() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("User_Post");
        final String userid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        databaseReference1.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                    String key = dataSnapshot1.getKey();


                    assert key != null;
                    DatabaseReference updateData = FirebaseDatabase.getInstance()
                            .getReference("User_Post").child(userid).child(key);

                    String link = String.valueOf(downloadUri);
                    updateData.child("pub_pic").setValue(link);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //     lists.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String key = dataSnapshot1.getKey();
                    String u = dataSnapshot1.child("usr_id").getValue(String.class);

                    if (Objects.equals(u, userid)) {
                        assert key != null;
                        DatabaseReference updateData = FirebaseDatabase.getInstance()
                                .getReference("Post").child(key);

                        String link = String.valueOf(downloadUri);
                        updateData.child("pub_pic").setValue(link);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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