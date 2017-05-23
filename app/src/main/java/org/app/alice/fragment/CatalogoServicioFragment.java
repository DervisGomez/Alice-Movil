package org.app.alice.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.app.alice.R;
import org.app.alice.activity.ListaTipoServiciosActivity;
import org.app.alice.activity.VerNoticiaActivity;
import org.app.alice.activity.VerServicioActivity;
import org.app.alice.adapter.CatalogoNoticiasAdapter;
import org.app.alice.adapter.CatalogoServicioAdapter;
import org.app.alice.modelo.CategoriaServicio;
import org.app.alice.modelo.CategoriaServicioDao;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Noticia;
import org.app.alice.modelo.NoticiaDao;
import org.app.alice.service.VerificarConexionService;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CatalogoServicioFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private ProgressDialog pd = null;
    ListView catalogoServicio;

    View rootView;

    public CatalogoServicioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_catalogo_servicio, container, false);
        catalogoServicio=(ListView)rootView.findViewById(R.id.lvCatalogoSercicio);
        VerificarConexionService verificar=new VerificarConexionService(rootView.getContext());

        try {
            DAOApp daoApp=new DAOApp();
            CategoriaServicioDao categoriaServicioDao=daoApp.getCategoriaServicioDao();
            List<CategoriaServicio> list=categoriaServicioDao.loadAll();

                catalogoServicio.setAdapter(new CatalogoServicioAdapter(rootView.getContext(), list));
                catalogoServicio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        CategoriaServicio item = (CategoriaServicio) catalogoServicio.getAdapter().getItem(pos);
                        llamarActivity(item);
                    }
                });

        }catch (Exception e){
            showAlertDialog(rootView.getContext(),"Error",e.toString(),false);
        }


        return rootView;
    }




    public void llamarActivity(CategoriaServicio item){
        Intent i= new Intent(rootView.getContext(), ListaTipoServiciosActivity.class);
        i.putExtra("id",item.getId());
        i.putExtra("titulo",item.getDescripcion());
        startActivity(i);
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
