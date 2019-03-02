package com.example.mangamagum.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {

    //****DATABASE VARIABLES****//

    private static final String DATA_BASE_name = "mangamagum.db";
    private static final String TABLE_manga_0 = "manga";
    private static final String COL_manga_name_0 = "manga_name";
    private static final String COL_cover_link_0 = "cover_link";
    private static final String COL_id_manga_0 = "id_manga";

    private static final String TABLE_chapters_name_1 = "chapters";
    private static final String COL_id_book_1 = "id_book";
    private static final String COL_list_chapters_1 = "list_chapters";

    private static final String TABLE_pages_name_2 = "pages";
    private static final String COL_id_book_2 = "id_book";
    private static final String COL_chapitre_2 = "chapitre";
    private static final String COL_list_page_2 = "liste_page";

    private static final String TABLE_user = "user";
    private static final String COL_first_use = "first_use";

    private static final String TABLE_favorite_manga = "favorite_manga";
    private static final String COL_favorite_manga = "id_manga";

    private static final String TABLE_resume_manga = "resume_manga";
    private static final String COL_resume_manga_id_manga = "id_manga";
    private static final String COL_resume_manga_num_chapitre = "chapitre";


    //****QUERY FOR CREATING THE TABLES****//
    private static final String CREATE_TABLE_MANGA = "CREATE TABLE " + TABLE_manga_0 + "(" +
            COL_id_manga_0   + " INTEGER," +
            COL_cover_link_0 + " TEXT, " +
            COL_manga_name_0 + " TEXT" + ")" ;

    private static final String CREATE_TABLE_CHAPTER = "CREATE TABLE " + TABLE_chapters_name_1 + "(" +
            COL_id_book_1 + " INTEGER," +
            COL_list_chapters_1 + " TEXT " + ")" ;

    private static final String CREATE_TABLE_PAGE = "CREATE TABLE " + TABLE_pages_name_2 + "(" +
            COL_id_book_2 + " INTEGER , " +
            COL_chapitre_2 + " INTEGER, " +
            COL_list_page_2   + " TEXT " + ")" ;

    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_user + "(" +
            COL_first_use + " INTEGER"+ ")";

    private static final String CREATE_TABLE_FAVORITE_MANGA = "CREATE TABLE " + TABLE_favorite_manga + "(" +
            COL_favorite_manga + " INTEGER"+ ")";

    private static final String CREATE_TABLE_RESUME_CHAPTER = "CREATE TABLE " + TABLE_resume_manga + "(" +
            COL_resume_manga_id_manga + " INTEGER ," +
            COL_resume_manga_num_chapitre + " INTEGER" + ")";


    public DataBase(Context context) {
        super(context, DATA_BASE_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MANGA);
        db.execSQL(CREATE_TABLE_CHAPTER);
        db.execSQL(CREATE_TABLE_PAGE);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_FAVORITE_MANGA);
        db.execSQL(CREATE_TABLE_RESUME_CHAPTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //**** DROP old tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_manga_0);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_chapters_name_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_pages_name_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_favorite_manga);


        //create new TABLES
        onCreate(db);
    }


    public void insertManga(String manga_name , String cover_link, String id_manga){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_manga_name_0, manga_name);
        contentValues.put(COL_cover_link_0, cover_link);
        contentValues.put(COL_id_manga_0, id_manga);

        database.insert(TABLE_manga_0,null,contentValues);

    }

    public void insertChapter(int id_book , String list_of_chapters){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_id_book_1, id_book);
        contentValues.put(COL_list_chapters_1, list_of_chapters);

        database.insert(TABLE_chapters_name_1,null,contentValues);
    }

    public void insertPages(int id_book, int num_chapitre, String list_pages){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_id_book_2, id_book);
        contentValues.put(COL_chapitre_2, num_chapitre);
        contentValues.put(COL_list_page_2, list_pages);

        database.insert(TABLE_pages_name_2,null,contentValues);
    }

    public void clear_manga_table(){
        SQLiteDatabase database = this.getWritableDatabase();
//        database.delete(TABLE_manga_0,null,null);
        database.execSQL("DELETE FROM " + TABLE_manga_0 );

    }

    public void clear_chapter_table(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + TABLE_chapters_name_1);

    }

    public void clear_page_table(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + TABLE_pages_name_2);

    }

    public ArrayList<Book> get_all_manga(){
        SQLiteDatabase database = this.getWritableDatabase();
        ArrayList<Book> arrayList_book = new ArrayList<>();


        Cursor res = database.rawQuery("SELECT * FROM " + TABLE_manga_0 + " ORDER BY " + COL_id_manga_0 , null);

        if (res.getCount() >0){
            while (res.moveToNext()){
                String name = res.getString(2);
                String id_manga = res.getString(0);
                String cover_link = res.getString(1);

                arrayList_book.add(new Book(name, cover_link, id_manga));
            }
        }
        res.close();
        return arrayList_book;
    }

    private Cursor get_all_manga_cursor(){
        SQLiteDatabase database = this.getWritableDatabase();

        return database.rawQuery("SELECT * FROM " + TABLE_manga_0 + " ORDER BY " + COL_id_manga_0 , null);
    }

    public ArrayList<String> get_all_manga_names(){
        SQLiteDatabase database = this.getWritableDatabase();
        ArrayList<String> list_name = new ArrayList<>();

        Cursor res = database.rawQuery("SELECT * FROM " + TABLE_manga_0 + " ORDER BY " + COL_manga_name_0 , null);
        if (res.getCount() >0){
            while (res.moveToNext()){
                list_name.add(res.getString(2));
            }
        }
        res.close();
        return list_name;
    }

    public Book get_book_from_id(int id_book){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor res = database.rawQuery("SELECT * FROM " + TABLE_manga_0 + " WHERE " + "id_manga= " + id_book , null);
        String manga_name = "";
        String cover_link = "";
        String id_manga = "";

        if (res.getCount() >0){
            while (res.moveToNext()){
                id_manga = res.getString(0);
                cover_link = res.getString(1);
                manga_name = res.getString(2);

            }
        }
        res.close();
        return new Book(manga_name, cover_link,id_manga);
    }

    public int get_last_chapter(String id_book){
        SQLiteDatabase database = this.getReadableDatabase();

        String list_chapter = "";
        try (Cursor cursor = (Cursor) database.rawQuery("SELECT * FROM " + TABLE_chapters_name_1 + " WHERE id_book= " + id_book, null)) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    list_chapter = cursor.getString(1);
                }
            }
        }

        list_chapter = list_chapter.replace('[', ' ');
        list_chapter = list_chapter.replace(']', ' ');
        list_chapter = list_chapter.replace('"', ' ');
        list_chapter = list_chapter.replaceAll("\\s+","");
        String[] chapters = list_chapter.split(",");
        return Integer.parseInt(chapters[chapters.length - 1]);
    }

    public ArrayList<String> get_page_by_chapitre(int id_book, int chapitre){

        ArrayList<String> list_of_link_page = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();

        String item = "";
        try (Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_pages_name_2 + " WHERE " + COL_id_book_2 + " = " + Integer.toString(id_book) + " AND " + COL_chapitre_2 + " = " + Integer.toString(chapitre), null)) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    item = cursor.getString(2);
                }
            }
        }

        item = item.replace('[', ' ');
        item = item.replace(']', ' ');
        item = item.replace("'", "");
        item = item.replaceAll("\\s+", "");

        String[] url = item.split(",");
        for (String i :url){
            if (i != "" || i!= null){
                list_of_link_page.add(i);
            }
        }

        return list_of_link_page;
    }

    public void initiate_first_use(){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_first_use, 1000);

        database.insert(TABLE_user,null,contentValues);

    }

    public Cursor get_first_use(){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TABLE_user , null);
    }

    public void add_favorite(int id_manga){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_favorite_manga, id_manga);
        database.insert(TABLE_favorite_manga,null,contentValues);

    }

    public void remove_favorite(int id_manga ){
        String delete_fav = "DELETE FROM " +  TABLE_favorite_manga +  " WHERE " + COL_favorite_manga + "=" + Integer.toString(id_manga);
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor res = database.rawQuery(delete_fav,null);
        database.delete(TABLE_favorite_manga, "id_manga=" + Integer.toString(id_manga), null);
        res.close();
    }

    public boolean is_one_of_favorite(int id_manga){
        boolean contain = false;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor res = database.rawQuery("SELECT * FROM " + TABLE_favorite_manga + " ORDER BY " + COL_favorite_manga , null);
        ArrayList<Integer>list_of_fav = new ArrayList<>();

        if(res.getCount() > 0){
            while (res.moveToNext()){
                list_of_fav.add(res.getInt(0));
            }
            if (list_of_fav.contains(id_manga)){
                contain = true;
            }
        }
        else {
            res.close();
        }
        return contain;
    }

    public ArrayList<Integer> get_all_favorites(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor res = database.rawQuery("SELECT * FROM " + TABLE_favorite_manga , null);
        ArrayList<Integer> list_id_fav = new ArrayList<>();

        if(res.getCount() > 0){
            while (res.moveToNext()){
                list_id_fav.add(res.getInt(0));
            }
        }
        else {
            return list_id_fav;
        }
        res.close();
        return list_id_fav;
    }

    public void initiate_resume_table(){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ArrayList<Integer> list_id = new ArrayList<>();

        Cursor cursor = get_all_manga_cursor();
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                int id_manga = Integer.parseInt(cursor.getString(0));
                list_id.add(id_manga);
            }
        }
        for (Integer item : list_id){
            contentValues.put(COL_resume_manga_id_manga, item);
            contentValues.put(COL_resume_manga_num_chapitre, 1);
            database.insert(TABLE_resume_manga,null,contentValues);
        }
        cursor.close();
    }

    public int get_chapter_to_resume(int id_manga){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor res = database.rawQuery("SELECT * FROM " + TABLE_resume_manga + " WHERE " + "id_manga= " + id_manga , null);
        int chapter = 1;

        if (res.getCount() >0){
            while (res.moveToNext()){
                chapter = res.getInt(1);
            }
        }
        res.close();
        return chapter;
    }

    public void update_my_chapter(int new_my_chapter, String id_manga){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_resume_manga_num_chapitre, new_my_chapter);

        db.update(TABLE_resume_manga, contentValues, COL_resume_manga_id_manga+"=?", new String[]{id_manga});
    }


    public ArrayList<Book> search_bar_database(CharSequence input){
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<Book> like_list_result = new ArrayList<>();
        Cursor res = database.rawQuery("SELECT * FROM " + TABLE_manga_0 + " WHERE " + COL_manga_name_0+ " LIKE '%" + input.toString() + "%'" , null);

        if (res.getCount() >0){
            while (res.moveToNext()){
                String name = res.getString(2);
                String id_manga = res.getString(0);
                String cover_link = res.getString(1);

                like_list_result.add(new Book(name, cover_link, id_manga));
            }
        }
        res.close();
        return like_list_result;
    }

}