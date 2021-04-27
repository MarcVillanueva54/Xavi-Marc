package Excepciones;

public class TareaYaExisteException extends Exception{
    private String mesage;

    public TareaYaExisteException(){
        mesage = "ERROR: La tarea ya ha sido dada de alta.";
    }

    public String getMesage(){
        return mesage;
    }
}
