package Practica7;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Heaps {
	public static String toString(int[] heap) {
		StringBuilder s = new StringBuilder();
		int enNivel = 1;
		int finNivel = 1;
		for (int i = 0; i < heap.length; i++) {
			s.append(heap[i]);

			if (i != heap.length -1)
				if (i == finNivel-1) {
					s.append("] [");
					enNivel *= 2;
					finNivel += enNivel;
				} else
					s.append(", ");
		}
		s.append("]");
		return "Heap: [" + s.toString() + " - size: " + heap.length;
	}

	/**
	 * @return -1 if the queue contains a MinHeap, +1 if it is a maxHeap, or = 0 if its empty.
	 */
	public static int typeOfHeap(int [] data) {
		// TODO EJERCICIO 1
		boolean notMin = false, notMax = false;
		int res;
		for(int i = 0; i<data.length; i++){
			if (data.length>(i*2)+1){ //Hijo izq
				if(data[i]>data[(i*2)+1]){
					notMin = true;
				}else{
					notMax = true;
				}
			}
			if (data.length>(i*2)+2){ //Hijo der
				if(data[i]>data[(i*2)+2]){
					notMin = true;
				}else{
					notMax = true;
				}
			}
		}
		if (notMax&&!notMin){
			res = -1;
		}else if (notMin&&!notMax){
			res = 1;
		}else{
			res = 0;
		}
		return res;
	}
	
	/** Sinks the element in the position p, assuming that v is a maxHeap */
	private static void sink(int[] v, int p, int size) {
		// TODO EJERCICIO 2
		int max = p;
		int izq = 2*p+1;
			if(izq<size){
				if (v[izq]>v[p]){
					max = izq;
				}
				int der = izq+1;
				if (der<size){
					if (v[der]>v[max]){
						max = der;
					}
				}
				if(max != p){
					swap(p,max,v);
					sink(v,max,size);
				}
		}
	}

	private static void swap(int i, int j, int v[]){
		int aux = v[i];
		v[i] = v[j];
		v[j] = aux;
	}

	/**Converts an unsorted vector into a maxHeap.**/
	public static void maxHeapify (int[] v) {
		// TODO EJERCIO 3
		int i = 0;
		while(typeOfHeap(v) != 1){
			sink(v,i,v.length);
			if(v.length == 0)
				break;
			i  = (i + 1)%v.length;

		}
	}


	
	/** Sorts the vector v using the heapsort algorithm **/
	static public void heapsort (int [] v) {
		// TODO EJERICIO 4
		//Arrays.sort(v); Opción 1
		sort(v, v.length); // Opción 2
	}

	private static void sort(int v[], int size){
		if (size <= 1)
			return;

		for (int i=0; i<size-1; i++){
			if (v[i]>v[i+1]){
				swap(i, i+1, v);
			}
		}
		sort(v,size-1);
	}
}
