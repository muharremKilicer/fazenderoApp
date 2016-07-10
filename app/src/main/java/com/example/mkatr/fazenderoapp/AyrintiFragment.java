package com.example.mkatr.fazenderoapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class AyrintiFragment extends Fragment {

    SharedPreferences sha;
    SharedPreferences.Editor edit;
    TextView detayBaslik, detayFiyat;
    ImageView detayResim;
    Button btnSiparis;
    String siparisId;

    public AyrintiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ayrinti, container, false);
        detayBaslik = (TextView) view.findViewById(R.id.txtUrunDetayBaslik);
        detayFiyat = (TextView) view.findViewById(R.id.txtUrunDetayFiyat);
        detayResim = (ImageView) view.findViewById(R.id.urunDetayResim);
        btnSiparis = (Button) view.findViewById(R.id.btnSiparisVer);
        sha = this.getActivity().getSharedPreferences("kul", Context.MODE_PRIVATE);
        edit = sha.edit();


        String url2 = "http://jsonbulut.com/json/product.php?ref=37c663e00c3dfb1eeb0c95ad9375a6a6&start=1";
        new jsonOku(url2, this.getActivity()).execute();

        btnSiparis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sha.getString("kullaniciId", "").equals("")) {
                    try {
                        sepeteUrunEkle(getView());
                    }catch (Exception e){
                        Toast.makeText(getActivity(), "Sepet Json Hata", Toast.LENGTH_SHORT).show();
                    }
                    //Daha önce kullanıcı kayıtlı
                    //Sepet'ini görebilir.
                    SepetFragment fragment = new SepetFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            //Telefondan geri butonuna bastığı zaman çıksın
                            //.addToBackStack(null)
                            .commit();

                }else{
                    //Kullanıcı kayıtlı değil
                    //Giris fragment'a geçsin.
                    GirisFragment fragment = new GirisFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            //Telefondan geri butonuna bastığı zaman çıksın
                            //.addToBackStack(null)
                            .commit();
                }
            }
        });

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
                JSONArray products = obj.getJSONArray("Products");
                JSONObject katObj = products.getJSONObject(0);
                JSONArray bilgiler = katObj.getJSONArray("bilgiler");

                String thumb, thumb2, thumb3 = null;

                for (int i = 0; i < bilgiler.length(); i++) {
                    JSONObject bilgi = bilgiler.getJSONObject(i);
                    String productId = bilgi.getString("productId");
                    if (productId.equals(MainFragment.tiklananUrunId)) {
                        MainFragment.tiklananUrunId="";
                        detayBaslik.setText(bilgi.getString("productName"));
                        detayFiyat.setText(bilgi.getString("price"));
                        String imgDurum = bilgi.getString("image");
                        siparisId = bilgi.getString("productId");

                        if (!imgDurum.equals("false")) {
                            JSONArray resimAr = bilgi.getJSONArray("images");
                            thumb = resimAr.getJSONObject(0).getString("normal");
                        } else {
                            thumb = "http://vincinmerkezi.com/images/resimyok.gif";
                        }

                        Picasso.with(getActivity()).load(thumb).into(detayResim);
                    }

                    if (productId.equals(YiyeceklerFragment.tiklananYiyecekId)) {
                        YiyeceklerFragment.tiklananYiyecekId = "";
                        detayBaslik.setText(bilgi.getString("productName"));
                        detayFiyat.setText(bilgi.getString("price"));
                        String imgDurum = bilgi.getString("image");
                        siparisId = bilgi.getString("productId");

                        if (!imgDurum.equals("false")) {
                            JSONArray resimAr = bilgi.getJSONArray("images");
                            thumb2 = resimAr.getJSONObject(0).getString("normal");
                        } else {
                            thumb2 = "http://vincinmerkezi.com/images/resimyok.gif";
                        }

                        Picasso.with(getActivity()).load(thumb2).into(detayResim);

                    }

                    if (productId.equals(IceceklerFragment.tiklananIcecekId)) {
                        IceceklerFragment.tiklananIcecekId = "";
                        detayBaslik.setText(bilgi.getString("productName"));
                        detayFiyat.setText(bilgi.getString("price"));
                        String imgDurum = bilgi.getString("image");
                        siparisId = bilgi.getString("productId");

                        if (!imgDurum.equals("false")) {
                            JSONArray resimAr = bilgi.getJSONArray("images");
                            thumb3 = resimAr.getJSONObject(0).getString("normal");
                        } else {
                            thumb3 = "http://vincinmerkezi.com/images/resimyok.gif";
                        }
                        Picasso.with(getActivity()).load(thumb3).into(detayResim);
                    }

                    if (productId.equals(TatliFragment.tiklananTatliId)) {
                        TatliFragment.tiklananTatliId = "";
                        detayBaslik.setText(bilgi.getString("productName"));
                        detayFiyat.setText(bilgi.getString("price"));
                        String imgDurum = bilgi.getString("image");
                        siparisId = bilgi.getString("productId");

                        if (!imgDurum.equals("false")) {
                            JSONArray resimAr = bilgi.getJSONArray("images");
                            thumb3 = resimAr.getJSONObject(0).getString("normal");
                        } else {
                            thumb3 = "http://vincinmerkezi.com/images/resimyok.gif";
                        }
                        Picasso.with(getActivity()).load(thumb3).into(detayResim);
                    }

                    if (productId.equals(KampanyalarFragment.tiklananKampanyaId)) {
                        KampanyalarFragment.tiklananKampanyaId = "";
                        detayBaslik.setText(bilgi.getString("productName"));
                        detayFiyat.setText(bilgi.getString("price"));
                        String imgDurum = bilgi.getString("image");
                        siparisId = bilgi.getString("productId");

                        if (!imgDurum.equals("false")) {
                            JSONArray resimAr = bilgi.getJSONArray("images");
                            thumb3 = resimAr.getJSONObject(0).getString("normal");
                        } else {
                            thumb3 = "http://vincinmerkezi.com/images/resimyok.gif";
                        }
                        Picasso.with(getActivity()).load(thumb3).into(detayResim);
                    }

                    if (productId.equals(SepetFragment.tiklananSiparisId)) {
                        SepetFragment.tiklananSiparisId = "";
                        detayBaslik.setText(bilgi.getString("productName"));
                        detayFiyat.setText(bilgi.getString("price"));
                        String imgDurum = bilgi.getString("image");
                        btnSiparis.setVisibility(View.INVISIBLE);
                        siparisId = bilgi.getString("productId");

                        if (!imgDurum.equals("false")) {
                            JSONArray resimAr = bilgi.getJSONArray("images");
                            thumb3 = resimAr.getJSONObject(0).getString("normal");
                        } else {
                            thumb3 = "http://vincinmerkezi.com/images/resimyok.gif";
                        }
                        Picasso.with(getActivity()).load(thumb3).into(detayResim);
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void sepeteUrunEkle(View v){
        Toast.makeText(getActivity(), "kid." +sha.getString("kullaniciId", "")+"utunid: "+siparisId, Toast.LENGTH_SHORT).show();
        String url3 = "http://jsonbulut.com/json/orderForm.php?ref=37c663e00c3dfb1eeb0c95ad9375a6a6&customerId="+sha.getString("kullaniciId", "")+"&productId="+siparisId+"&html="+siparisId;
        new sepetJsonOku(url3, this.getActivity()).execute();
    }

    class sepetJsonOku extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pr;

        String url = "";
        String data = "";

        public sepetJsonOku(String url, Activity ac) {
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
                JSONArray order = obj.getJSONArray("order");
                JSONObject orderSifir = order.getJSONObject(0);

                if (orderSifir.getString("durum").equals("true")){
                    Toast.makeText(getActivity(), "Ürün eklendi.", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getActivity(), "Ürün sepete eklenemedi!"+orderSifir.getString("durum"), Toast.LENGTH_SHORT).show();
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
