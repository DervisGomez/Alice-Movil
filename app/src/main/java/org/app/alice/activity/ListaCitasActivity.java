package org.app.alice.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.app.alice.R;
import org.app.alice.adapter.CatalogoEventoAdapter;
import org.app.alice.adapter.ListaCitasAdapter;
import org.app.alice.adapter.ListaTipoServiciosAdapter;
import org.app.alice.modelo.Cita;
import org.app.alice.modelo.CitaDao;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.TipoServicio;
import org.app.alice.modelo.TipoServicioDao;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public class ListaCitasActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_citas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ListView listaCitas=(ListView)findViewById(R.id.lvListaCistas);


        DAOApp daoApp=new DAOApp();
        CitaDao citaDao=daoApp.getCitaDao();
        List<Cita> list= citaDao.loadAll();
        if(!list.isEmpty()) {
            listaCitas.setAdapter(new ListaCitasAdapter(this, list));
            listaCitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    Cita item = (Cita) listaCitas.getAdapter().getItem(pos);
                    llamarActivity(item);

                }
            });

        }else{
            showAlertDialog(this,"Informacion","No posea citas registradas",true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        final ListView listaCitas=(ListView)findViewById(R.id.lvListaCistas);


        DAOApp daoApp=new DAOApp();
        CitaDao citaDao=daoApp.getCitaDao();
        List<Cita> list= citaDao.loadAll();
        if(!list.isEmpty()) {
            listaCitas.setAdapter(new ListaCitasAdapter(this, list));
            listaCitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    Cita item = (Cita) listaCitas.getAdapter().getItem(pos);
                    llamarActivity(item);

                }
            });

        }else{
            showAlertDialog(this,"Informacion","No posea citas registradas",true);
        }
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
//		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }

    public void llamarActivity(Cita item){
        Intent i= new Intent(this, VerCitaActivity.class);
        i.putExtra("id",item.getId());
        i.putExtra("alerta",(long)0);
        startActivityForResult(i,1);
        //startActivity(i);
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
            case R.id.action_settings:
                Intent intent=new Intent(ListaCitasActivity.this, BuscarCitaActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
