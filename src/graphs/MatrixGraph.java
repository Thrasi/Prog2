package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class MatrixGraph<TYPE> implements Graph<TYPE> {
	
	private Map<TYPE,Integer> nodeIndex;
	private int maxNodes;
	private LinkedList<Integer> emptyPlaces;
	private Edge<TYPE>[][] data;
	
	public MatrixGraph(int maxNodes) {
		this.maxNodes = maxNodes;
		nodeIndex = new HashMap<TYPE,Integer>();
		data = new Edge[maxNodes][maxNodes];
		emptyPlaces = new LinkedList<Integer>();
		for (int i=0;i<maxNodes;i++) {
			emptyPlaces.add(i);
		}
	}
	
	@Override
	public void add(TYPE node) {
		if (emptyPlaces.isEmpty() || nodeIndex.containsKey(node)) { return; }
		nodeIndex.put(node, emptyPlaces.getFirst());
	}

	@Override
	public void connect(TYPE from, TYPE to, String by, int weight) {
		if ( getEdgeBetween(from, to) != null ) 
			{ throw new IllegalStateException("Nodes are already connected!"); }
		
		data[nodeIndex.get(from)][nodeIndex.get(to)] = new Edge<TYPE>(to, by, weight);
		data[nodeIndex.get(to)][nodeIndex.get(from)] = new Edge<TYPE>(from, by, weight);
	}

	@Override
	public boolean contains(TYPE node) {
		return nodeIndex.containsKey(node);
	}

	@Override
	public Edge<TYPE> getEdgeBetween(TYPE from, TYPE to) {
		if ( !contains(from) || !contains(to) ) 
			{ throw new NoSuchElementException("Node does not exist!"); }
		return data[nodeIndex.get(from)][nodeIndex.get(to)];
	}

	@Override
	public List<Edge<TYPE>> getEdgesFrom(TYPE from) {
		if (!contains(from)) {
			{ throw new NoSuchElementException("Node does not exist!"); }
		}
		List<Edge<TYPE>> edges = new ArrayList<Edge<TYPE>>();
		for (int i=0;i<maxNodes;i++) {
			if (data[nodeIndex.get(from)][i] != null) {
				edges.add(data[nodeIndex.get(from)][i]);
			}
		}
		return edges;
	}

	@Override
	public Set<TYPE> getNodes() {
		return new HashSet<TYPE>(nodeIndex.keySet());
	}

	@Override
	public void setConnectionWeight(TYPE from, TYPE to, int newWeight) {
		Edge<TYPE> e1 = getEdgeBetween(from, to);
		if ( e1 == null ) 
			{ throw new NoSuchElementException("Edge does not exist!"); }
		Edge<TYPE> e2 = getEdgeBetween(to, from);
		e1.setWeight(newWeight);
		e2.setWeight(newWeight);
	}

	@Override
	public void disconnect(TYPE from, TYPE to) {
		if (!contains(from) || !contains(to))
			{ throw new NoSuchElementException("Node does not exist!"); }
	
		data[nodeIndex.get(from)][nodeIndex.get(to)] = null;
		data[nodeIndex.get(to)][nodeIndex.get(from)] = null;
	}

	@Override
	public void remove(TYPE node) {
		int index = nodeIndex.get(node);
		for (int i=0;i<maxNodes;i++) {
			data[index][i] = null;
			data[i][index] = null;
		}
		nodeIndex.remove(node);
		emptyPlaces.add(index);
	}
}
