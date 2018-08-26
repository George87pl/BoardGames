package george87pl.boardgames;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DodajRozgrywke extends AppCompatActivity {
    private static final String TAG = "DodajRozgrywke";

    private EditText mOpisGry;
    ImageView rozgrywkaZdjeciePokaz;
    private String mCurrentPhotoPath;
    private Uri imageUri;
    static final int REQUEST_TAKE_PHOTO = 1;


    public DodajRozgrywke() {
        Log.d(TAG, "DodajRozgrywke: start");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_rozgrywke);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOpisGry = findViewById(R.id.graOpisDodaj);
        ImageView mRozgrywkaZdjecieZrob = findViewById(R.id.rozgrywkaZdjecieZrob);

        if (savedInstanceState != null) {

            mCurrentPhotoPath = savedInstanceState.getString("photo_path");

            if(mCurrentPhotoPath != null){
                File f = new File(mCurrentPhotoPath);
                imageUri = Uri.fromFile(f);
                rozgrywkaZdjeciePokaz = findViewById(R.id.rozgrywkaZdjeciePokaz);
                rozgrywkaZdjeciePokaz.setImageURI(imageUri);
                rozgrywkaZdjeciePokaz.setVisibility(View.VISIBLE);
            }


        }

        View.OnClickListener kiedyCosKlikniete = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.rozgrywkaZdjecieZrob:

                        dispatchTakePictureIntent();

                        break;

                }
            }
        };

        mRozgrywkaZdjecieZrob.setOnClickListener(kiedyCosKlikniete);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.graZapisz) {

            ContentResolver contentResolver = getContentResolver();
            ContentValues values = new ContentValues();

            Intent intentGra = getIntent();
            Gra gra = (Gra) intentGra.getSerializableExtra("Gra"); // Dostaje tu teraz kompletny obiekt, wybrany z kolekcji gier

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("d.MM.yyyy HH:mm");

            values.put(RozgrywkaContract.Kolumny.ROZGRYWKA_GRA, gra.getidGry());
            values.put(RozgrywkaContract.Kolumny.ROZGRYWKA_DATA, format.format(calendar.getTime()));
            values.put(RozgrywkaContract.Kolumny.ROZGRYWKA_OPIS, mOpisGry.getText().toString());
            if(mCurrentPhotoPath != null) {
                values.put(RozgrywkaContract.Kolumny.ROZGRYWKA_ZDJECIE, mCurrentPhotoPath);
            }
            contentResolver.insert(RozgrywkaContract.CONTENT_URI, values);


            Intent intent = new Intent(DodajRozgrywke.this, MainActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri image = FileProvider.getUriForFile(this,
                        "george87pl.boardgames.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, image);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            File f = new File(mCurrentPhotoPath);
            imageUri = Uri.fromFile(f);
            rozgrywkaZdjeciePokaz = findViewById(R.id.rozgrywkaZdjeciePokaz);
            rozgrywkaZdjeciePokaz.setImageURI(imageUri);
            rozgrywkaZdjeciePokaz.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("photo_path", mCurrentPhotoPath);
        savedInstanceState.putParcelable("imageUri", imageUri);
    }
}
