package graphs;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface Graph<TYPE> extends Serializable{
	public void add(TYPE node);
	public void connect(TYPE from, TYPE to, String by, int weight);
	public boolean contains(TYPE node);
	public Edge<TYPE> getEdgeBetween(TYPE from, TYPE to);
	public List<Edge<TYPE>> getEdgesFrom(TYPE from);
	public Set<TYPE> getNodes();
	public void setConnectionWeight(TYPE from, TYPE to, int newWeight);
	public String toString();
	public void disconnect(TYPE from, TYPE to);
	public void remove(TYPE node);
}
