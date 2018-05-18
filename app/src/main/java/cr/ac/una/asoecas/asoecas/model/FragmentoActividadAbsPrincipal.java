package cr.ac.una.asoecas.asoecas.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import cr.ac.una.asoecas.asoecas.R;

/**
 * Created by LM-772 on 19/03/2018.
 */

public abstract class FragmentoActividadAbsPrincipal extends AppCompatActivity {
    protected boolean cambia_fragmento = true;
    public boolean siPuedeCambiarFragmento() { return this.cambia_fragmento; }
    public void ponePuedeCambiarFragmento(boolean value) { this.cambia_fragmento = value; }

    public void NuevoFragmento(FragmentoAbsPrincipal fragment){
        if (siPuedeCambiarFragmento()){
            (getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Contenedor_Inicio , fragment))
                    .commit();}
    }


    public void agregueFragmentoAPila(FragmentoAbsPrincipal fragment) {
        // Initialize fragment transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace with fragment content
        ft.replace(R.id.Contenedor_Inicio, fragment);
        // Animation on change
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        // Clear stack (back button memory)
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }
    @Override
    public void onBackPressed() {
        // Check if fragment not doing any BE calling
        if (this.siPuedeCambiarFragmento()) {
            super.onBackPressed();
        }
    }
    protected SharedPreferences.Editor getEditor (){
        SharedPreferences.Editor editor = this.getApplicationSharedPrederences().edit();

        return editor;
    }
    public SharedPreferences getApplicationSharedPrederences() {
        SharedPreferences sp = getSharedPreferences(Constantes.PREFERENCIA_NOMBRE_ARCHIVO, Context.MODE_PRIVATE);

        return sp;
    }

}
