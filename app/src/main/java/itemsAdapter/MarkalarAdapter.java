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

import items.Markalar;

/**
 * Created by mkatr on 3.7.2016.
 */
public class MarkalarAdapter extends BaseAdapter {
    private LayoutInflater inf;
    private List<Markalar> urls;
    private Activity ac;

    public MarkalarAdapter(Activity activity, List<Markalar> urls) {
        inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.urls = urls;
        ac = activity;
    }

    @Override
    public int getCount() {//ürün sayısı kadar dön
        return urls.size();
    }

    @Override
    public Markalar getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView;
        rowView = inf.inflate(R.layout.markarow2, null);
        TextView markaAdi = (TextView) rowView.findViewById(R.id.txtMarkaAdi);
        ImageView resim = (ImageView) rowView.findViewById(R.id.markaResim);

        Markalar ul = urls.get(position);
        markaAdi.setText(ul.getProductName());
        Picasso.with(ac).load(ul.getImages()).into(resim);

        return rowView;
    }
}
