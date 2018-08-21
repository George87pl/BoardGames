package george87pl.boardgames;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerItemClickListener.OnRecyclerClickListener, AppDialog.DialogEvents{
    private static final String TAG = "MainActivity";

    private RozgrywkaRecyclerViewAdapter mRozgrywkaRecyclerViewAdapter;
    private List<Rozgrywka> listaRozgrywek;
    public static final int DELETE_DIALOG_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaRozgrywek = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.lista_rozgrywek);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DodajRozgrywke.class);
                startActivity(intent);
            }
        });


        String[] projection = {RozgrywkaContract.Kolumny.ROZGRYWKA_NAZWA,
                                RozgrywkaContract.Kolumny.ROZGRYWKA_OPIS,
                                RozgrywkaContract.Kolumny.ROZGRYWKA_ZDJECIE,
                                RozgrywkaContract.Kolumny.ROZGRYWKA_DATA};

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(RozgrywkaContract.CONTENT_URI,
                projection,
                null,
                null,
                null);


        String nazwa;
        String opis;
        String data;
        String zdjecie;

        if (cursor != null) {
            while (cursor.moveToNext()) {

                nazwa = cursor.getString(cursor.getColumnIndex(RozgrywkaContract.Kolumny.ROZGRYWKA_NAZWA));
                opis = cursor.getString(cursor.getColumnIndex(RozgrywkaContract.Kolumny.ROZGRYWKA_OPIS));
                data = cursor.getString(cursor.getColumnIndex(RozgrywkaContract.Kolumny.ROZGRYWKA_DATA));
                zdjecie = cursor.getString(cursor.getColumnIndex(RozgrywkaContract.Kolumny.ROZGRYWKA_ZDJECIE));

                listaRozgrywek.add(new Rozgrywka(null, nazwa, data, opis, zdjecie));

            }
        }

        mRozgrywkaRecyclerViewAdapter = new RozgrywkaRecyclerViewAdapter(this, listaRozgrywek);
        recyclerView.setAdapter(mRozgrywkaRecyclerViewAdapter);

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

        switch(item.getItemId()) {
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
    public void onItemClick(View view, int pozycja) {
        Log.d(TAG, "onItemClick: start");
        Toast.makeText(MainActivity.this, "Normal tap on position " +pozycja , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongItemClick(View view, int pozycja) {
        Log.d(TAG, "onLongItemClick: start");

        String nazwaGry = listaRozgrywek.get(pozycja).getNazwaGry();

        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, DELETE_DIALOG_ID);
        args.putString(AppDialog.DIALOG_MESSAGE, "Usunąć " +nazwaGry+ "?");
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.deldiag_positive_caption);

        String selection = RozgrywkaContract.Kolumny.ROZGRYWKA_NAZWA + " = \"" +nazwaGry +"\"";
        args.putString("selection", selection);

        dialog.setArguments(args);
        dialog.show(getFragmentManager(), null);

    }

    @Override
    public void onPositiveDialogResoult(int dialogId, Bundle args) {
        Log.d(TAG, "onPositiveDialogResoult: ");

        String selection = args.getString("selection");
        if(BuildConfig.DEBUG && selection == null) throw new AssertionError("selection jest puste");

        ContentResolver contentResolver = getContentResolver();
        contentResolver.delete(RozgrywkaContract.CONTENT_URI, selection, null);

        Log.d(TAG, "onLongItemClick: ");

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onNegativeDialogResoult(int dialogId, Bundle args) {
        Log.d(TAG, "onNegativeDialogResoult: ");
    }

    @Override
    public void onDialogCancelled(int dialogId) {
        Log.d(TAG, "onDialogCancelled: ");
    }
}
