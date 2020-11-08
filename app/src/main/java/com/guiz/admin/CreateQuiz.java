package com.guiz.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateQuiz extends AppCompatActivity {

    TextInputEditText question, opt1, opt2, opt3, ans;
    String _question, _opt1, _opt2, _opt3, _ans;
    MaterialButton add_question, create_quiz;
    List<ContainerClass.Question> qList;
    int no , q_no = 1;
    String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        add_question = (MaterialButton) findViewById(R.id.nxt_question);
        create_quiz = (MaterialButton) findViewById(R.id.create_quiz);
        question = (TextInputEditText) findViewById(R.id.question);
        opt1 = (TextInputEditText) findViewById(R.id.opt1);
        opt3 = (TextInputEditText) findViewById(R.id.opt2);
        opt2 = (TextInputEditText) findViewById(R.id.opt3);
        ans = (TextInputEditText) findViewById(R.id.ans);

        create_quiz.setVisibility(View.INVISIBLE);
        no = Integer.parseInt(getIntent().getStringExtra("no"));
        topic = getIntent().getStringExtra("topic");

        add_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _question = question.getText().toString();
                _opt1 = opt1.getText().toString();
                _opt2 = opt2.getText().toString();
                _opt3 = opt3.getText().toString();
                _ans = ans.getText().toString();

                if(_question.isEmpty())
                    question.setError("Please enter the question");
                if(_opt1.isEmpty())
                    opt1.setError("Please enter the option 1 ");
                if(_opt2.isEmpty())
                    opt2.setError("Please enter the option 2");
                if(_opt3.isEmpty())
                    opt3.setError("Please enter the option 3");
                if(_ans.isEmpty())
                    ans.setError("Please enter the answer");

                if(!_question.isEmpty() && !_opt1.isEmpty() && !_opt2.isEmpty() &&  !_opt3.isEmpty() && _ans.isEmpty()){
                    ContainerClass.Question question = new ContainerClass.Question();
                    List<String> option = new ArrayList<>();
                    option.add(_opt1);
                    option.add(_opt2);
                    option.add(_opt3);
                    question.setOptions(option);
                    question.setQuestion(_question);
                    question.setAnswer(_ans);

                    qList.add(question);
                    q_no += 1;

                    if(q_no == no){
                        add_question.setVisibility(View.INVISIBLE);
                        create_quiz.setVisibility(View.VISIBLE);
                    }

                    Toast.makeText(CreateQuiz.this, "Question Added!", Toast.LENGTH_SHORT).show();
                }
            }

        });

        create_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContainerClass.Quiz quiz = new ContainerClass.Quiz();
                quiz.setqList(qList);
                quiz.setMcq_no(no);
                quiz.setQid(createQid());
                quiz.setTopic(topic);
                quiz.setCreatorid(SplashScreen.admin.uid);
                quiz.setCreator(SplashScreen.admin.name);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("quiz");
                reference.child(quiz.getTopic()).child(quiz.getQid()).setValue(quiz);

                Toast.makeText(CreateQuiz.this, "Quiz created!", Toast.LENGTH_SHORT).show();

                finish();
            }
        });


    }

    private String createQid() {
        String qid = getNumericString(6);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("quiz/"+topic);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(qid))
                    createQid();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return qid;
    }


    String getNumericString(int n)
    {
        String AlphaNumericString = "0123456789";
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }


}