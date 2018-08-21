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
import android.widget.ImageView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DodajGre extends AppCompatActivity {
    private static final String TAG = "DodajGre";

    private EditText gNazwaGry;
    private ImageView gGraZdjecieZrob;
    private String mCurrentPhotoPath;
    private boolean czyByloZdjecie = false;
//    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGE = 1;
    Bitmap imageBitmap;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_gre);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gNazwaGry = findViewById(R.id.dodajNazweGry);
        gGraZdjecieZrob = findViewById(R.id.graZdjecieZrob);

//        if (savedInstanceState!=null)
//        {
//            imageBitmap=savedInstanceState.getParcelable("image");
//            gGraZdjecieZrob.setImageBitmap(imageBitmap);
//        }

        gGraZdjecieZrob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//
//                    czyByloZdjecie = true;
//                }

                Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_IMAGE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            imageBitmap = (Bitmap) extras.get("data");
//            gGraZdjecieZrob.setImageBitmap(imageBitmap);
//        }

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            Log.d(TAG, "onActivityResult: IMAGE URI!!!!!!!!! " +imageUri);
            gGraZdjecieZrob.setImageURI(imageUri);
            czyByloZdjecie = true;
        }
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

            if (czyByloZdjecie) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                try {
                    File image = File.createTempFile(
                            imageFileName,  /* prefix */
                            ".jpg",         /* suffix */
                            storageDir      /* directory */);

                    mCurrentPhotoPath = image.getAbsolutePath();
                } catch (IOException e) {
                    Log.d(TAG, "onOptionsItemSelected: IOException " + e);
                }


                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(mCurrentPhotoPath);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 120, 96, false);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



                values.put(GraContract.Kolumny.GRA_ZDJECIE, mCurrentPhotoPath);
            }

            values.put(GraContract.Kolumny.GRA_NAZWA, gNazwaGry.getText().toString());
            contentResolver.insert(GraContract.CONTENT_URI, values);

            Intent intent = new Intent(DodajGre.this, KolekcjaGier.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("image", imageBitmap);
    }
}

