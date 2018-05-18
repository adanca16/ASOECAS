package cr.ac.una.asoecas.asoecas.controller_access;
//Linea para la importacion de V4
import android.content.Intent;
import android.os.Handler;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import cr.ac.una.asoecas.asoecas.R;
import cr.ac.una.asoecas.asoecas.controller.Home_user;
import cr.ac.una.asoecas.asoecas.data.dataWebService;
import cr.ac.una.asoecas.asoecas.model.FragmentoAbsPrincipal;
import cr.ac.una.asoecas.asoecas.model.Usuario_Datos;

/**
 Fragemento para realizar el login o acceso al sistema.
 */
public class Login extends FragmentoAbsPrincipal implements View.OnClickListener{
    Button acceder;
    EditText usuario;
    EditText clave;
    TextView nombreUsuario;
    private dataWebService data;
    private View vista;
    public Login() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Acceso al sistema");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.vista = inflater.inflate(R.layout.fragment_login,container,false);
        data = new dataWebService(getActivity());
        usuario = (EditText) vista.findViewById(R.id.usuarioCorreo);
        acceder = (Button) vista.findViewById(R.id.btnAcceder);
        acceder.setOnClickListener(this);
        return vista;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAcceder){
            if(TextUtils.isEmpty(usuario.getText().toString())){
                Toast.makeText(getContext(), "Debes ingresar un usuario!!!", Toast.LENGTH_LONG).show();
            }else{
                if(TextUtils.isEmpty(clave.getText().toString())){
                    Toast.makeText(getContext(), "Debes ingresar una contraseña!!!", Toast.LENGTH_LONG).show();
                }else{
                 //   data.execute("http://127.0.0.1:81/Api-android/business/actionRegistro.php?operacion=login&usuario="+usuario.getText().toString()+"&contrasena="+clave.getText().toString());
                    if(data.isOnlineNetTrue()){
                        data.execute("https://asoecas.000webhostapp.com/business/actionRegistro.php?operacion=login&usuario="+usuario.getText().toString()+"&contrasena="+clave.getText().toString());
                        waitResponce();
                    }else{
                        Toast.makeText(getContext(),"No hay conexion a internet",Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
    }
    private void waitResponce(){
        Handler Progreso = new Handler();
        Progreso.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!data.obtenerRespuesta().isEmpty()){
                    onPostExecute(data.obtenerRespuesta());
                }else{
                    waitResponce();
                }
            }
        },1000);
    }
    protected void onPostExecute(String result) {
        try {
            JSONArray array = new JSONArray(result);
            if(array.length()!=0){
                for (int i = 0 ; i < array.length();i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    Usuario_Datos.id= jsonObject.getString("id");
                    Usuario_Datos.nombre = jsonObject.getString("nombre");
                    Usuario_Datos.apellido = jsonObject.getString("apellido");
                    Usuario_Datos.carrera = jsonObject.getString("carrera");
                    Usuario_Datos.correo = jsonObject.getString("correo");
                    Usuario_Datos.telefono = jsonObject.getString("telefono");
                    Usuario_Datos.imagen = jsonObject.getString("imagen");
                    Usuario_Datos.rol = Integer.parseInt(jsonObject.getString("rol"));
                    //Creo una nueva actividad..
                    Intent intentHome = new Intent (getContext(), Home_user.class);
                    startActivity(intentHome);
                }
            }else{
                Toast.makeText(getContext(), "Datos invalidos!!!", Toast.LENGTH_LONG).show();
                usuario.setText("");
                usuario.setActivated(true);
                clave.setText("");
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}