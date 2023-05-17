package evaluation;

import java.io.File;


import domain.Instance;
import domain.ProductionSchedule;
import io.DirectoryWalker;
import io.InstanceReader;
import GA.GaSolver;
import io.ReadBestResults;

public class Start {

	public static void main(String[] args) {
		new Start().exec();
	}

	

	public void exec() {
	
		
		int numberOptimal      = 0;
		int numberInstanzen    = 0;
		double gapOptimal      = 0.;
		double avgGapOptimal   = 0.;
		double optimalValue    = 0.;
		double precision       = 0.001;
		
		String insPath         = "instances";
		String solFile         = "solutions.txt";
		DirectoryWalker walker = new DirectoryWalker(new File(insPath), false);

		for (File insFile : walker) {
			InstanceReader inReader = new InstanceReader(insFile);
			Instance i              = inReader.parse();
			optimalValue            = ReadBestResults.getBestSolution(new File(solFile), i.getName());

			
			
			
			
			/* CHOOSE the following number of Fitness-Evaluations for different classes of instances
			 * class1: numberOfFitnessEval =  50000
			 * class2: numberOfFitnessEval = 200000
			 * class3: numberOfFitnessEval = 400000
			 */
			
			int numberOfFitnessEval = 2000;			
			GaSolver solver         = new GaSolver(numberOfFitnessEval);
			
			
			long start = System.currentTimeMillis();
			ProductionSchedule solution = solver.solve(i);
			long time = System.currentTimeMillis() - start;

			if(optimalValue > 0.){
				gapOptimal = 100*((solution.getCostSum()-optimalValue)/optimalValue);
				if (Math.abs(gapOptimal) < precision)numberOptimal++;
				avgGapOptimal += gapOptimal;
				numberInstanzen++;
				System.out.print("Instance " + i.getName() + " solved in " + time + " ms");
				System.out.print(" ZF = " + solution.getCostSum() + " OPT = " + optimalValue);
				System.out.print(" Gap from Best known or Optimal solution: " + gapOptimal + " %");
				System.out.println();
			}
			else{
				System.out.print("No Literature Value for: " + i.getName());
				System.out.print(" solved in " + time + " ms");
				System.out.print(" ZF = " + solution.getCostSum());
			}
		}
		System.out.println();
		if (numberInstanzen != 0) {
			System.out.println("----> Number of solved Instances: " + numberInstanzen);
			System.out.println("----> Number of instances with best known or optimal solution values: "	+ numberOptimal + "\t (Precision: " + precision + ")");
			System.out.println("----> Avg % GAP: " + (avgGapOptimal / numberInstanzen));
		}
	}
}
