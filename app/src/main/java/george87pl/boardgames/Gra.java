package george87pl.boardgames;

class Gra {

    private int idGry;
    private String gZdjecieGry;
    private String gNazwaGry;

    public Gra(int gIdGry, String gZdjecieGry, String gNazwaGry) {
        this.idGry = gIdGry;
        this.gZdjecieGry = gZdjecieGry;
        this.gNazwaGry = gNazwaGry;
    }

    public int getidGry() {
        return idGry;
    }

    public String getZdjecieGry() {
        return gZdjecieGry;
    }

    public String getNazwaGry() {
        return gNazwaGry;
    }

    @Override
    public String toString() {
        return "Gra{" +
                "gZdjecieGry='" + gZdjecieGry + '\'' +
                ", gNazwaGry='" + gNazwaGry + '\'' +
                '}';
    }
}