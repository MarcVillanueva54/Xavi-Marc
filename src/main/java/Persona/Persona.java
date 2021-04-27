package Persona;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import UtilidadesLista.*;
import Tarea.Tarea;

public class Persona implements Serializable, tieneLista<Tarea>, tieneClave<String> {
    private String nombre;
    private String correo;
    private ArrayList<Tarea> listTareas;
    private ArrayList<Tarea> listTareasResponsable;

    public Persona(String nombre, String correo, ArrayList<Tarea> listTareas){
        this.nombre = nombre;
        this.correo = correo;
        this.listTareas = listTareas;
        listTareasResponsable = new ArrayList<>();
    }

    public String getNombre(){return nombre;}
    public String getCorreo(){return correo;}

    public void asignarTarea(Tarea tarea){listTareas.add(tarea);}
    public void desasignarTarea(Tarea tarea){listTareas.remove(tarea);}

    public void asignarResponsable(Tarea tarea){listTareasResponsable.add(tarea);}
    public void desasignarResponsable(Tarea tarea){listTareasResponsable.remove(tarea);}


    public ArrayList<String> getListTareasTitulos(){
        ArrayList<String> listTitulos = new ArrayList<String>();
        for (Tarea tarea : listTareas)
            listTitulos.add(tarea.getTitulo());
        return listTitulos;
    }

    public ArrayList<Tarea> getListTareas(){return listTareas;}
    public ArrayList<String> getListTareasResponsableTitulos(){
        ArrayList<String> listTitulos = new ArrayList<String>();
        for (Tarea tarea : listTareasResponsable)
            listTitulos.add(tarea.getTitulo());
        return listTitulos;
    }
    public ArrayList<Tarea> getListTareasResponsable(){return listTareasResponsable;}

    @Override
    public List getLista() {
        return listTareasResponsable;
    }

    @Override
    public String getClave() {
        return nombre;
    }
}
