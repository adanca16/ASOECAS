package cr.ac.una.asoecas.asoecas;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import cr.ac.una.asoecas.asoecas.data.dataWebService;


/**
 * A simple {@link Fragment} subclass.
 */
public class Register extends FragmentoAbsPrincipal implements View.OnClickListener, View.OnFocusChangeListener {

    EditText cajaCedula;
    EditText cajaNombre;
    EditText cajaApellido;
    EditText cajaCarrera;
    EditText cajaCorreo;
    EditText cajaTelefono;
    EditText cajaImagen;
    EditText cajaClave1;
    EditText cajaClave2;
    private dataWebService data;
    private int actividad;
    Button btnRegistro;

    Toast toast;
    public Register() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("REGISTRO ASOECAS");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_register,container,false);
        actividad = 0;
        data = new dataWebService();
        cajaCedula = (EditText)vista.findViewById(R.id.cajaCedula);
        //cajaCedula.setOnFocusChangeListener(this);
        cajaNombre = (EditText)vista.findViewById(R.id.cajaNombre);
        cajaApellido= (EditText)vista.findViewById(R.id.cajaApellido);
        cajaCarrera= (EditText)vista.findViewById(R.id.cajaCarrera);
        cajaCarrera.setOnClickListener(this);
         cajaCorreo= (EditText)vista.findViewById(R.id.cajaCorreo);
         cajaTelefono= (EditText)vista.findViewById(R.id.cajaTelefono);
         cajaImagen= (EditText)vista.findViewById(R.id.cajaImagen);

        cajaClave1 = (EditText)vista.findViewById(R.id.cajaClave1);
        cajaClave2 = (EditText)vista.findViewById(R.id.cajaClave2);

         btnRegistro = (Button) vista.findViewById(R.id.btn_Register);
        btnRegistro.setOnClickListener(this);
        return vista;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Register) {
          if (validarInput()){
          //    data.execute("https://asoecas.000webhostapp.com/business/actionRegistro.php?operacion=insert&cedula="+cajaCedula.getText().toString()+"&nombre="+cajaNombre.getText().toString()+"&apellido="+cajaApellido.getText().toString()+"&correo="+cajaCorreo.getText().toString()+"&carrera="+actividad+"&telefono="+cajaTelefono.getText().toString()+"&imagen="+cajaImagen.getText().toString()+"&clave="+cajaClave1.getText().toString());
              data.execute("http://localhost:81/Api-android//business/actionRegistro.php?operacion=insert&cedula="+cajaCedula.getText().toString()+"&nombre="+cajaNombre.getText().toString()+"&apellido="+cajaApellido.getText().toString()+"&correo="+cajaCorreo.getText().toString()+"&carrera="+actividad+"&telefono="+cajaTelefono.getText().toString()+"&imagen="+cajaImagen.getText().toString()+"&clave="+cajaClave1.getText().toString());
              waitResponce();
          }else{
              toast =Toast.makeText(getContext(), "Verifique los datos", Toast.LENGTH_LONG);
              toast.show();
          }
        }else if(v.getId() == R.id.cajaCarrera){
            showDialog();
       //     Toast.makeText(getContext(),actividad,Toast.LENGTH_LONG).show();
        }
    }
    private void waitResponce(){
        Handler Progreso = new Handler();
        Progreso.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!data.obtenerRespuesta().isEmpty()){
                    if(TextUtils.equals(data.obtenerRespuesta().trim(),"true")){
                        Toast.makeText(getContext(),"Datos guardados con exito",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(),"No hemos podido guardar los datos!"+data.obtenerRespuesta(),Toast.LENGTH_SHORT).show();
                    }

                 //   limpiarImput();
                }else{
                    waitResponce();
                }
            }
        },1000);
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Interesados");

        //list of items
        final String[] items = getResources().getStringArray(R.array.opciones_carrera);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actividad = (Integer)which+1;
                        cajaCarrera.setText(items[which] );
                    }
                });
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private  boolean validarInput(){
        boolean enviar = true;
        if ( TextUtils.isEmpty(cajaCedula.getText().toString())){
            enviar = false;
        }
        if ( TextUtils.isEmpty(cajaNombre.getText().toString())){
            enviar = false;
        }
        if ( TextUtils.isEmpty(cajaApellido.getText().toString())){
            enviar = false;
        }
        if ( TextUtils.isEmpty(cajaCorreo.getText().toString())){
            enviar = false;
        }
        if ( TextUtils.isEmpty(cajaTelefono.getText().toString())){
            enviar = false;
        }
        if ( TextUtils.isEmpty(cajaClave1.getText().toString())){
            enviar = false;
        }
        if ( TextUtils.isEmpty(cajaClave2.getText().toString())){
            enviar = false;
        }
        if (!TextUtils.equals(cajaClave2.getText().toString(),cajaClave1.getText().toString())){
            enviar = false;
            Toast.makeText(getContext(),"Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
        return  enviar;
    }
    private void clearInput() {
        cajaCedula.setText("");
        cajaNombre.setText("");
        cajaApellido.setText("");
        cajaCarrera.setText("");
        cajaCorreo.setText("");
        cajaTelefono.setText("");
        cajaImagen.setText("");
        cajaClave1 .setText("");
        cajaClave2 .setText("");
    }
//Metodo para validar si el usuario esta registrado o no
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
        //    data.execute("https://asoecas.000webhostapp.com/business/actionRegistro.php?operacion=verificarUsuario&usuario="+cajaCedula.getText().toString());
            data.execute("http://127.0.0.1:81/Api-android/business/actionRegistro.php?operacion=verificarUsuario&usuario="+cajaCedula.getText().toString());

            waitResponceUser();
        }
    }

    private void waitResponceUser(){
        Handler Progreso = new Handler();
        Progreso.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!data.obtenerRespuesta().isEmpty()){
                    if(Integer.parseInt(data.obtenerRespuesta().trim())==1){
                        Toast.makeText(getContext(),"El usuario esta registrado",Toast.LENGTH_SHORT).show();
                        cajaCedula.setText("");
                    }
                }else{
                    if(Integer.parseInt(data.obtenerRespuesta().trim())==0){
                        Toast.makeText(getContext(),"Cedula no registrada!",Toast.LENGTH_SHORT).show();
                    }else{
                        waitResponceUser();
                    }
                }
            }
        },1000);
    }
}

