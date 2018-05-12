package cr.ac.una.asoecas.asoecas;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class Register extends FragmentoAbsPrincipal implements View.OnClickListener {

    EditText cajaCedula;
    EditText cajaNombre;
    EditText cajaApellido;
    EditText cajaCarrera;
    EditText cajaCorreo;
    EditText cajaTelefono;
    EditText cajaImagen;
    EditText cajaClave1;
    EditText cajaClave2;


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

        cajaCedula = (EditText)vista.findViewById(R.id.cajaCedula);
         cajaNombre = (EditText)vista.findViewById(R.id.cajaNombre);
         cajaApellido= (EditText)vista.findViewById(R.id.cajaApellido);
         cajaCarrera= (EditText)vista.findViewById(R.id.cajaCarrera);
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
              new CargarDatosUsuario().execute("https://asoecas.000webhostapp.com/business/actionRegistro.php?operacion=insert&cedula="+cajaCedula.getText().toString()+"&nombre="+cajaNombre.getText().toString()+"&apellido="+cajaApellido.getText().toString()+"&correo="+cajaCorreo.getText().toString()+"&carrera="+cajaCarrera.getText().toString()+"&telefono="+cajaTelefono.getText().toString()+"&imagen="+cajaImagen.getText().toString()+"&clave="+cajaClave1.getText().toString());
          }else{
              toast =Toast.makeText(getContext(), "Verifique los datos", Toast.LENGTH_LONG);
              toast.show();
          }
        }
    }


    private class CargarDatosUsuario  extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {
                return downloadUrl(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(TextUtils.equals(result.trim(),"true")){
                toast =Toast.makeText(getContext(), "Datos guardados con exito!", Toast.LENGTH_LONG);
                toast.show();
                clearInput();
            }else{
                toast =Toast.makeText(getContext(), "No se ha podido guardar la informacion "+result, Toast.LENGTH_LONG);
                toast.show();
          //      clearInput();
            }

        }

        private String downloadUrl(String myurl) {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                is = conn.getInputStream();
                Log.d("Respuesta", "The response is: " + response+" contetn As String ");

                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
                Log.d("Respuesta", "The contetn As String " + contentAsString);

                return contentAsString;
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            }catch (IOException e){
                toast =Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
            return myurl;
        }
        // Reads an InputStream and converts it to a String.
        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }
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


}
