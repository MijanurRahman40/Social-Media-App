package com.example.social_media_app.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.social_media_app.Notifications.Token;
import com.example.social_media_app.ViewsModel.BottomMenuFragments.HomeFragment;
import com.example.social_media_app.ViewsModel.BottomMenuFragments.ProfileFragment;
import com.example.social_media_app.R;
import com.example.social_media_app.ViewsModel.BottomMenuFragments.UsersFragment;
import com.example.social_media_app.ViewsModel.BottomMenuFragments.ChatListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class DashboardActivity extends AppCompatActivity {

    // firebase auth
    FirebaseAuth firebaseAuth;

    ActionBar actionBar;

    String myUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //  Action and its title
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Profile");

        // firebase init
        firebaseAuth = FirebaseAuth.getInstance();

        // bottom navigation
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        // home fragment transaction (default on start)
        actionBar.setTitle("Home"); // change action bar title
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, homeFragment, "");
        fragmentTransaction.commit();

        checkUserStatus();

    }

    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }

    public void updateToken(String token) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token myToken = new Token(token);
        databaseReference.child(myUID).setValue(myToken);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // handle fragment transaction
            switch (item.getItemId()) {
                case R.id.nav_home:
                    // home fragment transaction
                    actionBar.setTitle("Home"); // change action bar title
                    HomeFragment homeFragment = new HomeFragment();
                    FragmentTransaction fragmentTransaction_1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction_1.replace(R.id.content, homeFragment, "");
                    fragmentTransaction_1.commit();
                    return true;

                case R.id.nav_profile:
                    // profile fragment transaction
                    actionBar.setTitle("Profile"); // change action bar title
                    ProfileFragment profileFragment = new ProfileFragment();
                    FragmentTransaction fragmentTransaction_2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction_2.replace(R.id.content, profileFragment, "");
                    fragmentTransaction_2.commit();
                    return true;

                case R.id.nav_users:
                    // users fragment transaction
                    actionBar.setTitle("Users"); // change action bar title
                    UsersFragment usersFragment = new UsersFragment();
                    FragmentTransaction fragmentTransaction_3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction_3.replace(R.id.content, usersFragment, "");
                    fragmentTransaction_3.commit();
                    return true;

                case R.id.nav_chat:
                    // users fragment transaction
                    actionBar.setTitle("Chats"); // change action bar title
                    ChatListFragment chatListFragment = new ChatListFragment();
                    FragmentTransaction fragmentTransaction_4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction_4.replace(R.id.content, chatListFragment, "");
                    fragmentTransaction_4.commit();
                    return true;

            }
            return false;
        }
    };

    private void checkUserStatus() {
        // get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // user is signed in stay here
            // set email of logged in user
            // emailText.setText(user.getEmail());
            myUID = user.getUid();

            // save uid of current signed in user in shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences("SP_USER", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Current_USERID", myUID);
            editor.apply();

            // update token
            updateToken(FirebaseInstanceId.getInstance().getToken());

        } else {
            // user not sign in, go to main activity
            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        // check on start of app
        checkUserStatus();
        super.onStart();
    }
}
