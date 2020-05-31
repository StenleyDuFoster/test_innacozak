package com.stenleone.innakozak;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.stenleone.innakozak.api.JsonPlaceHolderApi;
import com.stenleone.innakozak.api.PostUser;
import com.stenleone.innakozak.database.User;
import com.stenleone.innakozak.recycler.RecyclerAdapter;
import com.stenleone.innakozak.recycler.CardScript;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Activity1 extends AppCompatActivity {

    Intent intent;

    Realm realm;
    RealmConfiguration config;

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    ProgressBar load;

    ArrayList<CardScript> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        init();

        initRecycler(initDatabase());
        initRetrofit();
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

    void init(){
        load = findViewById(R.id.load);
        Realm.init(this);
        config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        //Realm.deleteRealm(config);
        realm = Realm.getDefaultInstance();
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        dataList = new ArrayList<>();
        intent = new Intent(this, Activity2.class);
        Log.d("SATAN", "SATAN");
        Log.d("Instance ID", FirebaseInstanceId.getInstance().getId());


        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        //Init FCM and print token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "1"+ task.getException().toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    Toast.makeText(this,"2"+ token, Toast.LENGTH_SHORT).show();
                });
    }

    void animateFade(View v,float x,float y){
        ObjectAnimator fade = new ObjectAnimator().ofFloat(v,"alpha",x,y);
        fade.setDuration(500);
        fade.start();
    }

    ArrayList<CardScript> initDatabase(){

        RealmResults<User> users = realm.where(User.class).findAll();
        for(User c:users) {
            dataList.add(new CardScript(c.getTitle() + " id: " + c.getId(),
                    null));
        }

        return dataList;
    }

    private void createData(String n, String i) {
        realm.beginTransaction();

        User user = new User();
        user.setId(Integer.valueOf(i));
        user.setTitle(n);
        realm.copyToRealmOrUpdate(user);

        realm.commitTransaction();
    }

    void initRecycler(ArrayList<CardScript> localList){

        mAdapter = new RecyclerAdapter(localList);
        recyclerView.setAdapter(mAdapter);
        ((RecyclerAdapter) mAdapter).setOnItemClickListener(position -> {
            intent.putExtra("id", localList.get(position).getText1().split(" ")[0]);
            startActivity(intent);
        });
    }

    void initRetrofit(){
        animateFade(load,0,1);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<PostUser>> call = jsonPlaceHolderApi.getPostsUser();

        call.enqueue(new Callback<List<PostUser>>() {
            @Override
            public void onResponse(Call<List<PostUser>> call, Response<List<PostUser>> response) {

                if (!response.isSuccessful()) {
                    return;
                }

                List<PostUser> postUsers = response.body();
                dataList = new ArrayList<>();

                for (PostUser postUser : postUsers) {
                    dataList.add(new CardScript(postUser.getLogin() + " id: " + postUser.getId(),null));
                    createData(postUser.getLogin(), postUser.getId());
                }
                initRecycler(dataList);
                animateFade(load,load.getAlpha(),0);
            }
            @Override
            public void onFailure(Call<List<PostUser>> call, Throwable t) {
                animateFade(load,load.getAlpha(),0);
                Toast.makeText(Activity1.this, "no internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}