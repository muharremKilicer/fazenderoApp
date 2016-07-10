package itemsAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mkatr.fazenderoapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import items.Siparisler;

/**
 * Created by mkatr on 9.7.2016.
 */
public class SiparislerAdapter extends BaseAdapter {

    private LayoutInflater inf;
    private List<Siparisler> katls;
    private Activity ac;

    public SiparislerAdapter(Activity activity, List<Siparisler> urls) {
        inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.katls = urls;
        ac = activity;
    }

    @Override
    public int getCount() {//ürün sayısı kadar dön
        return katls.size();
    }

    @Override
    public Siparisler getItem(int position) {
        return katls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView;
        rowView = inf.inflate(R.layout.sepetrow, null);
        TextView urunBaslik = (TextView) rowView.findViewById(R.id.txtSepetUrunBaslik);
        TextView urunFiyat = (TextView) rowView.findViewById(R.id.txtSepetUrunFiyat);
        ImageView resim = (ImageView) rowView.findViewById(R.id.sepetResim);

        Siparisler ul = katls.get(position);
        urunBaslik.setText(ul.getUrun_adi());
        urunFiyat.setText(ul.getFiyat());
        String imgUrl;
        if (!ul.getResAdi().equals("null")) {
            imgUrl = "http://jsonbulut.com/admin/resim/server/php/files/" + ul.getResKlasör() + "/thumbnail/" + ul.getResAdi();
            Picasso.with(ac).load(imgUrl).into(resim);
        }
        if (ul.getResAdi().equals("null")) {
            imgUrl = "http://vincinmerkezi.com/images/resimyok.gif";
            Picasso.with(ac).load(imgUrl).into(resim);
        }

        return rowView;
    }
}
