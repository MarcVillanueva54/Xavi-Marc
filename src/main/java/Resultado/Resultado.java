package Resultado;

import java.io.Serializable;

public class Resultado implements Serializable {
    private String identificador;
    private double horasEsperadas;
    private boolean interno;


    public Resultado(String identificador, double horasEsperadas, boolean interno){
        this.identificador = identificador;
        this.horasEsperadas = horasEsperadas;
        this.interno = interno;
    }

    public String getTipo(){
        if (!identificador.equals("")) {
            String tipo = identificador.substring(0, 3);
            if (tipo.equals("DOC"))
                return "Documentación";
            else if (tipo.equals("PRO"))
                return "Programa";
            else if (tipo.equals("BIB"))
                return "Biblioteca";
            else
                return "Página web";
        }
        return "No asignado";
    }

}
