package GA;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;


import domain.Instance;
import domain.ProductionSchedule;

public class Individual {

	static int firstPeriodforItems[];
	static int lastPeriodforItems[];
	static double pMut;
	private int[] genotype;
	private ProductionSchedule phaenotype;
	private double fitness;
	private Instance instance;

	public int[] getGenotype() {
		return genotype;
	}
	
	public static void firstLastPeriods(Instance inst){
		ProductionSchedule ps = new ProductionSchedule(inst.getItemCount(), inst.getPeriodCount());
    	inst.decodeMatrix(ps);
    	
    	firstPeriodforItems = new int[inst.getItemCount()];
    	lastPeriodforItems  = new int[inst.getItemCount()];
    	
    	for(int i=0;i<inst.getItemCount();i++){
    		boolean first = false;
    		for(int p=0;p<inst.getPeriodCount();p++){
    			if(ps.demand[i][p] != 0){
    				if(!first){
    					first=true;
    					firstPeriodforItems[i] = p;
    				}
    				lastPeriodforItems[i] = p;
    			}
    		}
    	}		
	}
	
	public ProductionSchedule getPhaenotype(){
		return phaenotype;
	}
	
	Individual(Instance inst){
		genotype = new int[inst.getItemCount()];
		instance = inst;
	}
	
	public void initRandom() {
	for (int i = 0; i < genotype.length; i++) {
		genotype[i] = (int) (Math.random()*instance.getPeriodCount());
		}
	}
	
	public void decoding(Instance instance){
		phaenotype = new ProductionSchedule(genotype, instance);		
	}
	
	public void evaluate(){
		fitness = phaenotype.getCostSum();
	}
	
	public double getFitness(){
		return fitness;
	}
	
	public void reproduce(Individual elter){
		for(int i=0;i<elter.genotype.length;i++){
			this.genotype[i] = elter.genotype[i];
		}
	}
	
	public void ausgabe(Instance instance){
		try{			
			String ausgabeName = instance.getName();
			PrintWriter pu = new PrintWriter(new FileWriter(ausgabeName + ".sol"));
			pu.println(instance.getName());
			pu.println("Fitness (total costs): " + fitness);
			pu.println("Genotype: ");
			
			for(int i=0;i<genotype.length;i++){
				pu.print(genotype[i]);
				pu.println();
			}
			pu.close();	
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public static void mutationsProp(){
		int anzahlPerioden  = 0;
		for(int i=0;i<firstPeriodforItems.length;i++){
    		anzahlPerioden +=  lastPeriodforItems[i]-firstPeriodforItems[i]+1;
		}
		pMut = 1./anzahlPerioden;
		//pMut = 0.0005;
		//System.out.println("Mutationswahrscheinlichkeit : " + pMut);
	}


	public void mutateReverse() {
		Random rand = new Random();
		int startGen = rand.nextInt(genotype.length);
		int endGen = rand.nextInt(genotype.length);
		if (startGen > endGen) {
			int startGenTemp = startGen;
			startGen = endGen;
			endGen = startGenTemp;
		}

		while (startGen < endGen) {
			int temp = genotype[startGen];
			genotype[startGen] = genotype[endGen];
			genotype[endGen] = temp;
			startGen++;
			endGen--;
		}
	}




	public static Individual[] crossover(Individual parent1, Individual parent2, Individual child1, Individual child2) {
		int length = parent1.genotype.length;

		Random rand = new Random();
		int crossoverPoint = rand.nextInt(length);

		for (int i = 0; i < length; i++) {
			if (i < crossoverPoint) {
				child1.genotype[i] = parent1.genotype[i];
				child2.genotype[i] = parent2.genotype[i];
			} else {
				child1.genotype[i] = parent2.genotype[i];
				child2.genotype[i] = parent1.genotype[i];
			}
		}

		Individual[] children = new Individual[] { child1, child2 };
		return children;
	}

}
