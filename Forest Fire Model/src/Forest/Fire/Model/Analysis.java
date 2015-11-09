package Forest.Fire.Model;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;



public class Analysis {

	static int[][] distances; 
	
	final StringBuffer buffer;
	final Graph parent;
	
	//Intermediate results
	int nodes;
	int edges;
    int maxDepth;
    int sumDepth;
	int maxDeg;
	int minDeg;
	int avgDeg;

	/**
	 * creates a link between the new instance of the analysis to the graph
	 * @param graph 
	 */
	public Analysis(Graph graph) {
		buffer = new StringBuffer();
		parent = graph;
	}

	/**
	 * Computes the diameter of the graph with the FLOYD-WARSHALL-ALGORITHMUS.
	 * and initializes the buffer
	 * @param graph 
	 * @return The diameter of the graph
	 */
	public int computeDiameter() {
		distances = new int[parent.getNodes()][parent.getNodes()];
		
		for(int row = 0; row < distances.length; row++){
			for(int column = 0; column < distances[row].length; column++){
				distances[row][column] = distances.length+1;
				if (row==column)
					distances[row][column] = 0;
			}	
		}

		Iterator<Edge> edgeiterator = parent.getEdgeList();
		while(edgeiterator.hasNext()){
			Edge edge = edgeiterator.next();
			//System.out.println("Edge: "+edge);
			
			distances[edge.start.ID][edge.end.ID]=1;
			if (!edge.directed) distances[edge.end.ID][edge.start.ID]=1;
		}
		

		//output(distances);
		for(int first=0; first < distances.length; first++){
			//System.out.println("");
			//System.out.println("Start Iteration");
			for(int second=0; second < distances.length; second++){
				for(int third=0; third < distances.length; third++){
					//System.out.println(distances[second][third] +">"+ distances[second][first] +" "+ distances[first][third]);
					if (distances[second][third] > distances[second][first] + distances[first][third]){
						distances[second][third] = distances[second][first] + distances[first][third];
					}
				}	
			}
			//output(distances);
		}
		
		int result = 0;
		for(int row = 0; row < distances.length; row++){
			for(int column = 0; column < distances[row].length; column++){
				result =  Math.max(result,distances[row][column]);
			}	
		}
		//output(distances);
		return result;
		
	
		
	}

	private void output(int[][] distances){
		for(int row = 0; row < distances.length; row++){
			String line ="[ ";
			for(int column = 0; column < distances[row].length; column++){
				line+= distances[row][column]+", ";
			}
			line+=" ]";
			System.out.println(line);
		}		
	}
	
	private void output(int[] distances){
		String line ="[ ";
		for(int pos = 0; pos < distances.length; pos++){
			line+= distances[pos]+", ";
		}
		line+=" ]";
		System.out.println(line);		
	}
		
	public int effectiveDiameter(){
		int size = (int) Math.pow(distances.length, 2);
		
		int[] sort = new int[size];
		for (int row=0; row < distances.length; row++){
			for (int column=0; column < distances.length; column++){
				sort[row*distances.length + column]= distances[row][column];
			}	
		}
		
		//mergesort(sort);
		Arrays.sort(sort);
		//output(sort);
		int pos= (int) (size*0.9);
		return sort[pos];
	}
		
	/**
	 * Time consuming analysis is done here: Diameter computation
	 * Results are stored in the StringBuffer
	 * @param graph
	 */
	public void complex(){
		//System.out.printf("%5d | %5d | %.4f | %7d | %7d | %7.3f | %5d | %5d \n",
		//				   graph.getNodes(),
		//		           graph.getEdges(),
		//		           graph.getAlpha(),
		//		           graph.getMaxDeg(), 
		//		           graph.getMinDeg(), 
		//		           graph.getAvgDeg(), 
		//		           computeDiameter(graph),
		//		           effectiveDiameter(graph));
		long time = System.nanoTime();
		buffer.append(parent.getNodes()			+Constants.separator+
					  parent.getEdges()			+Constants.separator+
					  parent.getAlpha()			+Constants.separator+
				      parent.getMaxDepth() 		+Constants.separator+ 
				      parent.getAvgDepth() 		+Constants.separator+
					  parent.getMaxDeg()		+Constants.separator+
					  parent.getMinDeg()		+Constants.separator+
					  parent.getAvgDeg()		+Constants.separator+
					  computeDiameter()			+Constants.separator+
					  effectiveDiameter()		+Constants.separator+
					  parent.creationTime		+Constants.separator); //O(1)
		time = System.nanoTime() - time;
		buffer.append(time						+"\n"); //O(1)
	}
	
	/**
	 * complex analysis minus diameter analysis
	 * data is stored in a buffer
	 */
	public void fast(){
		//System.out.printf("%5d | %5d | %.4f \n",
		//				   graph.getNodes(),
		//		           graph.getEdges(),
		//		           graph.getAlpha()
		//		           );
		long time = System.nanoTime();
		float[] results = parent.linearFactors();
		buffer.append(((int) results[0])		+Constants.separator+  //O(1)  
					  ((int) results[1])		+Constants.separator+  //O(1)
					  results[2]				+Constants.separator+  //O(1)
					  ((int) results[3])		+Constants.separator+  //O(n)
					  results[4]				+Constants.separator+  //O(n)
					  ((int) results[5])		+Constants.separator+  //O(n)
					  ((int) results[6])		+Constants.separator+  //O(n)
					  results[7]				+Constants.separator+  //O(n)
					  parent.creationTime		+Constants.separator); //O(1)
		time = System.nanoTime() - time;
		buffer.append(time						+"\n"); //O(1)
		//return time;
	}
	
	/**
	 * Creates the header which precedes the the results of the analysis
	 * @param rec
	 */
	public void header(int rec){
		buffer.append("Start computing the " +rec+ "th graph now\n");
		buffer.append("\n");
		buffer.append("Nodes"+Constants.separator+
				      "Edges"+Constants.separator+
				      "Alpha"+Constants.separator+
				      "Max_Dep"+Constants.separator+
				      "Average_Dep"+Constants.separator+
				      "Max_Deg"+Constants.separator+
				      "Min_Deg"+Constants.separator+
				      "Avg_Deg"+Constants.separator+
				      "Graph_Time"+Constants.separator+
				      "Analysis_time"+Constants.separator+
				      "Diameter\n");
	}
	
	public void close(long runningTime){
		buffer.append("Done in "+runningTime+" ns\n");
		//buffer.append(parent.toString());
		//System.out.println(buffer);
	}
	
	public void degrees(Map<Integer,Integer> degrees){
		Iterator<Node> iter = parent.getNodeList();
		while (iter.hasNext()){
			Node current = iter.next();
			int deg = current.getDegree();
			Integer occ = degrees.get(deg);
			if (occ != null){
				degrees.put(deg, occ+1);
			}else{
				degrees.put(deg, 1);
			}
		}
		
	}
	public void depth(Map<Integer,Integer> depth){
		Iterator<Node> iter = parent.getNodeList();
		while (iter.hasNext()){
			Node current = iter.next();
			int deg = current.depth;
			Integer occ = depth.get(deg);
			if (occ != null){
				depth.put(deg, occ+1);
			}else{
				depth.put(deg, 1);
			}
		}
	}
}
