package cr.ac.una.asoecas.asoecas;

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
import android.widget.TextView;

public class Home_user extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Para setear los datos al usuario
    TextView nombreUsuario;
    TextView correoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Contenedor_Home);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Contenedor_Home);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
          //  case  R.id.nav_login:
           //     fragemento = new Login();
            //    break;
           /* case  R.id.nav_register:
                fragemento = new Register();
                break;
*/
            case  R.id.nav_admin_eventos:
                fragemento = new AddEvento();
                break;
            default:
                fragemento = new Register();
                break;
        }
        if (fragemento != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.Contenedor_Home,fragemento);
            ft.commit();
        }
    }
}
