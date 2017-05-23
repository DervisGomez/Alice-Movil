package org.app.alice.activity;

import android.content.Intent;
import android.os.Bundle;
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
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Servicio;
import org.app.alice.modelo.ServicioDao;
import org.app.alice.modelo.TipoServicio;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brayan on 12/03/17.
 */

public class ListaEspecialistasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_especialistas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ListView listaEsp=(ListView)findViewById(R.id.lvListaEspecialistas);
        Bundle bolsa=getIntent().getExtras();
        long id=bolsa.getLong("id");
        String titulo=bolsa.getString("titulo");
        getSupportActionBar().setTitle(titulo);
        DAOApp daoApp=new DAOApp();
        ServicioDao servicioDao=daoApp.getServicioDao();
        List<Servicio> list=servicioDao._queryTipoServicio_Servicio(id);

        listaEsp.setAdapter(new ListaEspecialistasAdapter(this, list));
        listaEsp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Servicio item=(Servicio)listaEsp.getAdapter().getItem(pos);
                llamarActivity(item);

            }
        });
    }

    public void llamarActivity(Servicio item){
        Intent i= new Intent(ListaEspecialistasActivity.this, VerEspecialistaActivity.class);
        i.putExtra("id",item.getId());
        i.putExtra("alerta",(long)0);
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
                Intent intent=new Intent(ListaEspecialistasActivity.this, BuscarCitaActivity.class);
                startActivity(intent);*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
