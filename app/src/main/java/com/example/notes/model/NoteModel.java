package com.example.notes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoteModel {
    @SerializedName("id")
    @Expose
    private int mId;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("note")
    @Expose
    private String mNote;

    @SerializedName("color")
    @Expose
    private int mColor;

    @SerializedName("date")
    @Expose
    private String mDate;

    @SerializedName("success")
    @Expose
    private Boolean mSuccess;

    @SerializedName("message")
    @Expose
    private String mMessage;

    public NoteModel(int mId, String mTitle, String mNote, int mColor, String mDate) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mNote = mNote;
        this.mColor = mColor;
        this.mDate = mDate;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmNote() {
        return mNote;
    }

    public void setmNote(String mNote) {
        this.mNote = mNote;
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public Boolean getmSuccess() {
        return mSuccess;
    }

    public void setmSuccess(Boolean mSuccess) {
        this.mSuccess = mSuccess;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}
