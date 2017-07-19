package com.example.gus.proyectofinandroid.chat;

/**
 * Clase POJO de los mensajes en el chat
 * @author Agustín M. Etcheverry
 * @version 1.0
 */
public class ChatMessage {

    /**
     * Variables de clase
     */
    private boolean isImage, isMine;//si el mensaje es una imagen y propietario del mensaje
    private String content;//contenido del mensaje

    /**
     * Constructor de la clase
     * @param message Contenido del mensaje
     * @param mine Propietario del mensaje
     * @param image Si es una imagen
     */
    public ChatMessage(String message, boolean mine, boolean image) {
        content = message;
        isMine = mine;
        isImage = image;
    }

    /**
     * Obtener el contenido del mensaje
     * @return Contenido del mensaje
     */
    public String getContent() {
        return content;
    }

    /**
     * Establecer el contenido del mensaje
     * @param content Contenido del mensaje
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Obtener el propietario del mensaje
     * @return Propietario del mensaje
     */
    public boolean isMine() {
        return isMine;
    }

    /**
     * Establecer el propietario del mensaje
     * @param isMine Propietario del mensaje
     */
    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    /**
     * Establecer si es una imagen
     * @return Si es una imagen
     */
    public boolean isImage() {
        return isImage;
    }

    /**
     * Establecer si es una imagen
     * @param isImage Si es una imagen
     */
    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }
}
