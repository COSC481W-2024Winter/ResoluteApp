package com.example.resoluteapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.resoluteapp.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    //Initialize the database object in fragment
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Create tag for Error reporting in Logcat
    private static final String TAG = "MainActivity";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Register Button
        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText items for User input
                EditText regUsernameET = (EditText)getActivity().findViewById(R.id.register_username_entry);
                EditText regNameET = (EditText)getActivity().findViewById(R.id.register_name_entry);
                EditText regEmailET = (EditText)getActivity().findViewById(R.id.register_email_entry);
                EditText regPasswordET = (EditText)getActivity().findViewById(R.id.register_password_entry);

                //Get entered Strings
                String regUsernameString = regUsernameET.getText().toString();
                String regNameString = regNameET.getText().toString();
                String regEmailString = regEmailET.getText().toString();
                String regPasswordString = regPasswordET.getText().toString();

                //Hash(encrypt) password
                String hashword = ((MainActivity)getActivity()).hashString(regPasswordString);

                //Check that entered email contains "@"
                if(!regEmailString.contains("@")){
                    //Show Toast informing of email requirements
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Entered email must contain @ sign", Toast.LENGTH_SHORT);
                    toast.show();
                }
                //Check that entered password is long enough
                else if(!(regPasswordString.length() >= 8)){
                    //Show Toast informing of password requirements
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Entered password must be at least 8 characters", Toast.LENGTH_SHORT);
                    toast.show();
                }
                //Check if remaining textboxes aren't empty (avoids redundant checks on password and email)
                else if(regNameString.isEmpty() || regUsernameString.isEmpty()) {
                    //Show Toast informing of empty textbox(s)
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "All text fields must be filled", Toast.LENGTH_SHORT);
                    toast.show();
                }
                //Attempt a registration query, if the user if online
                else{
                    //Check if user is currently online (Only checks device's network status, not server connection)
                    if(isOnline()){
                        //Test connectivity to server by getting known item (FROM THE SERVER!!!)
                        db.collection("collectionTest")
                                .get(Source.SERVER)
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {
                                            //Attempt registration
                                            registration(regUsernameString, regNameString, regEmailString, hashword);
                                        } else {
                                            //Inform user they are offline, since the above test clearly failed
                                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    }
                                });
                    }
                    else {
                        //Inform user they are offline
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Please connect to the Internet", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        //Back to login Button
        binding.registerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(RegisterFragment.this)
                        .navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
    }

    //Function that returns true if current device is connected to a network
    public boolean isOnline() {
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }
        else{
            return false;
        }
    }

    //Function used to attempt registration. Written to make code cleaner
    public void registration(String un, String nm, String em, String pw){
        //Check if EITHER Username of Email is taken
        db.collection("users").where(Filter.or(
                Filter.equalTo("username", un),
                Filter.equalTo("email", em)
        )).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    //If more than zero results, username/email is taken.
                    if(task.getResult().size() != 0){
                        //Inform user Username or Email is taken
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Username/Email already taken", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    //Else, register this user's information in Database!
                    else{
                        //Fill the newUser map, serving as the document to insert in db
                        Map<String, Object> newUser = new HashMap<>();
                        newUser.put("username", un);
                        newUser.put("name", nm);
                        newUser.put("email", em);
                        newUser.put("password", pw);

                        //Insert the new user to the database
                        db.collection("users").document(un)
                                .set(newUser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                        //Notify user of successful registration
                                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT);
                                        toast.show();

                                        //Exit to login screen
                                        NavHostFragment.findNavController(RegisterFragment.this)
                                                .navigate(R.id.action_registerFragment_to_loginFragment);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Notify user of failure
                                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Registration failed. Check connection and try again.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        Log.w(TAG, "Error writing document", e);

                                        //Exit to login screen
                                        NavHostFragment.findNavController(RegisterFragment.this)
                                                .navigate(R.id.action_registerFragment_to_loginFragment);
                                    }
                                });
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}