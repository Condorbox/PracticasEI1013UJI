package Practica3;

import java.security.InvalidParameterException;
import java.util.*;

public class RedCarreteras {
	private Map<String, Map<String, Integer>> red;

	/**
	 * Constructor
	 * 
	 * Crea una red de carreteras vacías.
	 */
	public RedCarreteras() {
		//TO DO
		red = new HashMap<>();
	}

	/**
	 * Valida que un tramo sea correcto.
	 * 
	 * Es decir, que ninguna de las dos ciudades sea null, y que la distancia sea
	 * mayor de cero. No se admiten tramos de una ciudad con sigo misma. En el
	 * caso de que un tramo no sea válido produce una excepción.
	 * 
	 * @param una,
	 *            una ciudad
	 * @param otra,
	 *            la otra ciudad
	 * @param distancia,
	 *            la distancia del tramo
	 * @throws InvalidParamenterException
	 *             si el tramo no es válido.
	 */
	private void validarTramo(String una, String otra, int distancia) {
		// TO DO
		if (una == null || otra == null || una.equals(otra) || distancia <= 0) {
			throw new InvalidParameterException();
		}
	}

	/**
	 * Devuelve un conjunto con todas las ciudades de la red.
	 */
	public Set<String> ciudades() {
		return red.keySet();
	}

	/**
	 * Añade un tramo a la red.
	 * 
	 * Valida que el tramo sea valido. Si alguna de las dos ciudades no existiá
	 * previamente en la red, la añade. El tramo se añadirá dos veces, una para
	 * cada ciudad. En caso de que el tramo ya existiera remplazará el valor de
	 * la distancia.
	 * 
	 * @return Distancia previa del tramo, -1 si el tramo no exitía.
	 * @throws InvalidParamenterException
	 *             si el tramo no es válido.
	 */
	public int nuevoTramo(String una, String otra, int distancia) {
		// TO DO
		Map<String, Integer> tramo;
		int numToReturn = -1;

		validarTramo (una, otra, distancia);

		//Una
		if (red.containsKey(una)){
			tramo = red.get(una);
			if (!tramo.containsKey(otra)) {
				tramo.put(otra,distancia);
			}else
				numToReturn = tramo.put(otra,distancia);
		}else{
			tramo = new HashMap<>();
			tramo.put(otra,distancia);
			red.put(una,tramo);
		}
		//Otra
		if (red.containsKey(otra)){
			tramo = red.get(otra);
			if (!tramo.containsKey(una)){
				tramo.put(una,distancia);
			}else
				numToReturn = tramo.put(una,distancia);
		}else{
			tramo = new HashMap<>();
			tramo.put(una,distancia);
			red.put(otra,tramo);
		}

		return numToReturn;
	}

	/**
	 * Comprueba si existe un tramo entre dos ciudades.
	 * 
	 * @return La distancia del tramo, o -1 si no existe
	 */
	public int existeTramo(String una, String otra) {
		// TO DO
		Map<String, Integer> tramo;
		if (una == otra)
			return 0;
		if(red.containsKey(una)){
			tramo = red.get(una);
			if (tramo.containsKey(otra))
				return tramo.get(otra);
		}
		/*if(red.containsKey(otra)){
			tramo = red.get(otra);
			if (tramo.containsKey(una))
				return  tramo.get(una);
		}*/
		return -1;
	}

	/**
	 * Borra el tramo entre dos ciudades si existe.
	 * 
	 * @return La distancia del tramo, o -1 si no existía.
	 */
	public int borraTramo(String una, String otra) {
		int numToReturn = existeTramo(una, otra);
		Map<String, Integer> tramo;
		// TO DO
		if (numToReturn != -1){
			//Una
			tramo = red.get(una);
			tramo.remove(otra);
			//Otra
			tramo = red.get(otra);
			tramo.remove(una);
		}

		return numToReturn;
	}

	/**
	 * Comprueba si un camino es posible.
	 * 
	 * Un camino es una lista de ciudades. El camino es posible si existe un
	 * tramo entre cada para de ciudades consecutivas. Si dos ciudades
	 * consecutivas del camino son la misma el camino es posible y la distancia
	 * añadida es 0. Si el camino incluye una sóla ciudad de la red el resultado es 0.
	 * 
	 * @return La distancia total del camino, es decir la suma de las distancias
	 *         de los tramos, o -1 si el camino no es posible o no incluye ninguna ciudad.
	 */
	public int compruebaCamino(List<String> camino) {
		// TODO
		int tramo = 0;
		int suma;
		if (camino.size() == 0){
			tramo = -1;
		}else{
			ListIterator<String> it = camino.listIterator();
			String anterior = it.next();
			while (it.hasNext()){
				String e = it.next();
				suma = existeTramo(anterior,e);
				if (suma == -1){
					tramo = -1;
					break;
				}
				if (!e.equals(anterior))
					tramo += suma;
				System.out.println("Desde: " + anterior + " hasta: " + e + " hay: " + suma);
				anterior = e;
			}
		}

		return tramo;
	}

}
