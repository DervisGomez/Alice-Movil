package org.app.alice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.app.alice.R;
import org.app.alice.activity.PreguntasFrecuentesActivity;
import org.app.alice.modelo.Cita;
import org.app.alice.modelo.Pregunta;

import java.util.List;

/**
 * Created by brayan on 12/03/17.
 */

public class PreguntasFrecuentesAdapter extends BaseAdapter {

    private Context context;
    private List<Pregunta> items;

    public PreguntasFrecuentesAdapter(Context context, List<Pregunta> items) {
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
            rowView = inflater.inflate(R.layout.adapter_preguntas_frecuentes, parent, false);

            TextView pregunta=(TextView) rowView.findViewById(R.id.etPregunta);
            TextView tipoPreg=(TextView) rowView.findViewById(R.id.etTipoPregunta);
            TextView respuesta=(TextView) rowView.findViewById(R.id.etRespuesta);
            Pregunta item=items.get(position);
            pregunta.setText(item.getDescripcion());
            tipoPreg.setText(item.getTipoPregunta());
            respuesta.setText(item.getRespuesta());
        }

        // Set data into the view


        return rowView;
    }
}
