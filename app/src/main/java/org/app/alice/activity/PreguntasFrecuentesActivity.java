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
import org.app.alice.adapter.ListaTipoServiciosAdapter;
import org.app.alice.adapter.PreguntasFrecuentesAdapter;
import org.app.alice.modelo.Cita;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Pregunta;
import org.app.alice.modelo.PreguntaDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brayan on 12/03/17.
 */

public class PreguntasFrecuentesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas_frecuentes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListView listaPF=(ListView)findViewById(R.id.lvPreguntasFrecuentes);


        DAOApp daoApp=new DAOApp();
        PreguntaDao preguntaDao=daoApp.getPreguntaDao();
        List<Pregunta> list= preguntaDao.loadAll();
        listaPF.setAdapter(new PreguntasFrecuentesAdapter(this, list));
        /*listaTP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Intent i= new Intent(PreguntasFrecuentesActivity.this, ListaEspecialistasActivity.class);
                startActivity(i);

            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
