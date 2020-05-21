package com.example.notes.activities.main;

import com.example.notes.model.NoteModel;

import java.util.List;

public interface MainViewInterface {
    void showLoading();
    void hideLoading();
    void onRequestResult(List<NoteModel> notes);
    void onRequestError(String message);
}
