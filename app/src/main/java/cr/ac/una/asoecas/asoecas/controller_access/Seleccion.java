package cr.ac.una.asoecas.asoecas.controller_access;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
// Activity.getFragmentManager

import cr.ac.una.asoecas.asoecas.Upload_image;
import cr.ac.una.asoecas.asoecas.controller.Home_user;
import cr.ac.una.asoecas.asoecas.controller_access.Login;
import cr.ac.una.asoecas.asoecas.controller_access.Register;
import cr.ac.una.asoecas.asoecas.model.FragmentoAbsPrincipal;
import cr.ac.una.asoecas.asoecas.model.FragmentoActividadAbsPrincipal;
import cr.ac.una.asoecas.asoecas.R;
import cr.ac.una.asoecas.asoecas.model.Usuario_Datos;


public class Seleccion extends FragmentoAbsPrincipal implements View.OnClickListener {
    Button accederGuest;
    Button accederLogin;
    Button registro;

    WebView webViewSeleccion;

    public Seleccion() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("ACCESO ASOECAS");
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
/*
        webViewSeleccion = (WebView) vista.findViewById(R.id.webViewSeleccion);
        webViewSeleccion.getSettings().setJavaScriptEnabled(true);
        webViewSeleccion.getSettings().setBuiltInZoomControls(true);
        webViewSeleccion.loadUrl("https://asoecas.000webhostapp.com/carrusel_app.html");
*/

        return vista;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegister_main){
            //((FragmentoActividadAbsPrincipal) getActivity()).agregueFragmentoAPila(new Register());
            ((FragmentoActividadAbsPrincipal) getActivity()).agregueFragmentoAPila(new Upload_image());
        }
        if (v.getId() == R.id.btnInvitado_main){
            Usuario_Datos.id= "invitado";
            Usuario_Datos.nombre = "Invitado";
            Usuario_Datos.apellido = "UNA";
            Usuario_Datos.carrera = "Null";
            Usuario_Datos.correo = "una@una.una";
            Usuario_Datos.telefono = "2764-0234";
            //Usuario_Datos.imagen = "https://asoecas.000webhostapp.com/picture/user/icono_default.png";
            Usuario_Datos.imagen = "https://asoecas.000webhostapp.com/picture/user/icono_default.png";

            Usuario_Datos.rol = 4;

            Intent intentHome = new Intent (v.getContext(), Home_user.class);
            startActivity(intentHome);


        }
        if (v.getId() == R.id.btnLogin_main){
            ((FragmentoActividadAbsPrincipal) getActivity()).agregueFragmentoAPila(new Login());
        }
    }
}
