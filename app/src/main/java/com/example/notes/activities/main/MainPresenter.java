package com.example.notes.activities.main;

import com.example.notes.api.APIClint;
import com.example.notes.api.ApiInterface;
import com.example.notes.model.NoteModel;

import java.util.List;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter {
    private MainViewInterface viewInterface;

    public MainPresenter(MainViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    void getData() {
        viewInterface.showLoading();
        ApiInterface apiInterface = APIClint.getApiClint().create(ApiInterface.class);
        Call<List<NoteModel>> call = apiInterface.getNotes();

        call.enqueue(new Callback<List<NoteModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<NoteModel>> call, @NonNull Response<List<NoteModel>> response) {
                viewInterface.hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    viewInterface.onRequestResult(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<NoteModel>> call, @NonNull Throwable t) {
                viewInterface.hideLoading();
                viewInterface.onRequestError(t.getLocalizedMessage());
            }
        });
    }
}
