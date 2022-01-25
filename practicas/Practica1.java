package Practica1;

import java.util.*;

public class Practica1 {

    public static void main(String[] args) {
        Set<Integer> a = new HashSet<Integer>();
        Set<Integer> b = new HashSet<Integer>();
        Set<Integer> c = new HashSet<Integer>();

        a.add(5);
        a.add(2);
        a.add(3);
        b.add(1);
        b.add(13);
        c.add(12);


        List<Integer> l = new LinkedList<Integer>();

        l.add(0);
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(4);
        l.add(5);

        System.out.print("Pri:");
        for (int e: l)
            System.out.print(" " + e);
        l.add(l.size(),9);
        System.out.println();
        System.out.print("Seg:");
        for (int e: l)
            System.out.print(" " + e);
    }

    //Calcula la diferencia simétrica de s1 y s2 en s1. s2 contiene los elementos que están en ambos conjuntos
    public static <T> void difSimetrica (Set<T> s1, Set<T> s2) {
       // TODO: EJERCICIO 1
        Set<T> aux = new HashSet<T>(s1);
        aux.addAll(s2);
        Set<T> tmp = new HashSet<T>(s1);
        tmp.retainAll(s2);
        s1.addAll(aux);
        s1.removeAll(tmp);

        s2.retainAll(aux);
        s2.removeAll(s1);
    }

    //Calcula y devuelve la diferencia simétrica de una colección de conjuntos
    public static <T> Set<T> difSimetricaCol (Collection<Set<T>> col) {
        // TODO: EJERCICIO 2
        Set<T> res = new HashSet<T>();

        for (Set<T> e: col)
            difSimetrica(res, e);

        return res;
    }

    //Dado un iterador a una colleción de elementos, devolver un conjunto con los elementos que no se repiten
    //en la colección inicial deben quedar solo los elementos repetidos
    public static <T> Set<T> unicos (Iterator<T> it) {
       // TODO: EJERCICIO 3
        Set<T> aux = new HashSet<T>();
        Set<T> res = new HashSet<T>();

        while (it.hasNext()){
            T e = it.next();
            if (!aux.contains(e)){
                aux.add(e);
                if (!res.contains(e)){
                    res.add(e);
                    it.remove();
                }
            }
            else{
                res.remove(e);
            }
        }

        return res;
    }

    //Dada una colección y un conjunto de elementos del mismo tipo, devuelve cuántas veces ocurre el conjunto
    //en la colección (teniendo en cuenta que cada elemento de la colección solo puede ser usado una vez)
    public static <T> int  numOcurrecias  (Collection<T> col, Set<T> s) {
        // TODO: EJERCICIO 4
        int n = 0;
        int size = s.size();
        int tmp = 0;

        Iterator<T> it = col.iterator();
        while (it.hasNext()&&size!=0){
            T e = it.next();
            if (s.contains(e))
                tmp++;
            if (tmp == size){
                n++;
                tmp = 0;
            }
        }

        return n;
    }

    //Dividir una colección de elementos en conjuntos, según el orden de los elementos en la colección
    public static <T> Collection<Set<T>>  split  (Collection<T> col) {
       // TODO: EJERCICIO 5
        List<Set<T>> res = new ArrayList<Set<T>>();
        Set<T> aux = new HashSet<T>();

        for (T e:col){
            if (aux.contains(e)) {
                res.add(aux);
                System.out.println(Arrays.toString(res.toArray()));
                aux.clear();
            }
            aux.add(e);
        }

        res.add(aux);
        System.out.println(Arrays.toString(res.toArray()));
        return res;
    }
}
