package Forest.Fire.Model;

/**
 * 
 * @author alexander.alt
 *
 */
public class Edge {
	final boolean directed;
	final Node start;
	final Node end;
	
	/**
	 * A new undirected edge from start to end
	 * @param start the startnode
	 * @param end the endnode 
	 */
	public Edge(Node start, Node end){
		this(false,start,end);
		
	}
	
	/**
	 * Creates a new undirected or directed edge from start to end
	 * @param directed Is this edge directed or not?
	 * @param start the startnode
	 * @param end the endnode
	 */
	Edge(boolean directed, Node start,Node end){
		this.directed=directed;
		this.start=start;
		this.end=end;
	}
	
	///////////// DEBUG //////////////////
	/**
	 * @return IDs of startnode and endnode 
	 */
	public String toString(){
		return "["+start.ID+","+end.ID+"]";
	}
}
