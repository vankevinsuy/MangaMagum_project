package com.example.mangamagum.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mangamagum.Adapter.Library_Adapter;
import com.example.mangamagum.Model.Book;
import com.example.mangamagum.Model.DataBase;
import com.example.mangamagum.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Library extends AppCompatActivity {

    public ImageButton update_button;
    public ImageButton go_to_favorites;
    private RecyclerView manga_recycler_view;
    private RecyclerView.Adapter mAdapter;
    private EditText search_bar;

    private Context context;
    private Library activity;
    private DataBase dataBase;
    private Boolean internet_check;
    private Boolean network_check;

    public ArrayList<String> manga_table_data_list;
    public ArrayList<String> chapter_table_data_list;
    public ArrayList<String> page_table_data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library);

        update_button = findViewById(R.id.update_database);
        go_to_favorites = findViewById(R.id.go_to_favorite);
        go_to_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent favorite = new Intent(getApplicationContext(), Favorites.class);
                startActivity(favorite);
            }
        });
        search_bar = findViewById(R.id.search_bar);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        manga_recycler_view = findViewById(R.id.manga_recycler_view);
        manga_recycler_view.setHasFixedSize(false);
        manga_recycler_view.setLayoutManager(new GridLayoutManager(this,3));


        manga_table_data_list = new ArrayList<>();
        chapter_table_data_list = new ArrayList<>();
        page_table_data_list = new ArrayList<>();

        context = this.getApplicationContext();
        activity = this;
        dataBase = new DataBase(this);

        first_use(context);



//        Server server = new Server(this,this,dataBase);
//        server.execute();

//        internet_check = internetIsConnected();
//        network_check = isNetworkConnected(context);
//
//        ShowMessage("internet" , internet_check.toString());
//        ShowMessage("network" , network_check.toString());


        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_button.animate().rotation(update_button.getRotation()-360).setDuration(1000).start();
                fill_library(context);
                update_button.setEnabled(false);
                Server server = new Server(context , activity, dataBase);
                server.execute();
                dataBase.close();
                fill_library(context);
                update_button.setEnabled(true);
            }
        });

    }


    public ArrayList<Book> fill_library(Context context){
        final ArrayList<Book> arrayList_book = new ArrayList<>();
        dataBase = new DataBase(context);
        Cursor res= dataBase.get_all_manga();

        if (res.getCount() == 0){
//            ShowMessage("error", "no data found try to update");
            return null;
        }

        while (res.moveToNext()){
            String name = res.getString(2);
            String id_manga = res.getString(0);
            String cover_link = res.getString(1);

            arrayList_book.add(new Book(name, cover_link, id_manga));
        }

        mAdapter = new Library_Adapter(arrayList_book, context);
        ((Library_Adapter) mAdapter).setOnItemClickListener(new Library_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id_book = arrayList_book.get(position).getId_book();
                String manga_name = arrayList_book.get(position).getName();
                String cover_link = arrayList_book.get(position).getCover_link();

                Intent chosen_manga_activity = new Intent(getApplicationContext() , Chosen_manga.class);

                chosen_manga_activity.putExtra("id_book" , id_book);
                chosen_manga_activity.putExtra("manga_name" , manga_name);
                chosen_manga_activity.putExtra("cover_link" , cover_link);

                startActivity(chosen_manga_activity);
                finish();
            }
        });
        manga_recycler_view.setAdapter(mAdapter);
        return arrayList_book;
    }
    private void reload_activity(){
        Intent refresh = new Intent(getApplicationContext(), Library.class);
        startActivity(refresh);
    }
    private void ShowMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo()!= null;
    }
    private boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
    private void first_use(Context context){
        int use = -1;
        dataBase = new DataBase(context);
        Cursor res= dataBase.get_first_use();

        //if it's the first use
        if (res.getCount() == 0){
            dataBase.initiate_first_use();
            ShowMessage("Hello", "It's your first time on this app, wait a little bit, we are updating your database with all we have");
            update_button.setEnabled(false);
            Server server = new Server(context , activity, dataBase);
            server.execute();
            dataBase.close();
            fill_library(context);
            update_button.setEnabled(true);
        }


        //if we have already used
        while (res.moveToNext()){
            use = res.getInt(0);
            fill_library(context);
        }
    }


    // methode pour quitter l'application
    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Library.super.onBackPressed();
                        quit();
                        System.exit(0);
                    }
                }).create().show();
    }
    public void quit() {
        Intent start = new Intent(Intent.ACTION_MAIN);
        start.addCategory(Intent.CATEGORY_HOME);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(start);
    }

}

// class for updating the manga database
class Server extends AsyncTask<Void,Void, Void> {
    Context context;
    private WeakReference<Library> activityReference;

//    **** API URLs appart
//    private final String API_MANGA_URL = "http://192.168.0.21/MangaMaGum_API/get_manga_data.php";
//    private final String API_CHAPTER_URL = "http://192.168.0.21/MangaMaGum_API/get_chapters_data.php";
//    private final String API_PAGE_URL = "http://192.168.0.21/MangaMaGum_API/get_pages_data.php";

//    **** API URLs maison
//    private final String API_MANGA_URL = "http://192.168.0.35/MangaMaGum_API/get_manga_data.php";
//    private final String API_CHAPTER_URL = "http://192.168.0.35/MangaMaGum_API/get_chapters_data.php";
//    private final String API_PAGE_URL = "http://192.168.0.35/MangaMaGum_API/get_pages_data.php";


//    ***** API URLs isep
    private final String API_MANGA_URL = "http://172.16.231.127/MangaMaGum_API/get_manga_data.php";
    private final String API_CHAPTER_URL = "http://172.16.231.127/MangaMaGum_API/get_chapters_data.php";
    private final String API_PAGE_URL = "http://172.16.231.127/MangaMaGum_API/get_pages_data.php";



    private DataBase db;
    private ArrayList<String> data_list_manga;
    private ArrayList<String> data_list_chapter;
    private ArrayList<String> data_list_page;



    public Server(Context context, Library activity, DataBase dataBase) {
        this.context = context;
        activityReference = new WeakReference<Library>(activity);
        this.data_list_manga = new ArrayList<>();
        this.data_list_chapter = new ArrayList<>();
        this.data_list_page = new ArrayList<>();
        this.db = dataBase;

    }

    //**** filling list with server's datas
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL(API_MANGA_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line ="";

            while (line != null){
                line = bufferedReader.readLine();
                this.data_list_manga.add((String)line);
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

//      ------------------------------------------------------
        try {
            URL url = new URL(API_CHAPTER_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line ="";

            while (line != null){
                line = bufferedReader.readLine();
                this.data_list_chapter.add((String)line);
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        -----------------------------------------------------
        try {
            URL url = new URL(API_PAGE_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line ="";

            while (line != null){
                line = bufferedReader.readLine();
                this.data_list_page.add((String)line);
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Library activity = this.activityReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }
//------------------------------------
        activity.manga_table_data_list.clear();
        for(String n : this.data_list_manga){
            activity.manga_table_data_list.add(n);
        }
        this.db.clear_manga_table();

//**** Add manga's data in database
        for(String item : activity.manga_table_data_list){
            if (item != null){
                String manga_name = item.split(";")[0];
                String cover_link = item.split(";")[1];
                String id_manga = item.split(";")[2];
                this.db.insertManga(manga_name,cover_link,id_manga);
            }
        }
//---------------------------------------------
        activity.chapter_table_data_list.clear();
        for(String n : this.data_list_chapter){
            activity.chapter_table_data_list.add(n);
        }
        this.db.clear_chapter_table();

//**** Add manga's data in database
        for(String item : activity.chapter_table_data_list){
            if (item != null){
                String id_book = item.split(";")[0];
                String list_of_chapter = item.split(";")[1];

                this.db.insertChapter(Integer.parseInt(id_book), list_of_chapter);
            }
        }

//---------------------------------------------
        activity.page_table_data_list.clear();
        for(String n : this.data_list_page){
            activity.page_table_data_list.add(n);
        }
        this.db.clear_page_table();

//**** Add manga's data in database
        for(String item : activity.page_table_data_list){
            if (item != null){
                String id_book = item.split(";")[0];
                String num_chapitre = item.split(";")[1];
                String list_page = item.split(";")[2];

                this.db.insertPages(Integer.parseInt(id_book), Integer.parseInt(num_chapitre), list_page);
            }
        }
        this.db.close();
        Toast.makeText(context, "update done", Toast.LENGTH_SHORT).show();
        activity.fill_library(context);
        this.db.initiate_resume_table(context);
    }


}
