package george87pl.boardgames;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static george87pl.boardgames.AppProvider.CONTENT_AUTHORITY;
import static george87pl.boardgames.AppProvider.CONTENT_AUTHORITY_URI;

public class GraContract {

    static final String TABELA_NAZWA = "Gry";

    //pola tablicy "Gry"
    public static class Kolumny {
        public static final String GRA_ID = BaseColumns._ID;
        public static final String GRA_NAZWA = "Nazwa";
        public static final String GRA_ZDJECIE = "Zdjecie";

        private Kolumny() {
            //prywatny konstruktor aby zapobiec stworzeniu instancji
        }
    }

    /**
     * URI do tabeli Gry
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABELA_NAZWA);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vdn." + CONTENT_AUTHORITY + "." + TABELA_NAZWA;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vdn." + CONTENT_AUTHORITY + "." + TABELA_NAZWA;

    static Uri zbudujGraId(long gryId) {
        return ContentUris.withAppendedId(CONTENT_URI, gryId);
    }

    static long pobierzGraId(Uri uri) {
        return ContentUris.parseId(uri);
    }

}
