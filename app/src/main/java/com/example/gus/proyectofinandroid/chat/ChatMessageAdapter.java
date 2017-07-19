package com.example.gus.proyectofinandroid.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gus.proyectofinandroid.R;

import java.util.List;

/**
 * Adaptador del listView del chat
 * @author Agustín M. Etcheverry Vitolo
 * @version 1.0
 */
public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> { //TODO GAP-002 cambiar a reciclerview
    /**
     * Variables de clase
     */
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, MY_IMAGE = 2, OTHER_IMAGE = 3;//propietario del mensaje y si es una imagen

    /**
     * Constructor del adaptador
     * @param context Contexto de la actividad
     * @param data Mensajes de la conversación
     */
    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        super(context, R.layout.item_mine_message, data);
    }

    /**
     * Obtener el número de tipos que tiene el adaptador
     * @return Número de tipos
     */
    @Override
    public int getViewTypeCount() {
        return 4;//my message, other message, my image, other image
    }

    /**
     * Obtener el tipo de un item
     * @param position Posición del item
     * @return Tipo del item
     */
    @Override
    public int getItemViewType(int position) {
        ChatMessage item = getItem(position);//obtener item de esa posición
        if (item.isMine() && !item.isImage())//si es mensaje del usuario
            return MY_MESSAGE;
        else if (!item.isMine() && !item.isImage()) //si es mensaje del bot
            return OTHER_MESSAGE;
        else if (item.isMine() && item.isImage()) //si es imagen del usuario
            return MY_IMAGE;
        else //si es imagen del bot
            return OTHER_IMAGE;
    }

    /**
     * Obtener la vista de un item
     * @param position Posición del item
     * @param convertView Vista tratada
     * @param parent Vista padre
     * @return Vista del item
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //obtener tipo del item
        int viewType = getItemViewType(position);
        if (viewType == MY_MESSAGE) {//si es mensaje del usuario
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_message, parent, false);//inflar layout de mensaje de usuario
            TextView textView = (TextView) convertView.findViewById(R.id.text);//obtener textView del layout
            textView.setText(getItem(position).getContent());//ponerle el texto del mensaje
        } else if (viewType == OTHER_MESSAGE) {//si es mensaje del bot
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_other_message, parent, false);//inflar layout de mensaje de bot
            TextView textView = (TextView) convertView.findViewById(R.id.text);//obtener textView del layout
            textView.setText(getItem(position).getContent());//ponerle el texto del mensaje
        }
        //listener del item de la lista
        convertView.findViewById(R.id.chatMessageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "onClick", Toast.LENGTH_LONG).show();//TODO
            }
        });
        //devolver vista del item
        return convertView;
    }
}
