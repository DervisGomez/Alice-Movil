package org.app.alice.activity;

import android.app.AlertDialog;
import android.app.Notification;
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
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.app.alice.R;
import org.app.alice.modelo.Alerta;
import org.app.alice.modelo.AlertaDao;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Evento;
import org.app.alice.modelo.EventoDao;
import org.app.alice.modelo.NoticiaDao;
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
 * Created by brayan on 22/02/17.
 */

public class VerEventoActivity extends AppCompatActivity {
    Evento item;
    GlobalService globals = GlobalService.getInstance();
    ImageView imageEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_evento);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bolsa=getIntent().getExtras();
        long id=bolsa.getLong("id");
        long alerta=bolsa.getLong("alerta");
        DAOApp daoApp=new DAOApp();
        EventoDao eventoDao=daoApp.getEventoDao();
        item=eventoDao.load(id);

        TextView titulo=(TextView)findViewById(R.id.tvTituloEventoDetalle);
        TextView fecha=(TextView)findViewById(R.id.tvFechaEventoDetalle);
        WebView contenido=(WebView)findViewById(R.id.tvContenidoEventoDetalle);
        TextView tipo=(TextView)findViewById(R.id.tvTipoEventoDetalle);
        //TextView servicio=(TextView)findViewById(R.id.tvTipoServicioEventoDetalle);
        TextView lugar=(TextView)findViewById(R.id.tvLugarEventoDetalle);
        imageEvento =(ImageView)findViewById(R.id.imageEvento);

        if(alerta>0){
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel((int)alerta);
            AlertaDao alertaDao=daoApp.getAlertaDao();
            Alerta alerta1=alertaDao.load(alerta);
            alerta1.setEstatus(2);
            alertaDao.update(alerta1);
            new MiTareaPut(globals.getServer()+"notificaciones/"+String.valueOf(alerta1.getId())+".json","").execute();

        }

        if(item==null){
            new MiTareaGet(globals.getServer()+"eventos.json","").execute();
        }else{
            titulo.setText(item.getDescripcion());
            fecha.setText(item.getFecha());
            contenido.loadData(item.getContenido(),"","");
            tipo.setText(item.getTipoServicio().substring(0, item.getTipoServicio().length()-1)+"  -  "+item.getTipoEvento());
            //servicio.setText(item.getTipoServicio()+"  -  "+item.getTipoEvento());
            lugar.setText(item.getLugar());
            new DownloadImageTask().execute(item.getFoto());
        }

    }

    public void reiniciarTodo(){
        Bundle bolsa=getIntent().getExtras();
        long id=bolsa.getLong("id");
        long alerta=bolsa.getLong("alerta");
        DAOApp daoApp=new DAOApp();
        EventoDao eventoDao=daoApp.getEventoDao();
        item=eventoDao.load(id);

        TextView titulo=(TextView)findViewById(R.id.tvTituloEventoDetalle);
        TextView fecha=(TextView)findViewById(R.id.tvFechaEventoDetalle);
        WebView contenido=(WebView)findViewById(R.id.tvContenidoEventoDetalle);
        TextView tipo=(TextView)findViewById(R.id.tvTipoEventoDetalle);
        //TextView servicio=(TextView)findViewById(R.id.tvTipoServicioEventoDetalle);
        TextView lugar=(TextView)findViewById(R.id.tvLugarEventoDetalle);
        imageEvento =(ImageView)findViewById(R.id.imageEvento);

        if(alerta>0){
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel((int)alerta);
            AlertaDao alertaDao=daoApp.getAlertaDao();
            Alerta alerta1=alertaDao.load(alerta);
            alerta1.setEstatus(2);
            alertaDao.update(alerta1);
            new MiTareaPut(globals.getServer()+"notificaciones/"+String.valueOf(alerta1.getId())+".json","").execute();

        }

        if(item==null){
            showAlertDialog(this,"Cita","Esta cita se ha eliminado",true);
        }else{
            titulo.setText(item.getDescripcion());
            fecha.setText(item.getFecha());
            contenido.loadData(item.getContenido(),"","");
            tipo.setText(item.getTipoServicio().substring(0, item.getTipoServicio().length()-1)+"  -  "+item.getTipoEvento());
            //servicio.setText(item.getTipoServicio()+"  -  "+item.getTipoEvento());
            lugar.setText(item.getLugar());
            new DownloadImageTask().execute(item.getFoto());
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

    public void cargarEventos(String datos){
        //showAlertDialog(this,"Eventos",datos,true);
        if(datos.length()>2) {
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    List<Evento> lista=new ArrayList<>();
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        Evento item=new Evento();
                        if (!(objO.get("id").isJsonNull())) {
                            item.setCodigo(objO.get("id").getAsInt());
                        }
                        item.setTitulo("Titulo");
                        if (!(objO.get("descripcion").isJsonNull())) {
                            item.setDescripcion(objO.get("descripcion").getAsString());
                        }if (!(objO.get("estatus").isJsonNull())) {
                            item.setEstatus(objO.get("estatus").getAsInt());
                        }if (!(objO.get("contenido").isJsonNull())) {
                            item.setContenido(objO.get("contenido").getAsString());
                        }if (!(objO.get("foto_url").isJsonNull())) {
                            item.setFoto(objO.get("foto_url").getAsString());
                        }if (!(objO.get("created_at").isJsonNull())) {
                            item.setFecha(globals.fechaFormat(objO.get("created_at").getAsString()));
                        }if (!(objO.get("tipo_evento").isJsonNull())) {
                            JsonObject obj1 = objO.get("tipo_evento").getAsJsonObject();
                            if (!(obj1.get("descripcion").isJsonNull())) {
                                item.setTipoEvento(obj1.get("descripcion").getAsString());
                            }
                        }if (!(objO.get("ubicacion").isJsonNull())) {
                            JsonObject obj1 = objO.get("ubicacion").getAsJsonObject();
                            if (!(obj1.get("descripcion").isJsonNull())) {
                                item.setLugar(obj1.get("descripcion").getAsString());
                            }
                        }if (!(objO.get("tipo_servicios").isJsonNull())) {
                            JsonArray array1 = objO.get("tipo_servicios").getAsJsonArray();
                            if (!array1.isJsonNull()) {
                                String tipo="";
                                for (int i=0;i<array1.size(); i++){
                                    JsonObject obj2 = array1.get(i).getAsJsonObject();
                                    if (!(obj2.get("descripcion").isJsonNull())) {
                                        tipo=tipo+obj2.get("descripcion").getAsString()+"\n";
                                    }
                                }
                                item.setTipoServicio(tipo);
                            }
                        }
                        lista.add(item);
                    }
                    DAOApp daoApp=new DAOApp();
                    EventoDao dao=daoApp.getEventoDao();
                    dao.deleteAll();
                    dao.insertInTx(lista);
                    //showAlertDialog(this,"EVENTO",String.valueOf(dao.count()),true);


                }
            }catch (Exception e){
                showAlertDialog(this, "Error", e.toString(),false);
            }
        }else{
            DAOApp daoApp=new DAOApp();
            EventoDao dao=daoApp.getEventoDao();
            dao.deleteAll();
        }
        reiniciarTodo();

    }

    public void actualizarNotificacion(String datos){
        Bundle bolsa=getIntent().getExtras();
        long alerta=bolsa.getLong("alerta");
        DAOApp daoApp=new DAOApp();
        AlertaDao alertaDao=daoApp.getAlertaDao();
        Alerta alerta1=alertaDao.load(alerta);
        alerta1.setEstatus(2);
        alertaDao.update(alerta1);
        showAlertDialog(this,"prueba",datos,true);
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

    class DownloadImageTask extends AsyncTask<String, Void, Drawable>
    {

        final ProgressDialog progressDialog = new ProgressDialog(VerEventoActivity.this);

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

            imageEvento.setImageDrawable(imagen);
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
            cargarEventos(tiraJson);

        }
    }
}
