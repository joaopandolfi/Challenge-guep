package dersus.challenge_guep.model.persistence.DAO;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao on 29/05/18.
 */
public abstract class Dao<T> {
    protected SQLiteDatabase database ;

    public abstract ArrayList<T> query();

    public boolean isCreatedList(){
        List lista = query();
        if ( lista != null && lista.size() > 0) {
            return true ;
        }
        return false ;
    }
}

