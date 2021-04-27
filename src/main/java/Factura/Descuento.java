package Factura;

import Coste.Coste;
import Interfaces.CalculoFacturación;

public class Descuento extends Facturación implements CalculoFacturación {
    private double variable;
    private Coste coste;

    public Descuento(Coste coste, String op){
        super(coste, op);
        variable = 1.99;
        this.coste = coste;
    }

    @Override
    public double getPrecio() {
        return this.coste.precio - variable;
    }
}
