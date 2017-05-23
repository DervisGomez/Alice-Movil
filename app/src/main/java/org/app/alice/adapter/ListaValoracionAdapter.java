package org.app.alice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.app.alice.R;
import org.app.alice.modelo.Alerta;
import org.app.alice.modelo.Criterio;
import org.app.alice.modelo.CriterioDao;
import org.app.alice.modelo.DAOApp;

import java.util.List;

/**
 * Created by brayan on 19/03/17.
 */

public class ListaValoracionAdapter extends BaseAdapter{

    private Context context;
    private List<Criterio> items;



    public ListaValoracionAdapter(Context context, List<Criterio> items) {
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
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.adapter_evaluacion, parent, false);
        }
        DAOApp daoApp=new DAOApp();
        final CriterioDao criterioDao=daoApp.getCriterioDao();
        final Criterio item=items.get(position);
        final Button estrella1P;
        final Button estrella2P;
        final Button estrella3P;
        final Button estrella4P;
        final Button estrella5P;
        final EditText valoracionP;
        TextView titulo=(TextView)rowView.findViewById(R.id.tvTituloEvaluacion);
        titulo.setText(item.getDescripcion());
        estrella1P=(Button)rowView.findViewById(R.id.btnEstrellaP1);
        estrella2P=(Button)rowView.findViewById(R.id.btnEstrellaP2);
        estrella3P=(Button)rowView.findViewById(R.id.btnEstrellaP3);
        estrella4P=(Button)rowView.findViewById(R.id.btnEstrellaP4);
        estrella5P=(Button)rowView.findViewById(R.id.btnEstrellaP5);
        valoracionP=(EditText)rowView.findViewById(R.id.etvaloracionP);
        estrella1P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estrella1P.setBackgroundResource(R.drawable.est1);
                estrella2P.setBackgroundResource(R.drawable.est0);
                estrella3P.setBackgroundResource(R.drawable.est0);
                estrella4P.setBackgroundResource(R.drawable.est0);
                estrella5P.setBackgroundResource(R.drawable.est0);
                valoracionP.setText("Deficiente");
                item.setValor(1);
                criterioDao.update(item);

            }
        });
        estrella2P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estrella1P.setBackgroundResource(R.drawable.est1);
                estrella2P.setBackgroundResource(R.drawable.est1);
                estrella3P.setBackgroundResource(R.drawable.est0);
                estrella4P.setBackgroundResource(R.drawable.est0);
                estrella5P.setBackgroundResource(R.drawable.est0);
                valoracionP.setText("Malo");
                item.setValor(2);
                criterioDao.update(item);
            }
        });
        estrella3P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estrella1P.setBackgroundResource(R.drawable.est1);
                estrella2P.setBackgroundResource(R.drawable.est1);
                estrella3P.setBackgroundResource(R.drawable.est1);
                estrella4P.setBackgroundResource(R.drawable.est0);
                estrella5P.setBackgroundResource(R.drawable.est0);
                valoracionP.setText("Regular");
                item.setValor(3);
                criterioDao.update(item);
            }
        });
        estrella4P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estrella1P.setBackgroundResource(R.drawable.est1);
                estrella2P.setBackgroundResource(R.drawable.est1);
                estrella3P.setBackgroundResource(R.drawable.est1);
                estrella4P.setBackgroundResource(R.drawable.est1);
                estrella5P.setBackgroundResource(R.drawable.est0);
                valoracionP.setText("Bueno");
                item.setValor(4);
                criterioDao.update(item);
            }
        });
        estrella5P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estrella1P.setBackgroundResource(R.drawable.est1);
                estrella2P.setBackgroundResource(R.drawable.est1);
                estrella3P.setBackgroundResource(R.drawable.est1);
                estrella4P.setBackgroundResource(R.drawable.est1);
                estrella5P.setBackgroundResource(R.drawable.est1);
                valoracionP.setText("Excelente");
                item.setValor(5);
                criterioDao.update(item);
            }
        });


        return rowView;
    }


}