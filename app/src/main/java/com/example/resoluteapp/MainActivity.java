package com.example.resoluteapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.resoluteapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Commented out to remove navigation arrow at top of screen
        /*setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);*/

        //Commented out mail icon from activity_main.xml
        /*binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });*/
    }

    //Commented out menu from menu_main.xml
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    //Commented out settings option from menu_main.xml
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    //Function to store username and password values in SharedPreferences
    //Call this from fragment with ((MainActivity)getActivity()).setUsernameAndPassword(STRING);
    public void setUsernameAndPassword(String un, String pw){
        SharedPreferences sp = this.getSharedPreferences("com.example.ResoluteApp.SharedPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", un);
        editor.putString("password", "DUMMY PASSWORD (for security purposes)");
        editor.apply();
    }

    //Function to retrieve username from SharedPreferences
    //Call this function from fragment with "((MainActivity)getActivity()).getUsername();"
    public String getUsername(){
        SharedPreferences sp = this.getSharedPreferences("com.example.ResoluteApp.SharedPrefs",Context.MODE_PRIVATE);
        return sp.getString("username", "DEF USERNAME. BUG! REPORT!");
    }

    //Function to retrieve password from SharedPreferences
    //Call this function from fragment with "((MainActivity)getActivity()).getPassword();"
    public String getPassword(){
        SharedPreferences sp = this.getSharedPreferences("com.example.ResoluteApp.SharedPrefs",Context.MODE_PRIVATE);
        return sp.getString("password", "DEF PASSWORD. BUG! REPORT!");
    }


    //Function to check for the existence of a Username in SharedPreferences
        //Call this function from Fragment with "((MainActivity)getActivity()).checkForUsername();"
    public boolean checkForUsername(){
        SharedPreferences sp = this.getSharedPreferences("com.example.ResoluteApp.SharedPrefs",Context.MODE_PRIVATE);
        return sp.contains("username");
    }

    //Function to clear SharedPreferences. Done after login.
        //Call this function from Fragment with "((MainActivity)getActivity()).clearSharedPref();"
    public void clearSharedPref(){
        SharedPreferences sp = this.getSharedPreferences("com.example.ResoluteApp.SharedPrefs",Context.MODE_PRIVATE);
        sp.edit().remove("username").apply();
        sp.edit().remove("password").apply();
    }
  
    //Function to hash(encrypt) String
    //Call this function from Fragment with "((MainActivity)getActivity()).hashString(STRING);"
    public String hashString(String s){
        try {
            //Define imported hashing algorithm and hash String s
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(s.getBytes());

            byte[] result = messageDigest.digest(); //Byte array of hashed String

            //Rebuild byte array as hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(byte b: result){
                sb.append(String.format("%02x", b));
            }

            return sb.toString(); //Return statement if hash is successful
        } catch (NoSuchAlgorithmException exception){
            exception.printStackTrace();
        }
        //Failure case
        return "Failure to Hash";

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}