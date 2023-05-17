package GA;

import java.io.FileWriter;
import java.io.PrintWriter;


import domain.Instance;
import domain.ProductionSchedule;

public class Individual {
//	static int firstPeriodforItems[];
//	static int lastPeriodforItems[];
	static double pMut;
	private int[] genotype;
	private ProductionSchedule phaenotype;
	private double fitness;	
	
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

}
