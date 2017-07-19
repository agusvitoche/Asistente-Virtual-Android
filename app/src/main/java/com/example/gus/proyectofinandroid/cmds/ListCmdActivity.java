package com.example.gus.proyectofinandroid.cmds;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import com.example.gus.proyectofinandroid.MainActivity;
import com.example.gus.proyectofinandroid.R;
import com.example.gus.proyectofinandroid.ocr.OcrActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Lista con los comandos disponibles en la aplicación
 * @author Agustín M. Etcheverry
 * @version 1.0
 */
public class ListCmdActivity extends AppCompatActivity {

    /**
     * Variables de clase
     */
    private FloatingActionButton menuCHAT;//submenú para lanzar la actividad principal
    private FloatingActionButton menuOCR;//submenú para abrir la actividad de reconocimiento de caracteres
    private ExpandableListView expListView;//componente UI de la lista desplegable
    private ExpandableListAdapter listAdapter;//adaptador para la lista desplegable
    List<String> listDataHeader;//Lista de elementos padre
    HashMap<String, List<String>> listDataChild;//Map de elementos padre con sus elementos hijo
    //array con las referencias a las imagenes de la lista
    public static final Integer[] images = {
            R.drawable.cmd_whatsapp,//whatsapp
            R.drawable.cmd_alarm,//ver alarmas
            R.drawable.cmd_alarm,//crear alarmas
            R.drawable.cmd_timer,//temporizador
            R.drawable.cmd_photo,//foto
            R.drawable.cmd_video,//video
            R.drawable.cmd_dial,//marcar
            R.drawable.cmd_call,//llamar
            R.drawable.cmd_search,//buscar
            R.drawable.cmd_youtube,//youtube
            R.drawable.cmd_define,//definir
            R.drawable.cmd_web,//abrir url
            R.drawable.cmd_time,//fecha y hora
            R.drawable.cmd_settings//configuración
    };

    /**
     * Método onCreate de ListCmdActivity sobrescrito
     * @param savedInstanceState Instancia guarda al suspender o destruir la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cmd);

        /**
         * Instanciar variables de clase
         */
        expListView = (ExpandableListView) findViewById(R.id.listCmd);
        menuCHAT = (FloatingActionButton)findViewById(R.id.submenu_chat);
        menuOCR = (FloatingActionButton)findViewById(R.id.submenu_ocr);

        prepareListData();//preparar los datos de la lista
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);//establecer datos de la lista
        expListView.setAdapter(listAdapter);//establecer adaptador de la lista desplegable

        //listener del submenú para lanzar la actividad principal
        menuCHAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityOCR = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(activityOCR);
            }
        });

        //listener del submenú para abrir la actividad de reconocimiento de caracteres
        menuOCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityCMD = new Intent(getApplicationContext(), OcrActivity.class);
                startActivity(activityCMD);
            }
        });
    }

    /**
     * Preparar los datos para la lista desplegable
     */
    private void prepareListData() {
        //lista de elementos
        listDataHeader = new ArrayList<>();//lista de elementos padre
        listDataChild = new HashMap<>();//mapa de lementos padre con sus elementos hijo

        //añadir elementos padre
        listDataHeader.add("WhatsApp");
        listDataHeader.add("Ver alarmas");
        listDataHeader.add("Poner alarma");
        listDataHeader.add("Temporizador");
        listDataHeader.add("Sacar foto");
        listDataHeader.add("Grabar vídeo");
        listDataHeader.add("Marcar teléfono");
        listDataHeader.add("Llamar teléfono");
        listDataHeader.add("Buscar en internet");
        listDataHeader.add("Buscar en youtube");
        listDataHeader.add("Define");
        listDataHeader.add("Abrir URL");
        listDataHeader.add("Fecha y hora");
        listDataHeader.add("Configuración");

        //añadir elementos hijo
        List<String> whatsApp = new ArrayList<>();//whatsapp
        whatsApp.add("Abrir WhatsApp");
        List<String> verAlarmas = new ArrayList<>();//ver alarmas
        verAlarmas.add("Enseñame las alarmas");
        verAlarmas.add("Enseñame todas las alarmas");
        verAlarmas.add("Ver alarmas");
        verAlarmas.add("Ver todas las alarmas");
        List<String> ponerAlarma = new ArrayList<>();//crear alarma
        ponerAlarma.add("Alarma a las (hora) y (minutos)");
        ponerAlarma.add("Necesito una alarma a las (hora) y (minutos)");
        ponerAlarma.add("Pon una alarma a las (hora) y (minutos)");
        ponerAlarma.add("Puedes poner una alarma a las (hora) y (minutos)");
        List<String> temporizador = new ArrayList<>();//temporizador
        temporizador.add("Avísame en (segundos) segundos");
        temporizador.add("Pon un temporizador de (segundos)");
        temporizador.add("Pon un temporizador de (segundos) segundos");
        temporizador.add("Temporizador de (segundos)");
        temporizador.add("Temporizador de (segundos) segundos");
        List<String> foto = new ArrayList<>();//foto
        foto.add("Abre la cámara");
        foto.add("Abrir cámara");
        foto.add("Abrir la cámara");
        foto.add("Foto");
        foto.add("Sacar foto");
        foto.add("Tomar foto");
        List<String> video = new ArrayList<>();//video
        video.add("Grabar vídeo");
        video.add("Vídeo");
        List<String> dial = new ArrayList<>();//marcar
        dial.add("Dial (número de teléfono)");
        dial.add("Marca número de teléfono (número de teléfono)");
        dial.add("Marca el teléfono (número de teléfono)");
        dial.add("Marcar número de teléfono (número de teléfono)");
        dial.add("Marcar teléfono (número de teléfono)");
        List<String> call = new ArrayList<>();//llamar
        call.add("Llama al número de teléfono (número de teléfono)");
        call.add("Llama al teléfono (número de teléfono)");
        call.add("Llamar a teléfono (número de teléfono)");
        call.add("Llamar al teléfono (número de teléfono)");
        call.add("Llamar número de teléfono (número de teléfono)");
        call.add("Llamar teléfono (número de teléfono)");
        List<String> webSearch = new ArrayList<>();//buscar
        webSearch.add("Busca en internet (objeto de búsqueda)");
        webSearch.add("Busca en Google (objeto de búsqueda)");
        webSearch.add("Buscar (objeto de búsqueda)");
        webSearch.add("Buscar en Google (objeto de búsqueda)");
        webSearch.add("Buscar en internet (objeto de búsqueda)");
        List<String> youtubeSearch = new ArrayList<>();//youtube
        youtubeSearch.add("Buscar en youtube (objeto de búsqueda)");
        List<String> webDefine = new ArrayList<>();//definir
        webDefine.add("Busca la definición de (objeto de búsqueda)");
        webDefine.add("Define (objeto de búsqueda)");
        webDefine.add("Definir (objeto de búsqueda)");
        webDefine.add("¿Qué significa (objeto de búsqueda)?");
        List<String> webPage = new ArrayList<>();//abrir url
        webPage.add("Abre esta dirección web (url)");
        webPage.add("Abre la url (url)");
        webPage.add("Abrir url (url)");
        List<String> dateHour = new ArrayList<>();//fecha y hora
        dateHour.add("Hora");
        dateHour.add("¿Qué hora es?");
        dateHour.add("Día");
        dateHour.add("¿Qué día es hoy?");
        dateHour.add("Mes");
        dateHour.add("¿En qué mes estamos?");
        dateHour.add("Año");
        dateHour.add("¿En qué año estamos?");
        dateHour.add("Fecha");
        dateHour.add("Fecha y hora");
        List<String> settings = new ArrayList<>();//configuración
        settings.add("Abre configuración");
        settings.add("Abre configuración del dispositivo");
        settings.add("Abrir configuración");
        settings.add("Abrir configuración del dispositivo");
        settings.add("Abrir ajustes del dispositivo");
        settings.add("Ajustes");
        settings.add("Ajustes del dispositivo");
        settings.add("Configuración");
        settings.add("Configuración del dispositivo");
        settings.add("WiFi");
        settings.add("Modo Avión");
        settings.add("Bluetooth");
        settings.add("Fecha");
        settings.add("Teclado");
        settings.add("Pantalla");
        settings.add("Seguridad");

        //asignar los elementos hijo a sus padres en el map
        listDataChild.put(listDataHeader.get(0), whatsApp);
        listDataChild.put(listDataHeader.get(1), verAlarmas);
        listDataChild.put(listDataHeader.get(2), ponerAlarma);
        listDataChild.put(listDataHeader.get(3), temporizador);
        listDataChild.put(listDataHeader.get(4), foto);
        listDataChild.put(listDataHeader.get(5), video);
        listDataChild.put(listDataHeader.get(6), dial);
        listDataChild.put(listDataHeader.get(7), call);
        listDataChild.put(listDataHeader.get(8), webSearch);
        listDataChild.put(listDataHeader.get(9), youtubeSearch);
        listDataChild.put(listDataHeader.get(10), webDefine);
        listDataChild.put(listDataHeader.get(11), webPage);
        listDataChild.put(listDataHeader.get(12), dateHour);
        listDataChild.put(listDataHeader.get(13), settings);
    }
}
