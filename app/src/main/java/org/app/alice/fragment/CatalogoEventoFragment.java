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
import org.app.alice.activity.VerServicioActivity;
import org.app.alice.adapter.CatalogoEventoAdapter;
import org.app.alice.adapter.CatalogoNoticiasAdapter;
import org.app.alice.adapter.CatalogoServicioAdapter;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Evento;
import org.app.alice.modelo.EventoDao;
import org.app.alice.modelo.Noticia;
import org.app.alice.modelo.NoticiaDao;

import java.util.ArrayList;
import java.util.List;

public class CatalogoEventoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    View rootView;

    public CatalogoEventoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_catalogo_evento, container, false);
        final ListView catalogoEvento=(ListView)rootView.findViewById(R.id.lvCatalogoEvento);
        try {
            DAOApp daoApp=new DAOApp();
            EventoDao eventoDao=daoApp.getEventoDao();
            List<Evento> list=eventoDao.loadAll();

                catalogoEvento.setAdapter(new CatalogoEventoAdapter(rootView.getContext(), list));
                catalogoEvento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        Evento item = (Evento) catalogoEvento.getAdapter().getItem(pos);
                        llamarActivity(item);

                    }
                });

        }catch (Exception e){
            showAlertDialog(rootView.getContext(),"Error",e.toString(),false);
        }
        return rootView;

    }

    public void llamarActivity(Evento item){
        Intent i= new Intent(rootView.getContext(), VerEventoActivity.class);
        i.putExtra("id",item.getId());
        i.putExtra("alerta",(long)0);
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
