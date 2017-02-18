package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Kosh on 18 Feb 2017, 11:15 PM
 */

@Setter @Getter @NoArgsConstructor
public class CreateGistModel implements Parcelable {
    private HashMap<String, FilesListModel> files;
    private String description;
    @SerializedName("public") private boolean publicGist;

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.files);
        dest.writeString(this.description);
        dest.writeByte(this.publicGist ? (byte) 1 : (byte) 0);
    }

    @SuppressWarnings({"WeakerAccess", "unchecked"}) protected CreateGistModel(Parcel in) {
        this.files = (HashMap<String, FilesListModel>) in.readSerializable();
        this.description = in.readString();
        this.publicGist = in.readByte() != 0;
    }

    public static final Parcelable.Creator<CreateGistModel> CREATOR = new Parcelable.Creator<CreateGistModel>() {
        @Override public CreateGistModel createFromParcel(Parcel source) {return new CreateGistModel(source);}

        @Override public CreateGistModel[] newArray(int size) {return new CreateGistModel[size];}
    };
}
