package Factura;

import Coste.Coste;

public class Facturación {
    private double precio;
    private String op;
    private Urgente urgente;
    private ConsumoInterno consumoInterno;
    private Descuento descuento;

    public Facturación(Coste coste, String op){
        precio = coste.precio;
        this.op = op;
        escogerTipo(op);
    }

    void escogerTipo(String op){
        if (op.equals("0")){
            urgente = new Urgente(new Coste(precio), op);
        }else if (op.equals("1")){
            consumoInterno = new ConsumoInterno(new Coste(precio), op);
        }else{
            descuento = new Descuento(new Coste(precio), op);
        }
    }

    double getPrecioVariacion(){

    }









}
