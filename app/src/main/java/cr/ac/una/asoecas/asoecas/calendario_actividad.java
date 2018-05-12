package cr.ac.una.asoecas.asoecas;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ADAN on 24/04/2018.
 */

/**
 Fragemento para mostrar los eventos de futbol.
 */
@SuppressLint("ValidFragment")
public class calendario_actividad extends Fragment {


    Toast toast;
    CalendarView calender;
    // TextView textview;
    String fechaActividad;
    TextView textViewEventos;

    int actividad;
    String tituloFragmento;

    public calendario_actividad(int opcionMostrar, String titulo) {
        actividad = opcionMostrar;
        tituloFragmento = titulo;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(tituloFragmento);//Agrego un titulo al fragemento
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_calendario_actividad,container,false);
        calender = (CalendarView) vista.findViewById(R.id.calendarView1);

        textViewEventos =(TextView)vista.findViewById(R.id.textViewEventos);//Caja para los evenetos

        Calendar fecha = new GregorianCalendar();
        fechaActividad = fecha.get(Calendar.YEAR)+"-"+(fecha.get(Calendar.MONTH)+1)+"-"+fecha.get(Calendar.DAY_OF_MONTH);

        new ConsultarDatosEventos().execute("https://asoecas.000webhostapp.com/business/actionEvento.php?operacion=select&fecha="+fechaActividad+"&actividad="+actividad);

        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                cargando();
                // TODO Auto-generated method stub
                fechaActividad = year+"-"+(month+1)+"-"+dayOfMonth;
                new ConsultarDatosEventos().execute("https://asoecas.000webhostapp.com/business/actionEvento.php?operacion=select&fecha="+fechaActividad+"&actividad="+actividad);

            }
        });
        return  vista;
    }
    private void  cargando(){

    }
    private class ConsultarDatosEventos  extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {
            return downloadUrl(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        // onPostExecute displays the results of the AsyncTask.
        protected void onPostExecute(String result) {
            String salida = "No hay eventos para el dia ".toUpperCase()+fechaActividad;
            try {
                JSONArray array = new JSONArray(result);
                if(array.length()!=0){
                    salida = "";
                    for (int i = 0 ; i < array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        salida+= "\t\t"+jsonObject.getString("nombre").toUpperCase().replace('^',' ')+"\n\n";
                        salida+= "Descripcion: "+jsonObject.getString("descripcion").replace('^',' ')+"\n";
                        salida+= "Creado por: "+jsonObject.getString("nombreUsuario").replace('^',' ')+"\n\n\n";
                    }
                }
                textViewEventos.setText(salida);
            } catch (Exception e) {
                toast =Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_LONG);
                //toast =Toast.makeText(getContext(), "Error "+result, Toast.LENGTH_LONG);
                toast.show();
                e.printStackTrace();
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
}