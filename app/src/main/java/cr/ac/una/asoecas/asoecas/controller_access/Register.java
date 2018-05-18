package cr.ac.una.asoecas.asoecas.controller_access;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;

import java.util.Map;

import cr.ac.una.asoecas.asoecas.R;
import cr.ac.una.asoecas.asoecas.Upload_image;
import cr.ac.una.asoecas.asoecas.data.dataWebService;
import cr.ac.una.asoecas.asoecas.model.FragmentoAbsPrincipal;


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

    ImageView imagenUsuario;
    Button btnImagen;

    EditText cajaClave1;
    EditText cajaClave2;
    private dataWebService data;
    private int actividad;
    Button btnRegistro;
///Propiedades para la camara
    String path;
     MagicalPermissions magicalPermissions;
     MagicalCamera magicalCamera;
     private final static int RESIZE_PHOTO_PIXELS_PERCENTAGE = 50;
    Toast toast;
    public Register() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Registro ASOECAS");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_register,container,false);
        actividad = 0;
        path = "";
        data = new dataWebService(getActivity());
        cajaCedula = (EditText)vista.findViewById(R.id.cajaCedula);
        //cajaCedula.setOnFocusChangeListener(this);
        cajaNombre = (EditText)vista.findViewById(R.id.cajaNombre);
        cajaApellido= (EditText)vista.findViewById(R.id.cajaApellido);
        cajaCarrera= (EditText)vista.findViewById(R.id.cajaCarrera);
        cajaCarrera.setOnClickListener(this);
         cajaCorreo= (EditText)vista.findViewById(R.id.cajaCorreo);
         cajaTelefono= (EditText)vista.findViewById(R.id.cajaTelefono);

        imagenUsuario= (ImageView) vista.findViewById(R.id.imagenUsuarioRegistro);
        btnImagen = (Button) vista.findViewById(R.id.btnImagen);
        btnImagen.setOnClickListener(this);
        cajaClave1 = (EditText)vista.findViewById(R.id.cajaClave1);
        cajaClave2 = (EditText)vista.findViewById(R.id.cajaClave2);

         btnRegistro = (Button) vista.findViewById(R.id.btn_Register);
        btnRegistro.setOnClickListener(this);

//Lanzo los permisos para poder usuar la camara
        String[] permissions = new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        magicalPermissions = new MagicalPermissions(this, permissions);
        magicalCamera = new MagicalCamera(getActivity(),RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions);

        return vista;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Register) {
          if (validarInput()){
              data.execute("https://asoecas.000webhostapp.com/business/actionRegistro.php?operacion=insert&cedula="+cajaCedula.getText().toString()+"&nombre="+cajaNombre.getText().toString()+"&apellido="+cajaApellido.getText().toString()+"&correo="+cajaCorreo.getText().toString()+"&carrera="+actividad+"&telefono="+cajaTelefono.getText().toString()+"&imagen="+path+"&clave="+cajaClave1.getText().toString());
       //       data.execute("http://localhost:81/Api-android//business/actionRegistro.php?operacion=insert&cedula="+cajaCedula.getText().toString()+"&nombre="+cajaNombre.getText().toString()+"&apellido="+cajaApellido.getText().toString()+"&correo="+cajaCorreo.getText().toString()+"&carrera="+actividad+"&telefono="+cajaTelefono.getText().toString()+"&imagen=imagenUsuario&clave="+cajaClave1.getText().toString());
              waitResponce();
          }else{
              toast =Toast.makeText(getContext(), "Verifique los datos", Toast.LENGTH_LONG);
              toast.show();
          }
        }else if(v.getId() == R.id.cajaCarrera){
            showDialog();
       //     Toast.makeText(getContext(),actividad,Toast.LENGTH_LONG).show();
        } else if(v.getId() == R.id.btnImagen){
            //Lanzo la camara
            new Upload_image();
            //magicalCamera.takePhoto();
    }
    }
// Metodo para obtener la respuesta que genero la camara..
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Map<String, Boolean> map = magicalPermissions.permissionResult(requestCode, permissions, grantResults);
        for (String permission : map.keySet()) {
            Log.e("PERMISSIONS", permission + " was: " + map.get(permission));
        }
        //Following the example you could also
        //locationPermissions(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//Para saber si el resultado optenido tuvo exito!!!!
        //if(resultCode==RESULT_OK) {
            //CALL THIS METHOD EVER
            magicalCamera.resultPhoto(requestCode, resultCode, data);
            //this is for rotate picture in this method
            //magicalCamera.resultPhoto(requestCode, resultCode, data, MagicalCamera.ORIENTATION_ROTATE_180);

            //with this form you obtain the bitmap (in this example set this bitmap in image view)
            imagenUsuario.setImageBitmap(magicalCamera.getPhoto());

            //if you need save your bitmap in device use this method and return the path if you need this
            //You need to send, the bitmap picture, the photo name, the directory name, the picture type, and autoincrement photo name if           //you need this send true, else you have the posibility or realize your standard name for your pictures.
            path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(), "Adan", "ASOECAS", MagicalCamera.JPEG, true);

            if (path != null) {
                Toast.makeText(getContext(), "The photo is save in device, please check this path: " + path, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Sorry your photo dont write in devide, please contact with fabian7593@gmail and say this error", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getContext(), "Sorry your photo dont write in devide, please contact with fabian7593@gmail and say this error", Toast.LENGTH_SHORT).show();
       // }
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
        builder.setTitle("Seleccione su carrera");

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
        //cajaImagen.setText("");
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

