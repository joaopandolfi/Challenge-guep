package dersus.challenge_guep.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;

import dersus.challenge_guep.model.interfaces.Model;

/**
 * Created by joao on 29/05/18.
 */

public class GasStation implements Model<GasStation> {
    private int id;
    private String name;
    private String address;
    private LatLng latLng;

    public static final String nameTable = "GasStation";
    public static final String sqlCreateTable = "CREATE TABLE \"GasStation\" (\"id\" INTEGER PRIMARY KEY AUTOINCREMENT, \"name\" TEXT, \"address\" TEXT, \"lat\" REAL, \"lng\" REAL)";

    public GasStation(){
        this.id = 0;
        this.name = "";
        this.address = "";
        this.latLng = new LatLng(0,0);
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        //cv.put("id", String.valueOf(getId()));
        cv.put("name", String.valueOf(getName()));
        cv.put("address", String.valueOf(getAddress()));
        cv.put("lat", String.valueOf(getLatLng().latitude));
        cv.put("lng", String.valueOf(getLatLng().longitude));
        return cv;
    }

    public GasStation getCursorSqlite(Cursor c){
        GasStation g = new GasStation();
        g.setId(c.getInt(c.getColumnIndex("id")));// INT, NOT NULL
        g.setName(c.getString(c.getColumnIndex("name")));
        g.setAddress(c.getString(c.getColumnIndex("address")));
        g.setLatLng(c.getDouble(c.getColumnIndex("lat")),c.getDouble(c.getColumnIndex("lng")));

        return g ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setLatLng(double lat, double lng){
        this.latLng = new LatLng(lat,lng);
    }
}
