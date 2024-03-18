package com.example.resoluteapp;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.resoluteapp.databinding.FragmentFriendsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;

    FirebaseFirestore DB = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFriendsBinding.inflate(inflater, container, false);

        //Calls fillTable() when page is created so it is filled simultaneously
        try {
            fillTable();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Send Request Button
        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get current time
                LocalDateTime time = LocalDateTime.now();
                ZonedDateTime zdt = time.atZone(ZoneId.of("America/Detroit"));
                String currentTime = DateTimeFormatter.ofPattern("M/d/yy - h:mm:ss").format(zdt);

                //Get sending user
                String sendingUsername = ((MainActivity)getActivity()).getUsername();

                //Get requested user
                EditText enteredUsername = (EditText)getActivity().findViewById(R.id.enter_username);
                String requestedUsername = enteredUsername.getText().toString();

                //If user enters their own username, do not send request
                if (sendingUsername.equals(requestedUsername)) {
                    //Show "Cannot request yourself" Toast
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Cannot request yourself", Toast.LENGTH_SHORT);
                    toast.show();

                    //Reset enter_username text box
                    EditText resetText = (EditText)getActivity().findViewById(R.id.enter_username);
                    resetText.setText("");
                }

                //If user enters username other than their own, check if requestedUsername exists and send request
                else {
                    //Search users collection to see if requestedUsername exists
                    DB.collection("users")
                            .whereEqualTo("username", requestedUsername)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    //If requestedUsername exists, check if the request has already been sent
                                    if (task.getResult().size() == 1) {
                                        DB.collection("requests_" + requestedUsername)
                                                .whereEqualTo("username", sendingUsername)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        //If sendingUsername already exists in requestedUsername's requests, do not send request
                                                        if (task.getResult().size() == 1) {
                                                            //Show "Request has already been sent" Toast
                                                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Request has already been sent", Toast.LENGTH_SHORT);
                                                            toast.show();
                                                        }

                                                        //If sendingUsername does not already exist in requestedUsername's requests, check if they are already friends
                                                        else {
                                                            DB.collection("friends_" + sendingUsername)
                                                                    .whereEqualTo("username", requestedUsername)
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            //If requestedUsername already exists in sendingUsername's friends, do not send request
                                                                            if (task.getResult().size() == 1) {
                                                                                //Show "You are already friends" Toast
                                                                                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "You are already friends", Toast.LENGTH_SHORT);
                                                                                toast.show();
                                                                            }

                                                                            //If requestedUsername does not already exist in sendingUsername's friends, send sendingUsername to requests collection and collection of requests_[requestedUsername]
                                                                            else {
                                                                                //Create data to be stored in Firestore
                                                                                Map<String, Object> data = new HashMap<>();
                                                                                data.put("sent", currentTime);
                                                                                data.put("username", sendingUsername);

                                                                                //Add data to Firestore requests collection
                                                                                DB.collection("requests")
                                                                                        .add(data)
                                                                                        .addOnSuccessListener(documentReference -> {
                                                                                            // Data addition successful
                                                                                            Log.d("Firestore", "Data added successfully");
                                                                                        })
                                                                                        .addOnFailureListener(e -> {
                                                                                            // Handle errors
                                                                                            Log.e("Firestore", "Error adding data: " + e.getMessage());
                                                                                        });

                                                                                //Add data to Firestore requests_[requestedUsername] collection
                                                                                DB.collection("requests_" + requestedUsername)
                                                                                        .add(data)
                                                                                        .addOnSuccessListener(documentReference -> {
                                                                                            // Data addition successful
                                                                                            Log.d("Firestore", "Data added successfully");
                                                                                        })
                                                                                        .addOnFailureListener(e -> {
                                                                                            // Handle errors
                                                                                            Log.e("Firestore", "Error adding data: " + e.getMessage());
                                                                                        });

                                                                                //Show "Request sent" Toast
                                                                                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Request sent", Toast.LENGTH_SHORT);
                                                                                toast.show();
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });
                                    }

                                    //If requestedUsername does not exist, no request is sent
                                    else {
                                        //Show "Username not found" Toast
                                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Username not found", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }

                                    //Reset enter_username text box
                                    EditText resetText = (EditText) getActivity().findViewById(R.id.enter_username);
                                    resetText.setText("");
                                }
                            });
                }
            }
        });

        //To Friend Requests Button
        binding.toRequestFromFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FriendsFragment.this)
                        .navigate(R.id.action_friendsFragment_to_requestFragment);
            }
        });

        //Home Button
        binding.toHomeFromFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FriendsFragment.this)
                        .navigate(R.id.action_friendsFragment_to_homeFragment);
            }
        });

        //Inbox Button
        binding.toInboxFromFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FriendsFragment.this)
                        .navigate(R.id.action_friendsFragment_to_inboxFragment);
            }
        });

        //Profile Button
        binding.toProfileFromFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FriendsFragment.this)
                        .navigate(R.id.action_friendsFragment_to_profileFragment);
            }
        });
    }

    //Function to fill  table with data from user's friends collection
    public void fillTable() throws InterruptedException, ExecutionException {

        DB.collection("friends_" + ((MainActivity)getActivity()).getUsername()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        //Checks that there is currently data to retrieve
                        if (!queryDocumentSnapshots.isEmpty()) {

                            //Puts all documents into list
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            //Finds table in the fragment_friends.xml
                            TableLayout tl = (TableLayout) getView().findViewById(R.id.friends_table);
                            int columnWidth = tl.getWidth();

                            tl.removeViews(1, Math.max(0, tl.getChildCount() - 1));

                            //Loops through all documents in list to fill table
                            for (DocumentSnapshot d : list) {
                                //Puts each piece of data in document into its own variable
                                String username = d.getString("username");

                                //Creates a new row for friend and 1 text view in row for their username
                                TableRow tr = new TableRow(getActivity().getApplicationContext());
                                TextView tv1 = new TextView(getActivity().getApplicationContext());

                                //Sets text view with username data for friend
                                tv1.setText(username);
                                tv1.setTextColor(Color.BLACK);
                                tv1.setPadding(10, 10, 10, 10);
                                tv1.setTextSize(12);
                                tv1.setWidth(columnWidth);

                                //Adds text view to table row in first column
                                tr.addView(tv1, 0);

                                //When tableRow is clicked, remove friend popup appears
                                tr.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder
                                                .setTitle("Remove friend")
                                                .setPositiveButton("Remove", (dialog, which) -> {
                                                    //Removes friend from user's friend collection
                                                    DB.collection("friends_" + ((MainActivity)getActivity()).getUsername())
                                                            .whereEqualTo("username", username)
                                                            .get()
                                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                    //Puts all documents into list
                                                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                                    for (DocumentSnapshot d : list) {
                                                                        d.getReference().delete();
                                                                    }

                                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                                    //Refresh the page
                                                                    NavHostFragment.findNavController(FriendsFragment.this)
                                                                            .navigate(R.id.action_friendsFragment_self);
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w(TAG, "Error deleting document", e);
                                                                }
                                                            });

                                                    //Removes user from friend's friends collection
                                                    DB.collection("friends_" + username)
                                                            .whereEqualTo("username", ((MainActivity)getActivity()).getUsername())
                                                            .get()
                                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                    //Puts all documents into list
                                                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                                    for (DocumentSnapshot d : list) {
                                                                        d.getReference().delete();
                                                                    }

                                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");

                                                                    //Refresh the page
                                                                    NavHostFragment.findNavController(FriendsFragment.this)
                                                                            .navigate(R.id.action_friendsFragment_self);

                                                                    //Show "Friend removed" Toast
                                                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Friend removed", Toast.LENGTH_SHORT);
                                                                    toast.show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w(TAG, "Error deleting document", e);

                                                                    //Show "Error removing friend" Toast
                                                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Error removing friend", Toast.LENGTH_SHORT);
                                                                    toast.show();
                                                                }
                                                            });
                                                })

                                                .setNegativeButton("Cancel", (dialog, which) -> {
                                                });

                                        //Show the AlertDialog
                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                        //Change button colors
                                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.GREEN);
                                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.GRAY);
                                    }
                                });

                                //Adds row to table
                                tl.addView(tr);
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}