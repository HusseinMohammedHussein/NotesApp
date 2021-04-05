package com.example.notes.activities.main;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.activities.editor.EditActivity;
import com.example.notes.model.NoteModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainViewInterface {
    private static final int INTENT_ADD = 100;
    private static final int INTENT_EDIT = 200;
    @BindView(R.id.rv_mainactivity)
    RecyclerView mRvMainactivity;
    @BindView(R.id.swipe_mainactivity)
    SwipeRefreshLayout mSwipeMainactivity;
    @BindView(R.id.add_icon)
    FloatingActionButton mAddIcon;

    MainPresenter presenter;
    MainAdapter adapter;
    MainAdapter.ItemClickListener itemClickListener;
    List<NoteModel> note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initializeVeiws();
        floatingAction();
        iniPresenter();
        iniSwipe();
        iniOnItemClick();
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Notes");
    }

    private void initializeVeiws() {
        mSwipeMainactivity = findViewById(R.id.swipe_mainactivity);
        mRvMainactivity = findViewById(R.id.rv_mainactivity);
        mRvMainactivity.setLayoutManager(new LinearLayoutManager(this));
        mRvMainactivity.setHasFixedSize(true);
    }

    private void floatingAction() {
        mAddIcon.setOnClickListener(v ->
            startActivityForResult(
                    new Intent(MainActivity.this, EditActivity.class),
                    INTENT_ADD)
        );
    }

    private void iniPresenter() {
        /*MainPresenter*/
        presenter = new MainPresenter(this);
        presenter.getData();
    }

    private void iniSwipe() {
        /*SwipeRefresh*/
        mSwipeMainactivity.setOnRefreshListener(
                () -> presenter.getData());

    }

    private void iniOnItemClick() {
        /*ItemClick*/
        itemClickListener = ((view, position) -> {
            NoteModel notePosition = note.get(position);
            int id = notePosition.getMId();
            String title = notePosition.getMTitle();
            String note = notePosition.getMNote();
            int color = notePosition.getMColor();

            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("title", title);
            intent.putExtra("note", note);
            intent.putExtra("color", color);
            startActivityForResult(intent, INTENT_EDIT);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_ADD && resultCode == RESULT_OK) {
            presenter.getData();
        } else if (requestCode == INTENT_EDIT && resultCode == RESULT_OK) {
            presenter.getData();
        }
    }

    @Override
    public void showLoading() {
        mSwipeMainactivity.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeMainactivity.setRefreshing(false);
    }

    @Override
    public void onRequestResult(List<NoteModel> notes) {
            adapter = new MainAdapter(this, notes, itemClickListener);
            adapter.notifyDataSetChanged();
            mRvMainactivity.setAdapter(adapter);
            note = notes;
    }

    @Override
    public void onRequestError(String message) {
        if (!checkConnection()) {
            hideLoading();
            message("Internet Isn't Connection!");
        }else{
            hideLoading();
            message(message);
        }
    }

    public boolean checkConnection() {
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

    void message(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
