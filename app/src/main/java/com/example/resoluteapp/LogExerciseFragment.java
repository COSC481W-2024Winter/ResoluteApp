package com.example.resoluteapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.resoluteapp.databinding.FragmentLogExerciseBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class LogExerciseFragment extends Fragment {
    private FragmentLogExerciseBinding binding;
    private final FirebaseFirestore theDB = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLogExerciseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Log Exercise Button
        binding.logExerciseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    logExercise();
                } else {
                    Toast.makeText(requireActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Clear Fields Button
        binding.clearFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFields();
            }
        });

        //Discard Button
        binding.toHomeFromLogExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LogExerciseFragment.this).navigate(R.id.action_logExerciseFragment_to_homeFragment);
            }
        });
    }

    private boolean isInputValid() {
        String exerciseName = binding.editTextExercise.getText().toString().trim();
        String units = binding.editTextUnits.getText().toString().trim();
        String numberOfUnits = binding.editTextNumberOfUnits.getText().toString().trim();

        return !exerciseName.isEmpty() && !units.isEmpty() && !numberOfUnits.isEmpty();
    }


    private void logExercise() {

        //get the username of the user that is currently logged in
        String currentUser = ((MainActivity) getActivity()).getUsername();

        String exerciseName = binding.editTextExercise.getText().toString().trim();
        String units = binding.editTextUnits.getText().toString().trim();
        String numberOfUnits = binding.editTextNumberOfUnits.getText().toString().trim();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = dateFormat.format(new Date(System.currentTimeMillis()));

        // Create a data object
        Map<String, Object> theExercise = new HashMap<>();

        theExercise.put("Exercise", exerciseName);
        theExercise.put("Units", units);
        theExercise.put("Amount", numberOfUnits);
        theExercise.put("Date", Timestamp.now());
        theExercise.put("Date_String", dateString);
        theExercise.put("Username", currentUser);

        theDB.collection("exercises_" + currentUser).add(theExercise).addOnSuccessListener(documentReference -> {
            // Log success
            Log.d(TAG, "Exercise logged with ID: " + documentReference.getId());
            Toast successMessage = Toast.makeText(requireActivity().getApplicationContext(), "Exercise Logged Successfully", Toast.LENGTH_SHORT);
            successMessage.show();

            // Clear EditText fields
            clearFields();

            sendExercisesToFriends(currentUser, theExercise);


        }).addOnFailureListener(e -> {
            // Log failure
            Log.w(TAG, "Error adding exercise", e);
            // Handle error if needed
            Toast failureMessage = Toast.makeText(requireActivity().getApplicationContext(), "Failure To Log Exercise", Toast.LENGTH_LONG);
            failureMessage.show();
        });

    }

    private void clearFields() {
        // Clear EditText fields
        binding.editTextExercise.setText("");
        binding.editTextUnits.setText("");
        binding.editTextNumberOfUnits.setText("");
    }

    private void sendExercisesToFriends(String currentUser, Map<String, Object> theExercise) {

        // first Grab all the current users friends from the db

        // set a listener to make sure theres a collection to grab friends from
        theDB.collection("friends_" + currentUser).get().addOnCompleteListener(grabFriendsList -> {

            // if we've found a collection and there are friends to write to
            if (grabFriendsList.isSuccessful() && grabFriendsList.getResult() != null) {

                // for every friend in a users friend list
                for (QueryDocumentSnapshot friend : grabFriendsList.getResult()) {

                    // grab the username for the friend
                    String friendsUsername = friend.getString("username");

                    // make sure they have a username field
                    if (friendsUsername != null) {

                        // Add the exercise to their collection
                        theDB.collection("inbox_" + friendsUsername).add(theExercise)
                                // with logs for if it was successful or not
                                .addOnSuccessListener(documentReference -> Log.d(TAG, "Exercise sent to friend: " + friendsUsername))
                                .addOnFailureListener(error -> Log.d(TAG, "Error Sending Exercise to Friend: " + friendsUsername));
                    } else {

                        // the friend isnt set up right? how did we get here
                        Log.w(TAG, "Friend exists but doesn't?");

                    }

                }

            } else {
                // Friends list doesn't exist OR the user doesnt have friends
                Log.w(TAG, "No Friends Yet");
            }


        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}