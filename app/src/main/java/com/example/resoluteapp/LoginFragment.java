package com.example.resoluteapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.resoluteapp.databinding.FragmentFirstBinding;
import com.example.resoluteapp.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Login Button
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Username and Password EditText items
                EditText usernameET = (EditText)getActivity().findViewById(R.id.login_username_entry);
                EditText passwordET = (EditText)getActivity().findViewById(R.id.login_password_entry);

                //Get entered username and password
                String usernameString = usernameET.getText().toString();
                String passwordString = passwordET.getText().toString();

                //TESTING CODE START FOR COMMIT
                int duration = Toast.LENGTH_SHORT;

                Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "Username is: "+usernameString, duration);
                toast1.show();

                Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Password is: "+passwordString, duration);
                toast2.show();

                //TESTING CODE END FOR COMMIT

                //Navigate to home screen
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_homeFragment);
            }
        });

        //Register Button
        binding.toRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}