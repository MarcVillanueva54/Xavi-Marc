package Factura;

import Coste.Coste;
import Interfaces.CalculoFacturación;

public class Urgente extends Facturación implements CalculoFacturación {
    private double variable;
    private Coste coste;

    public Urgente(){
        super();
        variable = 1.99;

    }

    @Override
    public double getPrecio() {
        return 0;
    }
}
