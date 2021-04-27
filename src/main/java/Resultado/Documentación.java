package Resultado;

import java.io.Serializable;

public class Documentación extends Resultado implements Serializable {
    private String formato;
    private int nPaginas;
    private int espacioDisco;

    Documentación(String identificador, int horasEsperadas, boolean interno,
                  String formato, int nPaginas, int espacioDisco){

        super(identificador, horasEsperadas, interno);
        this.formato = formato;
        this.nPaginas = nPaginas;
        this.espacioDisco = espacioDisco;
    }

    String getFormato(){
        return formato;
    }

}
