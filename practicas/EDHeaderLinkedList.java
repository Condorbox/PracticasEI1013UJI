package Practica4;

import java.util.*;

/**
 * Implementación de la interface List<T> usando nodos doblemenete enlazados de
 * forma circular y con nodo cabecera.
 * 
 * La clase admite nulls como elementos de la lista, por lo tanto deberá tratar
 * correctamente su inserción, búsqueda y borrado.
 *
 * @param <T> Tipo básico de la lista
 */
public class EDHeaderLinkedList<T> implements List<T> {
 		private List<T> m = new LinkedList<>();
	/**
	 * Definición de nodo
	 */
	private class Node {
		public T data;
		public Node next;
		public Node prev;

		public Node(T element) {
			data = element;
			next = null;
			prev = null;
		}
	}

	// Enlace al nodo cabecera. A él se enlazan el primero y el último
	private Node header;
	// Número de elementos de la clase
	private int size;

	/**
	 * Constructor por defecto
	 */
	public EDHeaderLinkedList() {
		// TODO
		header = new Node(null);
		header.next = header;
		header.prev = header;

		size = 0;
	}

	/**
	 * Constructor que copía todos los elementos de otra clase
	 * 
	 * @param c
	 */
	public EDHeaderLinkedList(Collection<T> c) {
		this();
		addAll(c);
	}

	/**
	 * Compara si dos elementos son iguales teniendo en cuenta que uno o ambos
	 * pueden ser null. 
	 *
	 * @return Si ambos elementos son iguales
	 */
	private boolean equalsNull(T one, Object item) {
		if (one == null)
			return one == item;
		else
			return one.equals(item);
	}


	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return header.next == header;
	}

	@Override
	public void clear() {
		header.next = header.prev = header;
		size = 0;
	}

	private Node recorrer(int index){
		Node aux = header.next;
		if ((index>=size || index<0)&&size!=0){
			System.out.println("index: " + index + " size: " + size);
			throw new IndexOutOfBoundsException();
		}

		for (int i = 0; i<index; i++){
				aux = aux.next;
			//System.out.println("valor aux: " + aux.data + " i: " + i);
		}
		return aux;
	}

	@Override
	public boolean add(T item) {
		// TODO
		Node tmp = new Node(item);
		Node last = recorrer(size-1);

		if (size==0){
			header.next = tmp;
			header.prev = tmp;
			tmp.next = header;
			tmp.prev = header;
		}else{
			tmp.next = header;
			tmp.prev = last;
			last.next=tmp;
			header.prev = tmp;
		}
		size++;
		return true;
	}

	@Override
	public void add(int index, T element) {
		//TODO
		Node tmp = new Node(element);
		if (size==0){
			header.next = tmp;
			header.prev = tmp;

			tmp.next = header;
			tmp.prev = header;
		}else if (index == size){
			Node last = recorrer(index - 1);

			tmp.prev = last;
			tmp.next = last.next;

			last.next = tmp;
		}else{
			Node last = recorrer(index);

			tmp.next = last;
			tmp.prev = last.prev;

			last.prev.next = tmp;
			last.prev = tmp;
		}
		size++;
	}

	@Override
	public boolean remove(Object item) {
		//TODO
		Node aux = header.next;
		boolean found = false;
		T obj = (T)item;

		System.out.println();
		while (aux != header && !found) {//Encontrarlo
			System.out.print(" " + aux.data);
			if (equalsNull(obj, aux.data)){
				found = true;
			}
			else{
				aux = aux.next;
			}
		}

		if (found) {//Quitarlo
			aux.prev.next = aux.next;
			aux.next.prev = aux.prev;
			size--;
		}
		return found;
	}

	@Override
	public T remove(int index) {
		// TODO
		Node aux = recorrer(index);
		aux.prev.next = aux.next;
		aux.next.prev = aux.prev;
		size--;
		return aux.data;
	}


	@Override
	public boolean contains(Object item) {
		// TODO
		Node aux = header.next;
		boolean found = false;
		T obj = (T)item;

		System.out.println();
		while (aux != header && !found) {//Encontrarlo
			System.out.print(" " + aux.data);
			if (equalsNull(obj, aux.data)){
				found = true;
			}
			else{
				aux = aux.next;
			}
		}

		return found;
	}

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
	public T get(int index) {
		// TODO
		Node aux = recorrer(index);
		return aux.data;
	}

	@Override
	public T set(int index, T element) {
		// TODO
		Node aux = recorrer(index);
		System.out.println(aux.data);
		T lastElement = aux.data;
		aux.data = element;
		return lastElement;
	}

	@Override
	public int indexOf(Object item) {
		Node aux = header.next;
		int i = 0;

		while (aux != header) {
			if (equalsNull(aux.data, item))
				return i;
			aux = aux.next;
			i++;
		}

		return -1;
	}

	@Override
	public int lastIndexOf(Object item) {
		// TODO
		Node aux = header.next;
		int i = 0;
		int lastIndex = -1;
		while (aux != header) {
			if (equalsNull(aux.data, item))
				lastIndex  = i;
			aux = aux.next;
			i++;
		}
		return lastIndex;
	}

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
    throw new UnsupportedOperationException();
	}

    @Override
	public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

	@Override
	public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		//TODO
		Node aux = header.next;
		while (aux != header){
			if (!c.contains(aux.data))
				remove(aux.data);
			aux = aux.next;
		}
     	return true;
	}
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append("[" );
		if (header.next != header) {
			Node aux = header.next;
			str.append(aux.data == null ? "null" : aux.data.toString());
			while (aux.next != header) { 
				aux = aux.next;
				str.append(", ");
				str.append(aux.data == null ? "null" : aux.data.toString());
			}
		}
		str.append("],compar size = ");
		str.append(size);
		
		return str.toString();
	}

	@Override
	public Object[] toArray() {
		Object retVal[] = new Object[size];

		Node aux = header.next;
		int i = 0;
		while (aux != header)
			retVal[i++] = aux.data;
		return retVal;
	}

	@Override
	public <U> U[] toArray(U[] a) {
		U[] retVal;
		if (a.length > size)
			retVal = a;
		else
			retVal = (U[]) new Object[size];

		Node aux = header.next;
		int i = 0;
		while (aux != header)
			retVal[i++] = (U) aux.data;

		return retVal;
	}



}
