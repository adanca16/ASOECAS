package cr.ac.una.asoecas.asoecas;

import android.os.Bundle;

import cr.ac.una.asoecas.asoecas.controller_access.Bienvenida;
import cr.ac.una.asoecas.asoecas.model.FragmentoActividadAbsPrincipal;

public class MainActivity extends FragmentoActividadAbsPrincipal {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            NuevoFragmento(new Bienvenida());
        }
    }
}

