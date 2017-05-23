package org.app.alice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.app.alice.R;
import org.app.alice.modelo.Cita;

import java.util.List;

/**
 * Created by dervis on 25/01/17.
 */
public class ListaCitasAdapter extends BaseAdapter {

    private Context context;
    private List<Cita> items;

    public ListaCitasAdapter(Context context, List<Cita> items) {
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
            rowView = inflater.inflate(R.layout.adapter_lista_citas, parent, false);
            TextView servicio=(TextView) rowView.findViewById(R.id.tvNombreServicio);
            TextView estado=(TextView) rowView.findViewById(R.id.tvEstadoCita);
            TextView fecha=(TextView) rowView.findViewById(R.id.tvFecha);
            Cita item=items.get(position);
            servicio.setText(item.getDescripcion());
            //estado.setText(String.valueOf(item.getEstatus()));
            fecha.setText(item.getFecha());
            if(String.valueOf(item.getEstatus())=="1"){
                estado.setText("Solicitada");
            }if(String.valueOf(item.getEstatus())=="2"){
                estado.setText("Confirmada");
            }if(String.valueOf(item.getEstatus())=="3"){
                estado.setText("Atendida");
            }if(String.valueOf(item.getEstatus())=="4"){
                estado.setText("Evaluada");
            }if(String.valueOf(item.getEstatus())=="5"){
                estado.setText("Cancelada");
            }

        }

        // Set data into the view


        return rowView;
    }
}
