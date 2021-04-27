package Proyecto;

import Persona.Persona;
import Tarea.Tarea;
import UtilidadesLista.tieneClave;
import UtilidadesLista.tieneLista;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Proyecto implements Serializable, tieneLista<Persona>{
    private String nombre;
    private ArrayList<Persona> listPersonas;
    private ArrayList<Tarea> listTareas;

    public Proyecto(String nombre, ArrayList<Persona> listPersonas, ArrayList<Tarea> listTareas){
        this.nombre = nombre;
        this.listPersonas = listPersonas;
        this.listTareas = listTareas;
    }

    public void añadirPersonal(Persona persona){
        listPersonas.add(persona);
    }

    public void añadirTarea(Tarea tarea){listTareas.add(tarea);}

    public ArrayList<Tarea> getTareas(){return listTareas;}

    public ArrayList<Persona> getListPersonas(){return listPersonas;}

    public ArrayList<String> getListPersonasNombre(){
        ArrayList<String> listaPersonasNombre = new ArrayList<String>();
        for (Persona persona : listPersonas)
            listaPersonasNombre.add(persona.getNombre());

        return listaPersonasNombre;
    }

    @Override
    public List<Persona> getLista() {
        return listPersonas;
    }
}
