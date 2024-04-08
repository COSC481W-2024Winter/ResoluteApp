package com.example.resoluteapp;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.resoluteapp.databinding.FragmentRequestBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class RequestFragment extends Fragment {

    private FragmentRequestBinding binding;

    FirebaseFirestore DB = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(

            @NonNull
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRequestBinding.inflate(inflater, container, false);

        //Only fill table if user is online
        if(isOnline())
            populateTable();
        else {
            //User is offline. Table is not filled, navigation is added.
            Toast offlineToast = Toast.makeText(requireActivity().getApplicationContext(), "Offline", Toast.LENGTH_SHORT);
            offlineToast.show();
            buttonClicks();
        }


        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void populateTable() {

        // Get Username from Shared Preferences
        String savedUser = ((MainActivity) getActivity()).getUsername();
        String usersFriendsCollection = "requests_" + savedUser;
        String usersFriendsList = "friends_" + savedUser;

        // No Duplicate Requests
        HashSet<String> uniqueUsernames = new HashSet<>();

        // Pull from the correct Collection
        DB.collection(usersFriendsCollection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> listOfRequests = queryDocumentSnapshots.getDocuments();

                    TableLayout theTable = (TableLayout) getView().findViewById(R.id.requestTable);
                    int columnWidth = theTable.getWidth() / 3;
                    theTable.removeViews(1, Math.max(0, theTable.getChildCount() - 1));

                    for (DocumentSnapshot theDocument : listOfRequests) {

                        String requestingUser = theDocument.getString("username");

                        // Check to make sure we only display one request
                        if (!uniqueUsernames.contains(requestingUser)) {

                            // Add it to the Set
                            uniqueUsernames.add(requestingUser);

                            TableRow newRow = new TableRow(getActivity().getApplicationContext());

                            TextView tv1 = new TextView(getActivity().getApplicationContext());
                            tv1.setText(requestingUser);
                            tv1.setTextColor(Color.BLACK);
                            tv1.setPadding(10, 10, 10, 10);
                            tv1.setTextSize(12);
                            tv1.setWidth(columnWidth);
                            newRow.addView(tv1, 0);

                            // Button for approve
                            Button approveButton = new Button(getActivity().getApplicationContext());
                            approveButton.setText(R.string.approve);
                            approveButton.setTextColor(Color.WHITE);
                            approveButton.setBackgroundColor(Color.GREEN);
                            approveButton.setPadding(10, 10, 10, 10);
                            approveButton.setWidth(columnWidth);
                            newRow.addView(approveButton);

                            // Add onClick listeners for button
                            approveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    // Create an Object with String Attributes for the user's friend list
                                    Map<String, Object> theFriendForUser = new HashMap<>();
                                    theFriendForUser.put("username", requestingUser);

                                    // Add that Friend to the User's Friend List
                                    DB.collection(usersFriendsList).add(theFriendForUser).addOnSuccessListener(documentReference -> {

                                        // If the Write is Successful we'll know
                                        Toast successfulAdd = Toast.makeText(requireActivity().getApplicationContext(), "Friend Added", Toast.LENGTH_SHORT);
                                        successfulAdd.show();

                                        // Now add the user to the requestingUser's friend list
                                        Map<String, Object> theFriendForRequestingUser = new HashMap<>();
                                        theFriendForRequestingUser.put("username", savedUser);

                                        // The collection for the requesting user's friends
                                        String requestingUsersFriendsCollection = "friends_" + requestingUser;

                                        // Add the user to the requestingUser's friend list
                                        DB.collection(requestingUsersFriendsCollection).add(theFriendForRequestingUser).addOnSuccessListener(documentReferenceForRequestingUser -> {

                                            // Optional: You can show another Toast or log if you want to confirm this addition too

                                        }).addOnFailureListener(e -> {
                                            // Log or show Toast in case of failure to add the user to requestingUser's friends
                                        });

                                        // Remove the Row From the Table
                                        ViewGroup parentView = (ViewGroup) newRow.getParent();
                                        parentView.removeView(newRow);

                                        // Remove the Request Document
                                        theDocument.getReference().delete();

                                    }).addOnFailureListener(e -> {

                                        // If For Whatever reason the write couldn't happen. We'll Be Prompted.
                                        Toast failureToAdd = Toast.makeText(requireActivity().getApplicationContext(), "Error With Friend Add", Toast.LENGTH_SHORT);
                                        failureToAdd.show();

                                    });
                                }
                            });

                            // Button for deny
                            Button denyButton = new Button(getActivity().getApplicationContext());
                            denyButton.setText(R.string.deny);
                            denyButton.setTextColor(Color.WHITE);
                            denyButton.setBackgroundColor(Color.RED);
                            denyButton.setPadding(10, 10, 10, 10);
                            denyButton.setWidth(columnWidth);
                            newRow.addView(denyButton);

                            // Add onClick listeners for button
                            denyButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    // Deletes the ONE document that exists for a user THERES A PROBLEM HERE
                                    // a user can send multiple friend requests
                                    theDocument.getReference().delete();

                                    // Remove the TableRow from the TableLayout
                                    // Grab the most recent view (button touched) a rows parent (table)
                                    // use the table to remove a view associated with it ... the row touched.
                                    ViewGroup parentView = (ViewGroup) newRow.getParent();
                                    parentView.removeView(newRow);

                                }
                            });

                            theTable.addView(newRow);

                        }


                    }


                }
                //Table succeeded, add navigation
                buttonClicks();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Table failed, add navigation
                        buttonClicks();
                    }
                });

    }

    //Function that sets button's onClick() listeners only when it is convenient
    public void buttonClicks(){
        //Return to Friends List Button
        binding.toFriendsFromRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RequestFragment.this)
                        .navigate(R.id.action_requestFragment_to_friendsFragment);


            }
        });
    }

    //Function that returns true if current device is connected to a network
    public boolean isOnline() {
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}