package com.guiz.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FrangmentUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrangmentUser extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView ;
    List<ContainerClass.Quiz> qList ;

    public FrangmentUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrangmentUser.
     */
    // TODO: Rename and change types and number of parameters
    public static FrangmentUser newInstance(String param1, String param2) {
        FrangmentUser fragment = new FrangmentUser();
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
        View view =  inflater.inflate(R.layout.fragment_frangment_user, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.RV);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("quiz");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                qList = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    qList.add(ds.getValue(ContainerClass.Quiz.class));
                }

                Adapter adapter = new Adapter(qList);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    public class Adapter extends RecyclerView.Adapter<FrangmentUser.Adapter.ProductViewHolder> {

        private List<ContainerClass.Quiz> list;

        public Adapter( List<ContainerClass.Quiz> productList) {
            this.list = productList;
        }

        @Override
        public FrangmentUser.Adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //inflating and returning our view holder
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.quiz_card, parent, false);
            return new FrangmentUser.Adapter.ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final FrangmentUser.Adapter.ProductViewHolder holder, int position) {
            //getting the product of the specified position

            ContainerClass.Quiz quiz = list.get(position);

            holder.topic.setText(quiz.getTopic());
            holder.qid.setText(quiz.getQid());
            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "Hey! " +
                            "\n It is quiz id :- " +
                            "\n " + quiz.getQid() +
                            "\n With above id you can attend this quiz");
                    startActivity(intent);
                }

            });


            holder.next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.quiz = quiz;
                    startActivity(new Intent(getActivity(), UserList.class));
                }
            });
        }


        @Override
        public int getItemCount() {
            return list.size();
        }


        class ProductViewHolder extends RecyclerView.ViewHolder {

            TextView topic, qid;
            ImageView share, next ;

            public ProductViewHolder(View itemView) {
                super(itemView);

                topic = itemView.findViewById(R.id.topic);
                qid = itemView.findViewById(R.id.qid);
                share = itemView.findViewById(R.id.share);
                next = itemView.findViewById(R.id.next);
            }
        }
    }
}