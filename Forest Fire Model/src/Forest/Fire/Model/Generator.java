package Forest.Fire.Model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;


public class Generator {

	public static Graph graph;
	public static Random random;
	public static long runningTime;
	public static int rec;
	

	/**
	 * This function generates a undirected graph with (nodePerAnalysis*numberOfAnalysis) nodes.
	 * 
	 * Whenever a new node is entered into the graph this node chooses an ambassador u.a.r. and forms an edge to this ambassador.
	 * After (nodePerAnalysis) iterations the graph is analysed: Number of nodes/edges. (Max,Min,Avg) Degree, Diameter of the Tree.
	 * 
	 * This function follows the first two properties of the Forest Fire Model (no recursion).
	 * 
	 * @param fullAnalysis
	 * @param numberOfAnalysis
	 */
	public static void FFM(int recursions, boolean fullanalysis, boolean geometric){
		int newnodes;
		if(fullanalysis) newnodes = Constants.fullAnalysis;
		else newnodes = Constants.shortAnalysis;
		for(int k=0; k < Constants.numberOfAnalysis; k++){
			long starttime = System.nanoTime();
			
			for(int i=0; i<newnodes; i++){
				int nodeID = k*newnodes+i;
				//System.out.println("adds Node "+nodeID);
				Node node = graph.addnode(nodeID);
				if(nodeID != 0){
					//Every new node has an ambassador, who introduces him to the network
					int ambassadorID = random.nextInt(nodeID);	
					Node ambassador = graph.getNode(ambassadorID);
					graph.addedge(nodeID, ambassadorID);
					//System.out.println("Edge: "+nodeID+" - "+ambassadorID);
					
					ArrayList<Integer> burned = new ArrayList<Integer>();
					ArrayList<Integer> queue = new ArrayList<Integer>();
					ArrayList<Integer> nextQueue = new ArrayList<Integer>();
					
					burned.add(node.ID);
					burned.add(ambassador.ID);
					queue.add(ambassador.ID);
					int depth = 0;
					
					int randomcalls = 0;
					for (int recursion = 1; recursion <= recursions; recursion++){
						
						if(queue.isEmpty()) break;
						while(!queue.isEmpty()){
							depth = Math.max(depth,recursion);
							int currentID = queue.remove(0);
							Node current = graph.getNode(currentID);
							ArrayList<Integer> neighbors = new ArrayList<Integer>(current.outgoingNeighbors);
							
							neighbors.removeAll(burned);
							
							if (neighbors.isEmpty())
								continue;
							int rand;
							randomcalls++;
							if (geometric) rand = generateGeometricRandom(Constants.propability);
							else {
								rand = (int) Math.round((Constants.propability* (double) neighbors.size()));
								if(random.nextDouble() <= 0.5)
									rand++;
							}
							int copy = Math.min(neighbors.size(), rand);
							//Iterator<Node> iter = ambassador.getOutgoingNeighbors();
					
							if(copy == 0) continue;
							//System.out.println("     "+copy + " " + neighbors.size());
							for(int draws = 0; draws < copy; draws++){
								int pos = random.nextInt(neighbors.size());
								int newneighbor = neighbors.remove(pos);
								if (nodeID != newneighbor);
									graph.addedge(nodeID, newneighbor);
								//System.out.println("   Edge: "+nodeID+" - "+newneighbor + " at Depth "+ depth);
								burned.add(newneighbor);
								nextQueue.add(newneighbor);
							}
						}
						queue = new ArrayList<Integer>(nextQueue);
						nextQueue.clear();// = new ArrayList<Integer>();
						//System.out.println(queue.size()+" "+nextQueue.size());
					}
					
					node.depth = depth;
					//System.out.println("Node "+ node.ID+" added: Edges "+ node.getDegree() +" Tries "+randomcalls);
				}
				
			}
			runningTime += System.nanoTime() - starttime;
			graph.creationTime = runningTime;
			//System.out.println(graph.creationTime);
			if (fullanalysis) graph.analysis.complex();
			else			  graph.analysis.fast();
			
			System.out.println("Graph "+rec+" now contains "+graph.getNodes()+" Nodes");
		}
	}
	
	/**
	 * This function generates a undirected graph with (nodePerAnalysis*numberOfAnalysis) nodes.
	 * 
	 * Whenever a new node is entered into the graph this node chooses an ambassador u.a.r. and forms an edge to this ambassador.
	 * After (nodePerAnalysis) iterations the graph is analysed: Number of nodes/edges. (Max,Min,Avg) Degree, Diameter of the Tree.
	 * 
	 * This function follows the first two properties of the Forest Fire Model (no recursion).
	 * 
	 * @param fullAnalysis
	 * @param numberOfAnalysis
	 */
	public static void restartingFFM(int recursions, boolean fullanalysis, boolean geometric){
		int targetsize;
		if(fullanalysis) targetsize = Constants.fullAnalysis;
		else targetsize = Constants.shortAnalysis;
		for(int k=0; k < Constants.numberOfAnalysis; k++){
			graph.reset();
			long starttime = System.nanoTime();
			for(int i=0; i<targetsize; i++){
				
				int nodeID = i;
				Node node = graph.addnode(nodeID);
				if(nodeID != 0){
					//Every new node has an ambassador, who introduces him to the network
					int ambassadorID = random.nextInt(nodeID);	
					Node ambassador = graph.getNode(ambassadorID);
					graph.addedge(nodeID, ambassadorID);
					//System.out.println("Edge: "+nodeID+" - "+ambassadorID);
					
					ArrayList<Integer> burned = new ArrayList<Integer>();
					ArrayList<Integer> queue = new ArrayList<Integer>();
					ArrayList<Integer> nextQueue = new ArrayList<Integer>();
					
					burned.add(node.ID);
					burned.add(ambassador.ID);
					queue.add(ambassador.ID);
					int depth = 0;
					for (int recursion = 1; recursion <= recursions; recursion++){
						
						if(queue.isEmpty()) break;
						while(!queue.isEmpty()){
							depth = Math.max(depth,recursion);
							int currentID = queue.remove(0);
							Node current = graph.getNode(currentID);
							ArrayList<Integer> neighbors = new ArrayList<Integer>(current.outgoingNeighbors);
							neighbors.removeAll(burned);

							int rand; 
							if (geometric) rand = generateGeometricRandom(Constants.propability);
							else rand = (int) Math.round((Constants.propability* (double) neighbors.size()));
							int copy = Math.min(neighbors.size(), rand);
							//Iterator<Node> iter = ambassador.getOutgoingNeighbors();
					
							//System.out.println(copy);
							for(int draws = 0; draws < copy; draws++){
								int pos = random.nextInt(neighbors.size());
								int newneighbor = neighbors.remove(pos);
								graph.addedge(nodeID, newneighbor);
								//System.out.println("Edge: "+nodeID+" - "+newneighbor);
								burned.add(newneighbor);
								nextQueue.add(newneighbor);
							}
						}
						queue = new ArrayList<Integer>(nextQueue);
						nextQueue.clear();// = new ArrayList<Integer>();
						//System.out.println(queue.size()+" "+nextQueue.size());
					}
					node.depth = depth;
				}
			}
			runningTime += System.nanoTime() - starttime;
			graph.creationTime = runningTime;
			
			if (fullanalysis) graph.analysis.complex();
			else			  graph.analysis.fast();
			System.out.println("Graph "+rec+" now contains "+graph.getNodes()+" Nodes");
			if(fullanalysis) targetsize += Constants.fullAnalysis;
			else targetsize += Constants.shortAnalysis;
		}
	}
	
	/**
	 * TODO:
	 * Many Values are currently hardwired (let them be decided by the user (only over default values)
	 * Currently analysis is run every 50 steps. Tweak the analysis such that simpler analysis (i.e comparing of nodes and edges) is run more frequently than distance computation.
	 * 
	 * Give output not only as list, but also as graph (where the user can choose logarithmic scale).
	 * 
	 * Presently only a single graph is analysed with the ForestFireModel.
	 */
	public static void run(boolean inc, boolean fullanalysis, boolean geometric){
		random = new Random();
		Map<Integer,Integer> degrees = new HashMap<Integer,Integer>();
		Map<Integer,Integer> depth = new HashMap<Integer,Integer>();
		//int rec = Constants.numberOfRecursions;

		for(rec =1; rec<=Constants.maximumRecursionDepth; rec++){
			StringBuffer buffer = new StringBuffer();
			System.out.println("Start computing the " +rec+ "th graph now");
			
			graph = new Graph();
			graph.analysis.header(rec);
			runningTime=0;
		
			//generateTree();
			//undirectedSimpleFFM();
			//undirectedFFM();
			if (inc) FFM(rec,fullanalysis,geometric);
			else	 FFM(Integer.MAX_VALUE,fullanalysis,geometric);
			//if (inc) recursiveRestartingFFM(rec,fullanalysis,geometric);
			//else	 recursiveRestartingFFM(Integer.MAX_VALUE,fullanalysis,geometric);
			
			graph.analysis.close(runningTime);
			System.out.println("Done in "+runningTime+" ns\n");

			buffer.append(graph.analysis.buffer);
			
			graph.analysis.degrees(degrees);
			graph.analysis.depth(depth);
			//System.out.println(graph);
			//Question: Does the number of edges (edge densification) depend on the number of recursions?
			try{ 
				String dist;
				if (geometric) dist ="geom";
				else           dist ="cons";
				writeToFile("Data/"+(Constants.maximumRecursionDepth)+"graphs"+dist+Constants.propability+".csv",buffer);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		try{ 
			String dist;
			if (geometric) dist ="geom";
			else           dist ="cons";
			writeToFile("Data/"+(Constants.maximumRecursionDepth)+"graphs"+dist+Constants.propability+".csv",degrees);
			writeToFile2("Data/"+(Constants.maximumRecursionDepth)+"graphs"+dist+Constants.propability+".csv",depth);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void writeToFile(String filename, StringBuffer buffer) throws Exception{
		System.out.println("Start saving the graph now");
		BufferedWriter out = new BufferedWriter(new FileWriter(filename,true));
		out.append(buffer.toString());
		out.flush();
		out.close();
	}

	private static void writeToFile(String filename, Map<Integer,Integer> map) throws Exception{
		System.out.println("Start saving the degree distribution now");
		BufferedWriter out = new BufferedWriter(new FileWriter(filename,true));
		
		Iterator<Entry<Integer,Integer>> entries = map.entrySet().iterator();
		while(entries.hasNext()){
			Entry<Integer,Integer> entry = entries.next();
			String line = "Degrees:"+Constants.separator;
			line += entry.getKey()  +Constants.separator;
			line += "Frequency:"    +Constants.separator;
			line += entry.getValue() +"\n";
			out.append(line);
			//System.out.print(line);
		}
		
		out.flush();
		out.close();
	}
	
	private static void writeToFile2(String filename, Map<Integer,Integer> map) throws Exception{
		System.out.println("Start saving the degree distribution now");
		BufferedWriter out = new BufferedWriter(new FileWriter(filename,true));
		
		Iterator<Entry<Integer,Integer>> entries = map.entrySet().iterator();
		while(entries.hasNext()){
			Entry<Integer,Integer> entry = entries.next();
			String line = "Depth:"   +Constants.separator;
			line += entry.getKey()   +Constants.separator;
			line += "Frequency:"     +Constants.separator;
			line += entry.getValue() +"\n";
			out.append(line);
			//System.out.print(line);
		}
		
		out.flush();
		out.close();
	}
	
	/**
	 * A geometrical distributed random number generator based on java.util.random
	 * @param prop The burning probability
	 * @return the geometrically distributed random variable
	 */
	private static int generateGeometricRandom(double prop){
		prop = 1-prop;
		double uniform = random.nextDouble();
        double log = Math.log(1.0-prop);
        int result = (int) Math.ceil(Math.log(uniform)/log)-1;
        
        return result;
	}
}
