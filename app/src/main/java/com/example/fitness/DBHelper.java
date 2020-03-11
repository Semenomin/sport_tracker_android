package com.example.fitness;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DBHelper extends SQLiteOpenHelper {

    Context maincontext;
    private static final String FILE_NAME = "salt";
    private FileOutputStream fos;
    private FileInputStream fis;

    public DBHelper(@Nullable Context context) {
        super(context, "fitness.db", null, 1);
        this.maincontext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.execSQL("CREATE TABLE IF NOT EXISTS user (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name TEXT NOT NULL," +
                " weight INTEGER not null,"+
                " password text not null"+
                " )");

        db.execSQL("CREATE TABLE IF NOT EXISTS trainee (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " weight INTEGER ," +
                " date text not null" +
                " )");

        db.execSQL("CREATE TABLE IF NOT EXISTS exercise (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " exercise_name TEXT NOT NULL,"+
                " exercise_type TEXT NOT NULL," +
                " podhody integer not null," +
                " kollvo integer not null," +
                " id_train integer not null," +
                " FOREIGN KEY (id_train) REFERENCES trainee(id) on delete cascade" +
                " )");    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table user");
        db.execSQL("drop table trainee");
        db.execSQL("drop table exercise");
    }

    void createUser(String u_name,String u_password,int u_weight,Context ctx){
        if(u_name.length() >=2){
            if(u_password.length() >=4){
                String pass=null;
                if(readFile(ctx)==null){
                    byte[] sa = getSalt();
                    try {
                        pass = new String(getHash(sa,u_password));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    writeFile(sa,ctx);
                }
                else{
                    byte[]salt = readFile(ctx);
                    try {
                        pass = new String(getHash(salt,u_password));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
                SQLiteDatabase database = getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", u_name);
                contentValues.put("password", pass);
                contentValues.put("weight", u_weight);
                addTrainee(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),String.valueOf(u_weight));
                if (database.insert("user", null, contentValues) == -1)
                {
                    Toast.makeText(ctx, "Registration error, please enter valid name or password", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ctx, "Registration success", Toast.LENGTH_LONG).show();
                }
            }
            else Toast.makeText(ctx, "Password is less than 4 symbols", Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(ctx, "Name is less than 2 symbols", Toast.LENGTH_LONG).show();

    }

    void signIn(String password)  {
        SQLiteDatabase db = getReadableDatabase();
        Cursor query = db.rawQuery("select * from user" ,null);
        if(query.moveToFirst()){
             String pass = query.getString(3);
             byte[] salt = readFile(maincontext);
            String pass_input = null;
            try {
                pass_input = new String(getHash(salt,password));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if(pass.equals(pass_input)){
                 Intent intent = new Intent(maincontext,MainActivity.class);
                 intent.putExtra("name",query.getString(1));
                 intent.putExtra("weight",query.getInt(2));
                 maincontext.startActivity(intent);
             }
             else Toast.makeText(maincontext, "Invalid Password", Toast.LENGTH_LONG).show();
        }
    }

    boolean userExist(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor query = db.rawQuery("select * from user" ,null);
        return query.moveToFirst() && query.getCount() != 0;
    }

    String getUserName(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor query = db.rawQuery("select * from user" ,null);
        if(query.moveToFirst()){
           return query.getString(1);
        }
        return null;
    }

    List<Weight> getWeights(){
        List<Weight> weights = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor query =  db.rawQuery("select * from trainee",null);
        if(query.moveToFirst()){
            do{
                Weight f = new Weight();
                f.setWeight(query.getInt(1));
                f.setDate(query.getString(2));
                weights.add(f);
            }
            while(query.moveToNext());
        }
        return weights;
    }

    int getWeight(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor query =  db.rawQuery("select * from user",null);
        if(query.moveToFirst()){
            do{
                return query.getInt(2);
            }
            while(query.moveToNext());
        }
        return 0;
    }

    int getNewIdTraining(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor query =  db.rawQuery("SELECT Id FROM trainee ORDER BY Id DESC LIMIT 1",null);
        if(query.moveToFirst()){
            do{
               return query.getInt(0)+1;
            }
            while(query.moveToNext());
        }
        return 0;
    }

    public List<Exercise> getExercises(String id) {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor query =  db.rawQuery("select * from exercise where id_train = "+id,null);
        if(query.moveToFirst()){
            do{
                Exercise f = new Exercise();
                f.setName(query.getString(1));
                f.setType(query.getString(2));
                f.setKollvo(query.getInt(4));
                f.setPodhody(query.getInt(3));
                exercises.add(f);
            }
            while(query.moveToNext());
        }
        return exercises;
    }

    public void addTrainee(String date, String weight){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        if(weight != null){
            contentValues.put("weight", weight);
        }
        if (database.insert("trainee", null, contentValues) == -1)
        {
            Toast.makeText(maincontext, "Error", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(maincontext, "Success", Toast.LENGTH_LONG).show();
        }
    }

    public void addExercise(Exercise e,int id){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("exercise_name", e.getName());
        contentValues.put("exercise_type", e.getType());
        contentValues.put("podhody", e.getPodhody());
        contentValues.put("kollvo", e.getKollvo());
        contentValues.put("id_train", id);

        if (database.insert("exercise", null, contentValues) == -1)
        {
            Toast.makeText(maincontext, "Error", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(maincontext, "Success", Toast.LENGTH_LONG).show();
        }
    }


    public void deleteTrainee(String date){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from exercise where id_train = (select id from trainee where date = \""+ date + "\")");
        db.execSQL("delete from trainee where date = \""+ date + "\"");
    }

    public int getIdTranByDate(String date){
        SQLiteDatabase db = getReadableDatabase();
        Cursor query =  db.rawQuery("select id from trainee where date = \""+date+"\"",null);
        if(query.moveToFirst()){
            do{
                return query.getInt(0);
            }
            while(query.moveToNext());
        }
        return 0;
    }

    private byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[]salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] getHash(byte[] salt, String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        return md.digest(password.getBytes(StandardCharsets.UTF_8));
    }

    private void writeFile(byte[] salt, Context ctx) {
        try {
            ctx.deleteFile(FILE_NAME);
            fos = ctx.openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(salt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] readFile(Context ctx) {
        try {
            fis = ctx.openFileInput(FILE_NAME);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            return bytes;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
