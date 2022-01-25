package Practica6;

import org.w3c.dom.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.IllegalFormatCodePointException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MorseCode {

    EDBinaryNode<Character> morseRoot = null;

    public MorseCode() {
        morseRoot= new EDBinaryNode(null);
        readFileInfo();
    }

    private void readFileInfo() {
        Scanner input = null;
        try {
            input = new Scanner(new File("morseCodes.txt"));
        } catch (FileNotFoundException exception) {
            System.out.println("File not found!");
        }

        while (input.hasNextLine()) {
            String cars = input.next();
            char car = cars.charAt(0);
            String code = input.next();
            insert(car, code);
        }
        input.close();
    }

    public String inorderPrint() {
        return inorderPrint(morseRoot);
    }

    private String inorderPrint(EDBinaryNode<Character> n) {
        String s1, s2;
        if (n != null) {
            s1 = inorderPrint(n.left());
            s2 = inorderPrint(n.right());
            if (!n.containsNull())
                return s1 + n.data() + s2;
            else
                return s1 + s2;
        }
        return "";
    }

    /**
     *  Añade un nuevo carácter al árbol binario
     *
     *  El método debe añadir un nodo con el caracter car en el árbol binario, según la secuencia de puntos y rayas.
     *  Si el nodo ya existía se sobreescribw.
     *
     * @param car   Carácter que debe añadirse en el árbol binario
     * @param code  Codificación del carácter con puntos y rayas
     */
    public void insert(char car, String code) {
       // TODO
        EDBinaryNode<Character> aux = morseRoot;
        String s = " ";

        for (int i = 0; i < code.length(); i++) {
            s = code.substring(i, i + 1);
            if (s.equals(".")) {
                if (aux.hasLeft()) {
                    aux = aux.left();
                } else {
                    aux.setLeft(new EDBinaryNode<Character>(null));
                    aux = aux.left();
                }
            } else {
                if (aux.hasRight()) {
                    aux = aux.right();
                } else {
                    aux.setRight(new EDBinaryNode<Character>(null));
                    aux = aux.right();
                }
            }
        }
        aux.setData(car);
    }

    /**
     * Decodifica una secuencia de puntos y rayas y la convierte en un texta alfabético.
     *
     * @param codetext Secuencia de puntos y rayas. Los códigos de cada letra independiente están separados por un
     *                 espacio en blanco.
     *
     * @return La cadena de de letras resultante
     */
    public String decode(String codetext) {
        //TODO
        String res = "";
        EDBinaryNode<Character> aux = morseRoot;
        for (int i=0;i<codetext.length();i++){
            if (codetext.charAt(i)==' '){
                res+=aux.data();
                aux = morseRoot;
            }
            if (codetext.charAt(i)=='.'&&aux.hasLeft()){
                aux = aux.left();
            }
            else if(codetext.charAt(i)=='-'&&aux.hasRight()){
                aux = aux.right();
            }
        }
        res += aux.data();
        return  res;
    }

    /**
     * Toma una cadena de texto y la codifica en puntos y rayas
     * @param text El texto a codificar
     *
     * @return Una cadena de puntos y rayas, se añaden espacios para separar los códigos de la letras.
     */
    public String encode(String text) {
       // TODO
        EDBinaryNode<Character> r = morseRoot;
        String res = "";
        String s = "";
        char car;
        text = text.replace(" ", "");
        for (int i = 0; i < text.length(); i++) {
            car = text.charAt(i);
            System.out.println("A buscar: " + car);
            res += searchTree(r, car, s);
            if (i != text.length()-1){
                res += " ";
            }
        }
        return res;
    }

    public String searchTree(EDBinaryNode<Character> current, char car, String s) {
        if (current.data() != null && current.data() == car) {
            System.out.println("s: " + s +" data: " + current.data() + " izq: " + current.hasLeft() + " rig: " + current.hasRight());
            return s;
        }
        else {//No exite en el subarbol actual
            if (!nodeExits(current, car)){
                if (current.hasParent()) current = current.parent();
                s = deleteLast(s);
                System.out.println("CadenaNueva: " + s +" data: " + current.data() + " izq: " + current.hasLeft() + " rig: " + current.hasRight());
                return searchTree(current.right(), car, s + "-"); //Subarbol der
            }//Subarbol izq
            if (current.hasLeft()) {
                System.out.println("s: " + s +" data: " + current.data() + " izq: " + current.hasLeft() + " rig: " + current.hasRight());
                return searchTree(current.left(), car, s + ".");
            }
            System.out.println("Fin: " + s +" data: " + current.data() + " izq: " + current.hasLeft() + " rig: " + current.hasRight());
            return s;
        }
    }

    private Boolean nodeExits(EDBinaryNode<Character> current, char car){
        if (current == null) return false;

        if (current.data() != null&&current.data() == car) return true;

        boolean res1 = nodeExits(current.left(), car);
        if (res1) return true;

        boolean res2 = nodeExits(current.right(), car);
        return res2;
    }

    private String deleteLast(String txt){
        String res = "";
        for (int i = 0; i<txt.length() - 1;i++){
            res += txt.charAt(i);
        }
        return res;
    }
}
