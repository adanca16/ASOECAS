package cr.ac.una.asoecas.asoecas.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cr.ac.una.asoecas.asoecas.R;
import cr.ac.una.asoecas.asoecas.data.dataWebService;
import cr.ac.una.asoecas.asoecas.model.Usuario_Datos;

/**
 * A simple {@link Fragment} subclass.
 */
    public class AddEvento extends Fragment implements View.OnClickListener {
    private EditText cajaNombre;
    private EditText cajaDescripcion;
    private EditText cajaImagen;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
    private Date dateActual = null;
    private Date dateEvento = null;
    private int creadoPor;//Id del creador del evento
    private int estadoEvento;//Visible o no visible
    private int actividad = 0;
    private Switch cajaEstado;
    private Button btnRegistro;
    private CalendarView fechaEvento;
    //Variables para obtener la fecha
    private String fechaActividad;//Variable donde almaceno una fecha con un formato no valida
    private Calendar calendar;
    private String fechaActual;
    private EditText dirigidoPersona;
    private dataWebService data;
    public AddEvento() {
        creadoPor = Integer.parseInt( Usuario_Datos.id);
        Calendar fecha = new GregorianCalendar();
        fechaActividad = fecha.get(Calendar.YEAR)+"-"+(fecha.get(Calendar.MONTH)+1)+"-"+fecha.get(Calendar.DAY_OF_MONTH);
        fechaActual = fechaActividad;
        data = new dataWebService(getActivity());
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("REGISTRANDO UN NUEVO EVENTO");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View vista = inflater.inflate(R.layout.fragment_add_evento,container,false);
        cajaNombre = (EditText)vista.findViewById(R.id.cajaNombreEvento);
        cajaDescripcion= (EditText)vista.findViewById(R.id.cajaDescripcionEvento);
        //Click o funciom para el calendadrop
        fechaEvento= (CalendarView) vista.findViewById(R.id.cajaEventoFecha);
        fechaEvento.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                fechaActividad = year+"-"+(month+1)+"-"+dayOfMonth;
            }
        });
        cajaImagen= (EditText)vista.findViewById(R.id.cajaImagenEvento);
        dirigidoPersona = (EditText) vista.findViewById(R.id.dirigidoPersona);
        dirigidoPersona.setOnClickListener(this);
        actividad = 2;
        cajaEstado = (Switch) vista.findViewById(R.id.estadoEvento);
        btnRegistro = (Button) vista.findViewById(R.id.btn_RegisterEvento);
        btnRegistro.setOnClickListener(this);
        return vista;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_RegisterEvento) {

            if (cajaEstado.isChecked()) {
                estadoEvento = 1;
            } else {
                estadoEvento = 0;
            }
            if (validar() && validarFecha()) {
                String query ="https://asoecas.000webhostapp.com/business/actionEvento.php?operacion=insert&nombre="+cajaNombre.getText().toString()+"&descripcion="+cajaDescripcion.getText().toString()+"&fecha="+fechaActividad+"&imagen="+cajaImagen.getText().toString()+"&responsable="+creadoPor+"&estado="+estadoEvento+"&actividad="+actividad;
                String queryNew = query.replace(' ','^');
                    data.execute(queryNew);
                    waitResponce();
            }
        }   else if (v.getId() == R.id.dirigidoPersona) {
            showDialog();
        }
    }
    private boolean validar(){
        boolean enviar=true;
        if (cajaNombre.getText().toString().length()==0){
            enviar = false;
            Toast.makeText(getContext(),"No has ingresado un nombre",Toast.LENGTH_LONG).show();
        }else if (cajaDescripcion.getText().toString().length()==0){
            enviar = false;
            Toast.makeText(getContext(),"No has ingresado una descripcion del evento",Toast.LENGTH_LONG).show();
        }else if (cajaImagen.getText().toString().length()==0){
         //   enviar = false;
           // Toast.makeText(getContext(),"No has ingresado una imagen para el evento",Toast.LENGTH_LONG).show();
        }else{
            enviar = validarFecha();
        }
        return enviar;
    }
    //Comparacion si la fecha ya paso
    private  boolean validarFecha(){
        try {
            dateActual = simpleDateFormat.parse(fechaActual);
            dateEvento = simpleDateFormat.parse(fechaActividad);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        boolean fechaValida = true;
        if (dateActual.compareTo(dateEvento) > 0 ){
            fechaValida = false;
            Toast.makeText(getContext(),"Ya paso la fecha que estas ingresando, ingrese una fecha valida ",Toast.LENGTH_SHORT).show();
        }
            return fechaValida;
    }

    public void waitResponce(){
        Handler Progreso = new Handler();
        Progreso.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!data.obtenerRespuesta().isEmpty()){
                    Toast.makeText(getContext(),data.obtenerRespuesta(),Toast.LENGTH_SHORT).show();
                    limpiarImput();
                }else{
                    waitResponce();
                }
            }
        },1000);
    }

    private void limpiarImput() {
        cajaNombre.setText("");
        cajaDescripcion.setText("");
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Interesados");


        //list of items
        final String[] items = getResources().getStringArray(R.array.opciones);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actividad = (Integer)which+1;
                        dirigidoPersona.setText(items[which] );
                    }
                });
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       //
                        // positive button logic

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
        // display dialog
        dialog.show();
    }

}