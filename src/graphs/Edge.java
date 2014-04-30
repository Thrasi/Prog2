package graphs;

import java.io.Serializable;

public class Edge<TYPE> implements Serializable{
	private TYPE destination;
	private String name;
	private int weight;
	
	Edge(TYPE destination, String name, int weight) {
		this.destination = destination;
		this.name = name;
		setWeight(weight);
	}
	
	TYPE getDestination() { return destination; }
	
	public String getName() { return name; }
	
	public int getWeight() { return weight; }
	
	@Override
	public String toString() { return name + " to " + destination + "(" + weight + ")"; }

	void setWeight(int newWeight) { 
		if ( newWeight < 0 )
			{ throw new IllegalArgumentException("Edge weight cannot be negative!"); }
		weight = newWeight; 
	}
}
