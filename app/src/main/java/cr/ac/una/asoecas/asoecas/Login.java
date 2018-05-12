package cr.ac.una.asoecas.asoecas;
//Linea para la importacion de V4
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
/**
 Fragemento para realizar el login o acceso al sistema.
 */
public class Login extends FragmentoAbsPrincipal implements View.OnClickListener {
    Button acceder;
    EditText usuario;
    EditText clave;

    public Login() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_login,container,false);
        usuario = (EditText) vista.findViewById(R.id.usuarioCorreo);
        clave = (EditText) vista.findViewById(R.id.usuarioClave);
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
                    new ConsultarDatosUsusario().execute("https://asoecas.000webhostapp.com/business/actionRegistro.php?operacion=login&usuario="+usuario.getText().toString()+"&contrasena="+clave.getText().toString());
                }
            }
        }
    }
    private class ConsultarDatosUsusario extends AsyncTask<String,Void,String> {
        //Metodo para consultar los datos de la s bases de datos...
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_LONG).show();
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
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
                        Intent intent = new Intent (getContext(), Home_user.class);
                        startActivity(intent);

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
        private String downloadUrl(String myurl) throws IOException {
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
                Log.d("Respuesta", "The response is: " + response);
                is = conn.getInputStream();
                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
                return contentAsString;
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
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
}