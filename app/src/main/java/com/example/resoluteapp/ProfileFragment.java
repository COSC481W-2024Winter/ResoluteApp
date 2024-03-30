package com.example.resoluteapp;

import static android.content.ContentValues.TAG;

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

import com.example.resoluteapp.databinding.FragmentLoginBinding;
import com.example.resoluteapp.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    FirebaseFirestore DB = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String current_user = ((MainActivity)getActivity()).getUsername();

        EditText username = getActivity().findViewById(R.id.username);
        EditText name = getActivity().findViewById(R.id.name);
        EditText email = getActivity().findViewById(R.id.email);

        //Username cannot be edited, name and email can
        username.setEnabled(false);
        name.setEnabled(true);
        email.setEnabled(true);

        DB.collection("users")
                .whereEqualTo("username", current_user)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //Puts all documents into list
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot d : list) {
                            username.setText(d.getString("username"));
                            name.setText(d.getString("name"));
                            email.setText(d.getString("email"));
                        }
                    }
                });

        //Logout Button
        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Clear SharedPreferences (this is effectively the "logout" taking place)
                ((MainActivity)getActivity()).clearSharedPref();

                //Navigate to LoginFragment
                NavHostFragment.findNavController(ProfileFragment.this)
                        .navigate(R.id.action_profileFragment_to_loginFragment);
            }
        });

        //Apply Changes Button
        binding.applyChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String current_user = ((MainActivity)getActivity()).getUsername();

                EditText entered_name = getActivity().findViewById(R.id.name);
                String name = entered_name.getText().toString();

                EditText entered_email = getActivity().findViewById(R.id.email);
                String email = entered_email.getText().toString();

                DB.collection("users")
                        .whereEqualTo("username", current_user)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                //If name and email fields are not empty, check if email contains "@" symbol
                                if (!name.isEmpty() || !email.isEmpty()) {
                                    //If email contains "@" symbol, update information
                                    if(email.contains("@")) {
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("name", name);
                                        data.put("email", email);

                                        DB.collection("users").document(current_user)
                                                .update(data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        //Show "User information updated" Toast
                                                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "User information updated", Toast.LENGTH_SHORT);
                                                        toast.show();

                                                        Log.d(TAG, "User information updated");

                                                        //Navigate to home page
                                                        NavHostFragment.findNavController(ProfileFragment.this)
                                                                .navigate(R.id.action_profileFragment_to_homeFragment);
                                                    }
                                                })

                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        //Show "User information not updated" Toast
                                                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "User information not updated", Toast.LENGTH_SHORT);
                                                        toast.show();

                                                        Log.w(TAG, "Error updating user information", e);
                                                    }
                                                });
                                    }

                                    //If email does not contain "@" symbol, do not update information
                                    else {
                                        //Show "Email must contain @ symbol" Toast
                                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Email must contain @ symbol", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }

                                //If name and email fields are empty, do not update information
                                else {
                                    //Show "All fields must be filled" Toast
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "All fields must be filled", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        });
            }
        });

        //Change Password Button
        binding.toChangePasswordFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ProfileFragment.this)
                        .navigate(R.id.action_profileFragment_to_changePasswordFragment);
            }
        });

        //Friends Button
        binding.toFriendsFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ProfileFragment.this)
                        .navigate(R.id.action_profileFragment_to_friendsFragment);
            }
        });

        //Inbox Button
        binding.toInboxFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ProfileFragment.this)
                        .navigate(R.id.action_profileFragment_to_inboxFragment);
            }
        });

        //Home Button
        binding.toHomeFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate back to HomeFragment
                NavHostFragment.findNavController(ProfileFragment.this)
                        .navigate(R.id.action_profileFragment_to_homeFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}