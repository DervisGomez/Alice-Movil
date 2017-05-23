package org.app.alice.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.app.alice.R;
import org.app.alice.activity.VerEventoActivity;
import org.app.alice.activity.VerNoticiaActivity;
import org.app.alice.adapter.CatalogoNoticiasAdapter;
import org.app.alice.adapter.CatalogoServicioAdapter;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Noticia;
import org.app.alice.modelo.NoticiaDao;

import java.util.ArrayList;
import java.util.List;


public class CatalogoNoticiasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    View rootView;

    public CatalogoNoticiasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_catalogo_noticias, container, false);
        final ListView catalogoNoticias=(ListView)rootView.findViewById(R.id.lvCatalogoNoticias);

        try {
            DAOApp daoApp=new DAOApp();
            NoticiaDao noticiaDao=daoApp.getNoticiaDao();
            List<Noticia> list=noticiaDao.loadAll();

                catalogoNoticias.setAdapter(new CatalogoNoticiasAdapter(rootView.getContext(), list));
                catalogoNoticias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        Noticia item = (Noticia) catalogoNoticias.getAdapter().getItem(pos);
                        llamarActivity(item);
                    }
                });

        }catch (Exception e){
            showAlertDialog(rootView.getContext(),"Error",e.toString(),false);
        }


        return rootView;

    }


    public void llamarActivity(Noticia item){
        Intent i= new Intent(rootView.getContext(), VerNoticiaActivity.class);
        i.putExtra("id",item.getId());
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
