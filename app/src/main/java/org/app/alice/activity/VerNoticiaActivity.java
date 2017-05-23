package org.app.alice.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.app.alice.R;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Noticia;
import org.app.alice.modelo.NoticiaDao;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by brayan on 22/02/17.
 */

public class VerNoticiaActivity extends AppCompatActivity {
    Noticia item;
    Bitmap bm = null;
    ImageView imageNoticia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_noticia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bolsa=getIntent().getExtras();
        long id=bolsa.getLong("id");
        DAOApp daoApp=new DAOApp();
        NoticiaDao noticiaDao=daoApp.getNoticiaDao();
        item=noticiaDao.load(id);

        TextView titulo=(TextView)findViewById(R.id.tvTituloNoticiaDetalle);
        TextView fecha=(TextView)findViewById(R.id.tvFechaNoticiaDetalle);
        WebView contenido=(WebView)findViewById(R.id.tvContenidoNoticiaDetalle);
        //TextView tipo=(TextView)findViewById(R.id.tvTipoNoticiaDetalle);
        //imageNoticia =(ImageView)findViewById(R.id.imageNoticia);

        titulo.setText(item.getTitulo());
        fecha.setText(item.getTipoNoticia() +"  -  " +item.getFecha());
        contenido.loadData("<meta http-equiv=\"Content-Type\" content=\"text/html\" charset=\"ISO-8859-1\" />"+item.getContenido(),"","");
        //tipo.setText(item.getTipoNoticia());
        //new MiTareaGet("http://192.168.1.107:3000//system/eventos/fotos/000/000/001/medium/fondo1.jpg?1489979974","").execute();
        //new DownloadImageTask().execute("http://192.168.1.107:3000//system/eventos/fotos/000/000/001/medium/fondo1.jpg?1489979974");
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

    public void cargarImagen(String datos){
        imageNoticia.setImageBitmap(bm);
    }

    /*private class MiTareaGet extends AsyncTask<String, Float, String> {
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
                URL _url = new URL(ur);
                URLConnection con = _url.openConnection();
                con.connect();
                InputStream is = con.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
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

            cargarImagen(tiraJson);
        }
    }*/


    class DownloadImageTask extends AsyncTask<String, Void, Drawable>
    {

        final ProgressDialog progressDialog = new ProgressDialog(VerNoticiaActivity.this);

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

            imageNoticia.setImageDrawable(imagen);
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
}
