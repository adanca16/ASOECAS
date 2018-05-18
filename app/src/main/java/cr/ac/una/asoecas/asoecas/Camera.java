package cr.ac.una.asoecas.asoecas;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cr.ac.una.asoecas.asoecas.model.FragmentoAbsPrincipal;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.Request.Method.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class Camera extends FragmentoAbsPrincipal implements View.OnClickListener {
    private Button button;
    private String enconde_string , image_name;
    private Bitmap bitmap;
    private File file;
    private Uri file_uri;

    public Camera() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista =inflater.inflate(R.layout.fragment_camera, container, false);
        // Inflate the layout for this fragment
        button = (Button)vista.findViewById(R.id.start);
        button.setOnClickListener(this);
        return  vista;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.start){
            image_name = "pablo_me_la.jpg";
            Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getFileUri();
            intent.putExtra(MediaStore.EXTRA_OUTPUT,file_uri);
            startActivityForResult(intent,10);

        }
    }

    private void getFileUri() {
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                +File.separator+image_name);

        file_uri = Uri.fromFile(file);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==10 && resultCode ==RESULT_OK){
            new Encode_Image().execute();
        }
    }

    private class Encode_Image extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            bitmap = BitmapFactory.decodeFile(file_uri.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] array = stream.toByteArray();
            enconde_string = Base64.encodeToString(array, 0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest();
        }
    }
    private void makeRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(POST, "https://asoecas.000webhostapp.com/business/actionImage.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("encode_string",enconde_string);
                map.put("image_name",image_name);
                return map;
            }
        };
        requestQueue.add(request);
    }

}
