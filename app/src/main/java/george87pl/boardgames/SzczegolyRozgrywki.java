package george87pl.boardgames;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class SzczegolyRozgrywki extends AppCompatActivity {
    private static final String TAG = "SzczegolyRozgrywki";

    TextView nazwaGry;
    ImageView zdjecieGry;
    TextView dataRozgrywki;
    TextView opisRozgrywki;
    ImageView zdjecieRozgrywki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_szczegoly_rozgrywki);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nazwaGry = findViewById(R.id.nazwa_gry);
        zdjecieGry = findViewById(R.id.zdjecie_gry);
        dataRozgrywki = findViewById(R.id.data);
        opisRozgrywki = findViewById(R.id.opis);
        zdjecieRozgrywki = findViewById(R.id.zdjecie_rozgrywka);

        Intent intentRozgrywka = getIntent();
        Rozgrywka rozgrywka = (Rozgrywka) intentRozgrywka.getSerializableExtra("rozgrywka");

        Log.d(TAG, "ROZGRYWKA SZCZEGOLY: " +rozgrywka.getData());

        nazwaGry.setText(rozgrywka.getNazwaGry());
        if(rozgrywka.getZdjecieGry() != null) {
            String mCurrentPhotoPath = rozgrywka.getZdjecieGry();
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            zdjecieGry.setImageURI(contentUri);
        }

        dataRozgrywki.setText(rozgrywka.getData());
        opisRozgrywki.setText(rozgrywka.getOpis());

        if(rozgrywka.getZdjecieRozgrywki() != null) {
            String mCurrentPhotoPath = rozgrywka.getZdjecieRozgrywki();
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            zdjecieRozgrywki.setImageURI(contentUri);
        }

    }

}
