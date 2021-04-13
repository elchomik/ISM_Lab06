package pollub.ism.ism_lab06;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MagazynBazaDanych extends SQLiteOpenHelper {

    private static final String NAZWA_BAZY="Stoisko z warzywami";
    static final int WERSJA=1;
    public static final String NAZWA_TABELI="Warzywniak";
    public static final String NAZWA_KOLUMNY_1="NAME";
    public static final String NAZWA_KOLUMNY_2="QUANTITY";

    private final Context context;

    public MagazynBazaDanych(Context context) {
        super(context,NAZWA_BAZY,null,WERSJA);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlString="CREATE TABLE "+NAZWA_TABELI+"(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+NAZWA_KOLUMNY_1+" TEXT,"+NAZWA_KOLUMNY_2+" INTEGER)";
        db.execSQL(sqlString);

        ContentValues krotka=new ContentValues();
        String[] asortyment=context.getResources().getStringArray(R.array.Asortyment);

        for(String pozycja : asortyment){
            krotka.clear();
            krotka.put(NAZWA_KOLUMNY_1,pozycja);
            krotka.put(NAZWA_KOLUMNY_2,0);
            db.insert(NAZWA_TABELI,null,krotka);
        }

    }

    public Integer podajIlosc(String wybraneWarzywo){
        Integer ilosc=null;

        try (SQLiteDatabase bazaDoOdczytu = getReadableDatabase(); Cursor kursor = bazaDoOdczytu.query(
                MagazynBazaDanych.NAZWA_TABELI,
                new String[]{MagazynBazaDanych.NAZWA_KOLUMNY_2},
                MagazynBazaDanych.NAZWA_KOLUMNY_1 + "=?", new String[]{wybraneWarzywo},
                null, null, null)) {
            kursor.moveToFirst();
            ilosc = kursor.getInt(0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ilosc;
    }

    public void zmienStanMagazynu(String wybraneWarzywo,int nowaIlosc){

        try (SQLiteDatabase bazaDoZapisu = getWritableDatabase()) {

            ContentValues krotka = new ContentValues();
            krotka.put(NAZWA_KOLUMNY_2, Integer.toString(nowaIlosc));

            bazaDoZapisu.update(NAZWA_TABELI, krotka, NAZWA_KOLUMNY_1 + "=?", new String[]{wybraneWarzywo});
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
