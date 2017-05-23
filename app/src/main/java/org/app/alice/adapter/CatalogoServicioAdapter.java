package org.app.alice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.app.alice.R;
import org.app.alice.modelo.CategoriaServicio;

import java.util.List;

/**
 * Created by dervis on 25/01/17.
 */
public class CatalogoServicioAdapter extends BaseAdapter {

    private Context context;
    private List<CategoriaServicio> items;


    public CatalogoServicioAdapter(Context context, List<CategoriaServicio> items) {
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
            rowView = inflater.inflate(R.layout.adapter_catalogo_servicio, parent, false);
            TextView nombreServicio=(TextView) rowView.findViewById(R.id.tvNombreCategoriaServicio);
            ImageView foto=(ImageView) rowView.findViewById(R.id.imgServicio2);
            CategoriaServicio categoriaServicio=items.get(position);
            nombreServicio.setText(categoriaServicio.getDescripcion());
            switch (categoriaServicio.getId().intValue()){
                case 1:
                    foto.setBackgroundResource(R.drawable.laboratorio);
                    break;
                case 2:
                    foto.setBackgroundResource(R.drawable.imagenologia);
                    break;
                case 3:
                    foto.setBackgroundResource(R.drawable.laboratorio);
                    break;
                case 4:
                    foto.setBackgroundResource(R.drawable.laboratorio);
                    break;
                case 5:
                    foto.setBackgroundResource(R.drawable.imagenologia);
                    break;
                case 6:
                    foto.setBackgroundResource(R.drawable.consulta);
                    break;
            }

        }

        // Set data into the view


        return rowView;
    }

}
