package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class ListGraph<TYPE> implements Graph<TYPE> {
	private Map<TYPE, List<Edge<TYPE>>> data = new HashMap<TYPE, List<Edge<TYPE>>>();
	
	public void add(TYPE node) {
		if ( !data.containsKey(node)) 
			{ data.put(node, new ArrayList<Edge<TYPE>>()); }
	}
	
	public void connect(TYPE from, TYPE to, String by, int weight) {
		List<Edge<TYPE>> fromEdges = data.get(from);
		List<Edge<TYPE>> toEdges = data.get(to);
		
		if ( getEdgeBetween(from, to) != null ) 
			{ throw new IllegalStateException("Nodes are already connected!"); }
		
		fromEdges.add(new Edge<TYPE>(to, by, weight));
		toEdges.add(new Edge<TYPE>(from, by, weight));
	}
	
	public boolean contains(TYPE node) {
		return data.keySet().contains(node);
	}
	
	public void disconnect(TYPE from, TYPE to) {
		if (!contains(from) || !contains(to))
			{ throw new NoSuchElementException("Node does not exist!"); }
		
		data.get(from).remove( getEdgeBetween(from, to) );
		
		data.get(to).remove( getEdgeBetween(to, from) );
	}

	public Edge<TYPE> getEdgeBetween(TYPE from, TYPE to) {
		List<Edge<TYPE>> fromEdges = getEdgesFrom(from);
		
		if ( !contains(from) || !contains(to) ) 
			{ throw new NoSuchElementException("Node does not exist!"); }
		for ( Edge<TYPE> e : fromEdges ) {
			if ( e.getDestination().equals(to) )
				{ return e; }
		}
		return null;
	}
	
	public List<Edge<TYPE>> getEdgesFrom(TYPE from) { 
		if (!contains(from)) {
			{ throw new NoSuchElementException("Node does not exist!"); }
		}
		return new ArrayList<Edge<TYPE>>(data.get(from));
	}
	
	public Set<TYPE> getNodes() { 
		return new HashSet<TYPE>(data.keySet()); 
	}
	
	public void remove(TYPE node) {
		if (data.containsKey(node)) {
			while (getEdgesFrom(node).size() != 0) {
				disconnect(node, getEdgesFrom(node).get(0).getDestination());
			}
			data.remove(node);
		}
	}
	
	public void setConnectionWeight(TYPE from, TYPE to, int newWeight) {
		Edge<TYPE> e1 = getEdgeBetween(from, to);
		if ( e1 == null ) 
			{ throw new NoSuchElementException("Edge does not exist!"); }
		Edge<TYPE> e2 = getEdgeBetween(to, from);
		e1.setWeight(newWeight);
		e2.setWeight(newWeight);
	}
	
	public String toString() {
		String str = "";
		for (TYPE n : getNodes()) {
			str += n + ": ";
			for (Edge<TYPE> e: getEdgesFrom(n)) {
				str += e.toString() + " ";
			}
			str += "\n";
		}
		return str;
	}
}

















