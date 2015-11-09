package Forest.Fire.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author alexander.alt
 *
 */
public class Node {
	final int ID;
	int depth;
	private ArrayList<Edge> edges;
	List<Integer> outgoingNeighbors;
	List<Integer> incomingNeighbors;
	
	////////////////// Construktor /////////////////
	/**
	 * 
	 * @param id
	 */
	public Node(int id){
		ID=id;
		edges = new ArrayList<Edge>();
		outgoingNeighbors = new ArrayList<Integer>();
		incomingNeighbors = new ArrayList<Integer>();
	}
	
	/////////////////// ADD //////////////////////////
	/**
	 * Adds a Node into the outgoing neighbor list. 
	 * @param node The new neighbor
	 */
	boolean outgoingNode(Node node){
		if (outgoingNeighbors.contains(node.ID)) return false;
		outgoingNeighbors.add(node.ID);
		return true;
		
	}
	
	/**
	 * Adds a Node into the incoming neighbor list.
	 * @param node The new node
	 */
	boolean incomingNode(Node node){
		if (incomingNeighbors.contains(node.ID)) return false;
		incomingNeighbors.add(node.ID);
		return true;
	}
	
	/**
	 * Adds a Node into the outgoing and the incoming neighbor list.
	 * @param node The new node
	 */
	boolean undirectedNeighbor(Node node){
		boolean out = outgoingNode(node);
		boolean in  = incomingNode(node);
		return ( out || in);		
	}
	
	/**
	 * adds an edge which starts or end in this node
	 * 
	 * Warning: this function does not tests for duplicates.
	 * When e edge is undirected, nonetheless it must not added twice.
	 * This difference is managed by (incomming/outgoing)Neighbors.
	 * @param edge
	 */
	void addEdge(Edge edge){
		edges.add(edge);
	}
	
	//////////// GET /////////////////////
	
	/**
	 * @return a Iterator over all outgoing neighbors
	 */
	public Iterator<Integer> getOutgoingNeighbors(){
		return outgoingNeighbors.iterator();
	}
	
	/**
	 * @return a Iterator over all incoming neighbors
	 *
	public Iterator<Node> getIncomingNeighbors(){
		return incomingNeighbors.iterator();
	}
	*/
	/**
	 * The degree is update whenever a new edge is created. Thus this function runs in constant time.
	 * @return the number of edges which start or end in this node.
	 */
	public int getDegree(){
		return outgoingNeighbors.size();	
	}
	
	
	/////////// DEBUG /////////////////////
	/**
	 * @return Returns important informations about this node. 
	 */
	public String toString(){
		String result ="Node:\t";
		result += ID;
		int degree = getDegree();
		result +="\tdegree:\t"+degree;
		result +="\tdepth:\t"+depth;
		if(degree>0){
			result+="\tneighbors:\t[";
			for(int neighbor: outgoingNeighbors){
				result+=neighbor+",";
			}
			result+="]";
		}
		return result;
	}
}