package Gestor;

import Coste.Coste;
import Factura.Facturación;
import Resultado.Resultado;
import Persona.Persona;
import Proyecto.Proyecto;
import Tarea.Tarea;
import Excepciones.PersonalYaExisteException;
import Excepciones.*;
import UtilidadesLista.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class GestorMenú implements Serializable {

    public static Proyecto cargarProyecto() {
        Proyecto proyecto = null;
        boolean cargado = false;
        Scanner teclado = new Scanner(System.in);
        System.out.println("¿Desea crear un nuevo proyecto o cargar el anterior? (0/1):");
        if (teclado.nextLine().equals("1")) {
            try {
                FileInputStream fis = new FileInputStream("proyecto.bin");
                ObjectInputStream ois = new ObjectInputStream(fis);
                proyecto = (Proyecto) ois.readObject();
                ois.close();
                cargado = true;
                System.out.println("Proyecto cargado exitosamente.");
            } catch (FileNotFoundException e) {
                System.out.println("Proyecto anterior no existe o no ha sido encontrado.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al abrir en anterior proyecto.");
            }
        }
        if (!cargado) {
            System.out.println("Introducir nombre del nuevo proyecto: ");
            String nombre = teclado.nextLine();
            proyecto = new Proyecto(nombre, new ArrayList<Persona>(),
                    new ArrayList<Tarea>());
            System.out.println("Nuevo proyecto con nombre " + nombre + " creado exitosamente.");
        }

        return proyecto;
    }

    public static int menu(Scanner teclado){
        int opcion;
        System.out.println("\n\n");
        System.out.println("=====================================================");
        System.out.println("============            MENU        =================");
        System.out.println("=====================================================");
        System.out.println("0. Salir");
        System.out.println("1. Crear nuevo proyecto");
        System.out.println("2. Dar de alta personal");
        System.out.println("3. Dar de alta tarea");
        System.out.println("4. Finalizar tarea");
        System.out.println("5. Asignar personal a tarea");
        System.out.println("6. Eliminar personal de tarea");
        System.out.println("7. Listar personal del proyecto");
        System.out.println("8. Listar tareas del proyecto");
        System.out.println("9. Listar personal no responsable");
        System.out.println("10. Listar tareas sin personal");
        do {
            System.out.print("\nElige una opcion (0..10): ");
            opcion = teclado.nextInt();

        } while ((opcion < 0) || (opcion > 10));
        teclado.nextLine();
        return opcion;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, PersonalYaExisteException, FechaFinalTareaException {

        Scanner teclado = new Scanner(System.in);
        ArrayList<String> tipos = new ArrayList<>();
        tipos.add("DOC");
        tipos.add("PRO");
        tipos.add("BIB");
        tipos.add("WEB");

        Proyecto proyecto = cargarProyecto();
        int opcion;
        do {
            opcion = menu(teclado);
            switch (opcion) {
                case 0: {
                    System.out.println("Cerrando gestor...");
                    FileOutputStream fos = new FileOutputStream("proyecto.bin");
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(proyecto);
                    oos.close();
                    System.out.println("Fin de programa.");
                    break;
                }

                case 1: { // Crear nuevo proyecto
                    System.out.println("Introducir nombre del nuevo proyecto: ");
                    String nombre = teclado.nextLine();
                    proyecto = new Proyecto(nombre, new ArrayList<Persona>(),
                            new ArrayList<Tarea>());
                    System.out.println("Nuevo proyecto con nombre " + nombre + " creado exitosamente.");
                    break;
                }

                case 2: { // Dar de alta personal
                    System.out.println("Introducir nombre del personal (vacío para terminar): ");
                    String nombre = teclado.nextLine();
                    System.out.println("Introducir correo del personal: ");
                    String correo = teclado.nextLine();
                    while (!nombre.equals("")) {
                        darAltaPersonal(nombre, correo, proyecto);
                        System.out.println();
                        System.out.println("Introducir nombre del personal (vacío para terminar): ");
                        nombre = teclado.nextLine();
                        if (!nombre.equals("")) {
                            System.out.println("Introducir correo del personal: ");
                            correo = teclado.nextLine();
                        }
                    }
                    break;
                }

                case 3: { // Dar de alta tarea AÑADIR UTILIADES PARA LISTA
                    System.out.println("Introduce título de la tarea: ");
                    String nombre = teclado.nextLine();
                    while (nombre.equals("")) {
                        System.out.println("El nombre no puede estar vacío");
                        System.out.println("Introduce título de la tarea: ");
                        nombre = teclado.nextLine();
                    }
                    System.out.println("Introduce descripción de la tarea: ");
                    String descripcion = teclado.nextLine();
                    System.out.println("Introduce prioridad de la tarea (1-5): ");
                    String prioridad = teclado.nextLine();
                    while (!ComprobacionesTarea.comprobarPrio(prioridad)) {
                        System.out.println("La prioridad debe estar entre 1 y 5");
                        System.out.println("Introduce prioridad de la tarea (1-5): ");
                        prioridad = teclado.nextLine();
                    }
                    System.out.println("Introduce la fecha de finalización prevista o dejar vacío(YYYY-MM-DD): ");
                    String fecha = teclado.nextLine();

                    System.out.println("--Campos necesarios para el resultado esperado-- ");
                    System.out.println("Introduce identificador (Debe emepezar por DOC, PRO, BIB o WEB): ");
                    String identificador = teclado.nextLine();
                    while (!ComprobacionesTarea.comprobarIdentificador(identificador, tipos)) {
                        System.out.println("Introduce identificador (Debe emepezar por DOC, PRO, BIB o WEB): ");
                        identificador = teclado.nextLine();
                    }

                    System.out.println("Introduce horas esperadas: ");
                    String horas = teclado.nextLine();
                    while (!ComprobacionesTarea.comprobarHorasEsperadas(horas)) {
                        System.out.println("Introduce horas esperadas: ");
                        horas = teclado.nextLine();
                    }

                    System.out.println("--Campos necesarios para el resultado esperado introducidos-- ");

                    System.out.println();
                    System.out.println("Destinando a la comercialización? (S/N) ");
                    String res = teclado.nextLine();
                    while (!ComprobacionesTarea.comprobarComercializacion(res)) {
                        System.out.println("Destinando a la comercialización? (S/N) ");
                        res = teclado.nextLine();
                    }
                    System.out.println("Indica el coste previsto de la tarea (0.0): ");
                    String precio = teclado.nextLine();
                    Coste coste = new Coste(Double.parseDouble(precio));

                    System.out.println("Indica el tipo de facturación deseada (Urgente: 0, ConsumoInterno: 1," +
                            " Descuento: 2): ");
                    String op = teclado.nextLine();
                    Facturación facturación = new Facturación(coste, op);

                    System.out.println("Introduce etiquetas de la tarea (vacío para finalizar): ");
                    ArrayList<String> etiquetas = new ArrayList<String>();
                    String etiqueta = teclado.nextLine();
                    while (!etiqueta.equals("")) {
                        etiquetas.add(etiqueta);
                        System.out.println("Introduce otra etiqueta (vacío para finalizar): ");
                        etiqueta = teclado.nextLine();
                    }

                    darAltaTarea(nombre, descripcion, prioridad, fecha, identificador, horas, res, etiquetas, proyecto);
                    break;
                }

                case 4: { //Marcar tarea como finalizada
                    System.out.println("Introduce título de la tarea: ");
                    String nombre = teclado.nextLine();
                    if (finalizarTarea(nombre, proyecto))
                        System.out.println("Tarea.Tarea finalizada con éxito");
                    else
                        System.out.println("La tarea no ha sido dada de alta o ya está finalizada");
                    break;

                }

                case 5: { //Introducir personal en tarea
                    System.out.println("--Si la tarea no tiene asiganda personal, el responsable será el primero en introducirse.--");
                    System.out.println("Introduce nombre del personal: ");
                    String nombre = teclado.nextLine();
                    System.out.println("Introduce título de la tarea: ");
                    String titulo = teclado.nextLine();

                    asignarPersonalTarea(nombre, titulo, proyecto);
                    break;
                }

                case 6: { //Eliminar personal en tarea
                    System.out.println("Introduce nombre del personal: ");
                    String nombre = teclado.nextLine();
                    System.out.println("Introduce título de la tarea: ");
                    String titulo = teclado.nextLine();

                    eliminarPersonalTarea(nombre, titulo, proyecto);
                    break;
                }

                case 7: { //Listar personal de un proyecto
                    ArrayList<Persona> listaPersonas = proyecto.getListPersonas();
                    listarPersonal(listaPersonas);
                    break;
                }

                case 8: { //Listar tareas de un proyecto
                    ArrayList<Tarea> listaTareas = proyecto.getTareas();
                    listarTareas(listaTareas);
                    break;
                }

                case 9: { //Listar personas que no son responsables
                    List<Persona> listPersonas = proyecto.getListPersonas();
                    List<Persona> listaObjPersona = UtilidadesParaLista.elementosConListaVacia(listPersonas);
                    List<String> ret = new ArrayList<>();
                    for (Persona p : listaObjPersona)
                        ret.add(p.getNombre());
                    System.out.println("Personal responsable: " + ret);
                    break;
                }

                case 10: { //Listar tareas sin personal
                    List<Tarea> tareaList = proyecto.getTareas();
                    Persona persona = new Persona("Juan","", new ArrayList<Tarea>());
                    List<Tarea> tareaList1 = UtilidadesParaLista.elementosConListaVacia(tareaList);
                    List<String> ret = new ArrayList<>();
                    for (Tarea t : tareaList1)
                        ret.add(t.getTitulo());
                    System.out.println("Tareas sin personal asignado: " + ret);
                }
            }
        } while (opcion != 0);
    }

    //MÉTODOS DEL GESTOR
    private static void darAltaPersonal(String nombre, String correo, Proyecto proyecto) {
        Persona persona = new Persona(nombre, correo, new ArrayList<Tarea>());
        try {
            comprobarPersonalExiste(persona, proyecto);
            proyecto.añadirPersonal(persona);
            System.out.println("Personal con nombre " + nombre + " dado de alta correctamente");
        } catch (PersonalYaExisteException ex) {
            System.out.println(ex.getMesage());
        }
    }

    private static void darAltaTarea(String nombre, String descripcion, String prio, String fecha,
                                        String identificador, String horasEsperadas, String des,
                                        ArrayList<String> lEtiquetas, Proyecto proyecto) {
        //Comprobar título tarea
        try {
            comprobarTituloTareaExiste(nombre, proyecto);
        }catch (TareaYaExisteException ex){
            System.out.println(ex.getMesage());
            return;
        }

        //Comprobar fecha fin tarea
        LocalDate fechaI = LocalDate.now();
        LocalDate fechaF = (fecha.equals("") ? null : LocalDate.parse(fecha));
        if (fechaF != null)
            try {
                comprobarFechaFinal(fechaF);
            }catch (FechaFinalTareaException ex){
                System.out.println(ex.getMesage());
                return;
            }

        double horas;
        if (horasEsperadas.equals(""))
            horas = 0.0;
        else
            horas = Double.parseDouble(horasEsperadas);

        boolean interno = des.toUpperCase().equals("S");

        Resultado res = new Resultado(identificador, horas, interno);
        Tarea tarea = new Tarea(nombre, descripcion, new ArrayList<>(), Integer.parseInt(prio), fechaI, fechaF, res, lEtiquetas);
        proyecto.añadirTarea(tarea);

    }

    private static boolean finalizarTarea(String titulo, Proyecto proyecto) {
        ArrayList<Tarea> tareasP = proyecto.getTareas();
        boolean tareaEncontrada = false;
        for (Tarea tarea : tareasP) {
            if (tarea.getTitulo().equals(titulo) && !tarea.isFinalizado()) {
                tareaEncontrada = true;
                tarea.finalizarTarea();
                break;
            }
        }
        return tareaEncontrada;
    }

    private static void asignarPersonalTarea(String nombre, String titulo, Proyecto proyecto) {
        ArrayList<Tarea> tareasP = proyecto.getTareas();
        ArrayList<Persona> personalP = proyecto.getListPersonas();

        boolean tareaEncontrada = false;

        for (Tarea tarea : tareasP) {
            if (tarea.getTitulo().equals(titulo)) {
                tareaEncontrada = true;
                for (Persona persona : personalP) {
                    if (persona.getNombre().equals(nombre)) {
                        try {
                            comprobarAsignarPersonal(persona, tarea);
                            if (tarea.getResponsable().equals("")) {
                                tarea.asignarResponsable(persona);
                                persona.asignarResponsable(tarea);
                            }
                            tarea.introducirPersonal(persona);
                            persona.asignarTarea(tarea);
                            System.out.println("Personal " + nombre + " añadido correctamente a la tarea " + titulo);
                        } catch (PersonalYaAsignadoException ex) {
                            System.out.println(ex.getMesage());
                            return;

                        }
                        break;
                    }
                }
                break;
            }
        }
        if (!tareaEncontrada)
            System.out.println("Título de tarea erroneo o no está dada de alta");
    }

    private static void eliminarPersonalTarea(String nombre, String titulo, Proyecto proyecto) {
        ArrayList<Tarea> tareasP = proyecto.getTareas();

        boolean tareaEncontrada = false;

        for (Tarea tarea : tareasP) {
            if (tarea.getTitulo().equals(titulo)) {
                tareaEncontrada = true;

                for (Persona persona : proyecto.getListPersonas()) {
                    if (persona.getNombre().equals(nombre)) {
                        try {
                            comprobarPersonalAsignadoTarea(persona, tarea);
                            tarea.eliminarPersonal(persona);
                            persona.desasignarTarea(tarea);
                            if (tarea.getResponsable().equals(nombre)) {
                                persona.desasignarResponsable(tarea);
                                tarea.desasignarResponsable(persona);
                            }
                            System.out.println("Personal " + nombre + " eliminado correctamente de la tarea " + titulo);
                        }catch (PersonalNoAsignadoException ex){
                            System.out.println(ex.getMesage());
                            return;
                        }
                        break;
                    }
                }
                break;
            }
        }
        if (!tareaEncontrada)
            System.out.println("Título de tarea erroneo o no está dada de alta");

    }

    private static void listarPersonal(ArrayList<Persona> listaPersonas) {
        if (listaPersonas.size() == 0)
            System.out.println("El proyecto no tiene personal asignado");
        for (Persona persona : listaPersonas)
            System.out.println("Nombre: " + persona.getNombre() +
                    "; Correo: " + persona.getCorreo() +
                    "; Tareas asignadas: " + persona.getListTareasTitulos() +
                    "; Responsable de tareas: " + persona.getListTareasResponsableTitulos());
    }

    private static void listarTareas(ArrayList<Tarea> listaTareas) {
        if (listaTareas.size() == 0)
            System.out.println("El proyecto no tiene tareas asignadas");
        for (Tarea tarea : listaTareas) {
            System.out.println("Tarea " + tarea.getTitulo() +
                    "; Personal asignado: (R): " + tarea.getResponsable() + " (P): " + tarea.getNombrePersonal() +
                    "; Estado de la tarea: " + ((tarea.isFinalizado()) ? "Finalizada" : "No finalizada") +
                    "; Resultado: " + tarea.getResEsperado().getTipo());
        }
    }

    //=============================================================================================// EXCEPCIONES

    private static void comprobarFechaFinal(LocalDate fechaF ) throws FechaFinalTareaException {
        LocalDate fechaI = LocalDate.now();
        if (fechaF.compareTo(fechaI) < 0)
            throw new FechaFinalTareaException();
    }

    private static void comprobarTituloTareaExiste(String titulo, Proyecto proyecto) throws TareaYaExisteException{
        for (Tarea t : proyecto.getTareas()){
            if (t.getTitulo().equals(titulo))
                throw new TareaYaExisteException();
        }
    }

    private static void comprobarPersonalExiste(Persona persona, Proyecto proyecto) throws PersonalYaExisteException{
        if (UtilidadesParaLista.elementoConClave(persona, proyecto))
            throw new PersonalYaExisteException();
    }

    private static void comprobarAsignarPersonal(Persona persona, Tarea tarea) throws PersonalYaAsignadoException{
        if (UtilidadesParaLista.elementoConClave(persona,tarea))
            throw new PersonalYaAsignadoException();
    }

    private static void comprobarPersonalAsignadoTarea(Persona persona, Tarea tarea) throws PersonalNoAsignadoException{
        if (!UtilidadesParaLista.elementoConClave(persona, tarea))
            throw new PersonalNoAsignadoException();
    }

}
