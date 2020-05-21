package com.example.notes.activities.editor;

public interface EditorViewInterface {
    void showProgress();
    void hideProgress();
    void onResultSuccess(String message);
    void onResultError(String message);
}
