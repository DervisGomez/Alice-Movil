package org.app.alice.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import org.app.alice.R;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Usuario;
import org.app.alice.modelo.UsuarioDao;

import java.util.List;

public class VerPerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EditText nombre=(EditText)findViewById(R.id.etNombrePerfil);
        EditText cedula=(EditText)findViewById(R.id.et_cedulaPerfil);
        EditText correo=(EditText)findViewById(R.id.et_correoPerfil);
        EditText telefono=(EditText)findViewById(R.id.et_telefonoPerfil);
        EditText direccion=(EditText)findViewById(R.id.et_direccionPerfil);

        DAOApp daoApp=new DAOApp();
        UsuarioDao usuarioDao=daoApp.getUsuarioDao();
        List<Usuario> usuarios=usuarioDao.loadAll();
        if (usuarios.size()>0){
            Usuario usuario=usuarios.get(0);
            nombre.setText(usuario.getNombre()+" "+usuario.getApellido());
            cedula.setText(usuario.getCedula());
            correo.setText(usuario.getCorreo());
            telefono.setText(usuario.getTelefono());
            direccion.setText(usuario.getDireccion());
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
}
