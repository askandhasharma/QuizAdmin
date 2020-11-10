package com.guiz.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.provider.ContactsContract;
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

public class UserList extends AppCompatActivity {

    RecyclerView recyclerView ;
    List<ContainerClass.User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("quiz/" + MainActivity.quiz.getQid() );
        ref.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    userList.add(ds.getValue(ContainerClass.User.class));
                }
                Adapter adapter = new Adapter(userList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class Adapter extends RecyclerView.Adapter<UserList.Adapter.ProductViewHolder> {

        private List<ContainerClass.User> list;

        public Adapter(List<ContainerClass.User> productList) {
            this.list = productList;
        }

        @Override
        public UserList.Adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //inflating and returning our view holder
            LayoutInflater inflater = LayoutInflater.from(UserList.this);
            View view = inflater.inflate(R.layout.quiz_card, parent, false);
            return new UserList.Adapter.ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final UserList.Adapter.ProductViewHolder holder, int position) {
            //getting the product of the specified position

            ContainerClass.User user = list.get(position);

            holder.name.setText(user.getName());
            holder.marks.setText(user.getMarks());
            holder.ranking.setText(position + 1);
        }


        @Override
        public int getItemCount() {
            return list.size();
        }


        class ProductViewHolder extends RecyclerView.ViewHolder {

            TextView ranking, name, marks;

            public ProductViewHolder(View itemView) {
                super(itemView);

                ranking = itemView.findViewById(R.id.ranking);
                name = itemView.findViewById(R.id.name);
                marks = itemView.findViewById(R.id.marks);
            }
        }
    }
}