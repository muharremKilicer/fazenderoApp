package com.example.mkatr.fazenderoapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class KayitFragment extends Fragment {

    SharedPreferences sha;
    SharedPreferences.Editor edit;
    EditText userName, userSurname, userPhone, userMail, userPass;
    Button btnGirisYap, btnKayitOl;

    public KayitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kayit, container, false);
        userName = (EditText) view.findViewById(R.id.txtKayitUsername);
        userSurname = (EditText) view.findViewById(R.id.txtKayitUserSurname);
        userPhone = (EditText) view.findViewById(R.id.txtKayitUserPhone);
        userMail = (EditText) view.findViewById(R.id.txtKayitUserMail);
        userPass = (EditText) view.findViewById(R.id.txtKayitUserPass);
        btnGirisYap = (Button) view.findViewById(R.id.btnGirisYap);
        btnKayitOl = (Button) view.findViewById(R.id.btnKayitOl);

        sha = this.getActivity().getSharedPreferences("kul", Context.MODE_PRIVATE);
        edit = sha.edit();

        //Kullanıcı daha önce giriş yaptıysa tekrar kayıt olamaz. Önce çıkış yapsın.
        if (!sha.getString("kullaniciId", "").equals("")) {
            //Daha önce kullanıcı kayıtlı
            //Sepet fragment'a geçsin. Ordan çıkış yapar.
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
        btnKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fncKayit(getView());
            }
        });
        btnGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisSayfasi(getView());
            }
        });

        //Edittext'leri sırayla geçmek için
        //android:inputType="text" ve android:imeOptions="actionNext" eklenmelidir.

        //Son şifreyi girdiğinde klavyeden sağ alttaki sen işaretine tıklarsa fncKayit çalışssın.
        userPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    return true;
                }
                fncKayit(getView());
                return false;
            }
        });

        return view;
    }

    public void fncKayit(View v) {
        if (boslukKontrol(new EditText[]{userName, userSurname, userPhone, userMail, userPass})) {

            if (!GirisFragment.emailKontrol(userMail.getText().toString().trim())) {
                userMail.setError("Mail adresi uygun degil." + "\n" + "user@example.com");
                userMail.setText("");
                userMail.requestFocus();
            } else {
                String url = "http://jsonbulut.com/json/userRegister.php?ref=37c663e00c3dfb1eeb0c95ad9375a6a6&" +
                        "userName=" + userName.getText().toString().trim() +
                        "&userSurname=" + userSurname.getText().toString().trim() +
                        "&userPhone=" + userPhone.getText().toString().trim() +
                        "&userMail=" + userMail.getText().toString().trim() +
                        "&userPass=" + userPass.getText().toString().trim() + "";
                new jsonOku(url, this.getActivity()).execute();
            }

        }

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

                //Kullanıcı kayıt başarılıysa
                if (userSifir.getString("durum").equals("true")) {
                    edit.putString("kullaniciId", userSifir.getString("kullaniciId"));
                    edit.commit();

                    //Kullanıcı kayıt işlemi başarılı şekilde yapıldı.
                    MainActivity.navigationView.getMenu().findItem(R.id.nav_giris).setTitle("PROFİL");

                    //Sepet fragment'a geçiş
                    SepetFragment fragment = new SepetFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Toast.makeText(getActivity(), "Hata:" + userSifir.getString("mesaj"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void girisSayfasi(View v) {
        //Kayıt fragment'a geçiş
        GirisFragment fragment = new GirisFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private boolean boslukKontrol(EditText[] fields) {
        for (int i = 0; i < fields.length; i++) {
            EditText currentField = fields[i];
            if (currentField.getText().toString().length() <= 0) {
                //XML dosyasından edittext'e tag ekliyoruz.
                currentField.setError(currentField.getTag().toString());
                currentField.requestFocus();
                return false;
            }
        }
        return true;
    }
}
