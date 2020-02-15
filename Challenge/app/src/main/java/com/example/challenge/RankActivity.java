package com.example.challenge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RankActivity extends AppCompatActivity {
    DatabaseReference mDatabaseRef;
    FirebaseAuth mAuth;
    LinearLayout mLinLay;
    private ListView mListView;
    Custom_list_one arrayAdapter;
    private ArrayList<String> mUserList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Ranking");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();


        if (user == null) {
            startActivity(new Intent(RankActivity.this, MainActivity.class));
        }


        mLinLay = (LinearLayout) findViewById(R.id.linearLayout);
        mListView = (ListView) findViewById(R.id.listView);





        mUserList = new ArrayList<>();
        //mListView.setAdapter(new Custom_list_one(getApplicationContext(),mUserList));
        arrayAdapter=new Custom_list_one(getApplicationContext(),mUserList);
        mListView.setAdapter(arrayAdapter);

       // mUserList.add("1");
       // mUserList.add("2");

      //  arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUserList);
      //  mListView.setAdapter(arrayAdapter);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Total");
        Query query = mDatabaseRef.orderByChild("totalScore").limitToLast(1000);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot1 : dataSnapshot.getChildren()) {
                    final String id = Snapshot1.getKey();

                    Integer score = dataSnapshot.child(id).child("totalScore").getValue(Integer.class);

                    String score1 = String.valueOf(score);
                    String name = dataSnapshot.child(id).child("userName").getValue(String.class);

                    String institution=dataSnapshot.child(id).child("institution").getValue(String.class);





                   mUserList.add("Score: "+score1 + "\n"+"Name: " + name+"\n"+"Institution: "+institution);



                   arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    class Custom_list_one extends BaseAdapter {
        LayoutInflater layoutInflater;
        ViewHolder holder;
        ArrayList<String> arrayList = new ArrayList<String>();
        Context context;

        public Custom_list_one(  Context context,ArrayList<String> arrayList) {
            this.layoutInflater = LayoutInflater.from(context);
            this.arrayList = arrayList;
            this.context = context;
        }



        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public String getItem(int position) {
            return arrayList.get(getCount()-position-1);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.adapter, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(holder);
            } else {
                 holder=(ViewHolder) convertView.getTag();
            }
            holder.name.setText(position+1+" Rank"+"\n"+getItem(position));









            return convertView;



        }

        class ViewHolder{
             TextView name;
        }

    }
}







