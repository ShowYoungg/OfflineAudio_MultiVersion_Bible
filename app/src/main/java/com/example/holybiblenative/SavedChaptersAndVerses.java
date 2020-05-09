package com.example.holybiblenative;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "SavedChapter")
public class SavedChaptersAndVerses {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String books;
    private int chapter;
    private int verse;
    private String content;

    @Ignore
    public SavedChaptersAndVerses() {

        this.books = books;
        this.chapter = chapter;
        this.verse = verse;
        this.content = content;
    }

    public SavedChaptersAndVerses(int id, String books, int chapter,
                      int verse, String content) {
        this.id = id;
        this.books = books;
        this.chapter = chapter;
        this.verse = verse;
        this.content = content;
    }

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
