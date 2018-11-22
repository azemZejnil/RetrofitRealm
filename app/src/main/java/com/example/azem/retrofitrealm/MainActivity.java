package com.example.azem.retrofitrealm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.azem.retrofitrealm.model.Values;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    RetrofitClient retrofitClient;
    List<Values> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofitInit();
        loadFromRealm();
        updateBase();

    }

    private void retrofitInit(){
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitClient = retrofit.create(RetrofitClient.class);
    }

    private void loadFromRealm(){
        Realm realm = Realm.getDefaultInstance();
        List<Values> valuesList= realm.where(Values.class).findAll();
        values= realm.copyFromRealm(valuesList);

        for(int i = 0; i<values.size();i++){
            Log.d("values"+i+": ",values.get(i).getTitle());
        }

        realm.close();
    }

    private void updateBase(){
        retrofitClient.getPosts().enqueue(new Callback<Values>() {
            @Override
            public void onResponse(Call<Values> call, Response<Values> response) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                realm.close();
                loadFromRealm();
                Log.e("call log",response.body().toString());
            }

            @Override
            public void onFailure(Call<Values> call, Throwable t) {
                Log.e("call log",t.getMessage());
            }
        });
    }


}
