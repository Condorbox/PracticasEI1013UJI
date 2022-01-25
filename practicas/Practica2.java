package Practica2;

import java.util.*;

public class Practica2 {
    //Dado un iterador al inicio de una lista y un elemento elem, debe borrar las ocurrencias de elem en la
    //lista que ocupen posiciones pares. El 0 se considerará par
    public static<T> void borrarEnPares(ListIterator<T> it, T elem) {
        // TODO: EJERCICIO 1
        int i =0;
        while (it.hasNext()){
            T e = it.next();
            if (e.equals(elem)&&i%2==0)
                it.remove();
            i++;
        }
    }

    /** Invierte el orden de los elmentos de una lista.
     *
     * @param iter Un iterador de la lista. Puede estar en cualqueir posición de la lista.
     */
    static public void invierte(ListIterator<String> iter) {
        // TODO: EJERCICIO 2
        List<String> aux = new ArrayList<String>();

        while(iter.hasPrevious())
            iter.previous();

        while(iter.hasNext()){
            String e = iter.next();
            aux.add(e);
        }
        for(int i = 0; i < aux.size(); i++){
            iter.previous();
            iter.set(aux.get(i));
        }
    }

    //Busca la palabra y cambia la anterior palabra en la frase por **** (mismo número de * que
    //longitud de la palabra).
    public static int changeWord (List<List<String>> mytext, String word) {
        // TODO : EJERCICIO 3
        int  n = 0;
        ListIterator<List<String>> it = mytext.listIterator();
        while (it.hasNext()){
            List<String> frase = it.next();
            ListIterator<String> it2 = frase.listIterator();
            while (it2.hasNext()){
                String palabra = it2.next();
                if (palabra.equalsIgnoreCase(word)) {
                    if (it2.hasPrevious()){
                        ListIterator<String> it3 = frase.listIterator(it2.previousIndex());
                        if (it3.hasPrevious()){
                            String anterior = it3.previous();
                            it3.set(sustituir(anterior));
                            n++;
                        }
                    }
                }
            }
        }
        return n;
    }

    private static String sustituir(String palabra){
        char[] aux = palabra.toCharArray();
        for (int i = 0; i<aux.length;i++){
            aux[i] = '*';
        }
        palabra = String.valueOf(aux);
        return palabra;
    }

}
