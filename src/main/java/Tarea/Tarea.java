package Tarea;

import Coste.Coste;
import Factura.Facturación;
import Interfaces.CalculoFacturación;
import Persona.Persona;
import Resultado.Resultado;
import UtilidadesLista.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tarea implements Serializable, tieneLista<Persona>, tieneClave<String>, CalculoFacturación {
    private String titulo;
    private String descripcion;
    private ArrayList<Persona> listPersona;
    private Persona responsable;
    private int prio;
    private LocalDate fCreacion;
    private LocalDate fFinal;
    private boolean finalizado;
    private Resultado resEsperado;
    private ArrayList etiquetas;
    private Coste coste;
    private Facturación facturación;
    private CalculoFacturación calculoFacturación;

    public Tarea(String titulo, String descripcion, ArrayList<Persona> listPersona, int prio,
          LocalDate fCreacion, LocalDate fFinal, Resultado resEsperado, ArrayList etiquetas, Coste coste,
                 Facturación facturación, CalculoFacturación calculoFacturación){
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.listPersona = listPersona;
        this.responsable = new Persona("", "", new ArrayList<>());
        this.prio = prio;
        this.fCreacion = fCreacion;
        this.fFinal = fFinal;
        this.finalizado = false;
        this.resEsperado = resEsperado;
        this.etiquetas = etiquetas;
        this.coste = coste;
        this.facturación = facturación;
        this. calculoFacturación = calculoFacturación;
    }

    public void finalizarTarea(){ this.finalizado = true; }

    public void introducirPersonal(Persona persona){ listPersona.add(persona);}

    public void eliminarPersonal(Persona persona){listPersona.remove(persona);}

    public String getTitulo(){return titulo;}

    public ArrayList<Persona> getListPersona(){return listPersona;}

    public boolean isFinalizado(){return finalizado;}

    public Resultado getResEsperado(){return resEsperado;}

    public ArrayList<String> getNombrePersonal(){
        ArrayList<String> nombresPersonal = new ArrayList<String>();
        for (Persona persona : listPersona)
            nombresPersonal.add(persona.getNombre());
        return nombresPersonal;
    }

    public void asignarResponsable(Persona persona){
        responsable = persona;
    }
    public void desasignarResponsable(Persona persona){responsable = new Persona("", "",
            new ArrayList<Tarea>());}
    public String getResponsable(){
        return responsable.getNombre();
    }

    LocalDate getfCreacion(){return fCreacion;}
    LocalDate getfFinal(){return fFinal;}

    @Override
    public String getClave() {
        return titulo;
    }

    @Override
    public List<Persona> getLista() {
        return listPersona;
    }

    @Override
    public double getPrecio() {
        return this.calculoFacturación.getPrecio();
    }
}
