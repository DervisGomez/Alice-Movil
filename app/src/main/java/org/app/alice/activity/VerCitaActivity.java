package org.app.alice.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.app.alice.R;
import org.app.alice.adapter.ListaCitasAdapter;
import org.app.alice.modelo.Alerta;
import org.app.alice.modelo.AlertaDao;
import org.app.alice.modelo.CategoriaServicio;
import org.app.alice.modelo.CategoriaServicioDao;
import org.app.alice.modelo.Cita;
import org.app.alice.modelo.CitaDao;
import org.app.alice.modelo.Criterio;
import org.app.alice.modelo.CriterioDao;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Horario;
import org.app.alice.modelo.HorarioDao;
import org.app.alice.modelo.Servicio;
import org.app.alice.modelo.ServicioDao;
import org.app.alice.modelo.TipoServicio;
import org.app.alice.modelo.TipoServicioDao;
import org.app.alice.modelo.Turno;
import org.app.alice.modelo.TurnoDao;
import org.app.alice.modelo.Usuario;
import org.app.alice.modelo.UsuarioDao;
import org.app.alice.service.GlobalService;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brayan on 22/02/17.
 */

public class VerCitaActivity extends AppCompatActivity implements View.OnClickListener{
    Cita item;
    Servicio item2;
    Button accion;
    GlobalService globals = GlobalService.getInstance();
    long id;
    Button cancelar;
    private ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_cita);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bolsa=getIntent().getExtras();
        id=bolsa.getLong("id");
        long alerta=bolsa.getLong("alerta");
        DAOApp daoApp=new DAOApp();
        CitaDao citaDao=daoApp.getCitaDao();
        item=citaDao.load(id);



        if(item==null){
            UsuarioDao usuarioDao=daoApp.getUsuarioDao();
            List<Usuario> usuarios= usuarioDao.loadAll();
            new MiTareaGet(globals.getServer()+"citas.json?persona="+String.valueOf(usuarios.get(0).getId()),"").execute();
        }else{
            if(alerta>0){
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancel((int)alerta);
                AlertaDao alertaDao=daoApp.getAlertaDao();
                Alerta alerta1=alertaDao.load(alerta);
                alerta1.setEstatus(2);
                alertaDao.update(alerta1);
                new MiTareaPut(globals.getServer()+"notificaciones/"+String.valueOf(alerta1.getId())+".json","").execute();

            }


            EditText especialidad = (EditText) findViewById(R.id.etEspecialidad);
            EditText precio = (EditText) findViewById(R.id.et_precio);
            EditText lugar = (EditText) findViewById(R.id.et_lugar);
            EditText fecha = (EditText) findViewById(R.id.et_fecha);
            EditText servicioCita = (EditText) findViewById(R.id.et_servicioCita);
            EditText paciente = (EditText) findViewById(R.id.etPacienteCita);
            cancelar=(Button)findViewById(R.id.btnCancelar);
            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pd = ProgressDialog.show(VerCitaActivity.this, "Cita", "Cancelando cita...", true, false);
                    new MiTareaPut(globals.getServer()+"cancelar_movil/"+String.valueOf(id)+".json","").execute();
                }
            });
            accion=(Button)findViewById(R.id.btnAccion);
            accion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(VerCitaActivity.this,ValorarServicioActivity.class);
                    intent.putExtra("id",item2.getId());
                    intent.putExtra("cita",item.getId());
                    //startActivity(intent);
                    startActivityForResult(intent,1);
                }
            });
            if (item==null&&alerta==-1){
                item=globals.getCita();
                cancelar.setVisibility(View.GONE);
                accion.setVisibility(View.GONE);
            }else{
                switch (item.getEstatus()){
                    case 1:
                        accion.setVisibility(View.GONE);
                        break;
                    case 3:
                        cancelar.setVisibility(View.GONE);
                        break;
                    default:
                        cancelar.setVisibility(View.GONE);
                        accion.setVisibility(View.GONE);
                        break;
                }
            }
            especialidad.setText(item.getDescripcion());
            fecha.setText(item.getFecha());
            lugar.setText(item.getUbicacion());
            paciente.setText(item.getNombre()+" "+item.getApellido());
            precio.setText(String.valueOf(item.getPrecio()));
            ServicioDao servicioDao=daoApp.getServicioDao();
            item2=servicioDao.load(item.getIdServicioCita());
            servicioCita.setText(item2.getDescripcion());
        }


        /*if(item.getEstatus()=="3"){
            accion.setVisibility(View.VISIBLE);
        }
        if(item.getEstatus()=="1"){
            cancelar.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        final ListView listaCitas=(ListView)findViewById(R.id.lvListaCistas);
        Bundle bolsa=getIntent().getExtras();
        id=bolsa.getLong("id");
        DAOApp daoApp=new DAOApp();
        CitaDao citaDao=daoApp.getCitaDao();
        item=citaDao.load(id);
        switch (item.getEstatus()){
            case 1:
                accion.setVisibility(View.GONE);
                break;
            case 3:
                cancelar.setVisibility(View.GONE);
                break;
            default:
                cancelar.setVisibility(View.GONE);
                accion.setVisibility(View.GONE);
                break;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAccion:
                Intent intent=new Intent(VerCitaActivity.this,ValorarServicioActivity.class);
                intent.putExtra("id",item2.getId());
                intent.putExtra("cita",item.getId());
                startActivity(intent);
                finish();
            case R.id.btnCancelar:
                //try {


                /*} catch (JSONException e) {
                    Log.d("Error" , e.getMessage());
                    e.printStackTrace();
                }
                */
        }//cita_id
    }

    public void reiniciarTodo(){
        Bundle bolsa=getIntent().getExtras();
        id=bolsa.getLong("id");
        long alerta=bolsa.getLong("alerta");
        DAOApp daoApp=new DAOApp();
        CitaDao citaDao=daoApp.getCitaDao();
        item=citaDao.load(id);



        if(item==null){

        }else{
            if(alerta>0){
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancel((int)alerta);
                AlertaDao alertaDao=daoApp.getAlertaDao();
                Alerta alerta1=alertaDao.load(alerta);
                alerta1.setEstatus(2);
                alertaDao.update(alerta1);
                new MiTareaPut(globals.getServer()+"notificaciones/"+String.valueOf(alerta1.getId())+".json","").execute();

            }


            EditText especialidad = (EditText) findViewById(R.id.etEspecialidad);
            EditText precio = (EditText) findViewById(R.id.et_precio);
            EditText lugar = (EditText) findViewById(R.id.et_lugar);
            EditText fecha = (EditText) findViewById(R.id.et_fecha);
            EditText servicioCita = (EditText) findViewById(R.id.et_servicioCita);
            EditText paciente = (EditText) findViewById(R.id.etPacienteCita);
            cancelar=(Button)findViewById(R.id.btnCancelar);
            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pd = ProgressDialog.show(VerCitaActivity.this, "Cita", "Cancelando cita...", true, false);
                    new MiTareaPut(globals.getServer()+"cancelar_movil/"+String.valueOf(id)+".json","").execute();
                }
            });
            accion=(Button)findViewById(R.id.btnAccion);
            accion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(VerCitaActivity.this,ValorarServicioActivity.class);
                    intent.putExtra("id",item2.getId());
                    intent.putExtra("cita",item.getId());
                    //startActivity(intent);
                    startActivityForResult(intent,1);
                }
            });
            if (item==null&&alerta==-1){
                item=globals.getCita();
                cancelar.setVisibility(View.GONE);
                accion.setVisibility(View.GONE);
            }else{
                switch (item.getEstatus()){
                    case 1:
                        accion.setVisibility(View.GONE);
                        break;
                    case 3:
                        cancelar.setVisibility(View.GONE);
                        break;
                    default:
                        cancelar.setVisibility(View.GONE);
                        accion.setVisibility(View.GONE);
                        break;
                }
            }
            especialidad.setText(item.getDescripcion());
            fecha.setText(item.getFecha());
            lugar.setText(item.getUbicacion());
            paciente.setText(item.getNombre()+" "+item.getApellido());
            precio.setText(String.valueOf(item.getPrecio()));
            ServicioDao servicioDao=daoApp.getServicioDao();
            item2=servicioDao.load(item.getIdServicioCita());
            servicioCita.setText(item2.getDescripcion());
        }
    }

    public void cargarCitas(String datos){
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
                    DAOApp daoApp=new DAOApp();
                    CitaDao dao=daoApp.getCitaDao();
                    dao.deleteAll();
                    dao.insertInTx(lista);
                    //showAlertDialog(this,"Cita",String.valueOf(dao.count()),true);

                }
            }catch (Exception e){
                showAlertDialog(this, "Citas", e.toString(),false);
            }
        }else{
            DAOApp daoApp=new DAOApp();
            CitaDao dao=daoApp.getCitaDao();
            dao.deleteAll();
        }
        reiniciarTodo();
        //finish();
    }

    private class MiTareaPut extends AsyncTask<String, Float, String> {
        private String jsonObject;
        private final String HTTP_EVENT;
        private HttpClient httpclient;
        BufferedReader in = null;
        public MiTareaPut(String url,String jsonObject){

            Log.d("url", url);
            Log.d("JSON",jsonObject);

            this.HTTP_EVENT=url;
            this.jsonObject=jsonObject;
        }
        protected void onPreExecute() {

        }

        protected String doInBackground(String... urls){
            String resul = "";
            try {

                //Creamos un objeto Cliente HTTP para manejar la peticion al servidor
                HttpClient httpClient = new DefaultHttpClient();
                //Creamos objeto para armar peticion de tipo HTTP POST
                HttpPut pust = new HttpPut(HTTP_EVENT);

                //Configuramos los parametos que vaos a enviar con la peticion HTTP POST
                pust.setHeader("Content-type", "application/json");
                pust.setEntity(new StringEntity(jsonObject, HTTP.UTF_8));

                //Se ejecuta el envio de la peticion y se espera la respuesta de la misma.
                HttpResponse response = httpClient.execute(pust);
//   			Log.w(APP_TAG, response.getStatusLine().toString());

                //Obtengo el contenido de la respuesta en formato InputStream Buffer y la paso a formato String
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                resul=sb.toString();

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resul;
        }

        protected void onProgressUpdate (Float... valores) {

        }

        protected void onPostExecute(String datos) {
            item.setEstatus(5);
            DAOApp daoApp=new DAOApp();
            CitaDao citaDao=daoApp.getCitaDao();
            citaDao.update(item);
            showAlertDialog(VerCitaActivity.this,"Cita","Cita cancelada exitosamente",true);
            cancelar.setVisibility(View.GONE);
            if (pd != null) {
                pd.dismiss();
            }
        }
        private StringBuilder inputStreamToString(InputStream is) {
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader rd = new BufferedReader( new InputStreamReader(is) );
            try{
                while( (line = rd.readLine()) != null ){
                    stringBuilder.append(line);
                }
            }catch( IOException e){
                e.printStackTrace();
            }
            return stringBuilder;
        }
    }

    private class MiTareaGet extends AsyncTask<String, Float, String> {
        private String ur;

        public MiTareaGet(String url,String x){
            Log.d("url",url);
            this.ur=url+x;

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

            try
            {
                HttpResponse resp = httpClient.execute(del);
                responce = EntityUtils.toString(resp.getEntity());
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
            }
            return responce;
        }

        @Override
        protected void onProgressUpdate (Float... valores) {

        }

        @Override//Acción a realizar despues de consumir el servicio
        protected void onPostExecute(String tiraJson) {
            cargarCitas(tiraJson);

        }
    }
}
