package me.prototyp.database;

public class Towar {
    private int id;
    private String nazwa;
    private int vat;
    private String jm;
    private double netto;
    private double brutto;
    private int grupa;
    private String barcode;

    public Towar(int id, String nazwa, int vat, String jm, double netto, double brutto, int grupa, String barcode) {

        this.id = id;
        this.nazwa = nazwa;
        this.vat = vat;
        this.jm = jm;
        this.netto = netto;
        this.brutto = brutto;
        this.barcode = barcode;

    }

    public String toStringTowar(){
        return this.nazwa+", "+this.netto+", "+this.barcode;
    }

    public int getId() {
        return this.id;
    }

    public String getNazwa(){
        return this.nazwa;
    }

    public int getVat(){
        return this.vat;
    }

    public String getJm(){
        return this.jm;
    }

    public double getNetto(){
        return this.netto;
    }

    public double getBrutto(){
        return this.brutto;
    }

    public int getGrupa(){
        return this.grupa;
    }

    public String getBarcode(){
        return this.barcode;
    }
}
