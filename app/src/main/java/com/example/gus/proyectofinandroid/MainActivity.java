package com.example.gus.proyectofinandroid;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.gus.proyectofinandroid.chat.ChatMessage;
import com.example.gus.proyectofinandroid.chat.ChatMessageAdapter;
import com.example.gus.proyectofinandroid.cmds.LauncherCmds;
import com.example.gus.proyectofinandroid.cmds.ListCmdActivity;
import com.example.gus.proyectofinandroid.ocr.OcrActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;
import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.Graphmaster;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Actividad principal, funcionalidad de chat
 * @author Agustín M. Etcheverry
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    /**
     * Variables de clase
     */
    private FloatingActionButton menuSPEAK;//submenú para activar/desactivar sintesis de voz
    private FloatingActionButton menuVOZ;//submenú para activar reconocimiento de voz
    private FloatingActionButton menuOCR;//submenú para abrir la actividad de reconocimiento de caracteres
    private FloatingActionButton menuCMD;//submenú para abrir la lista de comandos
    private Button mButtonSend; //botón para enviar lo escrito
    private EditText mEditTextMessage;//donde se escribe el texto a introducir
    private ListView mListView; //litView del chat //TODO GAP-002 cambiar a reciclerview
    private ChatMessageAdapter mAdapter;//adaptador personalizado del listView
    public Bot bot;//objeto de librería AB que representa al agente conversacional
    public static Chat chat;//objeto de librería AB que representa la conversación con el bot
    private TextToSpeech textToSpeech;//objeto de librería speechTTS que permite la sintesis de voz
    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;//constante para lanzar actividad de reconocimiento de voz
    private boolean esSonidoOn;//variable sonido activado/desactivado //TODO GAP-001 pasar a preferencia
    public static Context context;//contexto de la actividad

    /**
     * Método onCreate de MainActivity sobrescrito
     * @param savedInstanceState Instancia guarda al suspender o destruir la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Instanciar variables de clase
         */
        esSonidoOn = true;//por defecto el sonido está activado //TODO GAP-001 pasar a preferencia
        context = getApplicationContext();//guardar el contexto de la actividad
        //instanciar los componentes de la interfaz gráfica
        mListView = (ListView) findViewById(R.id.listView); //TODO GAP-002 cambiar a reciclerview
        mButtonSend = (Button) findViewById(R.id.btn_send);
        menuSPEAK = (FloatingActionButton)findViewById(R.id.submenu_speak);
        menuSPEAK.setImageResource(R.drawable.speak_on);
        menuVOZ = (FloatingActionButton)findViewById(R.id.submenu_voz);
        menuOCR = (FloatingActionButton)findViewById(R.id.submenu_ocr);
        menuCMD = (FloatingActionButton)findViewById(R.id.submenu_cmd);
        mEditTextMessage = (EditText) findViewById(R.id.et_message);
        //crear y asignar adaptador a listView del chat //TODO GAP-002 cambiar a reciclerview
        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mListView.setAdapter(mAdapter);

        /**
         * Realizar la escritura de la arquitectura AIML en el dispositivo anfitrión
         */
        //TODO GAP-003 poner en un método
        isSDCARDAvailable();//comprobar si la unidad de almacenamiento está disponible
        AssetManager assets = getResources().getAssets();//recuperar los assets de la aplicación
        File jayDir = new File(Environment.getExternalStorageDirectory().toString() + "/prueba/bots/prueba");//directorio donde se guardarán los assets
        jayDir.mkdirs();//comprobar si se pueden crear directorios
        if (jayDir.exists()) {//si el directorio principal existe
            try {
                for (String dir : assets.list("prueba")) {//por cada directorio en el asset prueba
                    File subdir = new File(jayDir.getPath() + "/" + dir);//obtener directorio
                    subdir.mkdirs();//comprobar si se pueden crear directorios
                    for (String file : assets.list("prueba/" + dir)) {//por cada fichero en el directorio
                        File f = new File(jayDir.getPath() + "/" + dir + "/" + file);//obtener el fichero en el dispositivo
                        if (f.exists()) {//si existe en el dispositivo
                            continue;//continuar con el siguiente fichero
                        }
                        InputStream in = assets.open("prueba/" + dir + "/" + file);//abrir stream de lectura al fichero asset
                        OutputStream out = new FileOutputStream(jayDir.getPath() + "/" + dir + "/" + file);//abrir stream de escritura en el dispositivo
                        copyFile(in, out);//copiar el fichero en el dispositivo
                        in.close();//cerar stream de lectura
                        out.flush();//vaciar buffer de stream de escritura
                        out.close();//cerrar stream de escritura
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Preparar el agente conversacional
         */
        MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/prueba";//obtener el directorio de trabajo del bot
        AIMLProcessor.extension =  new PCAIMLProcessorExtension();//procesador de AIML de librería AB
        bot = new Bot("prueba", MagicStrings.root_path, "chat");//asignar los ficheros AIML para que las procese
        chat = new Chat(bot);//instanciar el objeto de la conversación
        textToSpeech = new TextToSpeech(this,this);//TODO GAP-004 donde va esto? condicion esSonidoOn
        MagicBooleans.trace_mode = false;//desactivar el log de depuración de la librería
        Graphmaster.enableShortCuts = true;//activar atajos

        //listener del submenú para activar/desactivar la sintetización de voz
        menuSPEAK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (esSonidoOn){//si el sonido está activado //TODO GAP-001 pasar a preferencia
                    menuSPEAK.setImageResource(R.drawable.speak_off);//imagen de sonido desactivado
                    esSonidoOn = false;//desactivar sonido
                } else {//si el sonido está desactivado
                    menuSPEAK.setImageResource(R.drawable.speak_on);//imagen de sonido activado
                    esSonidoOn = true;//activar sonido
                }
            }
        });

        //listener del submenú para activar el reconocimiento de voz
        menuVOZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActionRecognizeSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);//reconocimiento de voz
                intentActionRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");//configurar el lenguaje como español
                try {//lanzar activityForResult para recuperar el comando por voz
                    startActivityForResult(intentActionRecognizeSpeech, RECOGNIZE_SPEECH_ACTIVITY);
                } catch (ActivityNotFoundException a) {//capturar la excepción si no está disponible el micrófono
                    Toast.makeText(getApplicationContext(), "Tú dispositivo no soporta el reconocimiento por voz", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //listener del submenú para lanzar la actividad para el reconocimiento de caracteres
        menuOCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityOCR = new Intent(getApplicationContext(), OcrActivity.class);
                startActivity(activityOCR);
            }
        });

        //listener del submenú para lanzar la actividad que muestra la lista de comandos
        menuCMD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityCMD = new Intent(getApplicationContext(), ListCmdActivity.class);
                startActivity(activityCMD);
            }
        });

        //code for sending the message
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CORE - funcionalidad
                String message = mEditTextMessage.getText().toString();//recuperar mensaje escrito por el usuario
                String response = chat.multisentenceRespond(mEditTextMessage.getText().toString());//obtener respuesta del bot
                if (LauncherCmds.isCmd(response)) {//comprobar si el bot responde a un comando
                    Intent intentCmd = LauncherCmds.getIntentFromResponse(response);//obtener el intent correspondiente al comando
                    response = "Ejecutando comando";//cambiar a una respuesta estándar
                    if (intentCmd != null && intentCmd.resolveActivity(getPackageManager()) != null) {
                        startActivity(intentCmd);//lanzar el intent del comando, haciendo las comprobaciones apropiadas
                    }
                }
                if (TextUtils.isEmpty(message)) {//si el mensaje está vacío
                    return;//no hacer nada en la interfaz gráfica
                }
                //UI - interfaz gráfica
                sendMessage(message);//imprimir por pantalla el mensaje del usuario
                if (esSonidoOn){//si el sonido está activado //TODO GAP-001 pasar a preferencia
                    textToSpeech.setLanguage(new Locale("es", "español" ));//configurar el idioma a español
                    speak(response);//usar la librería para sintetizar el texto
                }
                mimicOtherMessage(response);//imprimir por pantalla el mensaje del bot
                mEditTextMessage.setText("");//vaciar el editTest
                mListView.setSelection(mAdapter.getCount() - 1);//poner el último mensaje como seleccionado
            }
        });
    }

    /**
     * Copiar el fichero assets de la estructura AIML al dispositivo
     * @param in Stream de lectura, fichero asset
     * @param out Stream de escritura, fichero en el dispositivo
     * @throws IOException Excepción al tratar con ficheros
     */
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];//buffer de escritura
        int read;//longitud del buffer
        while((read = in.read(buffer)) != -1){// si longitud de buffer -1, buffer vacío
            out.write(buffer, 0, read);//escribir buffer en dispositivo
        }
    }

    /**
     * Imprimir en la interfaz el mensaje del usuario
     * @param message Mensaje del usuario
     */
    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);//mensaje de chat del usuario
        mAdapter.add(chatMessage);//añadir mensaje al listView //TODO GAP-002 cambiar a reciclerview
    }

    /**
     * Imprimir en la interfaz el mensaje del agente conversacional
     * @param message Mensaje del bot
     */
    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false);//mensaje de char del bot
        mAdapter.add(chatMessage);//añadir mensaje al listView //TODO GAP-002 cambiar a reciclerview
    }

    /**
     * Inflar layout del menú en Toolbar
     * @param menu Componente UI que representa el menú
     * @return Siempre true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);//inflar layout del menú en toolbar
        return true;
    }

    /**
     * Listener de selección de un item en el menú
     * @param item Componente UI que representa un item del menú
     * @return Booleano si se ha seleccionado opción válida
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();//obtener ID del item seleccionado
        if (id == R.id.action_settings) {//si es action_settinds
            return true; //TODO
        }
        return super.onOptionsItemSelected(item);//llamada al método padre
    }

    /**
     * Método onInit sobrescito
     * @param status Estado de la actividad
     */
    @Override
    public void onInit(int status){
        //comprobar si el idioma que se necesita esta disponible //TODO GAP-004 condicion esSonidoOn
        if (status == TextToSpeech.LANG_MISSING_DATA | status == TextToSpeech.LANG_NOT_SUPPORTED){
            Toast.makeText(this, "ERROR LANG_MISSING_DATA | LANG_NOT_SUPPORTED", Toast.LENGTH_SHORT).show();//mostrar error por pantalla
        }
    }

    /**
     * Utilizar API speechTTS para sintesis de voz
     * @param str Texto a sintetizar
     */
    private void speak(String str) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//comprobar versión del sistema operativo
            textToSpeech.speak(str,TextToSpeech.QUEUE_FLUSH,null,null);//sintesis de voz
        } else {
            textToSpeech.speak(str, TextToSpeech.QUEUE_FLUSH, null);
        }
        textToSpeech.setSpeechRate(0.0f);//establecer velocidad de sintesis
        textToSpeech.setPitch(0.0f);//establecer el tono de sintesis
    }

    /**
     * Método onDestroy sobrescrito
     */
    @Override
    protected void onDestroy() { //TODO GAP-004 condicion esSonidoOn
        if (textToSpeech != null){//si aún esta instanciada la sintesis de voz
            textToSpeech.stop();//detenerla
            textToSpeech.shutdown();//y apagarla
        }
        super.onDestroy();
    }

    /**
     * Método onActivityResult sobrescrito
     * @param requestCode Código de petición
     * @param resultCode Código de respuesta
     * @param data Intent con datos
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {//según el código de petición
            case RECOGNIZE_SPEECH_ACTIVITY://si es por reconocimiento de voz
                if (resultCode == RESULT_OK && null != data) {//si ha ido bien
                    //obtener el texto introducido por voz
                    ArrayList<String> speech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String strSpeech2Text = speech.get(0);//obtener el string del texto
                    mEditTextMessage.setText(strSpeech2Text);//poner el texto en el editText
                }
                break;
            default:
                break;
        }
    }

    /**
     * Método estático para obtener el contexto de la actividad
     * @return Contexto de la actividad
     */
    public static Context getAppContext(){
        return context;
    }

    /**
     * Comprobar si la unidad del almacenamiento está disponible
     * @return void
     */
    public static boolean isSDCARDAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)? true :false;
    }


}
