package com.example.poetrious.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.poetrious.R;

public class Just_Test extends Fragment {
public EditText Text;
    private OnFragmentInteractionListener mListener;

    public Just_Test() {
        // Required empty public constructor
    }

    public interface OnDataPass {
        public void onDataPass(String data);
    }


     OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_just__test, container, false);

        Text=view.findViewById(R.id.description);


      Text.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {

          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {


              View view = getActivity().findViewById(R.id.do_post);
              TextView textView = (TextView) view;
              if(s.toString().trim().length()==0)
              {



                  textView.setEnabled(false);
                  Toast.makeText(getActivity().getApplicationContext(), "Post Disable", Toast.LENGTH_SHORT).show();

              }

              else
              {
                  textView.setEnabled(true);
                  Toast.makeText(getActivity().getApplicationContext(), "Post Enabled", Toast.LENGTH_SHORT).show();

                  passData(Text.getText().toString().trim());

              }

          }

          @Override
          public void afterTextChanged(Editable s) {

          }
      });

        return  view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void passData(String data) {
        dataPasser.onDataPass(data);
    }
}
