package george87pl.boardgames;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static george87pl.boardgames.AppProvider.CONTENT_AUTHORITY;
import static george87pl.boardgames.AppProvider.CONTENT_AUTHORITY_URI;

public class WidokContract {

    static final String TABELA_NAZWA = "vwRozgrywkiCalosc";

    //pola widoku
    public static class Kolumny {
        public static final String _ID = BaseColumns._ID;
        public static final String WIDOK_GRA = GraContract.Kolumny.GRA_NAZWA;
        public static final String WIDOK_GRA_ZDJECIE = GraContract.Kolumny.GRA_ZDJECIE;
        public static final String WIDOK_DATA = RozgrywkaContract.Kolumny.ROZGRYWKA_DATA;
        public static final String WIDOK_OPIS = RozgrywkaContract.Kolumny.ROZGRYWKA_OPIS;
        public static final String WIDOK_ROZGRYWKA_ZDJECIE = "ZdjecieRozgrywki";

        private Kolumny() {
            //prywatny konstruktor aby zapobiec stworzeniu instancji
        }
    }

    /**
     * URI do widoku
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABELA_NAZWA);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vdn." + CONTENT_AUTHORITY + "." + TABELA_NAZWA;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vdn." + CONTENT_AUTHORITY + "." + TABELA_NAZWA;

    static long pobierzWidokId(Uri uri) {
        return ContentUris.parseId(uri);
    }

}
