package org.app.alice.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.app.alice.R;
import org.app.alice.adapter.ListaCitasAdapter;
import org.app.alice.adapter.ListaEspecialistasAdapter;
import org.app.alice.adapter.ListaTipoServiciosAdapter;
import org.app.alice.modelo.CategoriaServicio;
import org.app.alice.modelo.CategoriaServicioDao;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.TipoServicio;
import org.app.alice.modelo.TipoServicioDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brayan on 12/03/17.
 */

public class ListaTipoServiciosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tipo_servicio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ListView listaTP=(ListView)findViewById(R.id.lvListaTipoServicios);
        Bundle bolsa=getIntent().getExtras();
        long id=bolsa.getLong("id");
        String titulo=bolsa.getString("titulo");
        getSupportActionBar().setTitle(titulo);
        DAOApp daoApp=new DAOApp();
        TipoServicioDao tipoServicioDao=daoApp.getTipoServicioDao();
        List<TipoServicio> list=tipoServicioDao._queryCategoriaServicio_TipoServicio(id);
        if(!list.isEmpty()){
            listaTP.setAdapter(new ListaTipoServiciosAdapter(this, list));
            listaTP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    TipoServicio item = (TipoServicio) listaTP.getAdapter().getItem(pos);
                    llamarActivity(item);

                }
            });
        }else{
            showAlertDialog(this,"Informacion","No esisten tipo servicio registrados",true);
        }

}


    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
//		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }







    public void llamarActivity(TipoServicio item){
        Intent i= new Intent(this, ListaEspecialistasActivity.class);
        i.putExtra("id",item.getId());
        i.putExtra("titulo",item.getDescripcion());
        startActivity(i);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            /*case R.id.action_settings:
                Intent intent=new Intent(ListaTipoServiciosActivity.this, BuscarCitaActivity.class);
                startActivity(intent);*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
