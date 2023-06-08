package GA;

import domain.Instance;
import domain.ProductionSchedule;

import java.util.Random;


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
		Individual parent1, parent2, child1, child2;
		int parent1Index, parent2Index = 0;

		for(int i=0;i<popSize;i++){
			pop[i] = new Individual(instance);
			pop[i].initRandom();
			pop[i].decoding(instance);
			pop[i].evaluate();
//			System.out.println("Individuum: " + i + " : " + pop[i].getFitness());
		}
		
		for(int g=0;g<maxIter;g++){


			parent1Index = randomSelection(pop);
			parent1 = pop[parent1Index];
			do {
				parent2Index = randomSelection(pop);
				parent2 = pop[parent2Index];
			} while (parent2 == parent1);
			child1 = new Individual(instance);
			child2 = new Individual(instance);
			Individual[] children = Individual.crossover(parent1, parent2, child1, child2);

			children[0].mutateReverse();
			children[1].mutateReverse();

			children[0].decoding(instance);
			children[0].evaluate();

			children[1].decoding(instance);
			children[1].evaluate();


			if(children[0].getFitness() < parent1.getFitness())
				pop[parent1Index] = children[0];
			if(children[1].getFitness() < parent2.getFitness())
				pop[parent2Index] = children[1];
			
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

	private int tournamentSelection(Individual[] population) {
		int tournamentSize = 5;
		Random rand = new Random();
		Individual best = null;
		int bestIndiIndex = 0;
		for (int i = 0; i < tournamentSize; i++) {
			int randomIndex = rand.nextInt(popSize);
			Individual candidate = population[randomIndex];
			if (best == null || candidate.getFitness() < best.getFitness()) {
				best = candidate;
				bestIndiIndex = randomIndex;
			}
		}
		return bestIndiIndex;
	}

	private int randomSelection(Individual[] population) {
		Random rand = new Random();
		return rand.nextInt(0, population.length);
	}
}
