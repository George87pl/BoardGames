package george87pl.boardgames;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

class RozgrywkaRecyclerViewAdapter extends RecyclerView.Adapter<RozgrywkaRecyclerViewAdapter.RozgrywkaWierszViewHolder> {
    private static final String TAG = "RozgrywkaRecyclerViewAd";

    private List<Rozgrywka> mRozgrywkaList;
    private Context mContext;

    public RozgrywkaRecyclerViewAdapter(Context mContext, List<Rozgrywka> mRozgrywkaList) {
        this.mRozgrywkaList = mRozgrywkaList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RozgrywkaWierszViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Called by the layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: ");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wyswietl_wiersz, parent, false);

        return new RozgrywkaWierszViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RozgrywkaWierszViewHolder holder, int position) {
        // Call by the layout manager when it wants new data in an existing row

        Rozgrywka rozgrywkaItem = mRozgrywkaList.get(position);

//        if(rozgrywkaItem.getZdjecieRozgrywki() != null) {
//            String mCurrentPhotoPath = rozgrywkaItem.getZdjecieRozgrywki();
//            File f = new File(mCurrentPhotoPath);
//            Uri contentUri = Uri.fromFile(f);
//            holder.zdjecie.setImageURI(contentUri);
//        }

        holder.nazwa.setText(rozgrywkaItem.getNazwaGry());
        holder.opis.setText(rozgrywkaItem.getOpis());
        holder.data.setText(rozgrywkaItem.getData());
    }

    @Override
    public int getItemCount() {
        return ((mRozgrywkaList != null) && (mRozgrywkaList.size() !=0) ? mRozgrywkaList.size() : 0);
    }


    public Rozgrywka getRozgrywka(int pozycja) {
        return ((mRozgrywkaList != null) && (mRozgrywkaList.size() !=0) ? mRozgrywkaList.get(pozycja) : null);
    }

    static class RozgrywkaWierszViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "RozgrywkaWierszViewHold";

        ImageView zdjecie;
        TextView nazwa;
        TextView opis;
        TextView data;

        public RozgrywkaWierszViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "RozgrywkaWierszViewHolder: ");


//            this.zdjecie = itemView.findViewById(R.id.zdjecie_gry);
            this.nazwa = itemView.findViewById(R.id.tytul_gry);
            this.opis = itemView.findViewById(R.id.opis_gry);
            this.data = itemView.findViewById(R.id.data_gry);


        }
    }

}
