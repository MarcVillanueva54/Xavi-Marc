package Excepciones;

public class FechaFinalTareaException extends Exception {
    String mesage;

    public FechaFinalTareaException(){
        mesage = "ERROR: La fecha final no puede ser anterior a la fecha inicial.";
    }

    public String getMesage(){
        return mesage;
    }
}
