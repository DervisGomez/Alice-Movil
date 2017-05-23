package org.app.alice.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.app.alice.R;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Horario;
import org.app.alice.modelo.HorarioDao;
import org.app.alice.modelo.Turno;
import org.app.alice.modelo.TurnoDao;

import java.util.List;

/**
 * Created by brayan on 12/03/17.
 */

public class VerHorarioActivity extends AppCompatActivity {
    Horario item;
    List<Turno> turnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_horario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bolsa=getIntent().getExtras();
        long id=bolsa.getLong("id");
        String titulo=bolsa.getString("titulo");
        getSupportActionBar().setTitle(titulo);
        DAOApp daoApp=new DAOApp();
        HorarioDao horarioDao=daoApp.getHorarioDao();
        item=horarioDao._queryServicio_Horario(id).get(0);
        TurnoDao turnoDao=daoApp.getTurnoDao();
        turnos=turnoDao._queryHorario_Turno(item.getId());
        TextView lunes1=(TextView)findViewById(R.id.tvLunes1);
        TextView lunes2=(TextView)findViewById(R.id.tvLunes2);
        TextView martes1=(TextView)findViewById(R.id.tvMartes1);
        TextView martes2=(TextView)findViewById(R.id.tvMartes2);
        TextView miercoles1=(TextView)findViewById(R.id.tvMiercole1);
        TextView miercoles2=(TextView)findViewById(R.id.tvMiercole2);
        TextView jueves1=(TextView)findViewById(R.id.tvJueves1);
        TextView jueves2=(TextView)findViewById(R.id.tvJueves2);
        TextView viernes1=(TextView)findViewById(R.id.tvViernes1);
        TextView viernes2=(TextView)findViewById(R.id.tvViernes2);
        TextView sabado1=(TextView)findViewById(R.id.tvSabado1);
        TextView sabado2=(TextView)findViewById(R.id.tvSabado2);
        TextView domingo1=(TextView)findViewById(R.id.tvDomingo1);
        TextView domingo2=(TextView)findViewById(R.id.tvDomingo2);
        //Toast.makeText(VerHorarioActivity.this, String.valueOf(turnos.size()), Toast.LENGTH_SHORT).show();

        for (int x=0;x<turnos.size();x++){
            Turno turno=turnos.get(x);
            String turn="";
            if(turno.getInicio().equals(turno.getFin())){
                //turn=turno.getInicio().substring(12,19)+" - "+turno.getFin().substring(12,19);
            }else{
                turn=turno.getInicio().substring(11,19)+" - "+turno.getFin().substring(11,19);
            }
            if (turno.getTipo()==1){
                switch (turno.getDia()){
                    case 1:
                        lunes1.setText(turn);
                        break;
                    case 2:
                        martes1.setText(turn);
                        break;
                    case 3:
                        miercoles1.setText(turn);
                        break;
                    case 4:
                        jueves1.setText(turn);
                        break;
                    case 5:
                        viernes1.setText(turn);
                        break;
                    case 6:
                        sabado1.setText(turn);
                        break;
                    case 7:
                        domingo1.setText(turn);
                        break;
                }
            }else{
                switch (turno.getDia()){
                    case 1:
                        lunes2.setText(turn);
                        break;
                    case 2:
                        martes2.setText(turn);
                        break;
                    case 3:
                        miercoles2.setText(turn);
                        break;
                    case 4:
                        jueves2.setText(turn);
                        break;
                    case 5:
                        viernes2.setText(turn);
                        break;
                    case 6:
                        sabado2.setText(turn);
                        break;
                    case 7:
                        domingo2.setText(turn);
                        break;
                }
            }
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
