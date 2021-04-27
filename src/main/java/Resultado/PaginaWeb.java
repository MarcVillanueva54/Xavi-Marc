package Resultado;


import java.io.Serializable;

public class PaginaWeb extends Resultado implements Serializable {
    private String tipo;
    private String lenguaje;
    private String backEnd;

    PaginaWeb(String identificador, int horasEsperadas, boolean interno,
              String tipo, String lenguaje, String backEnd){
        super(identificador, horasEsperadas, interno);
        this.tipo = tipo;
        this.lenguaje = lenguaje;
        this.backEnd = backEnd;
    }
}
