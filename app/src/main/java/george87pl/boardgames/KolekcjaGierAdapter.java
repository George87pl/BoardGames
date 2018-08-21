package george87pl.boardgames;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class KolekcjaGierAdapter extends BaseAdapter {
    private static final String TAG = "KolekcjaGierAdapter";

    private Context context;
    private ArrayList<Gra> listaGier;
    private static LayoutInflater inflater = null;

    public KolekcjaGierAdapter(Context context, ArrayList<Gra> listaGier) {
        Log.d(TAG, "KolekcjaGierAdapter: start");
        this.context = context;
        this.listaGier = listaGier;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listaGier.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView: start");

        if (convertView == null)
            convertView = inflater.inflate(R.layout.wyswietl_wiersz_gry, null);

        ImageView zdjecieGry = convertView.findViewById(R.id.gZdjecieGry);
        TextView nazwaGry = convertView.findViewById(R.id.gNazwaGry);

        if(listaGier.get(position).getZdjecieGry() != null) {
            String mCurrentPhotoPath = listaGier.get(position).getZdjecieGry();
            Log.d(TAG, "getView URI: " +mCurrentPhotoPath);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            zdjecieGry.setImageURI(contentUri);
        }
        nazwaGry.setText(listaGier.get(position).getNazwaGry());


        return convertView;

    }


}
