package graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphMethods {
	private static <T> void depthFirstSearch(Graph<T> graph, T source, Set<T> visited) {
		visited.add(source);
		for (Edge<T> e : graph.getEdgesFrom(source)) {
			if ( !visited.contains(e.getDestination()) ) {
				depthFirstSearch(graph, e.getDestination(), visited);
			}
		}
	}
	
	public static <T> ArrayList<Edge<T>> getPath(Graph<T> graph, T from, T to) {
		
		if (!pathExists(graph, from, to)) { return null; }

		Map<T, Integer> distance = new HashMap<T, Integer>();
		Map<T, T> priorNode = new HashMap<T, T>();
		Map<T, Boolean> processed = new HashMap<T, Boolean>();
		
		for (T node : graph.getNodes()) {
			distance.put(node, Integer.MAX_VALUE);
			priorNode.put(node, null);
			processed.put(node, false);
		}
		
		processed.put(from, true);
		distance.put(from, 0);
		T probe = from;
		
		while (processed.get(to) == false) {
			for (Edge<T> edge : graph.getEdgesFrom(probe)) {
				if ( distance.get(probe) + edge.getWeight() < distance.get(edge.getDestination()) ) {
					distance.put(edge.getDestination(), distance.get(probe) + edge.getWeight());
					priorNode.put(edge.getDestination(), probe);
				}
			}
			
			int minDist = Integer.MAX_VALUE;
			for (T node : graph.getNodes()) {
				if ( !processed.get(node) && distance.get(node) < minDist ) {
					minDist = distance.get(node);
					probe = node;
				}
			}
			processed.put(probe, true);
		}
		
		ArrayList<Edge<T>> path = new ArrayList<Edge<T>>();
		T dest = to;
		T prior = priorNode.get(dest);
		
		while (prior != null) {
			path.add(graph.getEdgeBetween(prior,dest));
			dest = prior;
			prior = priorNode.get(prior);
		}
		
		Collections.reverse(path);
		return path; 
	}
	
	public static <T> boolean pathExists(Graph<T> graph, T from, T to) {
		if ( !graph.contains(from) || !graph.contains(to)) { return false; }
		Set<T> visited = new HashSet<T>();
		depthFirstSearch(graph, from, visited);
		return visited.contains(to);
	}
}
