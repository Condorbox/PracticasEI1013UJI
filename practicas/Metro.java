package Practica8;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Metro {

	protected class InfoLineaMetro {
		protected String nombre;  //Nombre de la línea de metro
		protected String color;   //Color asociado a la línea de metro
		protected List<String> paradas;
	}
	private HashMap<String,InfoLineaMetro> lineasMetro; //información de cada linea
	private EDTreeGraph<String, String> grafoMetro;

	
	public Metro(String filename) {
		lineasMetro = new HashMap<>();
		grafoMetro = new EDTreeGraph<>(false, true); //grafo no dirigido
		leerGrafo(filename);
	}
	
	//Lee el fichero con la informaci�n de las lineas de metro
	private void leerGrafo (String nomfich) {
		Scanner inf=null;
		try {
			inf = new Scanner (new FileInputStream(nomfich));
		} catch(FileNotFoundException e){
			System.out.println("Error al abrir el fichero");
			System.exit(0);
		}
		int NLineasMetro=inf.nextInt(); //numero de lineas de metro

		for (int i=0; i<NLineasMetro; i++) {  //Para cada linea
			String lineaId = inf.next();  //linea, etiqueta arcos
			InfoLineaMetro linea=new InfoLineaMetro();
			linea.color = inf.next();
			linea.nombre = inf.next();
			//System.out.println("color "+linea.color + " nombre "+linea.nombre);
			int nparadas = inf.nextInt(); //numero de paradas de esa linea
			//System.out.println("nparadas "+nparadas);
			inf.nextLine();
			linea.paradas=new ArrayList<>();
			String origen = inf.nextLine();
			linea.paradas.add(origen.toLowerCase());  //se añade a la información de la línea
			if (!grafoMetro.containsNode(origen.toLowerCase()))  //Se añade al grafo si no está
				grafoMetro.insertNode(origen.toLowerCase());

			String dest;
			for (int j=1; j<nparadas; j++ ) {
				dest = inf.nextLine();
				linea.paradas.add(dest.toLowerCase());  //se añade a las paradas de la línea
				if (!grafoMetro.containsNode(dest.toLowerCase()))
					grafoMetro.insertNode(dest.toLowerCase());  //Se añade al grafo si no está

				grafoMetro.insertEdge(origen.toLowerCase(),dest.toLowerCase(),lineaId); //Se añade el arco al grafo
				origen=dest;
			}
			this.lineasMetro.put(lineaId,linea);
		}
		inf.close();
	}

	public void printMetro() {
		for (String linea:this.lineasMetro.keySet()) {
			InfoLineaMetro infolinea=this.lineasMetro.get(linea);
			System.out.println (linea+" "+infolinea.color+" "+infolinea.nombre+" estaciones: ");
			System.out.println(infolinea.paradas.toString());
		}
		System.out.println("Grafo del metro: ");
		this.grafoMetro.printGraphStructure();
		//Comprobaciones
		System.out.println("Numero de estaciones: "+this.grafoMetro.getKeys().size());
	}


	//Devuelve el n�mero total de estaciones del metro
	public int getNumeroEstaciones() {
		return grafoMetro.size();
	}

	
	public Set<String> getEstaciones() {
		return grafoMetro.getKeys();
	}
	
	public void printListaEstaciones() {
		Set<String> estaciones = getEstaciones();
		for (String estacion: estaciones)
			System.out.print(estacion+", ");
		System.out.println();
	}

	//Devuelve la linea que conecta directamente 2 estaciones y null si no están conectadas directamente
	public String estacionesConectadas(String origen, String destino) {
		return this.grafoMetro.getEdgeWeight(origen, destino);
	}

	//Devuelve un conjunto con los identificadores de las líneas de metro que tienen parada en esa estación
	//Si la estación no existe, devuelve null
	public Set<String> getLineasEstacion(String estacion) {
		// TODO EJERCICIO 2
		if(grafoMetro.containsNode(estacion)){
			Set<String> lineas = new HashSet<>();
			List<Edge<String, String>> adyacentes  = grafoMetro.getAdjacentArcs(estacion);
			for (Edge<String, String> ady:adyacentes){
				lineas.add(ady.getWeight());
			}
			return lineas;
		}
		return null;
	}

	//Devuelve todas las estaciones de to\do el recorrido de una linea dado su identificador
	//Si dir=='f', las devolverá en sentido almacenado, si no, en sentido contrario
	//Si la línea no existe, devuelve null
	public List<String> getRecorridoLinea(String idlinea, char dir) {
		// TODO EJERCICIO 3
		boolean existe = false;
		List<String> estaciones = new LinkedList<>();

		for (String e : lineasMetro.keySet()){
			if (e.equals(idlinea)){
				existe = true;
				Iterator<String> it = lineasMetro.get(e).paradas.iterator();
				it.forEachRemaining(estaciones::add);
			}
		}
		if (dir != 'f')
			Collections.reverse(estaciones);

		return existe? estaciones : null;
	}


	// Determina si dos estaciones se encuentran en la miusmo línea. Si es así devolver el código de la línea.
	// En caso contrario deveolverá null
	public String mismaLinea (String origen, String destino) {
		// TODO EJERCICIO 4
		Set<String> lienasOrigen = getLineasEstacion(origen);
		Set<String> lienasDestino = getLineasEstacion(destino);
		if (lienasOrigen!=null&&lienasDestino!=null){
			for (String e: lienasOrigen){
				if (lienasDestino.contains(e))
					return e;
			}
		}
		return null;
	}


	// Calcula la ruta entre la estación origen y la estación destino. Devuelve una lista
	// de arcos con la siguiente estación en la ruta y la línea por la que se llega.
	//Si alguna de las dos estaciones (origen/destino) no existe, devuelve una lista vacía
	//Para calcular la ruta: si las estaciones están en el recorrido de una misma línea de metro,
	//se devolverá esa línea. En caso contrario, se buscará la ruta con menor número de paradas.

	public List<Edge<String, String>> ruta(String origen, String destino) {
		// TODO EJERCICIO 5
		if (grafoMetro.containsNode(origen)&&grafoMetro.containsNode(destino)){
			List<Edge<String, String>> res = new LinkedList<>();
			String idLinea = mismaLinea(origen,destino);
			if (idLinea!=null){//Misma linea
				boolean esOrigen = false;
				List<String> estaciones = reverseEstaciones(idLinea, origen,destino);
				for (String e: estaciones){
					if (e.equals(origen)){
						esOrigen = true;
					}

					if (esOrigen){
						Edge<String, String> actual = new Edge(e,idLinea);
						res.add(actual);
					}

					if (e.equals(destino)){
						return res;
					}
				}
			}else{ //Distinta linea, recorrido por anchura
				/*List<Edge<String, String>> caminos = new LinkedList<>();
				for (String e:grafoMetro.getKeys()){
					Edge<String, String> ed = new Edge<>(e ,null);
					caminos.add(ed);
				}
				Queue<String> q  = new LinkedList<>();
				q.add(origen);
				while (!q.isEmpty()){
					String actual = q.remove();
					for (String edge : grafoMetro.getAdjacentNodes(actual)){
						int index = getNodeIndex(actual);
						if (caminos.get(index).getWeight() == null){
							caminos.get(index).setWeight(grafoMetro.getEdgeWeight(actual, edge));
							q.add(edge);
						}
					}
				}
				for (Iterator<Edge<String, String>> it = caminos.listIterator(); it.hasNext();){
					Edge<String, String> edge = it.next();
					if (edge.getWeight() == null){
						it.remove();
					}
				}
				return caminos;*/
				return bfs(origen, destino);
			}
		}
		return null;
	}

	private List<Edge<String, String>> bfs(String start, String end){
		List<Edge<String, String>> caminos = new LinkedList<>();
		Map<String, String> parent = new HashMap<>();
		boolean visitados[] = new boolean[grafoMetro.size()];

		for (int i = 0;i<visitados.length;i++){
			visitados[i] = false;
		}

		Queue<String> q = new LinkedList<>();
		q.add(start);
		parent.put(start, grafoMetro.getEdgeWeight(start,grafoMetro.getAdjacentNodes(start).get(0)));
		visitados[getNodeIndex(start)] = true;
		while (!q.isEmpty()&&visitados[getNodeIndex(end)]==false){
			String actual = q.remove();
			for (String ed : grafoMetro.getAdjacentNodes(actual)){
				int ady = getNodeIndex(ed);
				if (visitados[ady]==false){
					visitados[ady] = true;
					parent.put(ed, actual);
					q.add(ed);
				}
			}
		}

		String toBeFound = end;
		String anterior = end;
		boolean primero = true;

		while (toBeFound!=start){
			Edge<String, String> edge;

			if (primero){
				 edge = new Edge(toBeFound, grafoMetro.getEdgeWeight(toBeFound,grafoMetro.getAdjacentNodes(toBeFound).get(0)));
				 primero = false;
			}else{
				edge = new Edge(toBeFound, grafoMetro.getEdgeWeight(toBeFound, anterior));
			}

			caminos.add(edge);
			anterior = toBeFound;
			toBeFound = parent.get(toBeFound);
		}

		Edge<String, String> startEdge = new Edge(start, grafoMetro.getEdgeWeight(start,grafoMetro.getAdjacentNodes(start).get(0)));
		caminos.add(startEdge);

		Collections.reverse(caminos);

		return caminos;
	}

	private List<String> reverseEstaciones(String idLinea,String origen, String destino){
		boolean esOrigen = false;
		List<String> estaciones = getRecorridoLinea(idLinea, 'f');
		for (String e : estaciones){
			if (e.equals(origen)){
				esOrigen = true;
			}

			if (e.equals(destino)){
				if (!esOrigen){
					estaciones = getRecorridoLinea(idLinea, 'r');
				}
				break;
			}
		}
		return estaciones;
	}

	private int getNodeIndex(String origen){
		int res  = -1;
		int i  = 0;
		for (String e:grafoMetro.getKeys()){
			if (e.equals(origen)){
				return i;
			}
			i++;
		}

		return res;
	}
}
