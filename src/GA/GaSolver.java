package GA;

import domain.Instance;
import domain.ProductionSchedule;



public class GaSolver  {
	private final int maxNumberOfSolutions;

	/*
	 * here you can fix your GA parameter as populationSize, number of iterations
	 * 
	 */

	private final int popSize = 100;
	private final int maxIter;
	
	
	
	public GaSolver(int maxNumberOfSolutions) {
		this.maxNumberOfSolutions = maxNumberOfSolutions;
		this.maxIter = maxNumberOfSolutions/popSize;
		//System.out.println(maxNumberOfSolutions + " " + maxIter + " " + popSize);
	}

	public ProductionSchedule solve(Instance instance) {
		Individual.firstLastPeriods(instance);
//		Individual.mutationsProp();
		Individual bestIndi;
		
		/*
		 * Begin of your GA:
		 * Implement here your GA and 
		 * store your best Individual in bestIndi
		 */
		
		Individual[] pop = new Individual[popSize];
		Individual elter, child;

		for(int i=0;i<popSize;i++){
			pop[i] = new Individual(instance);
			pop[i].initRandom();
			pop[i].decoding(instance);
			pop[i].evaluate();
//			System.out.println("Individuum: " + i + " : " + pop[i].getFitness());
		}
		
		for(int g=0;g<maxIter;g++){
			int x1 = (int)(popSize*Math.random());
			int x2 = (int)(popSize*Math.random());
			int winner, looser;
			if(pop[x1].getFitness() < pop[x2].getFitness()){
				winner = x1;
				looser = x2;
			}
			else{
				winner = x2;
				looser = x1;
			}
			elter = pop[winner];
			child = new Individual(instance);
			child.reproduce(elter);
			child.mutate();
			child.decoding(instance);
			child.evaluate();
			
			//Replacement
			if(pop[looser].getFitness() > child.getFitness())
			pop[looser] = child;
			
		}
		
		//find the best solution in population as result
		int best = 0;
		double bestfit = pop[best].getFitness();
		for(int i=1;i<popSize;i++){
			if(pop[i].getFitness() < bestfit){
				best    = i;
				bestfit = pop[best].getFitness();
			}
		}
		bestIndi = pop[best];

		/*
		 * End of your GA 
		 */
		
		//bestIndi.ausgabe(instance);      //printing your best solution
		return bestIndi.getPhaenotype();   //return best decoded solution

	}
}
