package org.app.alice.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import org.app.alice.modelo.Alerta;
import org.app.alice.modelo.AlertaDao;
import org.app.alice.modelo.CategoriaServicio;
import org.app.alice.modelo.CategoriaServicioDao;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brayan on 12/03/17.
 */

public class VerEspecialistaActivity extends AppCompatActivity {
    Servicio item;
    GlobalService globals = GlobalService.getInstance();
    ImageView imageServicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_especialista);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bolsa=getIntent().getExtras();
        long id=bolsa.getLong("id");
        long alerta=bolsa.getLong("alerta");
        DAOApp daoApp=new DAOApp();
        ServicioDao servicioDao=daoApp.getServicioDao();
        item=servicioDao.load(id);

        if(item==null){
            new MiTareaGet(globals.getServer()+"categorias.json","").execute();
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

            TextView descripcion=(TextView)findViewById(R.id.tvDescripcionServicioDetalle);
            TextView precio=(TextView)findViewById(R.id.tvPrecioServicioDetalle);
            TextView lugar=(TextView)findViewById(R.id.tvUbicacionServicioDetalle);
            TextView persona=(TextView)findViewById(R.id.tvPersonaServicioDetalle);
            Button horario=(Button)findViewById(R.id.btnHorarioServicio);
            imageServicio =(ImageView)findViewById(R.id.imageServicio);
            horario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(VerEspecialistaActivity.this, VerHorarioActivity.class);
                    i.putExtra("id",item.getId());
                    i.putExtra("titulo",item.getDescripcion());
                    startActivity(i);
                }
            });

            persona.setText("Doctor: "+item.getPersona());
            lugar.setText("Lugar: "+item.getUbicacion());
            precio.setText("Precio: "+String.valueOf(item.getPrecio()));
            descripcion.setText(item.getDescripcion());
            new DownloadImageTask().execute(item.getFoto());
        }


    }

    public void cargarServicios(String datos){
        if(datos.length()>2) {
            try {
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array = (JsonArray) obje;
                if (!array.isJsonNull()) {
                    List<CategoriaServicio> lista = new ArrayList<>();
                    List<TipoServicio> items2 = new ArrayList<>();
                    List<Servicio> items3 = new ArrayList<>();
                    List<Horario> items4 = new ArrayList<>();
                    List<Turno> items5 = new ArrayList<>();
                    List<Criterio> items6 = new ArrayList<>();
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        CategoriaServicio item = new CategoriaServicio();
                        if (!(objO.get("id").isJsonNull())) {
                            item.setId(objO.get("id").getAsLong());
                        }
                        if (!(objO.get("descripcion").isJsonNull())) {
                            item.setDescripcion(objO.get("descripcion").getAsString());
                        }
                        if (!(objO.get("estatus").isJsonNull())) {
                            item.setEstatus(objO.get("estatus").getAsInt());
                        }
                        if (!(objO.get("tipo_servicios").isJsonNull())) {
                            JsonArray array1 = objO.get("tipo_servicios").getAsJsonArray();
                            if (!array1.isJsonNull()) {

                                for (int i = 0; i < array1.size(); i++) {
                                    JsonObject obj2 = array1.get(i).getAsJsonObject();
                                    TipoServicio item2 = new TipoServicio();
                                    if (!(obj2.get("id").isJsonNull())) {
                                        item2.setId(obj2.get("id").getAsLong());
                                    }
                                    if (!(obj2.get("descripcion").isJsonNull())) {
                                        item2.setDescripcion(obj2.get("descripcion").getAsString());
                                    }
                                    if (!(obj2.get("texto").isJsonNull())) {
                                        item2.setTexto(obj2.get("texto").getAsString());
                                    }
                                    if (!(obj2.get("estatus").isJsonNull())) {
                                        item2.setEstatus(obj2.get("estatus").getAsInt());
                                    }
                                    if (!(obj2.get("criterios").isJsonNull())) {
                                        JsonArray array2 = obj2.get("criterios").getAsJsonArray();
                                        if (!array2.isJsonNull()) {
                                            for (int y = 0; y < array2.size(); y++) {
                                                JsonObject obj3 = array2.get(y).getAsJsonObject();
                                                Criterio item3 = new Criterio();
                                                if (!(obj3.get("id").isJsonNull())) {
                                                    item3.setCodigo(obj3.get("id").getAsLong());
                                                }
                                                if (!(obj3.get("descripcion").isJsonNull())) {
                                                    item3.setDescripcion(obj3.get("descripcion").getAsString());
                                                }
                                                if (!(obj3.get("estatus").isJsonNull())) {
                                                    item3.setEstatus(obj3.get("estatus").getAsInt());
                                                }
                                                if (!(obj3.get("tipo_criterio").isJsonNull())) {
                                                    JsonObject obj4 = obj3.get("tipo_criterio").getAsJsonObject();
                                                    if (!(obj4.get("descripcion").isJsonNull())) {
                                                        item3.setTipoCriterio(obj4.get("descripcion").getAsString());
                                                    }
                                                }
                                                item3.setValor(3);
                                                item3.setIdTipoServicioCriterio(obj2.get("id").getAsLong());
                                                items6.add(item3);
                                            }
                                        }
                                    }
                                    if (!(obj2.get("servicios").isJsonNull())) {
                                        JsonArray array2 = obj2.get("servicios").getAsJsonArray();
                                        if (!array2.isJsonNull()) {
                                            for (int y = 0; y < array2.size(); y++) {
                                                JsonObject obj3 = array2.get(y).getAsJsonObject();
                                                Servicio item3 = new Servicio();
                                                if (!(obj3.get("id").isJsonNull())) {
                                                    item3.setId(obj3.get("id").getAsLong());
                                                }
                                                if (!(obj3.get("descripcion").isJsonNull())) {
                                                    item3.setDescripcion(obj3.get("descripcion").getAsString());
                                                }
                                                if (!(obj3.get("precio").isJsonNull())) {
                                                    item3.setPrecio(obj3.get("precio").getAsDouble());
                                                }
                                                if (!(obj3.get("foto_url").isJsonNull())) {
                                                    item3.setFoto(obj3.get("foto_url").getAsString());
                                                }
                                                if (!(obj3.get("estatus").isJsonNull())) {
                                                    item3.setEstatus(obj3.get("estatus").getAsInt());
                                                }
                                                if (!(obj3.get("especialista_id").isJsonNull())) {
                                                    JsonObject obj4 = obj3.get("especialista").getAsJsonObject();
                                                    if (!(obj4.get("persona").isJsonNull())) {
                                                        JsonObject obj5 = obj4.get("persona").getAsJsonObject();
                                                        String persona = "";
                                                        if (!(obj5.get("nombre").isJsonNull())) {
                                                            persona = obj5.get("nombre").getAsString() + " ";
                                                        }
                                                        if (!(obj5.get("apellido").isJsonNull())) {
                                                            persona = "- " + persona + obj5.get("apellido").getAsString() + " ";
                                                        }
                                                        item3.setPersona(persona);
                                                    }
                                                }
                                                if (!(obj3.get("ubicacion").isJsonNull())) {
                                                    JsonObject obj4 = obj3.get("ubicacion").getAsJsonObject();
                                                    if (!(obj4.get("descripcion").isJsonNull())) {
                                                        item3.setUbicacion(obj4.get("descripcion").getAsString());
                                                    }
                                                }

                                                if (!(obj3.get("horarios").isJsonNull())) {
                                                    JsonArray array3 = obj3.get("horarios").getAsJsonArray();
                                                    if (!array3.isJsonNull()) {

                                                        for (int j = 0; j < array3.size(); j++) {
                                                            JsonObject obj4 = array3.get(j).getAsJsonObject();
                                                            Horario item4 = new Horario();
                                                            if (!(obj4.get("id").isJsonNull())) {
                                                                item4.setId(obj4.get("id").getAsLong());
                                                            }
                                                            if (!(obj4.get("servicio_id").isJsonNull())) {
                                                                item4.setIdServicioHorario(obj4.get("servicio_id").getAsLong());
                                                            }
                                                            if (!(obj4.get("estatus").isJsonNull())) {
                                                                item4.setEstatus(obj4.get("estatus").getAsInt());
                                                            }

                                                            if (!(obj4.get("turnos").isJsonNull())) {
                                                                JsonArray array4 = obj4.get("turnos").getAsJsonArray();
                                                                if (!array4.isJsonNull()) {

                                                                    for (int l = 0; l < array4.size(); l++) {
                                                                        JsonObject obj5 = array4.get(l).getAsJsonObject();
                                                                        Turno item5 = new Turno();
                                                                        if (!(obj5.get("id").isJsonNull())) {
                                                                            item5.setId(obj5.get("id").getAsLong());
                                                                        }
                                                                        if (!(obj5.get("horario_id").isJsonNull())) {
                                                                            item5.setIdHorarioTurno(obj5.get("horario_id").getAsLong());
                                                                        }
                                                                        if (!(obj5.get("estatus").isJsonNull())) {
                                                                            item5.setEstatus(obj5.get("estatus").getAsInt());
                                                                        }
                                                                        if (!(obj5.get("hora_inicio").isJsonNull())) {
                                                                            item5.setInicio(obj5.get("hora_inicio").getAsString());
                                                                        }
                                                                        if (!(obj5.get("hora_fin").isJsonNull())) {
                                                                            item5.setFin(obj5.get("hora_fin").getAsString());
                                                                        }
                                                                        if (!(obj5.get("tipo_turno_id").isJsonNull())) {
                                                                            item5.setTipo(obj5.get("tipo_turno_id").getAsInt());
                                                                        }
                                                                        if (!(obj5.get("dia_id").isJsonNull())) {
                                                                            item5.setDia(obj5.get("dia_id").getAsInt());
                                                                        }
                                                                        items5.add(item5);
                                                                    }

                                                                }
                                                            }

                                                            items4.add(item4);
                                                        }

                                                    }
                                                }

                                                item3.setIdTipoServicio(obj2.get("id").getAsLong());
                                                items3.add(item3);
                                            }

                                        }
                                    }
                                    item2.setIdCategoriaServicio(objO.get("id").getAsLong());
                                    items2.add(item2);
                                }
                            }
                        }
                        lista.add(item);
                    }
                    DAOApp daoApp = new DAOApp();
                    CategoriaServicioDao catedao = daoApp.getCategoriaServicioDao();
                    catedao.deleteAll();
                    catedao.insertInTx(lista);
                    //showAlertDialog(this,"Categoria",String.valueOf(dao.count()),true);

                    TipoServicioDao tsdao = daoApp.getTipoServicioDao();
                    tsdao.deleteAll();
                    tsdao.insertInTx(items2);
                    //showAlertDialog(this,"tipo servicios",String.valueOf(dao.count()),true);

                    ServicioDao sdao = daoApp.getServicioDao();
                    sdao.deleteAll();
                    sdao.insertInTx(items3);
                    //showAlertDialog(this,"servicios",String.valueOf(dao.count()),true);

                    HorarioDao hdao = daoApp.getHorarioDao();
                    hdao.deleteAll();
                    hdao.insertInTx(items4);
                    //showAlertDialog(this,"Horarios",String.valueOf(dao.count()),true);


                    TurnoDao tdao = daoApp.getTurnoDao();
                    tdao.deleteAll();
                    tdao.insertInTx(items5);
                    //showAlertDialog(this,"Turnos",String.valueOf(dao.count()),true);


                    CriterioDao cdao = daoApp.getCriterioDao();
                    cdao.deleteAll();
                    cdao.insertInTx(items6);
                    //showAlertDialog(this,"Criterios",String.valueOf(dao.count()),true);


                }
            } catch (Exception e) {
                showAlertDialog(this, "Error", e.toString(), false);
            }
        }else {
            DAOApp daoApp = new DAOApp();
            CategoriaServicioDao catedao = daoApp.getCategoriaServicioDao();
            catedao.deleteAll();

            TipoServicioDao tsdao = daoApp.getTipoServicioDao();
            tsdao.deleteAll();

            ServicioDao sdao = daoApp.getServicioDao();
            sdao.deleteAll();

            HorarioDao hdao = daoApp.getHorarioDao();
            hdao.deleteAll();

            TurnoDao tdao = daoApp.getTurnoDao();
            tdao.deleteAll();

            CriterioDao cdao = daoApp.getCriterioDao();
            cdao.deleteAll();
        }
        reiniciarTodo();
    }

    public void reiniciarTodo(){
        Bundle bolsa=getIntent().getExtras();
        long id=bolsa.getLong("id");
        long alerta=bolsa.getLong("alerta");
        DAOApp daoApp=new DAOApp();
        ServicioDao servicioDao=daoApp.getServicioDao();
        item=servicioDao.load(id);

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

            TextView descripcion=(TextView)findViewById(R.id.tvDescripcionServicioDetalle);
            TextView precio=(TextView)findViewById(R.id.tvPrecioServicioDetalle);
            TextView lugar=(TextView)findViewById(R.id.tvUbicacionServicioDetalle);
            TextView persona=(TextView)findViewById(R.id.tvPersonaServicioDetalle);
            Button horario=(Button)findViewById(R.id.btnHorarioServicio);
            imageServicio =(ImageView)findViewById(R.id.imageServicio);
            horario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(VerEspecialistaActivity.this, VerHorarioActivity.class);
                    i.putExtra("id",item.getId());
                    i.putExtra("titulo",item.getDescripcion());
                    startActivity(i);
                }
            });

            persona.setText("Doctor: "+item.getPersona());
            lugar.setText("Lugar: "+item.getUbicacion());
            precio.setText("Precio: "+String.valueOf(item.getPrecio()));
            descripcion.setText(item.getDescripcion());
            new DownloadImageTask().execute(item.getFoto());
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

    class DownloadImageTask extends AsyncTask<String, Void, Drawable>
    {

        final ProgressDialog progressDialog = new ProgressDialog(VerEspecialistaActivity.this);

        protected void onPreExecute()
        {
            progressDialog.setTitle("");
            progressDialog.setMessage("Cargando imagen...");
            progressDialog.show();
        }

        protected Drawable doInBackground(String... urls)
        {
            Log.d("DEBUG", "drawable");

            return downloadImage(urls[0]);

        }

        protected void onPostExecute(Drawable imagen)
        {

            imageServicio.setImageDrawable(imagen);
            progressDialog.dismiss();
        }

        /**
         * Devuelve una imagen desde una URL
         * @return Una imagen
         */
        private Drawable downloadImage(String imageUrl)
        {
            try
            {
                URL url = new URL(imageUrl);
                InputStream is = (InputStream)url.getContent();
                return Drawable.createFromStream(is, "src");
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                return null;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }
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
            //actualizarNotificacion(datos);
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
            cargarServicios(tiraJson);

        }
    }
}
