package com.example.mkatr.fazenderoapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import items.Items;
import itemsAdapter.ItemsAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    ListView urunlerList;
    List<Items> urunlerArray = new ArrayList<>();
    static String tiklananUrunId;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        urunlerList = (ListView) view.findViewById(R.id.urunlerList);
        try {
            String url2 = "http://jsonbulut.com/json/product.php?ref=37c663e00c3dfb1eeb0c95ad9375a6a6&start=1&categoryId=1416";
            new jsonOku(url2, this.getActivity()).execute();
        }catch (Exception ex){
            Toast.makeText(getActivity(), "Veri getitrme hatası", Toast.LENGTH_SHORT).show();
        }

        urunlerArray.clear();

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

                String thumb = null;
                for (int i = 0; i < bilgiler.length(); i++) {
                    JSONObject bilgi = bilgiler.getJSONObject(i);
                    String productId = bilgi.getString("productId");
                    String productName = bilgi.getString("productName");
                    String price = bilgi.getString("price");
                    String imgDurum = bilgi.getString("image");

                    if (!imgDurum.equals("false")) {
                        JSONArray resimAr = bilgi.getJSONArray("images");
                        thumb = resimAr.getJSONObject(0).getString("normal");
                    } else {
                        thumb = "http://vincinmerkezi.com/images/resimyok.gif";
                    }

                    Items ur = new Items(productId, productName, price, thumb);
                    urunlerArray.add(ur);
                }

                ItemsAdapter adp = new ItemsAdapter(getActivity(), urunlerArray);
                urunlerList.setAdapter(adp);

                urunlerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            //Tıklanan'a ait olan bilgileri daha önce set edilmiş sınıftan get edilerek alındı
                            Items ur = urunlerArray.get(position);
                            tiklananUrunId = ur.getProductId();

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

