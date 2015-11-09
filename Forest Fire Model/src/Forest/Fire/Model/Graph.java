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
public class Graph {

	private int nodes;
	private int edges;
	
	Analysis analysis;
	
	private Map<Integer,Node> nodelist;
	private List<Edge> edgelist;
	
	//For every step count how man time for creation and analysis is used.
	public long creationTime;
	
	//////////// Construktor ///////////////
	/**
	 * Creates a new graph with no nodes and edges.
	 * Initializes all lists.
	 */
	public Graph(){
		nodes = 0;
		edges = 0;
		nodelist = new HashMap<Integer,Node>();
		edgelist = new ArrayList<Edge>();
		analysis = new Analysis(this);
	}
	
	public void reset() {
		nodes = 0;
		edges = 0;
		nodelist = new HashMap<Integer,Node>();
		edgelist = new ArrayList<Edge>();	
	}	
	////////////// ADD /////////////
	/**
	 * Adds a new isolated node to the graph.
	 * Because no ID is specified, the current number of nodes is used as default. 
	 */
	public void addnode(){
		addnode(nodes);
	}
	
	/**
	 * Adds a new isolated node to the graph.
	 * @param id The ID of the node. The IDs should be consecutive with the first node having ID 0 (the 14th node should have ID 13)
	 */
	public Node addnode(int id){
		Node newnode = new Node(id);
		nodelist.put(id,newnode);
		nodes++;
		return newnode;
	}
	
	/**
	 * Adds an undirected edge to the graph.
	 * @param start The ID of the start node
	 * @param end The ID of the end node
	 */
	public void addedge(int start, int end){
		addedge(false,start,end);
	}
	
	/**
	 * Adds an undirected or directed edge to the graph.
	 * @param directed Is the new edge directed or not?
	 * @param start The ID of the start node
	 * @param end The ID of the end node
	 */
	public void addedge(boolean directed, int start, int end){
		Node startnode = getNode(start);
		Node endnode = getNode(end);
		
		boolean newedge;
		if(!directed){
			boolean st = startnode.undirectedNeighbor(endnode);
			boolean en = endnode.undirectedNeighbor(startnode);
			newedge = (st || en); 
		}else{
			boolean st = startnode.outgoingNode(endnode);
			boolean en = endnode.incomingNode(startnode);
			newedge = (st || en);
		}
		if (newedge){
			Edge edge = new Edge(startnode,endnode);
			startnode.addEdge(edge);
			endnode.addEdge(edge);
			edgelist.add(edge);
			edges++;
		}
	}
	
	
	//////////////// NODE /////////////////
	/**
	 * @param ID The ID of the wanted node.
	 * @return Returns the Node with the specified ID.
	 */
	public Node getNode(int ID){
		return (Node) nodelist.get(ID);		
	}
	
	/**
	 * 
	 * @return An iterator over all nodes currently in the graph.
	 */
	public Iterator<Node> getNodeList(){
		return nodelist.values().iterator();
	}
	
	/**
	 * 
	 * @return The number of nodes in the graph. This value is automatically updated whenever a new node is added.
	 * Thus this function has constant running time
	 */
	public int getNodes(){
		return nodes;
	}
	
	////////////// EDGES ////////////////
	
	/**
	 * Not yet implemented
	 */
	public Edge getEdge(int startID, int endID){
		return null;
	}
	/**
	 * 
	 * @return An iterator over all edges currently in the graph.
	 */
	public Iterator<Edge> getEdgeList(){
		return edgelist.iterator();
	}
	
	/**
	 * 
	 * @return The number of edges in the graph. This value is automatically updated whenever a new node is added.
	 * Thus this function has constant running time
	 */
	public int getEdges(){
		return edges;
	}

	///////////// DEGREE //////////////////
	/**
	 * 
	 * @return The minimum degree of the entire graph.
	 */
	public int getMinDeg() {
		int result = Integer.MAX_VALUE;
		Iterator<Node> iter = nodelist.values().iterator();
		while(iter.hasNext()){
			Node node = iter.next();
			result = Math.min(result,node.getDegree());
		}
		return result;
	}
	
	/**
	 * 
	 * @return The maximum degree of the entire graph.
	 */
	public int getMaxDeg() {
		int result = 0;
		Iterator<Node> iter = nodelist.values().iterator();
		while(iter.hasNext()){
			Node node = iter.next();
			result = Math.max(result,node.getDegree());
		}
		return result;
	}
	
	/**
	 * 
	 * @return The average degree of the entire graph.
	 * This value is computed by iterating through all nodes, and not by 2*|E|/|V|, in order to enable double checking.
	 */
	public float getAvgDeg() {
		float result = 0;
		Iterator<Node> iter = nodelist.values().iterator();
		while(iter.hasNext()){
			Node node = iter.next();
			result += node.getDegree();
		}
		return result/nodes;
	}
	
	public float getAlpha(){
		double logedges = Math.log(edges);
		double lognodes = Math.log(nodes);
		float result = (float) (10000*logedges/lognodes);
		result = Math.round(result);
		result /= 10000;
		return result;
	}
	
	public int getMaxDepth() {
		int result = 0;
		Iterator<Node> iter = nodelist.values().iterator();
		while(iter.hasNext()){
			Node node = iter.next();
			result = Math.max(result,node.depth);
		}
		return result;
	}
	
	public float getAvgDepth() {
		float result = 0;
		Iterator<Node> iter = nodelist.values().iterator();
		while(iter.hasNext()){
			Node node = iter.next();
			result += node.depth;
		}
		result = (float) (10000*result/nodes);
		result = Math.round(result);
		result /= 10000;
		return result;
	}
	
	public float[] linearFactors(){
		float[] result = new float[8];
		//Nodes
		result[0] = nodes;
		
		//Edges
		result[1] = edges;
		
		//Alpha
		double logedges = Math.log(edges);
		double lognodes = Math.log(nodes);
		float alpha = (float) (10000*logedges/lognodes);
		alpha = Math.round(alpha);
		alpha /= 10000;
		result[2] = alpha;
		
		float maxDepth = 0;
		float sumDepth = 0;
		float maxDeg = 0;
		float minDeg = Integer.MAX_VALUE;
		float sumDeg = 0;
		Iterator<Node> iter = nodelist.values().iterator();
		while(iter.hasNext()){
			Node node = iter.next();
			
			//MaxDepth
			maxDepth = Math.max(maxDepth,node.depth);
	
		
			//AvgDepth
			sumDepth += node.depth;

			//MaxDeg
			maxDeg = Math.max(maxDeg,node.getDegree());
		//}
		//return result;
		
			//MinDeg
			minDeg = Math.min(minDeg,node.getDegree());
		//}
		//return result;
		
			//AvgDeg
			sumDeg += node.getDegree();
		//}
		//return result/nodes;
		}
		
		result[3] = maxDepth;
		
		sumDepth = (float) (10000*sumDepth/nodes);
		sumDepth = Math.round(sumDepth);
		sumDepth /= 10000;
		result[4] = sumDepth;
		
		result[5] = maxDeg;
		
		result[6] = minDeg;
		
		sumDeg = (float) (10000*sumDeg/nodes);
		sumDeg = Math.round(sumDeg);
		sumDeg /= 10000;
		result[7] = sumDeg;
		
		return result;
	}
	
	public String toString(){
		String result = "";
		Iterator<Node> iter = getNodeList();
		while(iter.hasNext()){
			result += iter.next().toString();
			result +="\n";
		}
		return result;
	}


}
