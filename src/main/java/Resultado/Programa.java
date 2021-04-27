package Resultado;


import java.io.Serializable;

public class Programa extends Resultado implements Serializable {
    private String lenguaje;
    private int lCodigo;
    private int nModulos;

    Programa(String identificador, int horasEsperadas, boolean interno,
             String lenguaje, int lCodigo, int nModulos){
        super(identificador, horasEsperadas, interno);
        this.lenguaje = lenguaje;
        this.lCodigo = lCodigo;
        this.nModulos = nModulos;
    }
}
