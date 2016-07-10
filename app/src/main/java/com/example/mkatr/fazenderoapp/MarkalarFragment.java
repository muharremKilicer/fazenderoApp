package com.example.mkatr.fazenderoapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import items.Markalar;
import itemsAdapter.MarkalarAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarkalarFragment extends Fragment {

    ListView markalarList;
    final List<Markalar> markalarArray = new ArrayList<>();
    public MarkalarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_markalar, container, false);
        markalarList = (ListView) view.findViewById(R.id.markalarList);
        String url2 = "http://jsonbulut.com/json/product.php?ref=37c663e00c3dfb1eeb0c95ad9375a6a6&start=1&categoryId=1411";
        new jsonOku(url2, this.getActivity()).execute();
        markalarArray.clear();
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
                JSONArray bilgiler  = katObj.getJSONArray("bilgiler");

                String thumb = null;
                for (int i = 0; i < bilgiler.length(); i++) {
                    JSONObject bilgi = bilgiler.getJSONObject(i);
                    String productId = bilgi.getString("productId");
                    String productName = bilgi.getString("productName");
                    String imgDurum = bilgi.getString("image");

                    if (!imgDurum.equals("false")) {
                        JSONArray resimAr = bilgi.getJSONArray("images");
                        thumb = resimAr.getJSONObject(0).getString("nor" +
                                "mal");
                    } else {
                        thumb = "http://vincinmerkezi.com/images/resimyok.gif";
                    }

                    Markalar ur = new Markalar(productId, productName, thumb);
                    markalarArray.add(ur);
                }

                MarkalarAdapter adp = new MarkalarAdapter(getActivity(), markalarArray);
                markalarList.setAdapter(adp);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
