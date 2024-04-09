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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.resoluteapp.databinding.FragmentInboxBinding;
import com.example.resoluteapp.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class InboxFragment extends Fragment {

    private FragmentInboxBinding binding;

    FirebaseFirestore DB = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentInboxBinding.inflate(inflater, container, false);

        //calls the fillTable() function when the page is created so it is filled simultaneously
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

        //Friends Button
        binding.toFriendsFromInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(InboxFragment.this)
                        .navigate(R.id.action_inboxFragment_to_friendsFragment);
            }
        });

        //Home Button
        binding.toHomeFromInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(InboxFragment.this)
                        .navigate(R.id.action_inboxFragment_to_homeFragment);
            }
        });

        //Profile Button
        binding.toProfileFromInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(InboxFragment.this)
                        .navigate(R.id.action_inboxFragment_to_profileFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //function to fill the table with data from exercise collection
    public void fillTable() throws InterruptedException, ExecutionException {
        //get username from SharedPreferences
        String spUsername = ((MainActivity)getActivity()).getUsername();

        //indicates what collection to retrieve data from
        DB.collection("inbox_" + spUsername).orderBy("Date")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        //checks that there is currently data to retrieve
                        if (!queryDocumentSnapshots.isEmpty()) {

                            //puts all the documents of data into a list
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            //finds the table in the fragment_prev_activity.xml
                            TableLayout tl = (TableLayout) getView().findViewById(R.id.tableLayout);
                            int columnWidth = tl.getWidth() / 4;

                            tl.removeViews(1, Math.max(0, tl.getChildCount() - 1));

                            //loops through all the documents in the list to fill the table
                            for (DocumentSnapshot d : list) {
                                //puts each piece of data in the document into its own variable
                                Timestamp date = d.getTimestamp("Date");
                                String exercise = d.getString("Exercise");
                                String amount = d.getString("Amount");
                                String unit = d.getString("Units");
                                String username = d.getString("Username");

                                //creates a new row for the exercise and 4 text views in the row
                                TableRow tr = new TableRow(getActivity().getApplicationContext());
                                TextView tv1 = new TextView(getActivity().getApplicationContext());
                                TextView tv2 = new TextView(getActivity().getApplicationContext());
                                TextView tv3 = new TextView(getActivity().getApplicationContext());
                                TextView tv4 = new TextView(getActivity().getApplicationContext());

                                //set id for tablerow
                                tr.generateViewId();

                                //sets height of tablerow
                                tr.setMinimumHeight(60);

                                //sets the first text view with the username data for the exercise
                                tv1.setText(username);
                                tv1.setTextColor(Color.BLACK);
                                tv1.setPadding(10, 10, 10, 10);
                                tv1.setTextSize(12);
                                tv1.setWidth(columnWidth);

                                //adds it to the table row in the first column
                                tr.addView(tv1, 0);

                                //sets the second text view with the exercise data for the exercise
                                tv2.setText(exercise);
                                tv2.setTextColor(Color.BLACK);
                                tv2.setPadding(10, 10, 10, 10);
                                tv2.setTextSize(12);
                                tv2.setWidth(columnWidth);

                                //adds it to the table row in the second column
                                tr.addView(tv2, 1);

                                //sets the third text view with the amount data for the exercise
                                tv3.setText(amount);
                                tv3.setTextColor(Color.BLACK);
                                tv3.setPadding(10, 10, 10, 10);
                                tv3.setTextSize(12);
                                tv3.setWidth(columnWidth);

                                //adds it to the table row in the third column
                                tr.addView(tv3, 2);

                                //sets the fourth text view with the unit data for the exercise
                                tv4.setText(unit);
                                tv4.setTextColor(Color.BLACK);
                                tv4.setPadding(10, 10, 10, 10);
                                tv4.setTextSize(12);
                                tv4.setWidth(columnWidth);

                                //adds it to the table row in the fourth column
                                tr.addView(tv4, 3);

                                //when tablerow is clicked, a reply popup appears
                                tr.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //build the alertdialog
                                        String[] choices = {"Keep at it!", "Good hustle!", "I'm falling behind!", "Proud of you!"};

                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder
                                                .setTitle("Encourage your friend?")
                                                .setPositiveButton("Send", (dialog, which) -> {
                                                    DB.collection("exercises_" + username)
                                                            .whereEqualTo("Date", date)
                                                            .whereEqualTo("Exercise", exercise)
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

                                                                        //Clicking "send" sends the reply to the appropriate sub-collection
                                                                        //Create a data object
                                                                        Map<String, Object> reply = new HashMap<>();

                                                                        //Format for current time
                                                                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                                                        String dateString = dateFormat.format(new Date(System.currentTimeMillis()));

                                                                        //Fill the reply query
                                                                        reply.put("Username", spUsername);
                                                                        reply.put("Reply", choices[selectedPosition]);
                                                                        reply.put("Date", Timestamp.now());
                                                                        reply.put("Date_String", dateString);

                                                                        //If exercise is found in friend's exercise collection, send reply and delete from inbox
                                                                        //if (task.getResult().size() == 1) {
                                                                        if (!task.getResult().isEmpty()) {
                                                                        //Send the reply query
                                                                        DB.collection("exercises_"+username)
                                                                                .document(d.getId())
                                                                                .collection("replies")
                                                                                .add(reply)
                                                                                .addOnSuccessListener(documentReference -> {
                                                                                    // Log success
                                                                                    Log.d(TAG, "Reply sent with ID: " + documentReference.getId());
                                                                                    Toast successMessage = Toast.makeText(requireActivity().getApplicationContext(), "Reply sent!", Toast.LENGTH_SHORT);
                                                                                    successMessage.show();

                                                                                    //Remove this document from inbox_USERNAME
                                                                                    DB.collection("inbox_"+spUsername)
                                                                                            .document(d.getId())
                                                                                            .delete()
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {
                                                                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                                                                    //Refresh the page
                                                                                                    NavHostFragment.findNavController(InboxFragment.this)
                                                                                                            .navigate(R.id.action_inboxFragment_self);
                                                                                                }
                                                                                            })
                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                    Log.w(TAG, "Error deleting document", e);
                                                                                                }
                                                                                            });

                                                                                }).addOnFailureListener(e -> {
                                                                                    // Log failure
                                                                                    Log.w(TAG, "Error sending reply", e);
                                                                                    // Handle error if needed
                                                                                    Toast failureMessage = Toast.makeText(requireActivity().getApplicationContext(), "Failure sending reply", Toast.LENGTH_LONG);
                                                                                    failureMessage.show();
                                                                                });
                                                                    }

                                                                    //If exercise is not found in friend's exercise collection, delete from inbox
                                                                    else {
                                                                            //Remove this document from inbox_USERNAME
                                                                            DB.collection("inbox_"+spUsername)
                                                                                    .document(d.getId())
                                                                                    .delete()
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                                                            //Refresh the page
                                                                                            NavHostFragment.findNavController(InboxFragment.this)
                                                                                                    .navigate(R.id.action_inboxFragment_self);
                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Log.w(TAG, "Error deleting document", e);
                                                                                        }
                                                                                    });

                                                                        //Show "Exercise not found" Toast
                                                                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Exercise not found", Toast.LENGTH_SHORT);
                                                                        toast.show();
                                                                    }
                                                                }
                                                            });

                                                    /*int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

                                                    //Clicking "send" sends the reply to the appropriate sub-collection
                                                    // Create a data object
                                                    Map<String, Object> reply = new HashMap<>();

                                                    //Format for current time
                                                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                                    String dateString = dateFormat.format(new Date(System.currentTimeMillis()));

                                                    //Fill the reply query
                                                    reply.put("Username", spUsername);
                                                    reply.put("Reply", choices[selectedPosition]);
                                                    reply.put("Date", Timestamp.now());
                                                    reply.put("Date_String", dateString);

                                                    //Send the reply query
                                                    DB.collection("exercises_"+username)
                                                            .document(d.getId())
                                                            .collection("replies")
                                                            .add(reply)
                                                            .addOnSuccessListener(documentReference -> {
                                                                // Log success
                                                                Log.d(TAG, "Reply sent with ID: " + documentReference.getId());
                                                                Toast successMessage = Toast.makeText(requireActivity().getApplicationContext(), "Reply sent!", Toast.LENGTH_SHORT);
                                                                successMessage.show();

                                                                //Remove this document from inbox_USERNAME
                                                                DB.collection("inbox_"+spUsername)
                                                                        .document(d.getId())
                                                                        .delete()
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                                                //Refresh the page
                                                                                NavHostFragment.findNavController(InboxFragment.this)
                                                                                        .navigate(R.id.action_inboxFragment_self);
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.w(TAG, "Error deleting document", e);
                                                                            }
                                                                        });

                                                            }).addOnFailureListener(e -> {
                                                                // Log failure
                                                                Log.w(TAG, "Error sending reply", e);
                                                                // Handle error if needed
                                                                Toast failureMessage = Toast.makeText(requireActivity().getApplicationContext(), "Failure sending reply", Toast.LENGTH_LONG);
                                                                failureMessage.show();
                                                            });*/
                                                })
                                                .setNegativeButton("No, thanks", (dialog, which) -> {

                                                })
                                                .setSingleChoiceItems(choices, 0, (dialog, which) -> {

                                                });

                                        //show the alertdialog
                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                        //change button colors
                                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.GREEN);
                                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.GRAY);
                                    }
                                });

                                //adds the row to the table
                                tl.addView(tr);
                            }
                        }
                    }
                });
    }
}