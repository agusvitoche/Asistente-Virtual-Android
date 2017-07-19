package com.example.gus.proyectofinandroid.cmds;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gus.proyectofinandroid.R;
import java.util.HashMap;
import java.util.List;

/**
 * Adaptador de la listView que muestra los comandos disponibles
 * @author Agustín M. Etcheverry Vitolo
 * @version 1.0
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    /**
     * Variables de clase
     */
    private Context context;//contexto de la actividad
    private List<String> listDataHeader;//lista de elementos padre
    private HashMap<String, List<String>> listDataChild;//mapa de padre-elementos hijo

    /**
     * Constructor del adaptador
     * @param context Contexto de la actividad
     * @param listDataHeader Lista de elementos padre
     * @param listChildData Mapa de elementos padre con sus elementos hijo
     */
    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    /**
     * Obtener un elemento hijo
     * @param groupPosition Posición del elemento padre
     * @param childPosititon Posición del elemento hijo en el elemento padre
     * @return Elemento hijo correspondiente
     */
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosititon);
    }

    /**
     * Obtener id de un elemento hijo
     * @param groupPosition Posición del elemento padre
     * @param childPosition Posición del elemento hijo en el elemento padre
     * @return Id del elemento hijo correspondiente
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Obtener vista de un elemento hijo
     * @param groupPosition Posición del elemento padre
     * @param childPosition Posición del elemento hijo en el elemento padre
     * @param isLastChild Si es el último elemento hijo
     * @param convertView Vista tratada
     * @param parent Vista del padre
     * @return Vista del elemento hijo correspondiente
     */
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);//obtener texto del elemento hijo
        if (convertView == null) {//si la vista tratada es nula
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_other_message, null);//inflar el layout para elemento hijo
        }
        TextView txtListChild = (TextView) convertView.findViewById(R.id.text);
        txtListChild.setText(childText);//establecer el texto del elemento hijo
        return convertView;//devolver vista tratada
    }

    /**
     * Obtener el número de hijos de un elemento padre
     * @param groupPosition Posición del elemento padre
     * @return Número de elementos hijo
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    /**
     * Obtener un elemento padre
     * @param groupPosition Posición del elemento
     * @return Elemento padre correspondiente
     */
    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    /**
     * Obtener el número de elementos padre
     * @return Número de elementos padre
     */
    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    /**
     * Obtener el id de un elemento padre
     * @param groupPosition Posición del elemento
     * @return Id del elemento
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * Obtener la vista de un elemento padre
     * @param groupPosition Posición del elemento
     * @param isExpanded Si esta expandido
     * @param convertView Vista tratada
     * @param parent Vista padre
     * @return Vista del elemento correspondiente
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);//obtener titulo del elemento
        if (convertView == null) {//si la vista es nula
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_list_cmd, null);//inflar layout para elemento padre
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.txtTitleCmd);
        ImageView imageCmd = (ImageView) convertView.findViewById(R.id.imgComando);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);//establecer el titulo del elemento
        imageCmd.setImageResource(ListCmdActivity.images[groupPosition]);//y su imagen
        return convertView;//devolver vista tratada
    }

    /**
     * Método hasStableIds sobrescrito
     * @return Siempre false
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Método isChildSelectable sobrescrito
     * @param groupPosition Posición del elemento padre
     * @param childPosition Posición del elemento hijo
     * @return Siempre true
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
