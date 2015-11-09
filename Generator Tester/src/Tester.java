import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


public class Tester {

	final static String separator = ";";
	
	static Random random;
	static int nodes_sum = 0;
	static int depth_sum = 0;
	
	private static void single_run(double prob){ //,Map depth, Map nodes){
		int current_nodes = 1;
		int current_depth = 1;
		nodes_sum +=1;
		depth_sum +=1;
		int next_nodes = 0;
		while(current_nodes != 0){
			//if(current_nodes != 0){
			//	Integer counter = (Integer) nodes.get(current_depth);
			//	if (counter== null){
			//		nodes.put(current_depth,current_nodes);
			//	}else{
			//		nodes.put(current_depth,counter+current_nodes);
			//	}	
			//}
			while (current_nodes !=0 ){
				current_nodes --;
				int newnodes = generateGeometricRandom(prob);
				next_nodes += newnodes;
				nodes_sum += newnodes;
				
			}
			if (next_nodes >0){
				current_nodes = next_nodes;
				next_nodes = 0;
				depth_sum ++;
				current_depth ++;
			}
		}
		//Integer counter = (Integer) depth.get(current_depth);
		//if (counter== null){
		//	depth.put(current_depth,1);
		//}else{
		//	depth.put(current_depth,counter+1);
		//}
	}
	
	private static void run (int no_tests, double prob){
		
		nodes_sum = 0;
		depth_sum = 0;
		//Map<Integer,Integer> depth = new HashMap<Integer,Integer>();
		//Map<Integer,Integer> nodes_depth = new HashMap<Integer,Integer>();
		for(int i=0; i< no_tests;i++){
			single_run(prob); //, depth, nodes_depth);
		}
		
		try{
			System.out.println("p = "+ prob +" complete");
			String filename = "generatortest.csv";
			BufferedWriter out = new BufferedWriter(new FileWriter(filename,true));
		
			out.append(prob + separator + nodes_sum/((float) no_tests)+ separator + depth_sum/((float) no_tests) +"\n");
			//Iterator<Entry<Integer,Integer>> entries = depth.entrySet().iterator();
			//while(entries.hasNext()){
			//	Entry<Integer,Integer> entry = entries.next();
			//	String line = entry.toString() +"\t";
			//	out.append(line);
			//	//System.out.print(line);
			//}
			
			//out.append("\t");
			//entries = nodes_depth.entrySet().iterator();
			//while(entries.hasNext()){
			//	Entry<Integer,Integer> entry = entries.next();
			//	String line = entry.toString() +"\t";
			//	out.append(line);
			//	//System.out.print(line);
			//}
			//out.append("\n");
			
			out.flush();
			out.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}

		
		//System.out.println(prob +"\t"+ nodes_sum/((float) no_tests)+ "\t"+ depth_sum/((float) no_tests) +"\t"+ depth);
		
		//System.out.println(no_tests+" nodes were added with probability p="+prob);
		//System.out.println("Average number of Nodes: "+ nodes_sum/((float) no_tests));
		//System.out.println("Average recursion depth: "+ depth_sum/((float) no_tests) );		
	}
	
	public static void main(String[] args) {
		random = new Random();
		int no_tests= 1000000;
		for (double i= 0.4; i<0.5;i += 0.001){
			
			run(no_tests,i);
		}
	}

	private static int generateGeometricRandom(double prop){
		prop = 1-prop;
		double uniform = random.nextDouble();
        double log = Math.log(1.0-prop);
        int result = (int) Math.ceil(Math.log(uniform)/log)-1;
        
        return result;
	}
}
