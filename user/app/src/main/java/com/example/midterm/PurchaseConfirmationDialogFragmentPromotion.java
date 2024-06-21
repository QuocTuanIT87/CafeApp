package com.example.midterm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.example.midterm.Model.Promotion;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PurchaseConfirmationDialogFragmentPromotion extends DialogFragment {



    private Promotion mPromotion;
    public PurchaseConfirmationDialogFragmentPromotion(Promotion promotion) {
        this.mPromotion = promotion;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.order_confirmation))
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("Promotion/" + mPromotion.getPromoId());
                    myRef.removeValue();
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                })
                .create();
    }

    public static String TAG = "PurchaseConfirmationDialog";
}
