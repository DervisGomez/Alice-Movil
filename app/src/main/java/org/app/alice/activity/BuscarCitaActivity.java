package org.app.alice.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.app.alice.R;
import org.app.alice.modelo.Cita;
import org.app.alice.modelo.CitaDao;
import org.app.alice.modelo.DAOApp;
import org.app.alice.service.GlobalService;

import java.util.ArrayList;
import java.util.List;

public class BuscarCitaActivity extends AppCompatActivity {
    private ProgressDialog pd = null;
    GlobalService globals = GlobalService.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_cita);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText cita=(EditText)findViewById(R.id.etCodigoBuscarCita);
        Button consultar=(Button)findViewById(R.id.btnConsultarCita);

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numero=cita.getText().toString();

                if (numero.length()>0){
                    DAOApp daoApp=new DAOApp();
                    CitaDao citaDao=daoApp.getCitaDao();
                    Cita cita1=citaDao.load(Long.valueOf(numero));
                    if(cita1==null){
                        pd = ProgressDialog.show(BuscarCitaActivity.this, "Cita", "Validando Datos...", true, false);
                        new MiTareaGet(globals.getServer()+"citas/"+numero+".json","").execute();
                    }else{
                        Intent i=new Intent(BuscarCitaActivity.this,VerCitaActivity.class);
                        i.putExtra("id",cita1.getId());
                        i.putExtra("alerta",(long)0);
                        startActivity(i);
                    }

                }else{
                    showAlertDialog(BuscarCitaActivity.this, "Campo Vacio", "Debe llenar le campo para buscar una cita", false);
                }
            }
        });

    }

    public void verCita(String datos){
        //showAlertDialog(this,"prueba",datos,true);
        datos = "["+datos+"]";
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    List<Cita> lista=new ArrayList<>();
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        Cita item=new Cita();
                        if (!(objO.get("id").isJsonNull())) {
                            item.setId(objO.get("id").getAsLong());
                        }if (!(objO.get("fecha").isJsonNull())) {
                            item.setFecha(globals.fechaFormat(objO.get("fecha").getAsString()));
                        }if (!(objO.get("persona").isJsonNull())) {
                            JsonObject obj1 = objO.get("persona").getAsJsonObject();
                            if (!(obj1.get("nombre").isJsonNull())) {
                                item.setNombre(obj1.get("nombre").getAsString());
                            }if (!(obj1.get("apellido").isJsonNull())) {
                                item.setApellido(obj1.get("apellido").getAsString());
                            }
                        }
                        if (!(objO.get("estatus").isJsonNull())) {
                            item.setEstatus(objO.get("estatus").getAsInt());
                        }if (!(objO.get("turno").isJsonNull())) {
                            JsonObject obj1 = objO.get("turno").getAsJsonObject();
                            if (!(obj1.get("horario").isJsonNull())) {
                                JsonObject obj2 = obj1.get("horario").getAsJsonObject();
                                if (!(obj2.get("servicio").isJsonNull())) {
                                    JsonObject obj3 = obj2.get("servicio").getAsJsonObject();
                                    if (!(obj3.get("descripcion").isJsonNull())) {
                                        item.setDescripcion(obj3.get("descripcion").getAsString());
                                    }
                                    if (!(obj3.get("precio").isJsonNull())) {
                                        item.setPrecio(obj3.get("precio").getAsDouble());
                                    }
                                    if (!(obj3.get("tipo_servicio_id").isJsonNull())) {
                                        item.setIdServicioCita(obj3.get("tipo_servicio_id").getAsLong());
                                    }if (!(obj3.get("ubicacion").isJsonNull())) {
                                        JsonObject obj4 = obj3.get("ubicacion").getAsJsonObject();
                                        if (!(obj4.get("descripcion").isJsonNull())) {
                                            item.setUbicacion(obj4.get("descripcion").getAsString());
                                        }
                                    }
                                }
                            }
                        }
                        lista.add(item);
                    }
                    if(lista.size()>0){
                        globals.setCita(lista.get(0));
                        Intent i=new Intent(BuscarCitaActivity.this,VerCitaActivity.class);
                        i.putExtra("id",lista.get(0).getId());
                        i.putExtra("alerta",(long)-1);
                        startActivity(i);
                        //showAlertDialog(this,"Cita","Prueba",true);

                    }else{
                        showAlertDialog(BuscarCitaActivity.this,"Cita", "No se encontro ninguna cita con ese codigo",false);
                    }
                }
            }catch (Exception e){
                showAlertDialog(BuscarCitaActivity.this,"Cita", "No se encontro ninguna cita con ese codigo",false);
            }
        }else{
            showAlertDialog(BuscarCitaActivity.this,"Cita", "No se encontro ninguna cita con ese codigo",false);
        }
        if (pd != null) {
            pd.dismiss();
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

    private class MiTareaGet extends AsyncTask<String, Float, String> {
        private String ur;

        public MiTareaGet(String url, String x) {
            Log.d("url", url);
            this.ur = url + x;

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {
            String responce = "";

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet(ur);
            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                responce = EntityUtils.toString(resp.getEntity());
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
            }
            return responce;
        }

        @Override
        protected void onProgressUpdate(Float... valores) {

        }

        @Override//Acción a realizar despues de consumir el servicio
        protected void onPostExecute(String tiraJson) {

            verCita(tiraJson);

        }
    }
}
