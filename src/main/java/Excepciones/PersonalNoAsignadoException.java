package Excepciones;

public class PersonalNoAsignadoException extends Exception {
    String mesage;

    public PersonalNoAsignadoException(){
        mesage = "ERROR: Nombre de personal erroneo o no está asignado a la tarea";
    }

    public String getMesage(){
        return mesage;
    }
}
