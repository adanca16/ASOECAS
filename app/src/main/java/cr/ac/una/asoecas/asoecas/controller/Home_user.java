package cr.ac.una.asoecas.asoecas.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cr.ac.una.asoecas.asoecas.R;
import cr.ac.una.asoecas.asoecas.controller_access.Bienvenida;
import cr.ac.una.asoecas.asoecas.data.AsyncTaskLoadImage;
import cr.ac.una.asoecas.asoecas.model.Usuario_Datos;

public class Home_user extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Para setear los datos al usuario
    TextView nombreUsuario;
    TextView correoUsuario;
    ImageView imageViewUsuario;
    TextView telefonoUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Contenedor_Home);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nombreUsuario=  (TextView) navigationView.getHeaderView(0).findViewById(R.id.nombreUsuario);
        nombreUsuario.setText(Usuario_Datos.nombre+" "+Usuario_Datos.apellido);
        correoUsuario=  (TextView) navigationView.getHeaderView(0).findViewById(R.id.correoUsuario);
        correoUsuario.setText(Usuario_Datos.correo);
        telefonoUsuario = (TextView) navigationView.getHeaderView(0).findViewById(R.id.telefonoUsuario);
        telefonoUsuario.setText("+506 "+Usuario_Datos.telefono);
//Cargo la imagen del usuario
        imageViewUsuario = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageViewProfile);
        showImageUser();


        switch (Usuario_Datos.rol){
            case 1:
                navigationView.getMenu().findItem(R.id.nav_admin_eventos).setVisible(true);
                break;
            case 2:
                navigationView.getMenu().findItem(R.id.nav_admin_eventos).setVisible(true);
                break;
            case 3:
                navigationView.getMenu().findItem(R.id.nav_admin_eventos).setVisible(false);
                break;
            case 4:
                navigationView.getMenu().findItem(R.id.nav_admin_eventos).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_Salir).setVisible(false);
                break;
        }
    }
    public void showImageUser() {
        String url = Usuario_Datos.imagen;
        new AsyncTaskLoadImage(imageViewUsuario).execute(url);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Contenedor_Inicio);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


  @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectScreen(item.getItemId());
        //Ocultar el menu lateral de la izquierda
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Contenedor_Home);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectScreen(int id){
        Toast.makeText(getApplicationContext(),"Id cliqueado "+id, Toast.LENGTH_LONG).show();
        Fragment fragemento = null;

        switch (id){
            case R.id.nav_todo:
                fragemento = new calendario_actividad(1,"Todos los eventos");
                break;
            case R.id.nav_futbol:
                fragemento = new calendario_actividad(2,"Campeonato de futbol");
                break;
            case R.id.nav_pingpong:
                fragemento = new calendario_actividad(3,"Campeonato de ping pong");
                break;
            case R.id.nav_baile:
                fragemento = new calendario_actividad(4,"Baile Folclorico");
                break;
            case R.id.nav_una:
                fragemento = new calendario_actividad(5,"Actividades Universitarias");
                break;
            case R.id.nav_comu:
                fragemento = new calendario_actividad(6,"Actividades Comunales");
                break;
            case  R.id.nav_admin_eventos:
                fragemento = new AddEvento();
                break;
            case  R.id.nav_Salir:
                salir();
                break;

            default:
              //  fragemento = new Register();
                break;
        }
       // if (fragemento != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.Contenedor_Home,fragemento);
            ft.commit();
       // }
    }
    //metodo para desactivar la actividad
    private void salir(){
        Toast.makeText(getApplicationContext(),"Saliendo",Toast.LENGTH_LONG).show();
        Intent intent = new Intent (getApplicationContext(), Bienvenida.class);
        startActivity(intent);
        finish();
        System.exit(0);

    }


}
