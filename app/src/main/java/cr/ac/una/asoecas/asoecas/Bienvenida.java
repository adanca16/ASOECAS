package cr.ac.una.asoecas.asoecas;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Bienvenida extends FragmentoAbsPrincipal {

    public Bienvenida() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("ASOECAS");
        // Inflate the layout for this fragment
        View vista=inflater.inflate(R.layout.fragment_bienvenida, container, false);
        Handler Progreso = new Handler();
        Progreso.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((FragmentoActividadAbsPrincipal)getActivity()).agregueFragmentoAPila(new Seleccion());
            }
        },Constantes.Tiempo_Espera);
        return vista;
    }

}
