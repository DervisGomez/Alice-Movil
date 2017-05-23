package org.app.alice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.app.alice.R;
import org.app.alice.modelo.Alerta;

import java.util.List;

/**
 * Created by dervis on 25/01/17.
 */
public class ListaNotificacionAdapter extends BaseAdapter {

    private Context context;
    private List<Alerta> items;

    public ListaNotificacionAdapter(Context context, List<Alerta> items) {
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
            rowView = inflater.inflate(R.layout.adapter_lista_notificacion, parent, false);
            TextView descripcion=(TextView)rowView.findViewById(R.id.tvDescripcionNotificación);
            TextView fecha=(TextView)rowView.findViewById(R.id.tvFechaNotificacion);
            Alerta item=items.get(position);
            descripcion.setText(item.getDescripcion());
            fecha.setText(item.getFecha());
        }

        // Set data into the view


        return rowView;
    }
}
