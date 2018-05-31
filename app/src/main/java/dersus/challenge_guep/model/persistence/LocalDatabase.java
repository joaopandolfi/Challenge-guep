package dersus.challenge_guep.model.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import dersus.challenge_guep.model.GasStation;

/**
 * Created by joao on 29/05/18.
 */

public class LocalDatabase {

    private static LocalDatabase instance;
    protected Context context;
    protected SQLiteDatabase database ;
    protected DatabaseGestor databaseGestor;

    final static String DATABASES_NAME = "challenge.db";
    final static int DATABASES_VERSION = 26;

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public static LocalDatabase getInstance(Context ctx){
        if ( instance == null) {
            instance = new LocalDatabase(ctx);
        }
        return instance ;
    }

    private LocalDatabase(Context ctx){
        databaseGestor = new DatabaseGestor(ctx, DATABASES_NAME, null, DATABASES_VERSION);
        database = open();
        context = ctx;
    }
    SQLiteDatabase open(){
        if (databaseGestor != null) {
            return databaseGestor.getWritableDatabase();
        }
        return null;
    }
    void close(){
        if ( databaseGestor != null )
            databaseGestor.close();
    }

}

class DatabaseGestor extends SQLiteOpenHelper {

    public DatabaseGestor(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {

        try {
            arg0.execSQL(GasStation.sqlCreateTable);
        } catch (SQLiteException e){

            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

        db.execSQL("DROP TABLE IF EXISTS " + GasStation.nameTable);
        onCreate(db);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

