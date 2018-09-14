package com.androidtutorialshub.loginregister.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidtutorialshub.loginregister.R;
import com.androidtutorialshub.loginregister.adapters.UsersRecyclerAdapter;
import com.androidtutorialshub.loginregister.model.Book;
import com.androidtutorialshub.loginregister.model.User;
import com.androidtutorialshub.loginregister.model.UserResp;
import com.androidtutorialshub.loginregister.network.ApiInterface;
import com.androidtutorialshub.loginregister.network.ApiUtils;
import com.androidtutorialshub.loginregister.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class UsersListActivity extends AppCompatActivity {

    private AppCompatActivity activity = UsersListActivity.this;
    private AppCompatTextView textViewName;
    private RecyclerView recyclerViewUsers;
    private List<User> listUsers;
    private UsersRecyclerAdapter usersRecyclerAdapter;
    private DatabaseHelper databaseHelper;
    private ImageView mLogoutBtn;
    private ApiInterface mApiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        getSupportActionBar().setTitle("");

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initViews();
        initObjects();
        sendNotification();



    }

    public void sendNotification() {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.logo)
                            .setContentTitle("Notifications Example")
                            .setContentText("Hi welcome !!");

            // Add as notification
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * This method is to initialize views
     */
    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerViewUsers);
        mApiService = ApiUtils.getAPIService();
        mLogoutBtn = findViewById(R.id.imageView);
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        SharedPreferences settings = getSharedPreferences("Login", 0);
        settings.edit()
                .putString("email", null )
                .putString("password" ,null)
                .commit();

        Intent intent = new Intent( getApplicationContext() , LoginActivity.class);
        startActivity(intent);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listUsers = new ArrayList<>();
        usersRecyclerAdapter = new UsersRecyclerAdapter(listUsers);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(usersRecyclerAdapter);
        databaseHelper = new DatabaseHelper(activity);

        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewName.setText(emailFromIntent);
//
       // getDataFromSQLite();

        getAllUsers();
        //getApiData();
    }

    private void getAllUsers() {
        if( listUsers  != null)
        listUsers.clear();
        Call<List<UserResp>> users = mApiService.allUsers();
        users.enqueue(new Callback<List<UserResp>>() {
            @Override
            public void onResponse(Call<List<UserResp>> call, Response<List<UserResp>> response) {

                if( response.isSuccessful() ){
                            List<UserResp> users = response.body();

                            User user ;
                            for( UserResp b:users){
                                user = new User();
                                user.setEmail( b.getEmail() );
                                user.setId(Integer.parseInt(b.getUserId()));
                                user.setName(b.getUserName());
                                user.setPassword(b.getPassword());
                                listUsers.add( user );
                            }

                            usersRecyclerAdapter.notifyDataSetChanged();

                        }

            }

            @Override
            public void onFailure(Call<List<UserResp>> call, Throwable t) {

            }
        });
    }

    private void getApiData() {
        mApiService.allBooks()
                .enqueue(new Callback<List<Book>>() {
                    @Override
                    public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
//

                        Toast.makeText(getApplicationContext(), response.body().size() +" is registered" ,Toast.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onFailure(Call<List<Book>> call, Throwable t) {

                    }
                });
    }

    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listUsers.clear();
                listUsers.addAll(databaseHelper.getAllUser());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                usersRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
