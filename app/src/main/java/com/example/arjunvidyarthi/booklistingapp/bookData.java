package com.example.arjunvidyarthi.booklistingapp;


import android.graphics.Bitmap;

import java.util.ArrayList;

public class bookData {
    private String bookTitle;
    private ArrayList<String> bookAuthors;
    private Bitmap bookImageBitmap;
    private String infoLink;

    public bookData(String bookTitle, ArrayList<String> bookAuthors, Bitmap bookImageBitmap, String infoLink){
        this.bookTitle = bookTitle;
        this.bookAuthors = bookAuthors;
        this.bookImageBitmap = bookImageBitmap;
        this.infoLink = infoLink;
    }

    public String getBookTitle(){
        return bookTitle;
    }

    public ArrayList<String> getBookAuthors(){
        return bookAuthors;
    }

    public Bitmap getBookImageBitmap() {
        return bookImageBitmap;
    }

    public String getInfoLink(){return infoLink;}
}
