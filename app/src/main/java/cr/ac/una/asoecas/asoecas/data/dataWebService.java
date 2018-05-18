package cr.ac.una.asoecas.asoecas.data;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import cr.ac.una.asoecas.asoecas.R;

/**
 * Created by ADAN on 13/05/2018.
 */

public class dataWebService extends AsyncTask<String,Void,String> {
    String resultado ;
    boolean haveConection;
    public dataWebService(Activity activity){
        haveConection = isOnlineNet();
        resultado = "";
    }

    public Boolean isOnlineNetTrue() {
        return haveConection;
    }
    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");
            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected String doInBackground(String... urls) {
        return downloadUrl(urls[0]);
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(result , Toast.LENGTH_LONG).show();
     //   Toast.makeText(getContext(),data.obtenerRespuesta(),Toast.LENGTH_LONG).show();
        resultado = result;
    }
    public String obtenerRespuesta(){
        return resultado;
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
            Log.d("Respuesta", "The response is: " + response);

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            Log.d("Respuesta", "The contetn As String " + contentAsString);
            return contentAsString;
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (IOException e) {
            Log.d("Error", "Error " + e.getMessage());
        }
        finally {
            try {
                is.close();
            }catch (Exception err){
                Log.d("Error", "Error al cerrar la conexion " + err.getMessage());
            }
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