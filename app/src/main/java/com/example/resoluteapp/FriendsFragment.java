package com.example.resoluteapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.resoluteapp.databinding.FragmentFriendsBinding;
import com.example.resoluteapp.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendsFragment newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Send Request Button
        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get current time
                LocalDateTime time = LocalDateTime.now();
                ZonedDateTime zdt = time.atZone(ZoneId.of("America/Detroit"));
                String currentTime = DateTimeFormatter.ofPattern("M/d/yy - h:mm:ss").format(zdt);

                //Get sending user
                String sendingUsername = "TEST";

                //Get requested user
                EditText enteredUsername = (EditText)getActivity().findViewById(R.id.enter_username);
                String requestedUsername = enteredUsername.getText().toString();

                FirebaseFirestore DB = FirebaseFirestore.getInstance();

                //Search users collection to see if requestedUsername exists
                DB.collection("users")
                        .whereEqualTo("username", requestedUsername)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                //If requestedUsername exists, send sendingUsername to requests collection and collection of requests_[requestedUsername]
                                if (task.getResult().size() == 1) {
                                    //Create data to be stored in Firestore
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("sent", currentTime);
                                    data.put("username", sendingUsername);

                                    //Add data to Firestore requests collection
                                    DB.collection("requests")
                                            .add(data)
                                            .addOnSuccessListener(documentReference -> {
                                                // Data addition successful
                                                Log.d("Firestore", "Data added successfully");
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle errors
                                                Log.e("Firestore", "Error adding data: " + e.getMessage());
                                            });

                                    //Add data to Firestore requests_[requestedUsername] collection
                                    DB.collection("requests_" + requestedUsername)
                                            .add(data)
                                            .addOnSuccessListener(documentReference -> {
                                                // Data addition successful
                                                Log.d("Firestore", "Data added successfully");
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle errors
                                                Log.e("Firestore", "Error adding data: " + e.getMessage());
                                            });

                                    //Show "Request sent" Toast
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Request sent", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                                //If requestedUsername does not exist, no request is sent
                                else {
                                    //Show "Username not found" Toast
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Username not found", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                                //Reset enter_username text box
                                EditText resetText = (EditText)getActivity().findViewById(R.id.enter_username);
                                resetText.setText("");
                            }
                        });
            }
        });

        //To Friend Requests Button
        binding.toRequestFromFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FriendsFragment.this)
                        .navigate(R.id.action_friendsFragment_to_requestFragment);
            }
        });

        //Home Button
        binding.toHomeFromFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FriendsFragment.this)
                        .navigate(R.id.action_friendsFragment_to_homeFragment);
            }
        });

        //Inbox Button
        binding.toInboxFromFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FriendsFragment.this)
                        .navigate(R.id.action_friendsFragment_to_inboxFragment);
            }
        });

        //Profile Button
        binding.toProfileFromFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FriendsFragment.this)
                        .navigate(R.id.action_friendsFragment_to_profileFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}