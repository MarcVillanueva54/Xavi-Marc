package Factura;

import Interfaces.CalculoFacturación;

public class Descuento extends Facturación implements CalculoFacturación {
    private double variable;

    public Descuento(){
        variable = 1.99;
    }

    @Override
    public double getPrecio() {
        return variable;
    }
}
