package george87pl.boardgames;

import android.net.Uri;

class Rozgrywka {

    private String mZdjecieGry;
    private String mNazwaGry;
    private String mData;
    private String mOpis;
    private String mZdjecieRozgrywki;

    public Rozgrywka(String mZdjecieGry, String mNazwaGry, String mData, String mOpis, String mZdjecieRozgrywki) {
        this.mZdjecieGry = mZdjecieGry;
        this.mNazwaGry = mNazwaGry;
        this.mData = mData;
        this.mOpis = mOpis;
        this.mZdjecieRozgrywki = mZdjecieRozgrywki;
    }

    public String getZdjecieGry() {
        return mZdjecieGry;
    }

    public String getNazwaGry() {
        return mNazwaGry;
    }

    public String getData() {
        return mData;
    }

    public String getOpis() {
        return mOpis;
    }

    public String getZdjecieRozgrywki() {
        return mZdjecieRozgrywki;
    }

    @Override
    public String toString() {
        return "Rozgrywka{" +
                "mZdjecieGry='" + mZdjecieGry + '\'' +
                ", mNazwaGry='" + mNazwaGry + '\'' +
                ", mData='" + mData + '\'' +
                ", mOpis='" + mOpis + '\'' +
                ", mZdjecieRozgrywki='" + mZdjecieRozgrywki + '\'' +
                '}';
    }
}