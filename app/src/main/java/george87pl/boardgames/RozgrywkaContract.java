package george87pl.boardgames;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static george87pl.boardgames.AppProvider.CONTENT_AUTHORITY;
import static george87pl.boardgames.AppProvider.CONTENT_AUTHORITY_URI;

public class RozgrywkaContract {

    static final String TABELA_NAZWA = "Rozgrywki";

    //pola tablicy "Rozgrywki"
    public static class Kolumny {
        public static final String ROZGRYWKA_ID = BaseColumns._ID;
        public static final String ROZGRYWKA_GRA = "KolekcjaGier";
        public static final String ROZGRYWKA_NAZWA = "Nazwa";
        public static final String ROZGRYWKA_DATA = "Data";
        public static final String ROZGRYWKA_ZDJECIE = "Zdjecie";
        public static final String ROZGRYWKA_OPIS = "Opis";

        private Kolumny() {
            //prywatny konstruktor aby zapobiec stworzeniu instancji
        }
    }

    /**
     * URI do tabeli Rzgrywki
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABELA_NAZWA);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vdn." + CONTENT_AUTHORITY + "." + TABELA_NAZWA;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vdn." + CONTENT_AUTHORITY + "." + TABELA_NAZWA;

    static Uri zbudujRozgrywkaId(long rozgrywkaId) {
        return ContentUris.withAppendedId(CONTENT_URI, rozgrywkaId);
    }

    static long pobierzRozgrywkaId(Uri uri) {
        return ContentUris.parseId(uri);
    }
}
