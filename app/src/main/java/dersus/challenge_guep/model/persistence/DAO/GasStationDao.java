package dersus.challenge_guep.model.persistence.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import dersus.challenge_guep.model.GasStation;
import dersus.challenge_guep.model.persistence.LocalDatabase;

/**
 * Created by joao on 29/05/18.
 */

public class GasStationDao extends Dao<GasStation> {

    private static GasStationDao instance ;

    //Garante uma unica instancia
    public static GasStationDao getInstance(SQLiteDatabase sql){
        if ( instance == null ) instance = new GasStationDao(sql);
        return instance;
    }

    public static GasStationDao getInstance(Context ctx){
        SQLiteDatabase db =  LocalDatabase.getInstance(ctx).getDatabase();

        if ( instance == null ) instance = new GasStationDao(db);
        return instance;
    }


    public GasStationDao(SQLiteDatabase sql){
        database = sql ;
    }

    /*
    * Salva o GasStation
    * @return Booelan {Se deu certo ou nao}
    */
    public Boolean save(GasStation gasStation) {

        long idRetorno = database.insert(GasStation.nameTable, null, gasStation.getContentValues());
        if (idRetorno != -1)
            return true;
        return false;
    }

    /*
    * Salva a lista de GasStation
    * @return Booelan {Se deu certo ou nao}
    */
    public Boolean saveList(List<GasStation> gasStationList) {
        String whereClause;
        Cursor cursor;
        for(GasStation gasStation: gasStationList){
            whereClause = " id = '" + gasStation.getId() + "' ";

            cursor = database.query(GasStation.nameTable, null, whereClause, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return null;
            } else {
                long idRetorno = database.insert(GasStation.nameTable, null, gasStation.getContentValues());
                if (idRetorno != -1) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    * Busca todos os GasStations
    * @return List {Lista de GasStations}
    */
    @Override
    public ArrayList<GasStation> query(){
        ArrayList<GasStation> array = new ArrayList<>();
        String queryWhere = "";
        Cursor cursor = database.query(GasStation.nameTable, null, queryWhere, null, null, null, null);
        if ( cursor != null &&  cursor.moveToFirst() ){
            do{
                array.add( new GasStation().getCursorSqlite(cursor));
            } while (cursor.moveToNext());
        }
        return array;
    }

}
