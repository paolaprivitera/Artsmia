package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	private Graph<ArtObject,DefaultWeightedEdge> graph;
	
	private Map<Integer, ArtObject> idMap; // IMPORTANTE!
	// Uso la mappa per salvare gli oggetti che ho recuperato dal db e creato
	// Prima di creare un nuovo oggetto vado sempre a vedere se c'e' gia' nella mappa
	// Quindi devo passare la mappa ad ogni metodo del DAO
	// in cui devo creare un nuovo oggetto (prima di fare la new)
	
	public Model() {
		idMap = new HashMap<Integer,ArtObject>();
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}
	
	public void creaGrafo() {
		ArtsmiaDAO dao = new ArtsmiaDAO();
		dao.listObjects(idMap);
		
		//aggiungo i vertici
		Graphs.addAllVertices(graph, idMap.values());
			// se avessi avuto la lista gli avrei potuto passare direttamente la lista
			// siccome abbiamo la mappa dobbiamo passare i valori della mappa
		
		//aggiungo gli archi
		List<Adiacenza> adj = dao.listAdiacenze();
		
		for(Adiacenza a : adj) {
			ArtObject source = idMap.get(a.getO1()); // xke' nell'idMap ho salvato l'id
			ArtObject dest = idMap.get(a.getO2());
			Graphs.addEdge(graph, source, dest, a.getPeso());
			
			// Al professore dava problemi con il db perche' c'erano delle righe incomplete
			// Posso risolvere cosi':
			/*
			 * try {
			 * 		Graphs.addEdge(graph, source, dest, a.getPeso());
			 * }
			 * catch(Throwable t) {}
			 */
		}
		
		System.out.println("Grafo creato: " + graph.vertexSet().size() + 
				" vertici e " + graph.edgeSet().size() + " archi");
		
	}

	public int getVertexSize() {
		return graph.vertexSet().size();
	}
	
	public int getEdgeSize() {
		return graph.edgeSet().size();
	}
	
}
