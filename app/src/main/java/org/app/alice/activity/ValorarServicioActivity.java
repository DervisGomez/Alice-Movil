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
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.app.alice.R;
import org.app.alice.adapter.ListaValoracionAdapter;
import org.app.alice.modelo.Cita;
import org.app.alice.modelo.CitaDao;
import org.app.alice.modelo.Criterio;
import org.app.alice.modelo.CriterioDao;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Servicio;
import org.app.alice.modelo.ServicioDao;
import org.app.alice.modelo.TipoServicio;
import org.app.alice.modelo.TipoServicioDao;
import org.app.alice.service.GlobalService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ValorarServicioActivity extends AppCompatActivity implements View.OnClickListener{

    Button siguiente;
    GlobalService globals = GlobalService.getInstance();
    private ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valorar_servicio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        siguiente=(Button)findViewById(R.id.btnSiguiente);
        siguiente.setOnClickListener(this);

        Bundle bolsa=getIntent().getExtras();
        long id=bolsa.getLong("id");

        DAOApp daoApp=new DAOApp();
        ServicioDao servicioDao=daoApp.getServicioDao();
        Servicio servicio=servicioDao.load(id);
        TipoServicioDao tipoServicioDao=daoApp.getTipoServicioDao();
        TipoServicio tipoServicio=tipoServicioDao.load(servicio.getIdTipoServicio());
        CriterioDao criterioDao=daoApp.getCriterioDao();

        //AlertaDao alertaDao=daoApp.getAlertaDao();
        //List<Alerta> list=alertaDao.loadAll();

        ListView listaCitas=(ListView)findViewById(R.id.lvCriterios);
        List<Criterio> list=criterioDao._queryTipoServicio_Criterio(tipoServicio.getId());

        listaCitas.setAdapter(new ListaValoracionAdapter(this, list));

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
        switch (view.getId()) {
            case R.id.btnSiguiente:
                pd = ProgressDialog.show(this, "Valoracion", "Enviando Datos...", true, false);
                Bundle bolsa=getIntent().getExtras();
                long id=bolsa.getLong("id");
                long cita=bolsa.getLong("cita");
                DAOApp daoApp=new DAOApp();
                ServicioDao servicioDao=daoApp.getServicioDao();
                Servicio servicio=servicioDao.load(id);
                TipoServicioDao tipoServicioDao=daoApp.getTipoServicioDao();
                TipoServicio tipoServicio=tipoServicioDao.load(servicio.getIdTipoServicio());
                CriterioDao criterioDao=daoApp.getCriterioDao();
                List<Criterio> list=criterioDao._queryTipoServicio_Criterio(tipoServicio.getId());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("cita_id",cita);
                    JSONArray array = new JSONArray();
                    for(int x=0;x<list.size();x++){
                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("criterio_id",list.get(x).getCodigo());
                        jsonObject2.put("estatus",list.get(x).getEstatus());
                        jsonObject2.put("tipo_calificacion_id",1);
                        jsonObject2.put("descripcion",list.get(x).getValor().toString());
                        array.put(jsonObject2);
                    }
                    jsonObject.put("calificaciones",array);
                    //showAlertDialog(ValorarServicioActivity.this,"Error11",jsonObject.toString(),true);
                    new MiTareaPost(globals.getServer()+"evaluaciones.json",jsonObject.toString()).execute();

                } catch (JSONException e) {
                    showAlertDialog(ValorarServicioActivity.this,"Error",jsonObject.toString(),true);
                }

        }
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
//		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.show();
    }

    private class MiTareaPost extends AsyncTask<String, Float, String> {
        private String jsonObject;
        private final String HTTP_EVENT;
        private HttpClient httpclient;
        BufferedReader in = null;
        public MiTareaPost(String url,String jsonObject){

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
                HttpPost post = new HttpPost(HTTP_EVENT);

                //Configuramos los parametos que vaos a enviar con la peticion HTTP POST
                post.setHeader("Content-type", "application/json");
                post.setEntity(new StringEntity(jsonObject, HTTP.UTF_8));

                //Se ejecuta el envio de la peticion y se espera la respuesta de la misma.
                HttpResponse response = httpClient.execute(post);
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
            showAlertDialog(ValorarServicioActivity.this,"Cita","Evaluacion realizada exitosamente",true);
            Bundle bolsa=getIntent().getExtras();
            long id=bolsa.getLong("id");
            long cita=bolsa.getLong("cita");
            DAOApp daoApp=new DAOApp();
            CitaDao citaDao=daoApp.getCitaDao();
            Cita item=citaDao.load(cita);
            item.setEstatus(4);

            citaDao.update(item);
            siguiente.setVisibility(View.GONE);
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
}
