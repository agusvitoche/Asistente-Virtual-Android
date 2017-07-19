package com.example.gus.proyectofinandroid.cmds;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.content.Context;

import com.example.gus.proyectofinandroid.MainActivity;

/**
 * Devuelve a la actividad el intent del comando introducido
 * @author Agustín M. Etcheverry Vitolo
 * @version 1.0
 */
public class LauncherCmds {

    /**
     * Obtener el intent correspondiente al comando
     * @param response Respuesta del bot a un comando
     * @return Intent correspondiente al comando
     */
    public static Intent getIntentFromResponse(String response){
        Intent intentComando = null;//intent que se devolverá
        if (response.contains("intentAlarma")) {//si es para abrir WhatsApp
            Context context = MainActivity.getAppContext();
            intentComando = getIntentWhatsApp(context);
        }
        if (response.contains("intentAlarma")) {//si es para crear un alarma
            String[] parameters = response.split(":");//hora y minutos de la alarma
            intentComando = getIntentAlarma("alarma", Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));
        }
        if (response.contains("intentTemporizador")) {//si es para crear un temporizador
            String[] parameters = response.split(":");//segundos del temporizador
            intentComando = getIntentTemporizador("temporizador", Integer.parseInt(parameters[1]));
        }
        if (response.contains("intentVerAlarmas")) //si es para ver las alarmas
            intentComando = getIntentVerAlarmas();
        if (response.contains("intentDial")){ //si es para marcar un teléfono
            String[] parameters = response.split(":");//telefono a marcar
            intentComando = getIntentDial(parameters[1]);
        }
        if (response.contains("intentLlamada")){//si es para llamar a un teléfono
            String[] parameters = response.split(":");//telefono al que llamar
            intentComando = getIntentLlamada(parameters[1]);
        }
        if (response.contains("intentFoto")) //si es para abrir la cámara para foto
            intentComando = getIntentFoto();
        if (response.contains("intentVideo")) //si es para abrir la cámara para vídeo
            intentComando = getIntentVideo();
        if (response.contains("intentWebSearch")){//si es para buscar en internet
            String[] parameters = response.split(":");//termino a buscar
            intentComando = getIntentWebSearch(parameters[1]);
        }
        if (response.contains("intentYoutubeSearch")){//si es para buscar en youtube
            String[] parameters = response.split(":");//termino a buscar
            intentComando = getIntentYoutubeSearch(parameters[1]);
        }
        if (response.contains("intentWebDefine")){//si es para definir una palabra
            String[] parameters = response.split(":");//termino a definir
            intentComando = getIntentWebSearch("define "+parameters[1]);//añadir define a la búsqueda
        }
        if (response.contains("intentWebPage")){//si es para abrir una url
            String[] parameters = response.split(":");//url a abrir
            intentComando = getIntentWebPage(parameters[1]);
        }
        if (response.contains("intentSettings"))//configuración
            intentComando = getIntentSettings();
        if (response.contains("intentWifiSettings"))//configuración wifi
            intentComando = getIntentWifiSettings();
        if (response.contains("intentAirplaneModeSettings"))//configuración modo avión
            intentComando = getIntentAirplaneModeSettings();
        if (response.contains("intentBluetoothSettings"))//configuración bluetooth
            intentComando = getIntentBluetoothSettings();
        if (response.contains("intentDateSettings"))//configuración fecha
            intentComando = getIntentDateSettings();
        if (response.contains("intentInputMethodSettings"))//configuración teclado
            intentComando = getIntentInputMethodSettings();
        if (response.contains("intentDisplaySettings"))//configuración pantalla
            intentComando = getIntentDisplaySettings();
        if (response.contains("intentSecuritySettings"))//configuración seguridad
            intentComando = getIntentSecuritySettings();
        //devolver el intent correspondiente al comando
        return intentComando;
    }

    /**
     * Comprobar si la respuesta del bot es a un comando
     * @param response Respuesta del bot
     * @return Si es un comando
     */
    public static Boolean isCmd(String response){
        Boolean esComando = false;
        if (response.contains("intent"))//si la respuesta contiene intent
            esComando = true;//es un comando
        return esComando;
    }

    /**
     * Obtener el intent para crear una alarma
     * @param message Mensaje que muestra la alarma
     * @param hour Hora de la alarma
     * @param minutes Minutos de la alarma
     * @return Intent
     */
    private static Intent getIntentAlarma(String message, int hour, int minutes){
        Intent intentAlarma = new Intent(AlarmClock.ACTION_SET_ALARM);
        intentAlarma.putExtra(AlarmClock.EXTRA_MESSAGE, message);//mensaje de la alarma
        intentAlarma.putExtra(AlarmClock.EXTRA_HOUR, hour);//hora a la que sonará
        intentAlarma.putExtra(AlarmClock.EXTRA_MINUTES, minutes);//minutos
        return intentAlarma;
    }

    /**
     * Obtener el intent para crear un temporizador
     * @param message Mensaje que muestra el temporizador
     * @param seconds Segundos del temporizador
     * @return Intent
     */
    private static Intent getIntentTemporizador(String message, int seconds){
        Intent intentTemporizador = new Intent(AlarmClock.ACTION_SET_TIMER);
        intentTemporizador.putExtra(AlarmClock.EXTRA_MESSAGE, message);//mensaje que mostrará
        intentTemporizador.putExtra(AlarmClock.EXTRA_LENGTH, seconds);//segundos que durará
        intentTemporizador.putExtra(AlarmClock.EXTRA_SKIP_UI, true);//solo mostrar notificación
        return intentTemporizador;
    }

    /**
     * Obtener el intent para ver las alarmas
     * @return Intent
     */
    private static Intent getIntentVerAlarmas(){
        Intent intentVerAlarmas = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        return intentVerAlarmas;
    }

    /**
     * Obtener el intent para abrir la cámara en modo foto
     * @return Intent
     */
    private static Intent getIntentFoto(){
        Intent intentFoto = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        return intentFoto;
    }

    /**
     * Obtener el intent para abrir la cámara en modo vídeo
     * @return Intent
     */
    private static Intent getIntentVideo(){
        Intent intentVideo = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
        return intentVideo;
    }

    /**
     * Obtener el intent para marcar un teléfono
     * @param phoneNumber Número de teléfono
     * @return Intent
     */
    private static Intent getIntentDial(String phoneNumber){
        Intent intentDial = new Intent(Intent.ACTION_DIAL);
        intentDial.setData(Uri.parse("tel:" + phoneNumber));//número de teléfono
        return intentDial;
    }

    /**
     * Obtener el intent para llamar a un teléfono
     * @param phoneNumber Número de teléfono
     * @return Intent
     */
    private static Intent getIntentLlamada(String phoneNumber){
        Intent intentLlamada = new Intent(Intent.ACTION_CALL);
        intentLlamada.setData(Uri.parse("tel:" + phoneNumber));//número de teléfono
        return intentLlamada;
    }

    /**
     * Obtener el intent para buscar en internet
     * @param query Término de la búsqueda
     * @return Intent
     */
    private static Intent getIntentWebSearch(String query){
        Intent intentWebSearch = new Intent(Intent.ACTION_SEARCH);
        intentWebSearch.putExtra(SearchManager.QUERY, query);//término de la búsqueda
        return intentWebSearch;
    }

    /**
     * Obtener el intent para abrir una url
     * @param url URL a abrir
     * @return Intent
     */
    private static Intent getIntentWebPage(String url){
        Uri webpage = Uri.parse(url);//castear string a uri
        Intent intentWebPage = new Intent(Intent.ACTION_VIEW, webpage);
        return intentWebPage;
    }

    /**
     * Obtener el intent para abrir la configuración del dispositivo
     * @return Intent
     */
    private static Intent getIntentSettings(){
        Intent intentSettings = new Intent(Settings.ACTION_SETTINGS);
        return intentSettings;
    }

    /**
     * Obtener el intent para abrir la configuración del wifi
     * @return Intent
     */
    private static Intent getIntentWifiSettings(){
        Intent intentWifiSettings = new Intent(Settings.ACTION_WIFI_SETTINGS);
        return intentWifiSettings;
    }

    /**
     * Obtener el intent para abrir la configuración del modo avión
     * @return Intent
     */
    private static Intent getIntentAirplaneModeSettings(){
        Intent intentAirplaneModeSettings = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        return intentAirplaneModeSettings;
    }

    /**
     * Obtener el intent para abrir la configuración del bluetooth
     * @return Intent
     */
    private static Intent getIntentBluetoothSettings(){
        Intent intentBluetoothSettings = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        return intentBluetoothSettings;
    }

    /**
     * Obtener el intent para abrir la configuración de la fecha y hora
     * @return Intent
     */
    private static Intent getIntentDateSettings(){
        Intent intentDateSettings = new Intent(Settings.ACTION_DATE_SETTINGS);
        return intentDateSettings;
    }

    /**
     * Obtener el intent para abrir la configuración del teclado
     * @return Intent
     */
    private static Intent getIntentInputMethodSettings(){
        Intent intentInputMethodSettings = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
        return intentInputMethodSettings;
    }

    /**
     * Obtener el intent para abrir la configuración de la pantalla
     * @return Intent
     */
    private static Intent getIntentDisplaySettings(){
        Intent intentDisplaySettings = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
        return intentDisplaySettings;
    }

    /**
     * Obtener el intent para abrir la configuración de seguridad
     * @return Intent
     */
    private static Intent getIntentSecuritySettings(){
        Intent intentSecuritySettings = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        return intentSecuritySettings;
    }

    /**
     * Obtener el intent para abrir WhatsApp
     * @param context Contexto de la actividad
     * @return Intent
     */
    private static Intent getIntentWhatsApp(Context context){
        //es necesario el contexto para obtener getPackageManager
        Intent intentWhatsApp = context.getPackageManager().getLaunchIntentForPackage("com.whatsapp");//paquete de la aplicación WhatsApp
        return intentWhatsApp;
    }

    /**
     * Obtener el intent para buscar en youtube
     * @return Intent
     */
    private static Intent getIntentYoutubeSearch(String query){
        Intent intentYoutubeSearch = new Intent(Intent.ACTION_SEARCH);
        intentYoutubeSearch.setPackage("com.google.android.youtube");//establecer que la búsqueda será en youtube
        intentYoutubeSearch.putExtra("query", query);//término de la búsqueda
        return intentYoutubeSearch;
    }

}
