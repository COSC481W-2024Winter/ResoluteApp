package com.example.resoluteapp;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.resoluteapp.databinding.FragmentHomeBinding;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        //Disable Back Button built in to Android (Prevents back button after logout bug)
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Do nothing to prevent back button
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get username from SharedPreferences
        String savedUser = ((MainActivity)getActivity()).getUsername();
        //Call setWelcomeText to change the welcome message
        setWelcomeText(savedUser);

        //Log Exercise button
        binding.toLogExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_homeFragment_to_logExerciseFragment);
            }
        });

        //Previous Activity button
        binding.toPreviousActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_homeFragment_to_prevActivityFragment);
            }
        });

        //Friends button
        binding.toFriendsFromHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_homeFragment_to_friendsFragment);
            }
        });

        //Inbox button
        binding.toInboxFromHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_homeFragment_to_inboxFragment);
            }
        });

        //Profile button
        binding.toProfileFromHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_homeFragment_to_profileFragment);
            }
        });
    }

    //Function that changes username in welcome message
    public void setWelcomeText(String username){
        //Change welcome message to include username
        TextView welcomeMessage = (TextView) getView().findViewById(R.id.welcome_message);
        welcomeMessage.setText("Hello " + username + "\nWelcome to Resolute!");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}