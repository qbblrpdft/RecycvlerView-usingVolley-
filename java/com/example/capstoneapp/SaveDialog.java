package com.example.capstoneapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveDialog extends DialogFragment {
    private EditText filenameEditText;
    private Context context;
    private SaveDialogListener listener;

    public SaveDialog() {}

    public SaveDialog(Context context, SaveDialogListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_save, null);
        filenameEditText = dialogView.findViewById(R.id.filename_edit);

        builder.setView(dialogView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String filename = filenameEditText.getText().toString().trim();
                        if (!filename.isEmpty()) {
                            if (listener != null) {
                                try {
                                    // Get the Documents folder
                                    File documentsFolder = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                                    // Create the file in the Documents folder
                                    File file = new File(documentsFolder, filename + ".csv");
                                    listener.onSave(file);
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (listener != null) {
                            listener.onCancel();
                        }
                        dialog.cancel();
                    }
                });
        return builder.create();
    }



    public interface SaveDialogListener {
        void onSave(File file);
        void onCancel();
    }
}


