package com.example.resoluteapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.resoluteapp.databinding.FragmentPrevActivityBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Table;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrevActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrevActivityFragment extends Fragment {

    private FragmentPrevActivityBinding binding;

    FirebaseFirestore DB = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentPrevActivityBinding.inflate(inflater, container, false);

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

        //Home Button
        binding.toHomeFromPreviousActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(PrevActivityFragment.this)
                        .navigate(R.id.action_prevActivityFragment_to_homeFragment);
            }
        });
    }

    public void fillTable() throws InterruptedException, ExecutionException {

        DB.collection("victoria_exercises").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            TableLayout tl = (TableLayout) getView().findViewById(R.id.tableLayout);
                            int tableWidth = tl.getWidth() / 4;

                            tl.removeViews(1, Math.max(0, tl.getChildCount() - 1));


                            for (DocumentSnapshot d : list) {
                                String date = d.getString("date");
                                String exercise = d.getString("exercise");
                                String amount = d.getString("amount");
                                String unit = d.getString("unit");

                                TableRow tr = new TableRow(getActivity().getApplicationContext());
                                TextView tv1 = new TextView(getActivity().getApplicationContext());
                                TextView tv2 = new TextView(getActivity().getApplicationContext());
                                TextView tv3 = new TextView(getActivity().getApplicationContext());
                                TextView tv4 = new TextView(getActivity().getApplicationContext());

                                tv1.setText(date);
                                tv1.setTextColor(Color.BLACK);
                                tv1.setPadding(10, 10, 10, 10);
                                tv1.setTextSize(12);
                                tv1.setWidth(tableWidth);

                                tr.addView(tv1, 0);

                                tv2.setText(exercise);
                                tv2.setTextColor(Color.BLACK);
                                tv2.setPadding(10, 10, 10, 10);
                                tv2.setTextSize(12);
                                tv2.setWidth(tableWidth);

                                tr.addView(tv2, 1);

                                tv3.setText(amount);
                                tv3.setTextColor(Color.BLACK);
                                tv3.setPadding(10, 10, 10, 10);
                                tv3.setTextSize(12);
                                tv3.setWidth(tableWidth);

                                tr.addView(tv3, 2);

                                tv4.setText(unit);
                                tv4.setTextColor(Color.BLACK);
                                tv4.setPadding(10, 10, 10, 10);
                                tv4.setTextSize(12);
                                tv4.setWidth(tableWidth);

                                tr.addView(tv4, 3);

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