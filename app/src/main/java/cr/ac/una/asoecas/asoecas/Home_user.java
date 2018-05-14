package cr.ac.una.asoecas.asoecas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import cr.ac.una.asoecas.asoecas.data.AsyncTaskLoadImage;

public class Home_user extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Para setear los datos al usuario
    TextView nombreUsuario;
    TextView correoUsuario;
    ImageView imageViewUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    //    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
     //   fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
      //          Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
       //                 .setAction("Action", null).show();
       //     }
       // });

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
//Cargo la imagen del usuario
        imageViewUsuario = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageViewProfile);
        showImageOne();


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
    public void showImageOne() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_user, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        displaySelectScreen(item.getItemId());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Contenedor_Home);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectScreen(int id){
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
        if (fragemento != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.Contenedor_Home,fragemento);
            ft.commit();
        }
    }
    //metodo para desactivar la actividad
    private void salir(){
        finish();
        System.exit(0);

    }


}
