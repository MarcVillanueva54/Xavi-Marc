package Factura;

import Coste.Coste;
import Interfaces.CalculoFacturación;

public class ConsumoInterno extends Facturación implements CalculoFacturación {
    private double variable;
    private Coste coste;

    public ConsumoInterno(){
        variable = 0.00;
    }

    @Override
    public double getPrecio() {
        return variable;
    }
}
