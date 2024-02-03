package com.example.resoluteapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.resoluteapp.databinding.FragmentPrevActivityBinding;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrevActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrevActivityFragment extends Fragment {

    private FragmentPrevActivityBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentPrevActivityBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Home Button
        binding.toHomeFromPreviousActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(PrevActivityFragment.this)
                        .navigate(R.id.action_prevActivityFragment_to_homeFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}