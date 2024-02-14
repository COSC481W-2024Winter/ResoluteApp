package com.example.resoluteapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.resoluteapp.databinding.FragmentLogExerciseBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogExerciseFragment#newInstance} factory method to
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
                logExercise();
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

    private void logExercise() {

        String exerciseName = binding.editTextExercise.getText().toString().trim();
        String units = binding.editTextUnits.getText().toString().trim();
        String numberOfUnits = binding.editTextNumberOfUnits.getText().toString().trim();

        // Create a data object
        Map<String, Object> theExercise = new HashMap<>();

        theExercise.put("Exercise", exerciseName);
        theExercise.put("Units", units);
        theExercise.put("Amount", numberOfUnits);
        theExercise.put("Date", Timestamp.now());

        theDB.collection("Jon_Exercises").add(theExercise).addOnSuccessListener(documentReference -> {
            // Log success
            Log.d(TAG, "Exercise logged with ID: " + documentReference.getId());
            // Navigate back to the home fragment
            NavHostFragment.findNavController(LogExerciseFragment.this).navigate(R.id.action_logExerciseFragment_to_homeFragment);
        }).addOnFailureListener(e -> {
            // Log failure
            Log.w(TAG, "Error adding exercise", e);
            // Handle error if needed
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}