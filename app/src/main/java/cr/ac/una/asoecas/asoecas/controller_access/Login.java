package cr.ac.una.asoecas.asoecas.controller_access;
//Linea para la importacion de V4
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
public class Login extends FragmentoAbsPrincipal implements View.OnClickListener,View.OnFocusChangeListener{
    Button acceder;
    EditText usuario;
    EditText clave;
    TextView nombreUsuario;
    private dataWebService data;
    private View vista;

    private TextInputLayout text_input_usuario;
    private TextInputLayout text_input_contra;

    private TextInputEditText cajaUsuario;
    private TextInputEditText cajaContra;

    private boolean verificar;


    public Login() {
        verificar = true;
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

        acceder = (Button) vista.findViewById(R.id.btnAcceder);
        acceder.setOnClickListener(this);

        //PABLO
        text_input_usuario = (TextInputLayout) vista.findViewById(R.id.text_input_usuario);
        cajaUsuario = (TextInputEditText) vista.findViewById(R.id.usuarioCorreo);

        text_input_contra = (TextInputLayout) vista.findViewById(R.id.text_input_contra);
        cajaContra = (TextInputEditText) vista.findViewById(R.id.usuarioClave);
        /////

        return vista;
    }
    private void secondValidate(){
        cajaUsuario.setOnFocusChangeListener(this);
        cajaContra.setOnFocusChangeListener(this);
    }
    public void validate(View vista) {
        verificar = true;
        String userError = null;
        if (TextUtils.isEmpty(cajaUsuario.getText())) {
            verificar = false;
            userError = getString(R.string.mandatory);
        }
        toggleTextInputLayoutError(text_input_usuario, userError);

        String passError = null;
        if (TextUtils.isEmpty(cajaContra.getText())) {
            verificar = false;
            passError = getString(R.string.mandatory);
        }
        toggleTextInputLayoutError(text_input_contra, passError);

        clearFocus(vista);
    }

    private static void toggleTextInputLayoutError(@NonNull TextInputLayout textInputLayout,
                                                   String msg) {
        textInputLayout.setError(msg);
        if (msg == null) {
            textInputLayout.setErrorEnabled(false);
        } else {
            textInputLayout.setErrorEnabled(true);
        }
    }

    private void clearFocus(View view) {
        view = getActivity().getCurrentFocus();
        if (view != null && view instanceof EditText) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAcceder){
            secondValidate();
            validate(v);
            if(verificar){
                //   data.execute("http://127.0.0.1:81/Api-android/business/actionRegistro.php?operacion=login&usuario="+usuario.getText().toString()+"&contrasena="+clave.getText().toString());
                if(data.isOnlineNetTrue()){
                    data.execute("https://asoecas.000webhostapp.com/business/actionRegistro.php?operacion=login&usuario="+cajaUsuario.getText().toString()+"&contrasena="+cajaContra.getText().toString());
                    waitResponce();
                }else{
                    Toast.makeText(getContext(),"No hay conexion a internet",Toast.LENGTH_LONG).show();
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            validate(v);
        }
        if(v.getId() == R.id.usuarioCorreo && !hasFocus && cajaUsuario.getText().length()<9){
            cajaUsuario.setText("");
        }
    }
}