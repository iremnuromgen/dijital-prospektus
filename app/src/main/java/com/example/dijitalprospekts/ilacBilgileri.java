package com.example.dijitalprospekts;

public class ilacBilgileri {
    private String uyari;
    private String nedir;
    private String neIcinKullanilir;
    private String dikkatEdilmesiGerekenler;

    private String nasilKullanilir;
    private String yanEtkileri;
    private String saklanmasi;

    public ilacBilgileri(String uyari, String nedir, String neIcinKullanilir, String dikkatEdilmesiGerekenler, String nasilKullanilir, String yanEtkileri, String saklanmasi) {
        this.uyari = uyari;
        this.nedir = nedir;
        this.neIcinKullanilir = neIcinKullanilir;
        this.dikkatEdilmesiGerekenler = dikkatEdilmesiGerekenler;
        this.nasilKullanilir = nasilKullanilir;
        this.yanEtkileri = yanEtkileri;
        this.saklanmasi = saklanmasi;
    }

    public String getUyari() {
        return uyari;
    }

    public String getNedir() {
        return nedir;
    }

    public String getNeIcinKullanilir() {
        return neIcinKullanilir;
    }

    public String getDikkatEdilmesiGerekenler() {
        return dikkatEdilmesiGerekenler;
    }

    public String getNasilKullanilir() {
        return nasilKullanilir;
    }

    public String getYanEtkileri() {
        return yanEtkileri;
    }

    public String getSaklanmasi() {
        return saklanmasi;
    }
}
