package Excepciones;

public class PersonalYaExisteException extends Exception{
    String mesage;
    public PersonalYaExisteException(){
        mesage = "ERROR: El personal ya ha sido dado de alta.";
    }

    public String getMesage(){
        return mesage;
    }
}
