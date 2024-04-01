package com.example.resoluteapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.resoluteapp.databinding.FragmentReplyBinding;
import com.example.resoluteapp.databinding.FragmentRequestBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class ReplyFragment extends Fragment {

    private FragmentReplyBinding binding;

    FirebaseFirestore DB = FirebaseFirestore.getInstance();

    String exerciseId;
    String spUsername;

    @Override
    public View onCreateView(
            @NonNull
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentReplyBinding.inflate(inflater, container, false);

        //get username and exercise's id for reply retrieving
        spUsername = ((MainActivity)getActivity()).getUsername();
        exerciseId = ((MainActivity)getActivity()).getExercise();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //set navigation functionality for back button
        binding.toPrevFromReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ReplyFragment.this)
                        .navigate(R.id.action_replyFragment_to_prevActivityFragment);
            }
        });
    }



}