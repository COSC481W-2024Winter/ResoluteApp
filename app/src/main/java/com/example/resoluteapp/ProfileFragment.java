package com.example.resoluteapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.resoluteapp.databinding.FragmentLoginBinding;
import com.example.resoluteapp.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

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