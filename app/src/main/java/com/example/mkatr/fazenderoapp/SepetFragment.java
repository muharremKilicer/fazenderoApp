package com.example.mkatr.fazenderoapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;

import items.Siparisler;
import itemsAdapter.SiparislerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class SepetFragment extends Fragment {

    SharedPreferences sha;
    SharedPreferences.Editor edit;
    Button btnCikis;
    TextView txtSepet;
    ListView siparislerListe;
    ArrayList<Siparisler> siparislerArray = new ArrayList<>();
    static String tiklananSiparisId;

    public SepetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sepet, container, false);
        btnCikis = (Button) view.findViewById(R.id.btnCikis);
        txtSepet = (TextView) view.findViewById(R.id.txtSepetUrun);
        siparislerListe = (ListView) view.findViewById(R.id.siparislerListe);

        sha = this.getActivity().getSharedPreferences("kul", Context.MODE_PRIVATE);
        edit = sha.edit();

        txtSepet.setVisibility(View.INVISIBLE);

        btnCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.clear();
                edit.commit();

                //Kullanıcı çıkışı başarılı şekilde yaptı.
                MainActivity.navigationView.getMenu().findItem(R.id.nav_giris).setTitle("ÜYE OL - GİRİŞ YAP");

                //Temizlendi. Tekrar giriş sayfasına..
                GirisFragment fragment = new GirisFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        //Telefondan geri butonuna bastığı zaman çıksın
                        //.addToBackStack(null)
                        .commit();
            }
        });

        String url = "http://jsonbulut.com/json/orderList.php?ref=37c663e00c3dfb1eeb0c95ad9375a6a6&musterilerID=" + sha.getString("kullaniciId", "");
        new jsonOku(url, this.getActivity()).execute();

        return view;
    }

    class jsonOku extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pr;

        String url = "";
        String data = "";

        public jsonOku(String url, Activity ac) {
            this.url = url;
            pr = new ProgressDialog(ac);
            pr.setMessage("Yükleniyor, Lütfen bekleyiniz...");
            pr.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute(); // bekleme durumu
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                data = Jsoup.connect(url).ignoreContentType(true).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                pr.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);// biten nokta
            try {
                JSONObject obj = new JSONObject(data);
                JSONArray order = obj.getJSONArray("orderList");
                JSONArray orderS = order.getJSONArray(0);
                for (int i = 0; i < orderS.length(); i++) {
                    JSONObject bil = orderS.getJSONObject(i);
                    if (!bil.isNull("urun_adi")) {
                        String urunSec = bil.getString("siparis_bilgisi");
                        String urunId = bil.getString("id_category");
                        String[] tokens = urunId.split(",");
                        String cat = "";
                        for (String t : tokens) {
                            cat = t;
                        }
                        String urunAdi = bil.getString("urun_adi");
                        String fiyat = bil.getString("fiyat");
                        String resimAdi = bil.getString("adi");
                        String klasor = bil.getString("klasor");
                        Siparisler siparisler = new Siparisler(cat, urunAdi, fiyat, resimAdi, klasor, urunSec);
                        siparislerArray.add(siparisler);
                    }
                }

                SiparislerAdapter adp = new SiparislerAdapter(getActivity(), siparislerArray);
                siparislerListe.setAdapter(adp);

                siparislerListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            //Tıklanan'a ait olan bilgileri daha önce set edilmiş sınıftan get edilerek alındı
                            Siparisler ur = siparislerArray.get(position);
                            tiklananSiparisId = ur.getUrunSec();

                            //Ayrinti fragment'a geçiş
                            AyrintiFragment fragment = new AyrintiFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "ID yakalama hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
