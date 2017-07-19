/*package com.example.gus.proyectofinandroid.ocr;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class Translator {

    private static final String API_KEY = "AIzaSyC05Ceoa9zMiPoHk-i-jyzW1JJh2HVY3CE";

    public static String translate(String textoAtraducir){
        TranslateOptions options = TranslateOptions.newBuilder()
                .setApiKey(API_KEY)
                .build();
        Translate translate = options.getService();
        final Translation translation = translate.translate(textoAtraducir, Translate.TranslateOption.targetLanguage("en"));
        String textoTraducido = translation.getTranslatedText();
        return textoTraducido;


        // Instantiates a client
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        // Translates some text into Russian
        Translation translation = translate.translate(textoAtraducir, Translate.TranslateOption.sourceLanguage("es"), Translate.TranslateOption.targetLanguage("en"));

        return translation.getTranslatedText();
    }

}*/
