package org.app.alice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.app.alice.R;
import org.app.alice.modelo.Evento;
import org.app.alice.modelo.Noticia;
import org.app.alice.service.GlobalService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dervis on 25/01/17.
 */
public class CatalogoEventoAdapter extends BaseAdapter {

    private Context context;
    private List<Evento> items;

    public CatalogoEventoAdapter(Context context, List<Evento> items) {
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
            rowView = inflater.inflate(R.layout.adapter_catalogo_evento, parent, false);
            TextView titulo=(TextView) rowView.findViewById(R.id.tvTituloEvento);
            TextView fecha=(TextView) rowView.findViewById(R.id.tvFechaEvento);
            TextView tipoServicio=(TextView) rowView.findViewById(R.id.tvDescripcionEvento);
            //TextView tipo=(TextView) rowView.findViewById(R.id.tvTipoEvento);
            Evento item=items.get(position);
            titulo.setText(item.getDescripcion());

            fecha.setText(item.getTipoEvento()+"  -  "+item.getFecha());
            tipoServicio.setText(item.getTipoServicio().substring(0,item.getTipoServicio().length()-1));
            //tipo.setText(item.getTipoEvento());
        }

        // Set data into the view


        return rowView;
    }
}
