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

import items.Items;

/**
 * Created by mkatr on 4.7.2016.
 */
public class ItemsAdapter extends BaseAdapter {

    private LayoutInflater inf;
    private List<Items> urls;
    private Activity ac;

    public ItemsAdapter(Activity activity, List<Items> urls) {
        inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.urls = urls;
        ac = activity;
    }

    @Override
    public int getCount() {//ürün sayısı kadar dön
        return urls.size();
    }

    @Override
    public Items getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView;
        rowView = inf.inflate(R.layout.urunrow, null);
        TextView urunBaslik = (TextView) rowView.findViewById(R.id.txtUrunBaslik);
        TextView urunFiyat = (TextView) rowView.findViewById(R.id.txtUrunFiyat);

        ImageView resim = (ImageView) rowView.findViewById(R.id.resim);

        Items ul = urls.get(position);
        urunBaslik.setText(ul.getProductName());
        urunFiyat.setText(ul.getPrice()+" TL");
        Picasso.with(ac).load(ul.getImages()).into(resim);

        return rowView;
    }
}
