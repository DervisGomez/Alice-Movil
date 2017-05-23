package org.app.alice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.app.alice.R;
import org.app.alice.modelo.TipoServicio;

import java.util.List;

/**
 * Created by brayan on 12/03/17.
 */

public class ListaTipoServiciosAdapter extends BaseAdapter {

    private Context context;
    private List<TipoServicio> items;

    public ListaTipoServiciosAdapter(Context context, List<TipoServicio> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.adapter_tipo_servicios, parent, false);
            TextView descripcion=(TextView) rowView.findViewById(R.id.tvDescripcionTipoServicio);
            TextView texto=(TextView) rowView.findViewById(R.id.tvTextoTipoServicio);
            TipoServicio item=items.get(position);
            descripcion.setText(item.getDescripcion());
            texto.setText(item.getTexto());
            texto.setVisibility(View.GONE);
        }

        // Set data into the view


        return rowView;
    }
}
