package com.example.poetrious.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.poetrious.ImageFilePath;
import com.example.poetrious.ImageUtil;
import com.example.poetrious.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Add_Profile extends AppCompatActivity {

    //Widgets
    ImageView img_set;
    Button upload;
    private RadioGroup radioGroup;
    EditText name, mDisplayDate, OP_email;

    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private StorageReference mStorageRef;

    //Mixed
    Uri selectedImageUri = null;
    String realPath;
    String realpath_del;
    final Bitmap[] bitmap = {null};
    String converted;
    Dialog dialog;
    private static final int PICK_IMAGE = 100;
    Uri downloadUri;
    Bitmap thumb_bitmap = null;
    Bitmap compressed;
    final int min = 1;
    final int max = 800;
    ProgressDialog progressDialog;
    protected DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    public void onBackPressed() {
        dialog.setContentView(R.layout.exit);
        TextView YES = dialog.findViewById(R.id.Yes);
        TextView NO = dialog.findViewById(R.id.No);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        NO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        YES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__profile);
        dialog = new Dialog(this);
        img_set = findViewById(R.id.ul_image);
        name = findViewById(R.id.ul_name);
        upload = findViewById(R.id.ul_save);
        mDisplayDate = findViewById(R.id.Age);
        radioGroup = findViewById(R.id.Gender);
        OP_email = findViewById(R.id.op_email);
        mStorageRef = FirebaseStorage.getInstance().getReference();
//        SharedPreferences sp = getSharedPreferences("UID", MODE_PRIVATE);
//        final String cb1 = sp.getString("User_id", String.valueOf(false));
//        Log.e("qqqqqqqqqqqqqqqqqqq",cb1+"");
        img_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);

            }
        });

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Add_Profile.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;


                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);

            }
        };

        upload.setOnClickListener(new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                File imageZipperFile;
                if (selectedImageUri == null) {
                    Toast.makeText(Add_Profile.this, "Kindly Select an Image", Toast.LENGTH_SHORT).show();
                } else if (name.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Add_Profile.this, "Kindly Enter Your Name", Toast.LENGTH_SHORT).show();

                } else if (mDisplayDate.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Add_Profile.this, "Kindly Enter Date of Birth", Toast.LENGTH_SHORT).show();

                } else {

                    progressDialog
                            = new ProgressDialog(Add_Profile.this);
                    progressDialog.setTitle("Saving ...");
                    progressDialog.setMessage("Please Wait !");
                    progressDialog.show();
                    compress compress1 = new compress(thumb_bitmap);
                    compress1.start();


                    //    Objects.requireNonNull(firebaseUser).getUid();


                }

            }
        });


    }
//    private Uri getImageUri(Context context, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            selectedImageUri = data.getData();
            realPath   = ImageFilePath.getPath(Add_Profile.this, data.getData());
            img_set.setImageURI(selectedImageUri);
            // final String path = getPathFromURI(selectedImageUri);

            Log.e("URIIIIIIIIII", selectedImageUri + "");
        }

    }



    class back_save extends Thread {
        DatabaseReference ref;
        HashMap<String, String> data;

        back_save() {

        }

        public back_save(DatabaseReference myRef, HashMap<String, String> insert_profile_data) {
            ref = myRef;
            data = insert_profile_data;
        }

        @Override
        public void run() {
            ref.setValue(data);

        }
    }




    class compress extends Thread {
        Bitmap bit;
        Bitmap decodedByte;
        Uri newuri;

        compress(Bitmap bitmap) {
            bit = bitmap;
        }

        public compress() {

        }

        @Override
        public void run() {
            super.run();

            final int random = new Random().nextInt((max - min) + 1) + min;
            bitmap[0] = ImageUtil.getInstant().getCompressedBitmap(String.valueOf(realPath));
            converted  = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap[0], random + " - " + System.currentTimeMillis(), null);
            //   String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap[0], "Title", null);
            realpath_del=  ImageFilePath.getPath(Add_Profile.this, Uri.parse(converted));

            final StorageReference ref = mStorageRef.child("images/" + converted + ".jpg");
            Log.i("Emptt", String.valueOf(selectedImageUri));
            final UploadTask uploadTask = ref.putFile(Uri.parse(converted));


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


//                                final UploadTask uploadTask = ref.putBytes(thumb_byte);
//
//
//                                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                                    @Override
//                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                                        if (!task.isSuccessful()) {
//                                            throw task.getException();
//                                        }
//
//                                        // Continue with the task to get the download URL
//                                        return ref.getDownloadUrl();
//                                    }
//                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Uri> task) {
//                                        if(task.isSuccessful())
//                                        {
//                                            thumb_uri=task.getResult();
//                                            Log.d("Thumb_uri",thumb_uri+"");
//                                        }
//
//
//                                    }
//                                });
                        progressDialog.dismiss();
                        File fdelete = new File(realpath_del);
                        deleteFileFromMediaStore(getContentResolver(),fdelete);
                        //    Toast.makeText(Add_Profile.this, "Data has Saved ", Toast.LENGTH_SHORT).show();
                        Log.d("down", String.valueOf(downloadUri));
                        String Num = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                        //       Log.d("Uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        //     Log.d("number", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()));
                        DatabaseReference myRef = database.getReference("Users_info").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        HashMap<String, String> insert_profile_data = new HashMap<>();

                        insert_profile_data.put("User_Name", name.getText().toString().trim());
                        insert_profile_data.put("User_ID", (FirebaseAuth.getInstance().getCurrentUser().getUid()));
                        insert_profile_data.put("User_Number", Num);
                        insert_profile_data.put("Profile_img_Download_link", downloadUri.toString());
                        if (OP_email.getText().toString().isEmpty()) {
                            insert_profile_data.put("User_Email", "");
                        } else {
                            insert_profile_data.put("User_Email", OP_email.getText().toString().trim());

                        }


                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton) findViewById(selectedId);

                        String gender = radioButton.getText().toString();


                        insert_profile_data.put("User_DOB", mDisplayDate.getText().toString().trim());
                        insert_profile_data.put("Gender", gender);
                        startActivity(new Intent(Add_Profile.this, MainActivity.class));
                        back_save thread = new back_save(myRef, insert_profile_data);
                        thread.start();

                        SharedPreferences sp = getSharedPreferences("check", MODE_PRIVATE);
                        SharedPreferences.Editor et = sp.edit();
                        et.putBoolean("Datasave", true);
                        et.apply();


                    } else {
                        // Handle failures
                        // ...

                        Log.d("down",
                                "failed");
                    }
                }
            });


            //   Log.i("Empp",decodedByte.toString());


        }

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




