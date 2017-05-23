package org.app.alice.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.app.alice.R;
import org.app.alice.adapter.ListaCitasAdapter;
import org.app.alice.adapter.ListaNotificacionAdapter;
import org.app.alice.modelo.Alerta;
import org.app.alice.modelo.AlertaDao;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.TipoServicio;

import java.util.ArrayList;
import java.util.List;

public class ListaNotificacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notificacion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ListView listaCitas=(ListView)findViewById(R.id.lvListaNotificacion);
        DAOApp daoApp=new DAOApp();
        AlertaDao alertaDao=daoApp.getAlertaDao();
        List<Alerta> list=alertaDao.loadAll();
        if(!list.isEmpty()) {
            listaCitas.setAdapter(new ListaNotificacionAdapter(this, list));
            listaCitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    Alerta item = (Alerta) listaCitas.getAdapter().getItem(pos);
                    llamarActivity(item);
                }
            });
        }else{
            showAlertDialog(this,"Informacion","No tiene notificaciones",true);
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
    public void llamarActivity(Alerta item){
        int x=item.getTipo_notificacion_id();
        switch (x){
            case 1:
                Intent i= new Intent(this, VerCitaActivity.class);
                i.putExtra("id",item.getEntidad_id());
                i.putExtra("alerta",(long)0);
                startActivity(i);
                break;
            case 2:
                Intent i1= new Intent(this, VerEspecialistaActivity.class);
                i1.putExtra("id",item.getEntidad_id());
                i1.putExtra("alerta",(long)0);
                startActivity(i1);
                break;
            case 3:
                Intent i2= new Intent(this, VerEventoActivity.class);
                i2.putExtra("id",item.getEntidad_id());
                i2.putExtra("alerta",(long)0);
                startActivity(i2);
            case 4:
                Intent i3= new Intent(this, VerEventualidadActivity.class);
                i3.putExtra("id",item.getEntidad_id());
                i3.putExtra("alerta",(long)0);
                startActivity(i3);
        }


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
