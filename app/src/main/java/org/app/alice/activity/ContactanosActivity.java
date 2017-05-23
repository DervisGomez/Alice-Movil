package org.app.alice.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.app.alice.R;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.TipoOpinio;
import org.app.alice.modelo.TipoOpinioDao;
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
 * Created by brayan on 12/03/17.
 */

public class ContactanosActivity extends AppCompatActivity implements View.OnClickListener{

    Button enviar;
    Spinner comboTipo;
    EditText nombre;
    EditText correo;
    EditText mensaje;
    GlobalService globals = GlobalService.getInstance();
    List<TipoOpinio> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactanos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nombre=(EditText)findViewById(R.id.etnombre);
        correo=(EditText)findViewById(R.id.etCorreo);
        mensaje=(EditText)findViewById(R.id.etmensaje);
        comboTipo = (Spinner) findViewById(R.id.spTipo);
        enviar = (Button) findViewById(R.id.btnEnviar);
        enviar.setOnClickListener(this);

        DAOApp daoApp=new DAOApp();
        TipoOpinioDao tipoOpinioDao=daoApp.getTipoOpinioDao();
        items = tipoOpinioDao.loadAll();
        List<String> values = new ArrayList<String>();

            // Elementos en Spinner
        for (int i=0; i<items.size(); i++) {
            values.add(items.get(i).getDescripcion());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboTipo.setAdapter(dataAdapter);
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
    public void onClick(View v) {
        String numNombre=nombre.getText().toString();
        String numCorreo=correo.getText().toString();
        String numMensaje=mensaje.getText().toString();
        if(numNombre.length()>0 && numCorreo.length()>0 && numMensaje.length()>0) {
        switch (v.getId()) {
            case R.id.btnEnviar:
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("nombre", nombre.getText());
                    jsonObject.put("correo", correo.getText());
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getDescripcion() == comboTipo.getSelectedItem().toString()) {
                            jsonObject.put("tipo_opinion_id", items.get(i).getId());
                        }
                    }
                    jsonObject.put("descripcion", mensaje.getText());
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("opinion", jsonObject);
                    new MiTareaPost(globals.getServer() + "opiniones.json", jsonObject2.toString()).execute();
                    showAlertDialog(ContactanosActivity.this, "Mensaje enviado", "Su mensaje ha sido enviado exitosamente.", false);


                } catch (JSONException e) {
                    showAlertDialog(ContactanosActivity.this, "ERROR!", "Su mensaje no ha podido ser enviado, intente mas tarde.", false);

                }

                    Intent intent = new Intent(ContactanosActivity.this, MainActivity.class);
                    startActivity(intent);

        }
        }else {
            showAlertDialog(ContactanosActivity.this, "Campos Vacios", "Debe llenar todos los campos para poder enviar su mensaje", false);
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
