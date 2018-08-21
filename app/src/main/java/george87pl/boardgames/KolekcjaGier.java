package george87pl.boardgames;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class KolekcjaGier extends AppCompatActivity implements AppDialog.DialogEvents{
    private static final String TAG = "KolekcjaGier";

    private GridView gridView;
    private ArrayList<Gra> listaGier;
    KolekcjaGierAdapter kolekcjaGierAdapter;
    public static final int DELETE_DIALOG_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kolekcja_gier);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KolekcjaGier.this, DodajGre.class);
                startActivity(intent);
            }
        });


        listaGier = new ArrayList<>();
        gridView = findViewById(R.id.lista_kolekcja_gier);

        String[] projection = {GraContract.Kolumny.GRA_ID, GraContract.Kolumny.GRA_NAZWA, GraContract.Kolumny.GRA_ZDJECIE};

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(GraContract.CONTENT_URI,
                projection,
                null,
                null,
                GraContract.Kolumny.GRA_NAZWA + " COLLATE NOCASE");


        int id;
        String nazwa;
        String zdjecie;

        if (cursor != null) {
            while (cursor.moveToNext()) {

                id = cursor.getInt(cursor.getColumnIndex(GraContract.Kolumny.GRA_ID));
                nazwa = cursor.getString(cursor.getColumnIndex(GraContract.Kolumny.GRA_NAZWA));
                zdjecie = cursor.getString(cursor.getColumnIndex(GraContract.Kolumny.GRA_ZDJECIE));

                listaGier.add(new Gra(id, zdjecie, nazwa));
            }
        }

        kolekcjaGierAdapter = new KolekcjaGierAdapter(this, listaGier);
        gridView.setAdapter(kolekcjaGierAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(KolekcjaGier.this, DodajRozgrywke.class);
                intent.putExtra("Gra", listaGier.get(position).getidGry());
                startActivity(intent);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                int idGry = listaGier.get(position).getidGry();
                Log.d(TAG, "onItemLongClick: ID DO USUNIECIA TO: " +idGry);
                String nazwaGry = listaGier.get(position).getNazwaGry();

                AppDialog dialog = new AppDialog();
                Bundle args = new Bundle();
                args.putInt(AppDialog.DIALOG_ID, DELETE_DIALOG_ID);
                args.putString(AppDialog.DIALOG_MESSAGE, "Usunąć " +nazwaGry+ "?");
                args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.deldiag_positive_caption);

                String selection = GraContract.Kolumny.GRA_ID + " = " +idGry;
                args.putString("selection", selection);

                dialog.setArguments(args);
                dialog.show(getFragmentManager(), null);

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {
            case R.id.action_rozgrywki:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.action_gry:
                Intent intent2 = new Intent(this, KolekcjaGier.class);
                startActivity(intent2);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPositiveDialogResoult(int dialogId, Bundle args) {

        String selection = args.getString("selection");
        if(BuildConfig.DEBUG && selection == null) throw new AssertionError("selection jest puste");

        ContentResolver contentResolver = getContentResolver();
        contentResolver.delete(GraContract.CONTENT_URI, selection, null);


        Intent intent = new Intent(KolekcjaGier.this, KolekcjaGier.class);
        startActivity(intent);

    }

    @Override
    public void onNegativeDialogResoult(int dialogId, Bundle args) {

    }

    @Override
    public void onDialogCancelled(int dialogId) {

    }
}
