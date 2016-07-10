package items;

/**
 * Created by mkatr on 9.7.2016.
 */
public class Siparisler {

    String urunId, urun_adi, fiyat, resAdi, resKlasör, urunSec;

    public Siparisler(String urunId, String urun_adi, String fiyat, String resAdi, String resKlasör, String urunSec) {
        this.urunId = urunId;
        this.urun_adi = urun_adi;
        this.fiyat = fiyat;
        this.resAdi = resAdi;
        this.resKlasör = resKlasör;
        this.urunSec = urunSec;
    }

    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }

    public String getUrunSec() {
        return urunSec;
    }

    public void setUrunSec(String urunSec) {
        this.urunSec = urunSec;
    }

    public String getUrunId() {
        return urunId;
    }

    public void setUrunId(String urunId) {
        this.urunId = urunId;
    }

    public String getUrun_adi() {
        return urun_adi;
    }

    public void setUrun_adi(String urun_adi) {
        this.urun_adi = urun_adi;
    }

    public String getResAdi() {
        return resAdi;
    }

    public void setResAdi(String resAdi) {
        this.resAdi = resAdi;
    }

    public String getResKlasör() {
        return resKlasör;
    }

    public void setResKlasör(String resKlasör) {
        this.resKlasör = resKlasör;
    }
}
