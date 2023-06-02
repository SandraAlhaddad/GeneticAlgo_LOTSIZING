package GA;

import java.io.FileWriter;
import java.io.PrintWriter;


import domain.Instance;
import domain.ProductionSchedule;

public class Individual {

	static int firstPeriodforItems[];
	static int lastPeriodforItems[];
	static double pMut;
	private int[] genotype;
	private ProductionSchedule phaenotype;
	private double fitness;

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
	}
	
	public void initRandom() {
	for (int i = 0; i < genotype.length; i++) {
		genotype[i] = (int) (Math.random()*genotype.length);
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
	
	public void mutate(){
		for(int i=0;i<genotype.length;i++){
				if(Math.random() < 0.02){
					if(genotype[i] == 1)
						genotype[i] = genotype[i]+1;
					else                   
						genotype[i] = genotype[i]+1;
				}
			}
		}
}
