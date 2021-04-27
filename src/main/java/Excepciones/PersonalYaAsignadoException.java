package Excepciones;

public class PersonalYaAsignadoException extends Exception {
    String mesage;

    public PersonalYaAsignadoException(){
        mesage = "ERROR: El personal ya ha sido asignado a la tarea.";
    }

    public String getMesage(){
        return mesage;
    }
}
