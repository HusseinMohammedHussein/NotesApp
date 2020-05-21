package com.example.notes.activities.editor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.notes.activities.main.MainActivity;
import com.example.notes.api.APIClint;
import com.example.notes.api.ApiInterface;
import com.example.notes.model.NoteModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorPresenter {
    private EditorViewInterface editorViewInterface;

    public EditorPresenter(EditorViewInterface editorViewInterface) {
        this.editorViewInterface = editorViewInterface;
    }

    void saveNote(final String title, final String note, final int color) {
        editorViewInterface.showProgress();
        Call<NoteModel> call = APIClint.getApiClint()
                .create(ApiInterface.class)
                .saveNote(title, note, color);
        call.enqueue(new Callback<NoteModel>() {
            @Override
            public void onResponse(@NonNull Call<NoteModel> call, @NonNull Response<NoteModel> response) {
                editorViewInterface.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    boolean success = response.body().getmSuccess();
                    if (success) {
                        editorViewInterface.hideProgress();
                        editorViewInterface.onResultSuccess(response.body().getmMessage());
                    } else {
                        editorViewInterface.hideProgress();
                        editorViewInterface.onResultError(response.body().getmMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NoteModel> call, @NonNull Throwable t) {
                editorViewInterface.hideProgress();
                editorViewInterface.onResultError("Error Message: " + t.getLocalizedMessage());
            }
        });
    }

    void updateNote(int id, String title, String note, int color) {
        Call<NoteModel> call = APIClint.getApiClint().create(ApiInterface.class)
                .updateNote(id, title, note, color);

        call.enqueue(new Callback<NoteModel>() {
            @Override
            public void onResponse(@NonNull Call<NoteModel> call, @NonNull Response<NoteModel> response) {
                editorViewInterface.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    boolean success = response.body().getmSuccess();
                    if (success) {
                        editorViewInterface.onResultSuccess(response.body().getmMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NoteModel> call, @NonNull Throwable t) {
                editorViewInterface.hideProgress();
                editorViewInterface.onResultError(t.getLocalizedMessage());
            }
        });
    }

    void deleteNote(int id) {
        editorViewInterface.showProgress();
        Call<NoteModel> call = APIClint.getApiClint().create(ApiInterface.class).deleteNote(id);
        call.enqueue(new Callback<NoteModel>() {
            @Override
            public void onResponse(@Nullable Call<NoteModel> call, @Nullable Response<NoteModel> response) {
                editorViewInterface.hideProgress();
                if (Objects.requireNonNull(response).isSuccessful() && response.body() != null) {
                    boolean success = response.body().getmSuccess();
                    if (success) {
                        editorViewInterface.onResultSuccess(response.body().getmMessage());
                    }
                }
            }

            @Override
            public void onFailure(@Nullable Call<NoteModel> call, @Nullable Throwable t) {
                editorViewInterface.showProgress();
                editorViewInterface.onResultError(Objects.requireNonNull(t).getLocalizedMessage());
            }
        });
    }

}
