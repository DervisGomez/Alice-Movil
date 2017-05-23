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
import org.app.alice.modelo.Servicio;

import java.util.List;

/**
 * Created by dervis on 25/01/17.
 */
public class BuscarTodoAdapter extends BaseAdapter {

    private Context context;
    private List<Object> items;
    private List<Integer> tipo;

    public BuscarTodoAdapter(Context context, List<Object> items, List<Integer> tipo) {
        this.context = context;
        this.items = items;
        this.tipo =tipo;
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
            rowView = inflater.inflate(R.layout.adapter_buscar_todo, parent, false);
            TextView titulo=(TextView) rowView.findViewById(R.id.tvTituloBusqueda);
            TextView fecha=(TextView) rowView.findViewById(R.id.tvFechaBusqueda);
            TextView tipoServicio=(TextView) rowView.findViewById(R.id.tvDescripcionBusqueda);
            TextView tipob=(TextView) rowView.findViewById(R.id.tvIconoBusqueda);

           int x=tipo.get(position);

            //Servicio item=(Servicio)items.get(position);

            switch (x){
                case 1:
                    Servicio item=(Servicio)items.get(position);
                    titulo.setText(item.getDescripcion());
                    fecha.setText(String.valueOf(item.getPrecio()));
                    tipob.setText("S");
                    break;
                case 2:
                    Evento item1=(Evento)items.get(position);
                    titulo.setText(item1.getDescripcion());
                    fecha.setText(item1.getFecha());
                    tipob.setText("E");
                    break;
                case 3:
                    Noticia item2=(Noticia)items.get(position);
                    titulo.setText(item2.getTitulo());
                    fecha.setText(item2.getFecha());
                    tipob.setText("N");
                    break;
            }
            //tipo.setText(item.getTipoEvento());
        }

        // Set data into the view


        return rowView;
    }
}
