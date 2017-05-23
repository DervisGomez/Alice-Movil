package org.app.alice.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import org.app.alice.R;
import org.app.alice.fragment.CatalogoEventoFragment;
import org.app.alice.fragment.CatalogoNoticiasFragment;
import org.app.alice.fragment.CatalogoServicioFragment;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Usuario;
import org.app.alice.modelo.UsuarioDao;
import org.app.alice.service.NotificacionServicio;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewCompat.animate;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int tabActual=0;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final boolean[] hidden = {true};
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            setSupportActionBar(toolbar);
        }catch (Exception e){
            showAlertDialog(this,"hola",e.toString(),true);
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionBoton(view);

            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        try {
            iniciarActivity();
        }catch (Exception e){
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.

            tabLayout.getTabAt(i).setIcon(mSectionsPagerAdapter.getIcon(i));
            //tabLayout.getTabAt(i).setText(mSectionsPagerAdapter.getPageTitle(i));
        }


        tabLayout.getTabAt(tabActual).setIcon(mSectionsPagerAdapter.getIconSus(tabActual));
        getSupportActionBar().setTitle(mSectionsPagerAdapter.getActionTitle(tabActual));
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                efectoBoton(fab);
                tabLayout.getTabAt(tabActual).setIcon(mSectionsPagerAdapter.getIcon(tabActual));
                tabLayout.getTabAt(position).setIcon(mSectionsPagerAdapter.getIconSus(position));
                getSupportActionBar().setTitle(mSectionsPagerAdapter.getActionTitle(position));
                tabActual=position;
            }
        });
        fab.setScaleX((float) 1.4);
        fab.setScaleY((float) 1.4);

        try{
            TextView usuario=(TextView)findViewById(R.id.tvUsuarioLogin);
            usuario.setText("Dervis");
        }catch (Exception e){

        }
        //startService(new Intent(this, NotificacionServicio.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(MainActivity.this, BuscarTodoActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_iniciar_sesion) {
            Intent intent=new Intent(this,IniciarSesionActivity.class);
            startActivityForResult(intent,1);

        } else if (id == R.id.nav_notificaciones) {
            Intent intent=new Intent(this,ListaNotificacionActivity.class);
            startActivity(intent);
        }   else if (id == R.id.nav_ver_perfil) {
            Intent inte=new Intent(this,VerPerfilActivity.class);
            startActivity(inte);
        } else if (id == R.id.nav_cerrar_sesion) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Importante!");
            dialogo1.setMessage("Desea cerrar sesión?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    DAOApp daoApp=new DAOApp();
                    UsuarioDao usuarioDao=daoApp.getUsuarioDao();
                    usuarioDao.deleteAll();
                    stopService(new Intent(MainActivity.this,
                            NotificacionServicio.class));
                    iniciarActivity();
                    showAlertDialog(MainActivity.this,"Sesión","Sesión cerrada exitosamente",true);
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();

        }else if (id == R.id.nav_contactanos) {
            Intent intent=new Intent(this,ContactanosActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_pregruntas_frecuentes) {
            Intent intent=new Intent(this,PreguntasFrecuentesActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        iniciarActivity();
    }

    public void iniciarActivity(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        List<Usuario> usuarios=new ArrayList<Usuario>();
        DAOApp daoApp=new DAOApp();
        UsuarioDao usuarioDao=daoApp.getUsuarioDao();
        usuarios=usuarioDao.loadAll();
        if (usuarios.size()>0){
            navigationView.getMenu().setGroupVisible(R.id.grupo1,false);
            navigationView.getMenu().setGroupVisible(R.id.grupo2,true);
            navigationView.getMenu().setGroupVisible(R.id.grupo3,true);
            fab.setVisibility(View.VISIBLE);
            /*NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(MainActivity.this)
                            .setSmallIcon(android.R.drawable.stat_sys_warning)
                            .setLargeIcon((((BitmapDrawable)getResources()
                                    .getDrawable(R.drawable.alice_mini)).getBitmap()))
                            .setContentTitle("Mensaje de Alerta")
                            .setContentText("Ejemplo de notificación.")
                            .setContentInfo("4")
                            .setTicker("Alerta!");
            Intent notIntent = new Intent(MainActivity.this, ListaNotificacionActivity.class);
            PendingIntent contIntent = PendingIntent.getActivity(MainActivity.this, 0, notIntent, 0);
            mBuilder.setContentIntent(contIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(2, mBuilder.build());*/

        }else{
            navigationView.getMenu().setGroupVisible(R.id.grupo1,true);
            navigationView.getMenu().setGroupVisible(R.id.grupo2,false);
            navigationView.getMenu().setGroupVisible(R.id.grupo3,false);
        }
    }

    public void accionBoton(View view){
        List<Usuario> usuarios=new ArrayList<Usuario>();
        DAOApp daoApp=new DAOApp();
        UsuarioDao usuarioDao=daoApp.getUsuarioDao();
        usuarios=usuarioDao.loadAll();
        if (usuarios.size()>0) {
            Intent inte = new Intent(this, ListaCitasActivity.class);
            startActivity(inte);
        }else{
            Intent inte = new Intent(this, BuscarCitaActivity.class);
            startActivity(inte);
        }

    }

    public void efectoBoton(final FloatingActionButton fab){
        animate(fab)
                .scaleX(0)
                .scaleY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(150);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animate(fab)
                        .scaleX((float) 1.4)
                        .scaleY((float) 1.4)
                        .setInterpolator(new LinearInterpolator())
                        .setDuration(150);
            }
        }, 150);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        Context context;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


        public PlaceholderFragment() {

        }


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber) {

            PlaceholderFragment fragment = new PlaceholderFragment();
            Fragment fra=null;
            switch (sectionNumber) {
                case 1:
                    fra= new CatalogoServicioFragment();
                    break;
                case 2:
                    fra= new CatalogoEventoFragment();
                    break;
                case 3:
                    fra= new CatalogoNoticiasFragment();
                    break;
            }
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fra;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "";
                case 1:
                    return "";
                case 2:
                    return "";
            }
            return null;
        }

        public CharSequence getActionTitle(int position) {
            switch (position) {
                case 0:
                    return "ALICE - Sevicios";
                case 1:
                    return "ALICE - Eventos";
                case 2:
                    return "ALICE - Noticias";
            }
            return null;
        }

        public int getIcon(int position){
            switch (position) {
                case 0:
                    return R.drawable.home2;
                case 1:
                    return R.drawable.servicio2;
                case 2:
                    return R.drawable.citas2;
            }
            return R.drawable.ic_menu_camera;
        }
        public int getIconSus(int position){
            switch (position) {
                case 0:
                    return R.drawable.home;
                case 1:
                    return R.drawable.servicio;
                case 2:
                    return R.drawable.citas;
            }
            return R.drawable.ic_menu_camera;
        }
    }
}
