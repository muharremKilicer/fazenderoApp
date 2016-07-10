package com.example.mkatr.fazenderoapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class GirisFragment extends Fragment {

    //Giriş kısmında tekrar tekrar yazmamak için
    //Mail: demo@a.com Şifre: 123456

    SharedPreferences sha;
    SharedPreferences.Editor edit;
    EditText userMail, userPass;
    Button btnGiris, btnKayit;
    NavigationView navigationView;

    public GirisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_giris, container, false);
        userMail = (EditText) view.findViewById(R.id.txtKayitUserMail);
        userPass = (EditText) view.findViewById(R.id.txtUserPass);
        btnGiris = (Button) view.findViewById(R.id.btnGiris);
        btnKayit = (Button) view.findViewById(R.id.btnKayit);

        sha = this.getActivity().getSharedPreferences("kul", Context.MODE_PRIVATE);
        edit = sha.edit();

        //Her seferinden yazmamak için
        userMail.setText("demo@a.com");
        userPass.setText("123456");

        //Kullanıcı daha önce giriş yaptıysa tekrar giriş yapmasına gerek yok
        if (!sha.getString("kullaniciId", "").equals("")) {
            //Daha önce kullanıcı kayıtlı
            //Sepet fragment'a geçsin
            SepetFragment fragment = new SepetFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    //Telefondan geri butonuna bastığı zaman çıksın
                    //.addToBackStack(null)
                    .commit();

        }


        //Fragment'ta yer alan butona onclick olarak bir fonksiyon verilemiyor.
        //Bunun için bizde tıklandığında o metodu tetikledik.
        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fncGiris(getView());
            }
        });
        btnKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayitSayfasi(getView());
            }
        });

        return view;
    }

    public void fncGiris(View v) {
        if (!emailKontrol(userMail.getText().toString().trim())) {
            userMail.setError("Mail adresi uygun degil." + "\n" + "user@example.com");
            userMail.setText("");
            userMail.requestFocus();
        } else if (userPass.getText().toString().trim().length() < 4) {
            userPass.setError("Şifreniz en az 4 karakter içermelidir");
            userPass.setText("");
            userPass.requestFocus();
        } else {

            String url = "http://jsonbulut.com/json/userLogin.php?ref=37c663e00c3dfb1eeb0c95ad9375a6a6&" +
                    "userEmail=" + userMail.getText().toString().trim() + "" +
                    "&userPass=" + userPass.getText().toString().trim() + "&face=no";
            new jsonOku(url, this.getActivity()).execute();

        }
    }

    //Email kontrol
    public static boolean emailKontrol(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
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
                JSONArray user = obj.getJSONArray("user");
                JSONObject userSifir = user.getJSONObject(0);

                //Kullanıcı giriş başarılıysa
                if (userSifir.getString("durum").equals("true")) {
                    JSONObject bilgi = userSifir.getJSONObject("bilgiler");
                    edit.putString("kullaniciId", bilgi.getString("userId"));
                    edit.putString("adSoyad", bilgi.getString("userName") + " " + bilgi.getString("userSurname"));
                    edit.commit();

                    //Kullanıcı girişi başarılı şekilde yaptı. Artık profil yazsın
                    MainActivity.navigationView.getMenu().findItem(R.id.nav_giris).setTitle("PROFİL");

                    //Sepet fragment'a geçiş
                    SepetFragment fragment = new SepetFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();

                } else {
                    Toast.makeText(getActivity(), "Kullanıcı adı veya şifre yanlış", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void kayitSayfasi(View v) {
        //Kayıt fragment'a geçiş
        KayitFragment fragment = new KayitFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


}
