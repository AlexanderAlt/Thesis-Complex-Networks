import Forest.Fire.Model.Constants;
import Forest.Fire.Model.Generator;


public class Launcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Geometric probability
		/*
		Constants.propability = 0.25;
		Generator.run(false,false,true);
		Constants.propability = 0.3;
		Generator.run(false,false,true);
		Constants.propability = 0.35;
		Generator.run(false,false,true);
		Constants.propability = 0.4;
		Generator.run(false,false,true);
		Constants.propability = 0.45;
		Generator.run(false,false,true);
		*/
		//Constants.propability = 0.5;
		//Generator.run(false,false,true);
				
		//Constant fraction
		
		Constants.propability = 0.25;
		for(int i= 0; i<=Constants.graphs; i++){
			Generator.run(false,false,true);
			Constants.propability += 0.05;
		}
		
	//TODO: Create the following tables
	//Nodes/Edges for different graph sizes
	//Average//99th percentile of recursion depth
	//Effective/Maximum diameter
	//Degree distribution
	}

}
