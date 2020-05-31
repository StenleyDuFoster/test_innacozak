package com.stenleone.innakozak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.stenleone.innakozak.api.JsonPlaceHolderApi;
import com.stenleone.innakozak.api.PostRepos;
import com.stenleone.innakozak.database.UserRepos;
import com.stenleone.innakozak.recycler.CardScript;
import com.stenleone.innakozak.recycler.RecyclerAdapter;

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

public class Activity2 extends AppCompatActivity {

    Bundle arguments;
    Intent intent;

    Realm realm;
    RealmConfiguration config;

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    ProgressBar load;

    ArrayList<CardScript> dataList;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        init();

        initRecycler(initDatabase());
        initRetrofit();
    }

    void init(){

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
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
        arguments = getIntent().getExtras();
        name = arguments.get("id").toString();
        setTitle("Repos\n" + name);
    }

    void animateFade(View v, float x, float y){
        ObjectAnimator fade = new ObjectAnimator().ofFloat(v,"alpha",x,y);
        fade.setDuration(500);
        fade.start();
    }

    void initRecycler(ArrayList<CardScript> localList){

        mAdapter = new RecyclerAdapter(localList);
        recyclerView.setAdapter(mAdapter);
        ((RecyclerAdapter) mAdapter).setOnItemClickListener(position -> {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(localList.get(position).getText2()));
            startActivity(intent);
        });
    }

    ArrayList<CardScript> initDatabase(){

        RealmResults<UserRepos> users = realm.where(UserRepos.class).equalTo("key",name).findAll();
        for(UserRepos c:users) {
            dataList.add(new CardScript(c.getName(), c.getUrl()));
        }

        return dataList;
    }

    private void createData(String n, String i,String k) {
        realm.beginTransaction();

        UserRepos user = new UserRepos();
        user.setKey(k);
        user.setName(n);
        user.setUrl(i);
        realm.copyToRealmOrUpdate(user);

        realm.commitTransaction();
    }

    void initRetrofit(){
        animateFade(load,0,1);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/users/"+name+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<PostRepos>> call = jsonPlaceHolderApi.getPostsRepos();

        call.enqueue(new Callback<List<PostRepos>>() {
            @Override
            public void onResponse(Call<List<PostRepos>> call, Response<List<PostRepos>> response) {

                if (!response.isSuccessful()) {
                    return;
                }

                List<PostRepos> postUsers = response.body();
                dataList = new ArrayList<>();

                for (PostRepos post : postUsers) {
                    dataList.add(new CardScript(post.getLogin(), post.getId()));
                    createData(post.getLogin(), post.getId(),name);
                }
                initRecycler(dataList);
                animateFade(load,load.getAlpha(),0);
            }
            @Override
            public void onFailure(Call<List<PostRepos>> call, Throwable t) {
                animateFade(load,load.getAlpha(),0);
                Toast.makeText(Activity2.this, "no internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
