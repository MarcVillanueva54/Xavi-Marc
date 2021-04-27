package Factura;

import Coste.Coste;
import Interfaces.CalculoFacturación;

public class ConsumoInterno extends Facturación implements CalculoFacturación {
    private double variable;
    private Coste coste;

    public ConsumoInterno(Coste coste, String op){
        super(coste, op);
        variable = 0.00;
        this.coste = coste;
    }

    @Override
    public double getPrecio() {
        return this.coste.precio + variable;
    }
}
