package com.example.notes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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
}
