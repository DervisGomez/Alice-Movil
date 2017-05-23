package org.app.alice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.app.alice.R;
import org.app.alice.modelo.Noticia;

import java.util.List;

/**
 * Created by dervis on 25/01/17.
 */
public class CatalogoNoticiasAdapter extends BaseAdapter {

    private Context context;
    private List<Noticia> items;

    public CatalogoNoticiasAdapter(Context context, List<Noticia> items) {
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
            rowView = inflater.inflate(R.layout.adapter_catalogo_noticias, parent, false);
            TextView titulo=(TextView) rowView.findViewById(R.id.tvTituloNoticia);
            TextView fecha=(TextView) rowView.findViewById(R.id.tvFechaNoticia);
            TextView descripcion=(TextView) rowView.findViewById(R.id.tvDescripcionNoticia);
            //TextView tipo=(TextView) rowView.findViewById(R.id.tvTipoNoticia);
            Noticia item=items.get(position);
            titulo.setText(item.getTitulo());
            fecha.setText(item.getTipoNoticia()+"  -  "+item.getFecha());
            descripcion.setText(item.getDescripcion());
            //tipo.setText(item.getTipoNoticia());
        }

        // Set data into the view


        return rowView;
    }
}
