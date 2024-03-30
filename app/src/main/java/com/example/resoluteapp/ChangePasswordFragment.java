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

import com.example.resoluteapp.databinding.FragmentChangePasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding binding;

    FirebaseFirestore DB = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Apply Changes Button
        binding.applyChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String current_user = ((MainActivity)getActivity()).getUsername();

                EditText entered_current_password = getActivity().findViewById(R.id.enter_current_password);
                String current_password = entered_current_password.getText().toString();
                String hash_current = ((MainActivity)getActivity()).hashString(current_password);

                EditText entered_new_password = getActivity().findViewById(R.id.enter_new_password);
                String new_password = entered_new_password.getText().toString();

                EditText entered_confirm_new_password = getActivity().findViewById(R.id.confirm_new_password);
                String confirm_new_password = entered_confirm_new_password.getText().toString();
                String hash_new = ((MainActivity)getActivity()).hashString(confirm_new_password);

                //Checks if currentPasswordString is equal to user's current password
                DB.collection("users")
                        .whereEqualTo("username", current_user)
                        .whereEqualTo("password", hash_current)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                //If a password field is empty, do not change password
                                if (current_password.isEmpty() || new_password.isEmpty() || confirm_new_password.isEmpty()) {
                                    //Show "All fields must be filled" Toast
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "All fields must be filled", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                                //If password fields are not empty, check if currentPasswordString is equal to user's current password
                                else {
                                    //If currentPasswordString is equal to user's current password, check if newPasswordString and confirmNewPassword are equal
                                    if (task.getResult().size() == 1) {
                                        //If newPasswordString and confirmNewPassword are equal, change password
                                        if (new_password.equals(confirm_new_password)) {
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("password", hash_new);

                                            DB.collection("users").document(current_user)
                                                    .update(data)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            //Clear and set username and password in SharedPreferences
                                                            ((MainActivity) getActivity()).clearSharedPref();
                                                            ((MainActivity) getActivity()).setUsernameAndPassword(current_user, hash_new);

                                                            //Show "Password changed" Toast
                                                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Password changed", Toast.LENGTH_SHORT);
                                                            toast.show();

                                                            clearFields();
                                                            Log.d(TAG, "Password changed");

                                                            //Navigate to home page
                                                            NavHostFragment.findNavController(ChangePasswordFragment.this)
                                                                    .navigate(R.id.action_changePasswordFragment_to_homeFragment);
                                                        }
                                                    })

                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            //Show "Password not changed" Toast
                                                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Password not changed", Toast.LENGTH_SHORT);
                                                            toast.show();

                                                            Log.w(TAG, "Error changing password", e);
                                                        }
                                                    });
                                        }

                                        //If newPassword and confirmNewPassword are not equal, do not change password
                                        else {
                                            //Show "New passwords to not match" Toast
                                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "New passwords do not match", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    }

                                    //If currentPasswordString is not equal to user's current password, do not change password
                                    else {
                                        //Show "Current password is incorrect" Toast
                                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Current password is incorrect", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }
                            }
                        });
            }
        });

        //Return to Profile Page Button
        binding.toProfileFromChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate back to HomeFragment
                NavHostFragment.findNavController(ChangePasswordFragment.this)
                        .navigate(R.id.action_changePasswordFragment_to_profileFragment);
            }
        });
    }

    private void clearFields() {
        // Clear EditText fields
        binding.enterCurrentPassword.setText("");
        binding.enterNewPassword.setText("");
        binding.confirmNewPassword.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}