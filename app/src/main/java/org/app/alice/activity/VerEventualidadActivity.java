package org.app.alice.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.app.alice.R;
import org.app.alice.modelo.Alerta;
import org.app.alice.modelo.AlertaDao;
import org.app.alice.modelo.CitaDao;
import org.app.alice.modelo.DAOApp;
import org.app.alice.service.GlobalService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by brayan on 15/03/17.
 */

public class VerEventualidadActivity extends AppCompatActivity {

    GlobalService globals = GlobalService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventualidad);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bolsa=getIntent().getExtras();
        long alerta=bolsa.getLong("alerta");
        DAOApp daoApp=new DAOApp();

        if(alerta>0){
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel((int)alerta);
            AlertaDao alertaDao=daoApp.getAlertaDao();
            Alerta alerta1=alertaDao.load(alerta);
            alerta1.setEstatus(2);
            alertaDao.update(alerta1);

            TextView descripcion=(TextView)findViewById(R.id.tvdescripcionEventualidad);
            TextView mensaje=(TextView)findViewById(R.id.tvMensajeEventualidad);
            TextView fecha=(TextView)findViewById(R.id.tvFechaEventualidad);

            descripcion.setText(alerta1.getDescripcion());
            mensaje.setText(alerta1.getMensaje());
            fecha.setText(alerta1.getFecha());

            new MiTareaPut(globals.getServer()+"notificaciones/"+String.valueOf(alerta1.getId())+".json","").execute();

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
}