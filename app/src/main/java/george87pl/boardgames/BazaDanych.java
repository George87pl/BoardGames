package george87pl.boardgames;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Prosta klasa bazodanowa
 *
 * Używana jedynie przez {@link AppProvider}.
 */

class BazaDanych extends SQLiteOpenHelper {
    private static final String TAG = "BazaDanych";

    private static final String BAZA_NAZWA = "PlayGames.db";
    private static final int BAZA_WERSJA = 1;

    //Implementacja BazyDanych jako Singleton
    private static BazaDanych instancja = null;

    public BazaDanych(Context context) {
        super(context, BAZA_NAZWA, null, BAZA_WERSJA);
    }
    /**
     * Tworzy jedyną instancję (singleton) klasy bazodanowej
     *
     * @param context content provider context
     * @return obiekt klasy SQLiteOpenHelper
     */
    public static BazaDanych stworzInstancje(Context context){
        if(instancja == null) {
            Log.d(TAG, "stworzInstancje: tworzenie nowej instancji");
            instancja = new BazaDanych(context);
        }
        return instancja;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: start");
        String sSQL; //Zmienna typu String do analizowania logów

        sSQL = "CREATE TABLE IF NOT EXISTS " +RozgrywkaContract.TABELA_NAZWA + " ("
                +RozgrywkaContract.Kolumny.ROZGRYWKA_ID+ " INTEGER PRIMARY KEY NOT NULL, "
                +RozgrywkaContract.Kolumny.ROZGRYWKA_GRA+ " INTEGER, "
                +RozgrywkaContract.Kolumny.ROZGRYWKA_NAZWA+ " INTEGER, "
                +RozgrywkaContract.Kolumny.ROZGRYWKA_DATA+ " TEXT, "
                +RozgrywkaContract.Kolumny.ROZGRYWKA_OPIS+ " TEXT, "
                +RozgrywkaContract.Kolumny.ROZGRYWKA_ZDJECIE+ " TEXT);";
        Log.d(TAG, sSQL);
        db.execSQL(sSQL);

        sSQL = "CREATE TABLE IF NOT EXISTS " +GraContract.TABELA_NAZWA + " ("
                +GraContract.Kolumny.GRA_ID+ " INTEGER PRIMARY KEY NOT NULL, "
                +GraContract.Kolumny.GRA_NAZWA+ " TEXT, "
                +GraContract.Kolumny.GRA_ZDJECIE+ " TEXT);";
        Log.d(TAG, sSQL);
        db.execSQL(sSQL);

        Log.d(TAG, "onCreate: koniec");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: start");

        switch(oldVersion) {
            case 1:
                // ulepszenie z wersji 1
                break;
            default:
                throw new IllegalStateException("onUpgrade() z nieznaną newVersion: " +newVersion);
        }
        Log.d(TAG, "onUpgrade: koniec");
    }
}
