package com.example.resoluteapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.resoluteapp.databinding.FragmentLogExerciseBinding;
import com.example.resoluteapp.databinding.FragmentLoginBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogExerciseFragment extends Fragment {
    private FragmentLogExerciseBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentLogExerciseBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Log Exercise Button
        binding.logExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LogExerciseFragment.this)
                        .navigate(R.id.action_logExerciseFragment_to_homeFragment);
            }
        });

        //Discard Button
        binding.toHomeFromLogExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LogExerciseFragment.this)
                        .navigate(R.id.action_logExerciseFragment_to_homeFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}