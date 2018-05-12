package cr.ac.una.asoecas.asoecas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
// Activity.getFragmentManager

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class Seleccion extends FragmentoAbsPrincipal implements View.OnClickListener {
    Button accederGuest;
    Button accederLogin;
    Button registro;

    public Seleccion() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_seleccion, container, false);
        // Inflate the layout for this fragment
        accederGuest = (Button) vista.findViewById(R.id.btnInvitado_main);
        accederLogin = (Button) vista.findViewById(R.id.btnLogin_main);
        registro = (Button) vista.findViewById(R.id.btnRegister_main);

        accederGuest.setOnClickListener(this);
        accederLogin.setOnClickListener(this);
        registro.setOnClickListener(this);
        return vista;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegister_main){
            ((FragmentoActividadAbsPrincipal) getActivity()).agregueFragmentoAPila(new Register());
        }
        if (v.getId() == R.id.btnInvitado_main){
            Usuario_Datos.id= "invitado";
            Usuario_Datos.nombre = "Invitado";
            Usuario_Datos.apellido = "Guest";
            Usuario_Datos.carrera = "Null";
            Usuario_Datos.correo = "una@una.una";
            Usuario_Datos.telefono = "2764-0234";
            Usuario_Datos.imagen = "imagen";
            Usuario_Datos.rol = 4;

            Intent intent = new Intent (v.getContext(), Home_user.class);
            startActivity(intent);

        }
        if (v.getId() == R.id.btnLogin_main){
            ((FragmentoActividadAbsPrincipal) getActivity()).agregueFragmentoAPila(new Login());
        }
    }
}
