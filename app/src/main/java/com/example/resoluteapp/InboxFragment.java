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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private FirebaseFirestore DB = FirebaseFirestore.getInstance();

    // Variable to store the username retrieved from SharedPreferences
    // This variable holds the username of the current user, which is obtained from SharedPreferences.
    // It is used to identify the user and perform user-specific operations throughout the fragment.
    private String spUsername;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInboxBinding.inflate(inflater, container, false);
        spUsername = ((MainActivity) requireActivity()).getUsername();

        //calls the fillTable() function when the page is created so it is filled simultaneously
        try {
            fillTable();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Friends Button
        binding.toFriendsFromInbox.setOnClickListener(v -> NavHostFragment.findNavController(InboxFragment.this).navigate(R.id.action_inboxFragment_to_friendsFragment));
        //Home Button
        binding.toHomeFromInbox.setOnClickListener(v -> NavHostFragment.findNavController(InboxFragment.this).navigate(R.id.action_inboxFragment_to_homeFragment));
        //Profile Button
        binding.toProfileFromInbox.setOnClickListener(v -> NavHostFragment.findNavController(InboxFragment.this).navigate(R.id.action_inboxFragment_to_profileFragment));
        // Reply to All Button
        binding.ReplyAll.setOnClickListener(v -> {
            showReplyDialog(null, null); // Show reply dialog for "Reply All"
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Function to reply to all rows
    private void replyToAllRows(String reply) {
        TableLayout tl = binding.tableLayout;
        for (int i = 1; i < tl.getChildCount(); i++) {
            TableRow tr = (TableRow) tl.getChildAt(i);
            TextView tvUsername = (TextView) tr.getChildAt(0);
            String username = tvUsername.getText().toString();
            sendReply(username, reply);
            removeFromTable(tr.getTag().toString());
        }
        refreshTable(); // Refresh the table after replying to all rows
    }

    // Function to reply to a single row
    private void sendReply(String username, String reply) {
        // Send reply to Firestore
        // Create a data object
        Map<String, Object> replyData = new HashMap<>();

        //Format for current time
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = dateFormat.format(new Date(System.currentTimeMillis()));

        //Fill the reply query
        replyData.put("Username", spUsername);
        replyData.put("Reply", reply);
        replyData.put("Date", Timestamp.now());
        replyData.put("Date_String", dateString);

        //Send the reply query
        DB.collection("exercises_" + username)
                .document(/* Add the document ID here */)
                .collection("replies")
                .add(replyData)
                .addOnSuccessListener(documentReference -> {

                    // Log success
                    Log.d(TAG, "Reply sent with ID: " + documentReference.getId());
                    Toast.makeText(requireActivity().getApplicationContext(), "Reply sent!", Toast.LENGTH_SHORT).show();
                    refreshTable();
                })
                .addOnFailureListener(e -> {
                    // Log failure
                    Log.w(TAG, "Error sending reply", e);
                    // Handle error if needed
                    Toast.makeText(requireActivity().getApplicationContext(), "Failure sending reply", Toast.LENGTH_LONG).show();
                });
    }

    // Function to remove the corresponding row from the table
    private void removeFromTable(String docId) {

        //Remove this document from inbox_USERNAME
        DB.collection("inbox_"+spUsername)
                .document(docId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    //Refresh the page
                    refreshTable();
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
    }

    // Function to fill the table with data from inbox collection
    private void fillTable() throws InterruptedException, ExecutionException {

        //indicates what collection to retrieve data from
        DB.collection("inbox_" + spUsername).orderBy("Date")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    //checks that there is currently data to retrieve
                    if (!queryDocumentSnapshots.isEmpty()) {

                        //puts all the documents of data into a list
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        // Get the TableLayout from the binding
                        TableLayout tl = binding.tableLayout;

                        // Remove child views starting from index 1 (excluding the header row) up to the last index
                        // Math.max(0, tl.getChildCount() - 1) ensures that if the TableLayout has only the header row,
                        // it won't try to remove a negative number of views.
                        tl.removeViews(1, Math.max(0, tl.getChildCount() - 1));

                        //loops through all the documents in the list to fill the table
                        for (DocumentSnapshot d : list) {
                            String date = d.getString("Date_String");
                            String exercise = d.getString("Exercise");
                            String amount = d.getString("Amount");
                            String unit = d.getString("Units");
                            String username = d.getString("Username");
                            String docId = d.getId();

                            //creates a new row for the exercise and 4 text views in the row
                            TableRow tr = new TableRow(requireContext());
                            tr.setTag(docId); // Set document ID as tag
                            TextView tv1 = new TextView(requireContext());
                            TextView tv2 = new TextView(requireContext());
                            TextView tv3 = new TextView(requireContext());
                            TextView tv4 = new TextView(requireContext());

                            //set id for tablerow
                            tr.generateViewId();

                            //sets height of tablerow
                            tr.setMinimumHeight(60);

                            //sets the first text view with the username data for the exercise
                            tv1.setText(username);
                            tv1.setTextColor(Color.BLACK);
                            tv1.setPadding(10, 10, 10, 10);
                            tv1.setTextSize(12);
                            tv1.setWidth(tl.getWidth() / 4);

                            //adds it to the table row in the first column
                            tr.addView(tv1, 0);

                            //sets the second text view with the exercise data for the exercise
                            tv2.setText(exercise);
                            tv2.setTextColor(Color.BLACK);
                            tv2.setPadding(10, 10, 10, 10);
                            tv2.setTextSize(12);
                            tv2.setWidth(tl.getWidth() / 4);

                            //adds it to the table row in the second column
                            tr.addView(tv2, 1);

                            //sets the third text view with the amount data for the exercise
                            tv3.setText(amount);
                            tv3.setTextColor(Color.BLACK);
                            tv3.setPadding(10, 10, 10, 10);
                            tv3.setTextSize(12);
                            tv3.setWidth(tl.getWidth() / 4);

                            //adds it to the table row in the third column
                            tr.addView(tv3, 2);


                            //sets the fourth text view with the unit data for the exercise
                            tv4.setText(unit);
                            tv4.setTextColor(Color.BLACK);
                            tv4.setPadding(10, 10, 10, 10);
                            tv4.setTextSize(12);
                            tv4.setWidth(tl.getWidth() / 4);

                            //adds it to the table row in the fourth column
                            tr.addView(tv4, 3);

                            // Set a click listener on the TableRow to trigger the reply dialog
                            tr.setOnClickListener(v -> showReplyDialog(username, docId)); // Pass docId

                            //adds the row to the table
                            tl.addView(tr);
                        }
                    }
                });
    }

    // Function to refresh the table after replying or removing a row
    private void refreshTable() {
        try {
            fillTable();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Function to show reply dialog
    private void showReplyDialog(String username, String docId) {

        //build the alertdialog
        String[] choices = {"Keep at it!", "Good hustle!", "I'm falling behind!", "Proud of you!"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Encourage your friend?")
                .setPositiveButton("Send", (dialog, which) -> {
                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                    // Extract the selected reply from the choices array based on the user's selection
                    String reply = choices[selectedPosition];
                    // Check if the username is null, indicating that the reply action is for all rows
                    if (username == null) {
                        // If so, reply to all rows with the selected reply
                        replyToAllRows(reply);
                    } else {
                        // If the username is not null, reply to the specific row with the selected reply
                        sendReply(username, reply);
                        // Remove the corresponding row from the table based on its document ID
                        removeFromTable(docId);
                    }
                })
                .setNegativeButton("No, thanks", null)
                .setSingleChoiceItems(choices, 0, null);

        //show the alertdialog
        AlertDialog dialog = builder.create();
        dialog.show();
        //change button colors
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.GREEN);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.GRAY);
    }
}
