package com.example.arjunvidyarthi.booklistingapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    customAdapter mAdapter;
    Button go;
    EditText search;
    String search_name;
    TextView isEmpty;
    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView bookListView = (ListView) findViewById(R.id.books_list);
        mAdapter = new customAdapter(this, new ArrayList<bookData>());
        isEmpty = (TextView) findViewById(R.id.empty);
        pbar = (ProgressBar) findViewById(R.id.progressBar);
        pbar.setVisibility(View.GONE);
        bookListView.setEmptyView(isEmpty);
        bookListView.setAdapter(mAdapter);

        search = (EditText) findViewById(R.id.search_bar) ;
        go = (Button) findViewById(R.id.search_button);

            search.setOnKeyListener(
                    new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                                    (keycode == KeyEvent.KEYCODE_ENTER)) {
                                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo nInfo = connMgr.getActiveNetworkInfo();
                                if(nInfo ==null || !nInfo.isConnected()){
                                    mAdapter.clear();
                                    isEmpty.setText("No connection.");
                                    return false;
                                }

                                mAdapter.clear();
                                search_name = search.getText().toString();
                                isEmpty.setVisibility(View.GONE);
                                pbar.setVisibility(View.VISIBLE);
                                MyAsyncTask task = new MyAsyncTask();
                                task.execute("https://www.googleapis.com/books/v1/volumes?q="+search_name+"&maxResults=10");
                                return true;
                            }
                            return false;
                        }
                        }
            );

            go.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo nInfo = connMgr.getActiveNetworkInfo();
                            if(nInfo ==null || !nInfo.isConnected()){
                                mAdapter.clear();
                                isEmpty.setText("No connection.");
                                return ;
                            }


                            mAdapter.clear();
                            search_name = search.getText().toString();
                            isEmpty.setVisibility(View.GONE);
                            pbar.setVisibility(View.VISIBLE);
                            MyAsyncTask task = new MyAsyncTask();
                            task.execute("https://www.googleapis.com/books/v1/volumes?q="+search_name+"&maxResults=10");
                        }
                    }
            );

            bookListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            bookData currentBook = mAdapter.getItem(i);
                            String URL = currentBook.getInfoLink();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(URL));
                            startActivity(intent);
                        }
                    }
            );

    }

    private class MyAsyncTask extends AsyncTask<String, Void, ArrayList<bookData>>{

        @Override
        protected ArrayList<bookData> doInBackground(String... strings) {
            String url = strings[0];


            if(url==null){
                return null;
            }

            ArrayList<bookData> result = null;
            try {
                result = Utils.networkReq(url);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return result;

        }

        @Override
        protected void onPostExecute(ArrayList<bookData> bookDatas) {

            mAdapter.clear();

            if (bookDatas != null && !bookDatas.isEmpty()) {
                mAdapter.addAll(bookDatas);
            }
            pbar.setVisibility(View.GONE);
            isEmpty.setText("No books found for given search.");

        }
    }
}
