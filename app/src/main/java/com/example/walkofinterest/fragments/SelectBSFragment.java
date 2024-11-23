package com.example.walkofinterest.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.walkofinterest.R;
import com.example.walkofinterest.interfaces.OnBottomSheetClosedListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SelectBSFragment extends BottomSheetDialogFragment {
    boolean isSelectOnMap = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.CL_SelectOnMap).setOnClickListener(v -> {
            isSelectOnMap = true;
            dismiss();
        });

        view.findViewById(R.id.CL_CurrentLocation).setOnClickListener(v -> {
            //...logic current location
            dismiss();
        });

        view.findViewById(R.id.btnClose).setOnClickListener(v -> dismiss());
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        if (manager.findFragmentByTag(tag) == null) {
            super.show(manager, tag);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            if (bottomSheet != null)
                bottomSheet.setBackground(null);
        });
        return dialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getActivity() instanceof OnBottomSheetClosedListener) {
            ((OnBottomSheetClosedListener) getActivity()).onBottomSheetClosed(isSelectOnMap);
        }
    }
}