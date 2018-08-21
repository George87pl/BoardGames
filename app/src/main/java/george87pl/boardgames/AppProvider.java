package george87pl.boardgames;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Provider dla aplikacji PlayMoreGames. Jako jedyny wie o {@link BazaDanych}
 */

public class AppProvider extends ContentProvider{
    private static final String TAG = "AppProvider";

    private BazaDanych obiektBaza;

    private static final UriMatcher sUriMatcher = zbudujUriMatcher();

    static final String CONTENT_AUTHORITY = "george87pl.boardgames.provider";
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" +CONTENT_AUTHORITY);

    private static final int ROZGRYWKA = 100;
    private static final int ROZGRYWKA_ID = 101;

    private static final int GRA = 200;
    private static final int GRA_ID = 201;

    private static UriMatcher zbudujUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        //content://akademia.george87pl.playmoregames.provider/ROZGRYWKI
        matcher.addURI(CONTENT_AUTHORITY, RozgrywkaContract.TABELA_NAZWA, ROZGRYWKA);
        matcher.addURI(CONTENT_AUTHORITY, GraContract.TABELA_NAZWA, GRA);

        //np. content://akademia.george87pl.playmoregames.provider/ROZGRYWKI/6
        matcher.addURI(CONTENT_AUTHORITY, RozgrywkaContract.TABELA_NAZWA + "/#", ROZGRYWKA_ID);
        matcher.addURI(CONTENT_AUTHORITY, GraContract.TABELA_NAZWA + "/#", GRA_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        obiektBaza = BazaDanych.stworzInstancje(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query: URI " +uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "query: match " +match);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (match) {
            case ROZGRYWKA:
                queryBuilder.setTables(RozgrywkaContract.TABELA_NAZWA);
                break;
            case ROZGRYWKA_ID:
                queryBuilder.setTables(RozgrywkaContract.TABELA_NAZWA);
                long rozId = RozgrywkaContract.pobierzRozgrywkaId(uri);
                queryBuilder.appendWhere(RozgrywkaContract.Kolumny.ROZGRYWKA_ID + " = " + rozId);
                break;

            case GRA:
                queryBuilder.setTables(GraContract.TABELA_NAZWA);
                break;
            case GRA_ID:
                queryBuilder.setTables(GraContract.TABELA_NAZWA);
                long graId = GraContract.pobierzGraId(uri);
                queryBuilder.appendWhere(GraContract.Kolumny.GRA_ID + " = " + graId);
                break;

            default:
                throw new IllegalArgumentException("Nieznane URI" +uri);
        }

        SQLiteDatabase db = obiektBaza.getReadableDatabase();
//        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        Log.d(TAG, "query: zwraca cursor " +cursor.getCount());

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ROZGRYWKA:
                return RozgrywkaContract.CONTENT_TYPE;

            case ROZGRYWKA_ID:
                return RozgrywkaContract.CONTENT_ITEM_TYPE;

            case GRA:
                return GraContract.CONTENT_TYPE;

            case GRA_ID:
                return GraContract.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Nieznane URI" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "insert: wywołany z uri: " +uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match: " +match);

        final SQLiteDatabase db;

        Uri zwrocUri = null;
        long rekordId = 0;

        switch(match) {
            case ROZGRYWKA:
                db = obiektBaza.getWritableDatabase();
                rekordId = db.insert(RozgrywkaContract.TABELA_NAZWA, null, values);
                if (rekordId >=0) {
                    zwrocUri = RozgrywkaContract.zbudujRozgrywkaId(rekordId);
                } else {
                    throw new android.database.SQLException("Błąd podczas dodawania do bazy " +uri);
                }
                break;

            case ROZGRYWKA_ID:
                db = obiektBaza.getWritableDatabase();
                break;

            case GRA:
                db = obiektBaza.getWritableDatabase();
                rekordId = db.insert(GraContract.TABELA_NAZWA, null, values);
                if (rekordId >=0) {
                    zwrocUri = GraContract.zbudujGraId(rekordId);
                } else {
                    throw new android.database.SQLException("Błąd podczas dodawania do bazy " +uri);
                }
                break;

            case GRA_ID:
                db = obiektBaza.getWritableDatabase();
                break;

            default:
                throw new IllegalArgumentException("Nieznane uri: " +uri);
        }

        if(rekordId >= 0) {
            //cos zostalo dodane
            Log.d(TAG, "insert: notyfikacja sie zmienila " +uri);
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            Log.d(TAG, "insert: nic nie zostalo dodane");
        }
        Log.d(TAG, "Istniejący insert: " +zwrocUri);
        return zwrocUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "delete wywołany przez uri: " +uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match: " +match);

        final SQLiteDatabase db;
        int licznik;

        String wybraneKryteria;

        switch(match) {
            case ROZGRYWKA:
                db = obiektBaza.getWritableDatabase();
                licznik = db.delete(RozgrywkaContract.TABELA_NAZWA, selection, selectionArgs);
                break;

            case ROZGRYWKA_ID:
                db = obiektBaza.getWritableDatabase();
                long rozgrywkaId = RozgrywkaContract.pobierzRozgrywkaId(uri);
                wybraneKryteria = RozgrywkaContract.Kolumny.ROZGRYWKA_ID + " = " + rozgrywkaId;

                if((selection != null) && (selection.length()>0)){
                    wybraneKryteria += " AND (" + selection + ")";
                }
                Log.d(TAG, "delete: " +wybraneKryteria);
                licznik = db.delete(RozgrywkaContract.TABELA_NAZWA,  wybraneKryteria, selectionArgs);
                break;

            case GRA:
                db = obiektBaza.getWritableDatabase();
                licznik = db.delete(GraContract.TABELA_NAZWA, selection, selectionArgs);
                break;

            case GRA_ID:
                db = obiektBaza.getWritableDatabase();
                long graId = GraContract.pobierzGraId(uri);
                wybraneKryteria = GraContract.Kolumny.GRA_ID + " = " + graId;

                if((selection != null) && (selection.length()>0)){
                    wybraneKryteria += " AND (" + selection + ")";
                }
                Log.d(TAG, "delete: " +wybraneKryteria);
                licznik = db.delete(GraContract.TABELA_NAZWA,  wybraneKryteria, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Nieznane uri: " +uri);
        }

        if(licznik > 0) {
            //cos zostalo usuniete
            Log.d(TAG, "delete: ustawianie notyfikacji");
            getContext().getContentResolver().notifyChange(uri, null);
        } else{
            Log.d(TAG, "delete: nic nie skasowane");
        }
        Log.d(TAG, "Istniejący delete zwraca: " +licznik);
        return licznik;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "update wywołany przez uri: " +uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match: " +match);

        final SQLiteDatabase db;
        int licznik;

        String wybraneKryteria;

        switch(match) {
            case ROZGRYWKA:
                db = obiektBaza.getWritableDatabase();
                licznik = db.update(RozgrywkaContract.TABELA_NAZWA, values, selection, selectionArgs);
                break;

            case ROZGRYWKA_ID:
                db = obiektBaza.getWritableDatabase();
                long rozgrywkaId = RozgrywkaContract.pobierzRozgrywkaId(uri);
                wybraneKryteria = RozgrywkaContract.Kolumny.ROZGRYWKA_ID + " = " + rozgrywkaId;

                if((selection != null) && (selection.length()>0)){
                    wybraneKryteria += " AND (" + selection + ")";
                    Log.d(TAG, "wybraneKryteria: " +wybraneKryteria);
                }
                licznik = db.update(RozgrywkaContract.TABELA_NAZWA, values, wybraneKryteria, selectionArgs);
                break;

            case GRA:
                db = obiektBaza.getWritableDatabase();
                licznik = db.update(GraContract.TABELA_NAZWA, values, selection, selectionArgs);
                break;

            case GRA_ID:
                db = obiektBaza.getWritableDatabase();
                long graId = GraContract.pobierzGraId(uri);
                wybraneKryteria = GraContract.Kolumny.GRA_ID + " = " + graId;

                if((selection != null) && (selection.length()>0)){
                    wybraneKryteria += " AND (" + selection + ")";
                    Log.d(TAG, "wybraneKryteria: " +wybraneKryteria);
                }
                licznik = db.update(GraContract.TABELA_NAZWA, values, wybraneKryteria, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Nieznane uri: " +uri);
        }

        if(licznik > 0) {
            //cos zostalo usuniete
            Log.d(TAG, "update: ustawianie notyfikacji");
            getContext().getContentResolver().notifyChange(uri, null);
        } else{
            Log.d(TAG, "update: nic nie skasowane");
        }

        Log.d(TAG, "Istniejący update zwraca: " +licznik);
        return licznik;
    }
}
