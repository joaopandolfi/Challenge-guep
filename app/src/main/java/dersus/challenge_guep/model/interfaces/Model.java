package dersus.challenge_guep.model.interfaces;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by joao
 */
public interface Model<T> {
    public ContentValues getContentValues();
    public T getCursorSqlite(Cursor c);
}

