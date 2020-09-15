package com.example.poetrious.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.poetrious.R;


public class Text_Image extends Fragment {



    public EditText img_des;
    private OnFragmentInteractionListener mListener;
    ImageButton go_gallery, go_camera;
    EditText Des;


    public ImageView imageView;

    Uri selectedImageUri = null;

    String check = "add";
    String uri;


    public Text_Image() {
        // Required empty public constructor
    }


    public interface OnDataPasss {
        public void onDataPass(String data,String uri);
    }

  public    OnDataPasss dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPasss) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text__image, container, false);


        imageView = view.findViewById(R.id.post_img);
        img_des = view.findViewById(R.id.img_description);

        img_des.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                View view1 = getActivity().findViewById(R.id.post_img);
                ImageView imageView = (ImageView) view1;
                View view = getActivity().findViewById(R.id.do_post);
                TextView textView = (TextView) view;
                if (s.toString().trim().length() == 0 && imageView.getDrawable() == null) {


                    textView.setEnabled(false);
                    Toast.makeText(getActivity().getApplicationContext(), "Post Disable", Toast.LENGTH_SHORT).show();

                } else if (s.toString().trim().length() == 0) {


                    textView.setEnabled(true);
                    Toast.makeText(getActivity().getApplicationContext(), "Post Disable", Toast.LENGTH_SHORT).show();

                    //Do your stuff

                } else {
                    textView.setEnabled(true);
                    Toast.makeText(getActivity().getApplicationContext(), "Post Enabled", Toast.LENGTH_SHORT).show();

Log.d("checkimg",check);

                    passData(img_des.getText().toString().trim(), check);

                }


            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        return view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }



    public void passData(String data,String URi) {
        dataPasser.onDataPass(data,URi);
    }
}
