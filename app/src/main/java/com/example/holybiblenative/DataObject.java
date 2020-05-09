package com.example.holybiblenative;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by SHOW on 8/17/2018.
 */


@Entity(tableName = "Bible")
public class DataObject implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String books;
    private int chapter;
    private int verse;
    private String content;

    @Ignore
    public DataObject() {

        this.books = books;
        this.chapter = chapter;
        this.verse = verse;
        this.content = content;
    }

    public DataObject(int id, String books, int chapter,
                      int verse, String content) {
        this.id = id;
        this.books = books;
        this.chapter = chapter;
        this.verse = verse;
        this.content = content;
    }

    protected DataObject(Parcel in) {
        id = in.readInt();
        books = in.readString();
        chapter = in.readInt();
        verse = in.readInt();
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(books);
        dest.writeInt(chapter);
        dest.writeInt(verse);
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DataObject> CREATOR = new Creator<DataObject>() {
        @Override
        public DataObject createFromParcel(Parcel in) {
            return new DataObject(in);
        }

        @Override
        public DataObject[] newArray(int size) {
            return new DataObject[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBooks() {
        return books;
    }

    public void setBooks(String books) {
        this.books = books;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getChapter() {
        return chapter;
    }

    public void setVerse(int verse) {
        this.verse = verse;
    }

    public int getVerse() {
        return verse;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
