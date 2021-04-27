package Factura;

import Coste.Coste;

enum Tipo{
    Urgente, ConsumoInterno, Descuento
}

public class Facturación {
    private double precio;
    private String op;

    public Facturación(Coste coste, String op){
        precio = coste.precio;
        this.op = op;
    }

    





}
