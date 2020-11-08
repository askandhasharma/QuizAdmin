package com.guiz.admin;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentQuiz#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentQuiz extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText mcq_no;
    MaterialButton submit;
    ImageView plus , minus;
    TextInputEditText topic ;

    public static ContainerClass.Quiz quiz;

    public FragmentQuiz() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentQuiz.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentQuiz newInstance(String param1, String param2) {
        FragmentQuiz fragment = new FragmentQuiz();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_quiz, container, false);

        mcq_no = (EditText) view.findViewById(R.id.mcq_no);
        submit = (MaterialButton) view.findViewById(R.id.submit);
        plus = (ImageView) view.findViewById(R.id.plus);
        minus = (ImageView) view.findViewById(R.id.minus);
        topic = (TextInputEditText) view.findViewById(R.id.topic);

        MainActivity.quiz.setMcq_no(Integer.parseInt(mcq_no.getText().toString()));
        mcq_no.setText("5");

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int no  = Integer.parseInt(mcq_no.getText().toString());
                mcq_no.setText(String.valueOf(no+1));
            }
        });


        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int no  = Integer.parseInt(mcq_no.getText().toString());
                mcq_no.setText(String.valueOf(no-1));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(topic.getText().toString().isEmpty())
                    topic.setError("Please Enter the Topic of the Quiz");

               if(mcq_no.getText().toString().isEmpty())
                    mcq_no.setError("Please enter the no. of mcq questions");

                if( !mcq_no.getText().toString().isEmpty() && !topic.getText().toString().isEmpty()){
                    Intent intent = new Intent(getActivity(), CreateQuiz.class);
                    intent.putExtra("no", mcq_no.getText().toString());
                    intent.putExtra("topic",topic.getText().toString());
                    startActivity(intent);
                }

            }
        });



        return view;
    }
}