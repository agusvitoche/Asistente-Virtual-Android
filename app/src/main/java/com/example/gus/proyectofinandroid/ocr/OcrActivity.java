package com.example.gus.proyectofinandroid.ocr;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.gus.proyectofinandroid.MainActivity;
import com.example.gus.proyectofinandroid.R;
import com.example.gus.proyectofinandroid.cmds.ListCmdActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Actividad para el reconocimiento optico de caracteres
 * @author Agustín M. Etcheverry Vitolo
 * @version 1.0
 */
public class OcrActivity extends AppCompatActivity {

    /**
     * Variables de clase
     */
    private FloatingActionButton menuCHAT;//submenú para lanzar la actividad principal
    private FloatingActionButton menuCMD;//submenú para abrir la lista de comandos
    private static String APP_DIRECTORY = "ProyectoFinAndroid/";//ruta del directorio principal de la app
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";//ruta del directorio de imagenes
    private String mPath;//ruta al directorio de imagenes de la aplicación
    private String datapath = "";//ruta del fichero de configuración de la API Tesseract
    private final int PHOTO_CODE = 200;//constante para lanzar la cáma y tomar foto
    private final int SELECT_PICTURE = 300;//constante para seleccionar una foto de la galería
    private Spinner spinner = null;//spinner de selección de idioma //TODO GAP-005 traductor
    private RelativeLayout relativeLayoutImage = null;//layout de la imagen a analizar
    private ImageView imageView = null;//contenedor de la imagen a analizar
    private Bitmap image = null;//imagen a analizar
    private TessBaseAPI mTess = null;//API Tesseract

    /**
     * Método onCreate de OcrActivity sobrescrito
     * @param savedInstanceState Instancia guarda al suspender o destruir la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        /**
         * Instanciar variables de clase
         */
        final TextView txtOCR = (TextView)findViewById(R.id.OCRTextView);
        Button btnOCR = (Button) findViewById(R.id.OCRbutton);
        imageView = (ImageView)findViewById(R.id.imageView);
        spinner = (Spinner)findViewById(R.id.translatePicklist);
        relativeLayoutImage = (RelativeLayout) findViewById(R.id.ImageContainer);
        menuCHAT = (FloatingActionButton)findViewById(R.id.submenu_chat);
        menuCMD = (FloatingActionButton)findViewById(R.id.submenu_cmd);

        //copiar ficheros de configuración de la API Tesseract si es necesario
        datapath = getFilesDir() + "/tesseract/";//directorio donde debería estar
        checkFile(new File(datapath + "tessdata/"));//método que comprueba si esta el fichero

        //iniciar API Tesseract
        String lang = "spa";//idioma a reconocer
        mTess = new TessBaseAPI();//instanciar objeto de reconocimiento
        mTess.init(datapath, lang);//iniciar el objeto pasando el idioma y el fichero de configuración

        //cargar los datos para el spinner
        final String[] idiomas = new String[]{"TRANSLATE", "Español","Inglés","Francés","Italiano","Portugués"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, idiomas);//adaptador para strings
        spinner.setAdapter(adaptador);//establecer el adaptador al spinner

        //volver a poner imagen por defecto
        imageView.setImageResource(R.drawable.test_image);
        //listener para el contenedor de la imagen
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();//lanzar dialogo de selección de imagen
            }
        });

        //listener del botón que inicia el reconocimiento de caracteres
        btnOCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ocrResult;
                mTess.setImage(image);//pasar a la API la imagen a analizar
                ocrResult = mTess.getUTF8Text();//obtener el string en UTF-8
                txtOCR.setText(ocrResult);//imprimir por pantalla lo reconocido
            }
        });

        //listener del spinner //TODO
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //listener del submenú para lanzar la actividad principal
        menuCHAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityOCR = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(activityOCR);
            }
        });

        //listener del submenú para abrir la lista de comandos
        menuCMD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityCMD = new Intent(getApplicationContext(), ListCmdActivity.class);
                startActivity(activityCMD);
            }
        });
    }

    /**
     * Comprobar si los ficheros de configuración de Tesseract están en el dispositivo
     * @param dir Directorio donde deberían estar los ficheros de configuración
     */
    private void checkFile(File dir) {
        if (!dir.exists() && dir.mkdirs()) //si el directorio no existe pero se puede crear
            copyFiles();//copiar los ficheros de configuración
        if (dir.exists()){//si el directorio existe pero no está el fichero
            String dataFilePath = datapath + "/tessdata/spa.traineddata";//ruta al fichero de configuración
            File dataFile = new File(dataFilePath);//fichero en el dispositivo
            if (!dataFile.exists())//si el fichero no esta en el dispositivo
                copyFiles();//copiar ficheros de configuración
        }
    }

    /**
     * Método para copiar los ficheros de configuración de Tesseract al dispositivo
     */
    private void copyFiles(){
        try {
            String filePath = datapath + "/tessdata/spa.traineddata";//directorio donde copiar los ficheros
            AssetManager assetManager = getAssets();//obtener los assets
            InputStream inputStream = assetManager.open("tessdata/spa.traineddata");//abrir stream de lectura en el asset
            OutputStream outputStream = new FileOutputStream(filePath);//abrir stream de escritura en el dispositivo
            //copiar los ficheros
            byte[] buffer = new byte[1024];//buffer de escritura
            int read;//longitud del buffer
            while ((read = inputStream.read(buffer)) != -1) {//mientras el buffer no este vacío
                outputStream.write(buffer, 0, read);//escribir en el dispositivo
            }
            outputStream.flush();//vaciar buffer
            outputStream.close();//cerrar stream de escritura
            inputStream.close();//cerrar stream de salida
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * Redimensionar la imagen capturada o recuperada para adaptarla al layout
     * @param mBitmap La imagen a redimensionar
     * @param newWidth Nueva anchura
     * @param newHeigth Nueva altura
     * @return Imagen redimensionada
     */
    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, double newWidth, double newHeigth){
        int width = mBitmap.getWidth();//anchura actual
        int height = mBitmap.getHeight();//altura actual
        float scaleWidth = ((float)newWidth) / width;//ratio de escala de anchura
        float scaleHeight = ((float)newHeigth) / height;//ratio de escala de altura
        Matrix matrix = new Matrix();//crear un matrix para manipular el bitmap
        matrix.postScale(scaleWidth, scaleHeight);//redimensionar el matrix
        //volver a crear el bitmap con el matrix
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);//devolver imagen redimensionada
    }

    /**
     * Mostrar el diálogo para elegir entre la cámara o la galería
     */
    private void showOptions() {
        final CharSequence[] option = {"Cámara", "Galería", "Cancelar"};//opciones
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OcrActivity.this);//constructor del diálogo
        dialogBuilder.setTitle("Elige una opción");//título del diálogo
        dialogBuilder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(option[which] == "Cámara"){//si se selecciona la cámara
                    openCamera();//abrir la cámara
                }else if(option[which] == "Galería"){//si se selecciona la galería
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");//establecer selecciona de una imagen en el dispositivo
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);//lanzar intent
                }else {//si selecciona cancelar
                    dialog.dismiss();//cancelar diálogo
                }
            }
        });
        dialogBuilder.show();//mostrar dialogo
    }

    /**
     * Método para abrir la cámara del dispositivo y recoger la imagen tomada
     */
    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);//directorio de imagenes de la aplicación
        boolean isDirectoryCreated = file.exists();
        if(!isDirectoryCreated) //si el directorio no existe
            isDirectoryCreated = file.mkdirs();//ver si se pueden crear directorios
        if(isDirectoryCreated){ //si se pueden crear directorios
            Long timestamp = System.currentTimeMillis() / 1000;//obtener un nombre aleatorio
            String imageName = timestamp.toString() + ".jpg";//nombre aleatorio a string
            //ruta donde se guardará la foto
            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator + imageName;
            File newFile = new File(mPath);//fichero de la foto tomada
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//intent de camara
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));//pasar uri donde irá la foto
            startActivityForResult(intent, PHOTO_CODE);//lanzar intent
        }
    }

    /**
     * Método onSaveInstanceState sobrescrito
     * @param outState Instancia de la actividad
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);//llamada al metodo padre
        outState.putString("file_path", mPath);//guardar el directorio de la foto tomada
    }

    /**
     * Método onRestoreInstanceState sobrescrito
     * @param savedInstanceState Instancia de la actividad
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);//llamada al metodo padre
        mPath = savedInstanceState.getString("file_path");//recuperar el directorio de la foto tomada
    }

    /**
     * Método onActivityResult sobrescrito
     * @param requestCode Código de petición
     * @param resultCode Código de respuesta
     * @param data Intent con datos
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);//llamada al método padre
        if(resultCode == RESULT_OK){//si ha ido bien
            Bitmap imagenRed;//imagen que irá en el layout
            //cuanto se tiene que cambiar el ancho según el alto, para que no se distorsione
            Double ratioRedimension = ((double)relativeLayoutImage.getHeight())/((double)relativeLayoutImage.getWidth());
            switch (requestCode){//según el código de petición
                case PHOTO_CODE://si es de la cámara
                    MediaScannerConnection.scanFile(this, new String[]{mPath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> Uri = " + uri);
                        }
                    });
                    image = BitmapFactory.decodeFile(mPath);//obtener bitmap de la foto tomada por cámara
                    //redimensionar la imagen según el ratio de conversión
                    imagenRed = redimensionarImagenMaximo(image, (relativeLayoutImage.getWidth()*ratioRedimension), relativeLayoutImage.getHeight());
                    imageView.setImageBitmap(imagenRed);//imprimir la imagen en la interfaz gráfica
                    break;
                case SELECT_PICTURE://si es de la galería
                    Uri path = data.getData();//obtener la uri de la foto seleccionada
                    try{
                        image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);//obtener bitmap de la uri
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                    //redimensionar la imagen según el ratio de conversión
                    imagenRed = redimensionarImagenMaximo(image, (relativeLayoutImage.getWidth()*ratioRedimension), relativeLayoutImage.getHeight());
                    imageView.setImageBitmap(imagenRed);//imprimir la imagen en la interfaz gráfica
                    break;
            }
        }
    }
}
