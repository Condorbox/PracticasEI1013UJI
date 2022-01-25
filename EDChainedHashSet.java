package Practica5;

import java.util.*;

/**
 * Clase generica que implementa la interfaz Set<T> usando una tabla de
 * dispersión con resolución de colisiones mediante encadenamiento.
 * 
 * Admite elmentos null como validos
 * 
 * No implementa iteradores
 * 
 * @param <T> 
 */
public class EDChainedHashSet<T> implements Set<T> {
    /**
     * Parte privada
     * 
     * DEFAULT_CAPACITY: tamaño por defecto de la tabla de dispersión
     * DEFAULT_LOAD_FACTOR: factor de carga a aprtir del cual se realiza el
     * rehashing. 1.0 es un 100%.
     * 
     * class node: el nodo 
     * 
     * table: la tabla de dispersión. 
     * 
     * size: numero de
     * elementos de la tabla 
     * 
     * rehashThreshold: umbral a partir del cual se debe
     * realizar el rehashing (size > rehashThreshold)
     */
    static int DEFAULT_CAPACITY = 7;
    static double DEFAULT_LOAD_FACTOR = 3.0; // 300%

    private class Node {
        T data;
        Node next;

        public Node(T item) {
            data = item;
            next = null;
        }
    }

    private Node table[];
    private int size;
    private int rehashThreshold;

    // constructores
    @SuppressWarnings("unchecked")
    public EDChainedHashSet(int capacity, double loadFactor) {
        table = new EDChainedHashSet.Node[capacity];
        size = 0;
        rehashThreshold = (int) Math.floor(loadFactor * capacity);
    }

    public EDChainedHashSet(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public EDChainedHashSet(double loadFactor) {
        this(DEFAULT_CAPACITY, loadFactor);
    }

    public EDChainedHashSet() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Calcula el valor positivo de la funcion de dispersion para item ajustado
     * al tamaño, y teniendo en cuenta que puede ser null
     * 
     * @param item
     * @return Valor de dispersion
     */
    private int hash(Object item) {
        if (item == null)
            return 0;

        return (item.hashCode() & Integer.MAX_VALUE) % table.length;
    }

    /**
     * Compara si item1 y item2 son iguales teniendo en cuenta que ambos puede
     * ser null
     * 
     * @param item1
     * @param item2
     * @return si item1 y item2 son iguales
     */
    @SuppressWarnings("unchecked")
    private boolean equalsNull(Object item1, Object item2) {
        if (item1 == null)
            return item1 == item2;
        else
            return ((T) item1).equals((T) item2);
    }
    /**
     * Realiza el rehashing si es necesario, cuando (size > rehashThreshiold). Al
     * realizarlo dobla el tamaño de la tabla y recalcula el rehashThreshold
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        // TODO
        if(size > rehashThreshold){
            Node aux[] = table;
            rehashThreshold = table.length*2;
            table = new EDChainedHashSet.Node[rehashThreshold];
            for (int i = 0; i<aux.length; i++){
                Node tmp = aux[i];
                while (tmp!=null){
                    add(tmp.data);
                    tmp = tmp.next;
                }
            }
        }
    }

    public int getCapacity() {
        return table.length;
    }
    
    @Override
    public boolean add(T item) {
        // TODO
        boolean x = contains(item);
        if (!x){
            int index = hash(item);
            Node aux = new Node(item);
            Node tmp = table[index];

            while (tmp != null){
                if (tmp.next == null){ break; }
                tmp = tmp.next;
            }

            if (table[index] == null){
                table[index] = aux;
            }else{
                tmp.next = aux;
            }

            size++;
            rehash();
        }

        return !x;
    }

    @Override
    public boolean contains(Object item) {
        // TODO
        int index = hash(item);
        Node aux = table[index];
        boolean found = false;
        while (aux!=null&&hash(aux.data)==index&&!found){
            if (equalsNull(item,aux.data))
                found = true;
            else
                aux = aux.next;
        }
        return found;
    }

    @Override
    public boolean remove(Object item) {
        // TODO
        int index = hash(item);
        Node aux = table[index];
        Node anterior = table[index];;
        boolean found = false;
        while (aux!=null&&hash(aux.data)==index&&!found){
            if (equalsNull(item,aux.data))
                found = true;
            else{
                anterior = aux;
                aux = aux.next;
            }
        }
        if (found){
            if (aux.next != null){
                anterior.next = aux.next;
            }else{
                anterior.next = null;
            }
            if (aux==table[index]){
                if (aux.next!=null&&hash(aux.next.data)==index){
                    table[index] = aux.next;
                }else{
                    table[index] = null;
                }
            }
            size--;
        }
        return found;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        // TODO
        for (int i = 0; i<table.length;i++){
            table[i] = null;
        }
        size = 0;
    }

    // Los siguientes métodos asumen la existencia de iteradores.

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c.isEmpty())
            return true;
        if (isEmpty())
            return false;
        for (Object item : c) {
            if (!contains(item))
                return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(Collection<? extends T> c) {
        int oldSize = size;
        for (Object item : c)
            add((T) item);

        return (size != oldSize);
    }

    /* Conserva sólo los elementos de esta colección que están contenidos en la colección especificada (operación
        opcional).

        En otras palabras, se elimina de todos los elementos de esta colección que no están contenidos en la
       colección especificada.    true  si esta colección cambia como resultado de la llamada
    */
    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO
        boolean notFound = false;

        for (Object e: c){
            if (!contains(e)){
                notFound = true;
                remove(e);
            }
        }

        return notFound;
    }
    
    //mirar cada coleccion y si es c borra toda la coleccion

    @Override
    public boolean removeAll(Collection<?> c) {
        int oldSize = size();
        for (Object item : c)
            remove(item);

        return size() != oldSize;
    }

    @Override
    public Object[] toArray() {
        Object ret[] = new Object[size];

        int i = 0;
        for (Object item : this) {
            ret[i] = item;
            i++;
        }
        return ret;
    }

    @SuppressWarnings({ "unchecked", "hiding" })
    @Override
    public <T> T[] toArray(T[] a) {
        T[] ret = null;

        if (a.length > size) {
            ret = a;
            for (int i = size; i < ret.length; i++)
                ret[i] = null;
        } else
            ret = (T[]) new Object[size];

        int i = 0;
        for (Object item : this) {
            ret[i] = (T) item;
            i++;
        }

        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(toArray());
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EDChainedHashSet<T> other = (EDChainedHashSet<T>) obj;

        if (size() != other.size())
            return false;

        for (T item : other) {
            if (!contains(item))
                return false;
        }

        return true;
    }

    public String toString() {
        StringBuilder str = new StringBuilder("[\n ");
        for (int row = 0; row < table.length; row++) {
            if (table[row] != null) {
                str.append("{" + row + ": ");
                str.append(table[row].data);
                Node aux = table[row].next;
                while (aux != null) {
                    str.append(", " + aux.data);
                    aux = aux.next;
                }
                str.append("}");
            }
            else
                str.append("null");
            if (row != table.length-1)
                str.append(",\n ");
        }

        str.append("\n ] (size: " + size + ", capacity: " + table.length + ")");
        return str.toString();
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

}
