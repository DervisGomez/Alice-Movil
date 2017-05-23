package org.app.alice.activity;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.app.alice.R;
import org.app.alice.adapter.BuscarTodoAdapter;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Evento;
import org.app.alice.modelo.EventoDao;
import org.app.alice.modelo.Noticia;
import org.app.alice.modelo.NoticiaDao;
import org.app.alice.modelo.Servicio;
import org.app.alice.modelo.ServicioDao;

import java.util.ArrayList;
import java.util.List;

public class BuscarTodoActivity extends AppCompatActivity {
    EditText editText;
    LinearLayout layaut;
    ListView lista;
    List<Integer> integers=new ArrayList<Integer>();
    List<Object> objects=new ArrayList<Object>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_todo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final EditText buscar=(EditText)findViewById(R.id.etTextoBuscar);
        Button ir=(Button)findViewById(R.id.btnBuscarTodo);
        lista=(ListView)findViewById(R.id.lvListaBusqueda);
        layaut=(LinearLayout)findViewById(R.id.layautBusqueda);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                llamarActiviy(pos);
            }
        });
        lista.setVisibility(View.GONE);

        ir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String busqueda=buscar.getText().toString();

                if(busqueda.length()>0){
                    DAOApp daoApp=new DAOApp();
                    ServicioDao servicioDao=daoApp.getServicioDao();
                    List<Servicio> servicios=servicioDao.loadAll();
                    for(int x=0; x<servicios.size();x++){
                        if(servicios.get(x).getDescripcion().startsWith(busqueda)){
                            objects.add(servicios.get(x));
                            integers.add(1);
                        }
                    }

                    EventoDao eventoDao=daoApp.getEventoDao();
                    List<Evento> eventos=eventoDao.loadAll();
                    for(int x=0; x<eventos.size();x++){
                        if(eventos.get(x).getDescripcion().startsWith(busqueda)){
                            objects.add(eventos.get(x));
                            integers.add(2);
                        }
                    }

                    NoticiaDao noticiaDao=daoApp.getNoticiaDao();
                    List<Noticia> noticias=noticiaDao.loadAll();
                    for(int x=0; x<noticias.size();x++){
                        if(noticias.get(x).getTitulo().startsWith(busqueda)){
                            objects.add(noticias.get(x));
                            integers.add(3);
                        }
                    }
                    if(objects.size()>0){
                        lista.setAdapter(new BuscarTodoAdapter(BuscarTodoActivity.this, objects,integers));
                        lista.setVisibility(View.VISIBLE);
                        layaut.setVisibility(View.GONE);
                    }else{
                        showAlertDialog(BuscarTodoActivity.this,"Busqueda","No se encontraron coincidencias",true);
                    }

                }else{

                }

            }
        });
    }

    public void llamarActiviy(int pos){
        int x=integers.get(pos);
        switch (x){
            case 1:
                Servicio item = (Servicio) objects.get(pos);
                Intent i= new Intent(this, VerEspecialistaActivity.class);
                i.putExtra("id",item.getId());
                i.putExtra("alerta",(long)0);
                startActivity(i);
                break;
            case 2:
                Evento item1 = (Evento) objects.get(pos);
                Intent i1= new Intent(this, VerEventoActivity.class);
                i1.putExtra("id",item1.getId());
                i1.putExtra("alerta",(long)0);
                startActivity(i1);
                break;
            case 3:
                Noticia item2 = (Noticia) objects.get(pos);
                Intent i2= new Intent(this, VerNoticiaActivity.class);
                i2.putExtra("id",item2.getId());
                i2.putExtra("alerta",(long)0);
                startActivity(i2);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            case R.id.action_settings:
                lista.setVisibility(View.GONE);
                layaut.setVisibility(View.VISIBLE);
                integers.clear();
                objects.clear();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

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
}
