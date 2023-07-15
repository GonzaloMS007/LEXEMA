package Principal;
//Importamos librerias
import error.ErrorLexico;
import error.Error;
import tablaDeSimbolos.Simbolos;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
//creamos la clase AnalizadorLexico
public class AnalizadorLexico {
    private BufferedReader LeerTexto;
    //creamos variables
    private String linea, caracter, constanteEntera, identificador, literales, palabra = "";
    //creamos un comentario vacio
    private boolean comentario = false;
    //Creamos 
    //creamos un arraylist para los diferentes tipos de texto
    private final List delimitadores = new ArrayList();
    private final List OperadorRelacional = new ArrayList();
    private final List PalabraReservada = new ArrayList();
    private final List OperadorAsignado = new ArrayList();
    private final List<Elemento> tokens = new ArrayList();
    //creamos una variable para llamar a la ubicacion del archivo a leer
    private String nombreDelArchivo;
    //creamos una subclase para la lectura de texto del archivo a leer    
    public AnalizadorLexico(String pathFile){
        try {
        int i = 10;
        //
        this.nombreDelArchivo = pathFile;
        constanteEntera = ("^\\d+|^\\d+\\.?\\d+");
        identificador = ("^\\D\\w+|^\\D\\w?$");
        literales = ("^\".*\"$");
        OperadorRelacional.add("<="); OperadorRelacional.add("<>"); OperadorRelacional.add("<");            
        OperadorRelacional.add(">="); OperadorRelacional.add(">");  OperadorRelacional.add("=");
        OperadorRelacional.add(":="); OperadorRelacional.add(";");

        PalabraReservada.add("PROGRAM"); PalabraReservada.add("ARRAY"); PalabraReservada.add("VAR");
        PalabraReservada.add("CONST");   PalabraReservada.add("BEGIN"); PalabraReservada.add("END");
        PalabraReservada.add("INTEGER"); PalabraReservada.add("IF");    PalabraReservada.add("THEN");
        PalabraReservada.add("REAL");    PalabraReservada.add("STEP");  PalabraReservada.add("CASE");
        PalabraReservada.add("ELSE");    PalabraReservada.add("CHAR");  PalabraReservada.add("FOR");
        PalabraReservada.add("TO");      PalabraReservada.add("DO");    PalabraReservada.add("WHILE");
        PalabraReservada.add("REPEAT");  PalabraReservada.add("UNTIL");

        delimitadores.add(" ");    delimitadores.add(",");  delimitadores.add("==");
        delimitadores.add("!=");   delimitadores.add("(");  delimitadores.add(")");
        delimitadores.add("\\n");  delimitadores.add("{");  delimitadores.add("}");
        OperadorAsignado.add("¿?");    OperadorAsignado.add("+=");  OperadorAsignado.add("-=");
        OperadorAsignado.add("*=");    OperadorAsignado.add("/=");  OperadorAsignado.add("%=");
        OperadorAsignado.add("++");    OperadorAsignado.add("--");
       //Vamos a mandar a llamar a nuestro archivo de texto
        LeerTexto = new BufferedReader(new FileReader(pathFile));
         //Agegamos una excepcion
        } catch (FileNotFoundException ex) {
            //Si no se carga el archivo entonces mostrara el siguiente mensaje
            JOptionPane.showMessageDialog(null, "Archivo no encontrado");
        }
    }
    // Creamos la clase analizarLexema
    public void analizarLexema() throws IOException{
        //comenzamos en linea vacia
        int nLinea = 0;
        while (true){
            //se incrementa el numero de lineas
            nLinea++;
            //llamamos a linea y leemos linea por linea
            linea = LeerTexto.readLine();
            if (linea == null)
                break;
            int size = linea.length();
            linea = linea.split("\r")[0];
            caracter = "";
             //ciclo for para la lectura de textos
            for (int i = 0; i < size;i++){
                caracter = linea.substring(i,i+1);
                if (delimitadores.contains(caracter)){
                    if ((!comentario) && (palabra.length() >= 2) && (palabra.substring(0,2).equals(
                        "//"))){
                        palabra = "";
                        break;
                    }
                    if ((!comentario) && (palabra.length() >= 2) && (palabra.substring(0,2).equals(
                        "/*"))){
                        palabra = "";
                        comentario = true;
                    }
                    if ((comentario) && (palabra.length() >= 2) && (palabra.substring(0,2).equals(
                        "*/"))){
                        palabra = "";
                        comentario = false;
                    } 
                    if (!comentario){ 
                        if ((!palabra.equals("")) && (!palabra.contains("/*")))
                            this.addToken(palabra,nLinea);
                        //alineamos el token correspondiente
                    }
                        palabra = "";
                }
                else
                    palabra = palabra + caracter;
            }            
        }
    }
//Imprimimos la constantes enteras
    private void addToken(String palabra,int nLinea) {
        if (palabra.matches(constanteEntera)){
            Elemento elemento = new Elemento();
            elemento.setToken("\nCONSTANTE ENTERA: ");
            elemento.setLexema(palabra);
            tokens.add(elemento);
            return;
        }
//Imprimimos la palabras literales
        if (palabra.matches(literales)){
            Elemento elemento = new Elemento();
            elemento.setToken("\nLITERALES ");
            elemento.setLexema(palabra);
            tokens.add(elemento);
            return;
        }          
//Imprimimos los operadore relacionales
        if (OperadorRelacional.contains(palabra)){
            Elemento elemento = new Elemento();
            elemento.setToken("\nOPERADOR RELACIONAL ");
            elemento.setLexema(palabra);
            tokens.add(elemento);
            return;
        }
//Imprimimos las palabras reservadas
        if (PalabraReservada.contains(palabra)){
            Elemento elemento = new Elemento();
            elemento.setToken("\nOPERADOR RESERVADA: ");
            elemento.setLexema(palabra);
            tokens.add(elemento);
            return;
        }
//Imprimimos los operadores asignados
        if (OperadorAsignado.contains(palabra)){
            Elemento elemento = new Elemento();
            elemento.setToken("\nOPERADOR ASIGNADO ");
            elemento.setLexema(palabra);
            tokens.add(elemento);
            return;
        }
//Imprimimos los operadores aritmeticos
        if(!palabra.equals(PalabraReservada)){
            if (palabra.matches(identificador)){
                Elemento elemento = new Elemento();
                elemento.setToken("\nOPERADOR ARITMETICO ");
                elemento.setLexema(palabra);
                tokens.add(elemento);
                Simbolos simbolo = new Simbolos();
                simbolo.setNome(palabra);
                Simbolos.addSimbolo(simbolo);
                return;
            }         
        }
//Imprimimos los identificadores desconocidos
        Error error = new ErrorLexico();
        error.setCodigo(101);
        error.setDescripcion("IDENTIFICADORES DESCONOCIDOS: " + palabra);
        error.setNombreArchivo(this.nombreDelArchivo);
        error.setNumLinea(nLinea);
        Error.addError(error);   
    }
    //arraylist de los tokens
    public List<Elemento> getTokens(){
        return tokens;
    }
    //metodo para iniciar nuestro programa
    public static void main(String[] args) {
    //llamamos al archivo que vamos a analizar
        AnalizadorLexico analizador = new AnalizadorLexico("Texto.txt");
        try {
            analizador.analizarLexema();
            System.out.println("INSTITUTO TECNOLOGICO SUPERIOR DE ALAMO TEMAPACHE");
            System.out.println("LENGUAJES Y AUTOMATAS 1 || PROGRAMA: ANALIZADOR LEXICO");
            System.out.println("DOCENTE: DR. TANIA TURRUBIATES LOPEZ");
            System.out.println("PERIODO ESCOLAR: FEB 2023 - JUN 2023 || GRUPO: 6S1A");
            System.out.println("ALUMNO: GONZALO MARTINEZ SILVERIO\n"+ analizador.getTokens());   
            System.out.println("\nLISTA DE ERRORES LEXICOS");
            //mostramos los errores 
            int i;
            for (i = 0; i < Error.getErros().size();i++){
                Error error = Error.getErros().get(i);      
                //mostramos los errores 
                System.out.println(error.showErrores());
            }
            //mostramos los simbolos
            System.out.println("\nTABLA DE SIMBOLOS\n" + Simbolos.getTabelaDeSimbolos());
        //excepcion
        } catch (IOException ex) {
         //si no se puede leer el archivo de texto mostrara un mensaje 
            JOptionPane.showMessageDialog(null, "ERROR AL LEER EL ARCHIVO DE TEXTO");
        }
    }
}




