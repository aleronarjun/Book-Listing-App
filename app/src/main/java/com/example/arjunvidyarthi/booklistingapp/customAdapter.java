package com.example.arjunvidyarthi.booklistingapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Arjun Vidyarthi on 06-Oct-17.
 */

public class customAdapter extends ArrayAdapter<bookData> {
    public customAdapter(Activity context, ArrayList<bookData> resource) {

        super(context, 0, resource);

    }



    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null )
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        bookData currentBookData = getItem(position);

        TextView bookTitle = listItemView.findViewById(R.id.book_title);
        bookTitle.setText(currentBookData.getBookTitle());

        TextView bookAuthor = listItemView.findViewById(R.id.book_author);
        StringBuilder authors = new StringBuilder();

        authors.append(currentBookData.getBookAuthors().get(0));

        if(currentBookData.getBookAuthors().size()>1) {
            for (int i = 1; i < currentBookData.getBookAuthors().size(); i++) {

                authors.append(", " + currentBookData.getBookAuthors().get(i));
            }
        }

        bookAuthor.setText(authors.toString());

        ImageView bookImage = listItemView.findViewById(R.id.book_image);
        bookImage.setImageBitmap(currentBookData.getBookImageBitmap());

        return listItemView;

}


}