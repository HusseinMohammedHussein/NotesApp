package com.example.notes.activities.editor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notes.R;
import com.thebluealliance.spectrum.SpectrumPalette;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EditActivity extends AppCompatActivity implements EditorViewInterface {
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_note)
    EditText etNote;
    @BindView(R.id.palate)
    SpectrumPalette mPalate;
    @BindView(R.id.nestedScroll)
    NestedScrollView mNestedScroll;
    private ProgressDialog progressDialog;
    private EditorPresenter presenter;

    Menu actionMenu;
    int mColor, id;
    String mTitle, mNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        initializeProgDialog();
        initializePalate();
        presenter = new EditorPresenter(this);
        getDataFromIntentExtra();
    }


    private void initializePalate() {
        mPalate.setOnColorSelectedListener(
                color -> {
                    mColor = color;
                }
        );

        mPalate.setSelectedColor(getResources().getColor(R.color.white, EditActivity.this.getTheme()));
        mColor = getResources().getColor(R.color.white, EditActivity.this.getTheme());


    }

    private void initializeProgDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
    }

    private void getDataFromIntentExtra() {
        id = getIntent().getIntExtra("id", 0);
        mTitle = getIntent().getStringExtra("title");
        mNote = getIntent().getStringExtra("note");
        mColor = getIntent().getIntExtra("color", 0);
        setDataFromIntentExtra();
    }

    private void setDataFromIntentExtra() {
        if (id != 0) {
            etTitle.setText(mTitle);
            etNote.setText(mNote);
//            mPalate.setSelectedColor(mColor);
            mNestedScroll.setBackgroundColor(mColor);
            Objects.requireNonNull(getSupportActionBar()).setTitle("Update Note");
            readMode();
        } else {
            mPalate.setSelectedColor(getResources().getColor(R.color.white, EditActivity.this.getTheme()));
            mColor = getResources().getColor(R.color.white, EditActivity.this.getTheme());
            editMode();
        }

    }

    private void readMode() {
        etTitle.setFocusableInTouchMode(false);
        etNote.setFocusableInTouchMode(false);
        etTitle.setFocusable(false);
        etNote.setFocusable(false);
        mPalate.setEnabled(false);
        mPalate.setVisibility(View.GONE);
    }

    private void editMode() {
        etTitle.setFocusableInTouchMode(true);
        etNote.setFocusableInTouchMode(true);
        mPalate.setVisibility(View.VISIBLE);
        mPalate.setEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        actionMenu = menu;

        if (id != 0) {
            actionMenu.findItem(R.id.edit).setVisible(true);
            actionMenu.findItem(R.id.delete).setVisible(true);
            actionMenu.findItem(R.id.save).setVisible(false);
            actionMenu.findItem(R.id.update).setVisible(false);
        } else {
            actionMenu.findItem(R.id.edit).setVisible(false);
            actionMenu.findItem(R.id.delete).setVisible(false);
            actionMenu.findItem(R.id.save).setVisible(true);
            actionMenu.findItem(R.id.update).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = etTitle.getText().toString().trim();
        String note = etNote.getText().toString().trim();
        int color = this.mColor;
        switch (item.getItemId()) {
            case R.id.save:
                if (title.isEmpty()) {
                    etTitle.setError("Field is Required!");
                } else if (note.isEmpty()) {
                    etNote.setError("Field is Required!");
                } else {
                    presenter.saveNote(title, note, color);
                }
                return true;

            case R.id.edit:
                editMode();
                actionMenu.findItem(R.id.edit).setVisible(false);
                actionMenu.findItem(R.id.delete).setVisible(false);
                actionMenu.findItem(R.id.save).setVisible(false);
                actionMenu.findItem(R.id.update).setVisible(true);
                return true;

            case R.id.update:
                if (title.isEmpty()) {
                    etTitle.setError("Field is Required!");
                } else if (note.isEmpty()) {
                    etNote.setError("Field is Required!");
                } else {
                    presenter.updateNote(id, title, note, color);
                }
                return true;

            case R.id.delete:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Confirm!");
                alert.setMessage("Are you sure Delete: " + mTitle);
                alert.setNegativeButton("Yes", ((dialog, which) ->{
                    dialog.dismiss();
                    presenter.deleteNote(id);
                }));
                alert.setPositiveButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.hide();
    }

    @Override
    public void onResultSuccess(String message) {
        if (checkConnection()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Internet Not Connection! onAddSuccess", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResultError(String message) {
        if (!checkConnection()) {
            Toast.makeText(this, "Internet Not Connection! onAddError", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error Message: " + message, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkConnection() {
        boolean connect = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            connect = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connect;
        } catch (Exception e) {
            Log.e("Connectivity Exception ", e.getMessage());
        }
        return connect;
    }
}
