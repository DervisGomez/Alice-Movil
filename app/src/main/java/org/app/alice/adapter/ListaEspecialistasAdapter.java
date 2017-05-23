package org.app.alice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.app.alice.R;
import org.app.alice.modelo.Servicio;

import java.util.List;

/**
 * Created by brayan on 12/03/17.
 */

public class ListaEspecialistasAdapter extends BaseAdapter {

    private Context context;
    private List<Servicio> items;

    public ListaEspecialistasAdapter(Context context, List<Servicio> items) {
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
            rowView = inflater.inflate(R.layout.adapter_lista_especialistas, parent, false);
            TextView nombre=(TextView)rowView.findViewById(R.id.tvNombreEspecialista);
            TextView precio=(TextView)rowView.findViewById(R.id.tvPrecioEspecialista);
            Servicio item=items.get(position);
            nombre.setText(item.getDescripcion());
            precio.setText(String.valueOf(item.getPrecio()));
        }

        // Set data into the view


        return rowView;
    }
}
