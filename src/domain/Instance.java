package domain;


import java.util.HashMap;
import java.util.Map;

/**
 * Class for a single problem instance. Contains all information from a file.
 * 
 * @author andi, philipp (ehemalige Studierende der HFT Stuttgart)
 */

public class Instance {

    private int finalGoodCount;
    private int periodCount;
    private int itemCount;
    private String name;
    private Map<Integer, Item> items = new HashMap<Integer, Item>();
    

    public Instance(String name) {
        this.name = name;
    }

    public void addItem(Item item) {
        items.put(item.getKey(), item);
    }

    public Item getItem(int key) {
        return items.get(key);
    }

    public void determineLevel() {
        for (int i = 0; i < finalGoodCount; i++) {
            items.get(i + 1).determineItemLevel();
        }
    }

    private void calculateCosts(ProductionSchedule ps) {
        double costs = 0;
        for (int itemKey = 1; itemKey <= itemCount; itemKey++) {

            costs = 0;

            for (int period = 1; period <= periodCount; period++) {
                if (ps.getProduction(itemKey, period) > 0) {

                    costs = costs + items.get(itemKey).getProductionCosts();
                    ps.tcost[itemKey-1][period-1] = items.get(itemKey).getProductionCosts();

                }
                costs = costs + ((double) ps.getOnHold(itemKey, period) * items.get(itemKey).getHoldingCosts());
                ps.tcost[itemKey-1][period-1] += ((double) ps.getOnHold(itemKey, period) * items.get(itemKey).getHoldingCosts());
            }
            ps.setCostsForItem(itemKey, costs);
        }
    }
    
    
    /**
     * Decodes the genome for a {@link ProductionSchedule} into demand,
     * production and holding values.
     * 
     * @param ps
     *            the ProductionSchedule
     */
    
    public void decodeMatrix(ProductionSchedule ps) {
    	for(int i=0;i<ps.demand.length;i++){
    		for(int j=0;j<ps.demand[i].length;j++){
    			ps.demand[i][j]     = 0;
    			ps.production[i][j] = 0;
    			ps.onHold[i][j]     = 0;
    			ps.tcost[i][j]      = 0;
    		}
    		ps.cost[i] = 0;	
    	}
    	
        for (int itemKey = 1; itemKey <= itemCount; itemKey++) {
        	determineDemandForClass(ps, itemKey);
        	if (getProblemClass() == ProblemClass.THREE) {
                determineDemandForClassThree(ps, itemKey);
            }
//            else{
//            	// for each item, put the first bit = 1
//            	ps.setBitInGenome(itemKey, 1, 1);
//            }
            determineProduction(ps, itemKey);
        }
        calculateCosts(ps);
    }
    
    private void determineDemandForClassThree(ProductionSchedule ps, int itemKey) {
    	if (items.get(itemKey).getSuccessors().size() == 0) {
            for (int period = 1; period <= periodCount; period++) {
                ps.setDemandForPeriod(itemKey, period, items.get(itemKey).getDemandForPeriod(period));
            }
        } 
    	else {
            for (int period = 1; period < periodCount; period++) {
                ps.setDemandForPeriod(itemKey, period, ps.getDemandForPeriod(itemKey, period + 1));
            }
            ps.setDemandForPeriod(itemKey, periodCount, 0);
        }

//        for (int period = 1; period <= periodCount; period++) {
//            if (ps.getDemandForPeriod(itemKey, period) > 0) {
//            	ps.setBitInGenome(itemKey, period, 1);            	
//            	break;
//            }
//        }
    }

    private void determineDemandForClass(ProductionSchedule ps, int itemKey) {
        if (items.get(itemKey).getSuccessors().size() == 0) {
            for (int period = 1; period <= periodCount; period++) {
                ps.setDemandForPeriod(itemKey, period, items.get(itemKey).getDemandForPeriod(period));
            }
        } 
        else{
            for (Item successor : items.get(itemKey).getSuccessors()) {
                for (int period = 1; period <= periodCount; period++) {
                    ps.setDemandForPeriod(itemKey, period, ps.getDemandForPeriod(itemKey, period) + ps.getProduction(successor.getKey(), period));
                }
            }
        }
        
        
    }

    private void determineProduction(ProductionSchedule ps, int itemKey) {
        int onHold = 0;
        int succ = 0;
        
        for (int period = periodCount; period >= 1; period--) {
        	if(items.get(itemKey).getProductionCosts() < ((double) ps.getOnHold(itemKey, period) * items.get(itemKey).getHoldingCosts()) ) {
                ps.setProduction(itemKey, period, ps.getDemandForPeriod(itemKey, period) + onHold);
                ps.setOnHold(itemKey, period, onHold);
                onHold = 0;
                succ = 0;
            } else {
            	if(succ < ps.getGenome(itemKey)) {
	                ps.setProduction(itemKey, period, 0);
	                ps.setOnHold(itemKey, period, onHold);
	                onHold += ps.getDemandForPeriod(itemKey, period);
	                succ = succ + 1;
            	} else {
            		onHold = 0;
                    ps.setProduction(itemKey, period, ps.getDemandForPeriod(itemKey, period) + onHold);
                    ps.setOnHold(itemKey, period, onHold);
            	}
            }
        }
    }
    
//    private void determineProduction2(ProductionSchedule ps, int itemKey) {
//        int onHold = 0;
//        int succ = 0;
//        for (int period = periodCount; period >= 1; period--) {
//        	if(items.get(itemKey).getProductionCosts() < items.get(itemKey).getHoldingCosts() ) {
//                ps.setProduction(itemKey, period, ps.getDemandForPeriod(itemKey, period) + onHold);
//                ps.setOnHold(itemKey, period, onHold);
//                onHold = 0;
//                succ = 0;
//            } else {
//            	if(succ < ps.getGenome2(itemKey)) {
//	                ps.setProduction(itemKey, period, 0);
//	                ps.setOnHold(itemKey, period, onHold);
//	                onHold += ps.getDemandForPeriod(itemKey, period);
//	                succ = succ + 1;
//            	} else {
//                    ps.setProduction(itemKey, period, ps.getDemandForPeriod(itemKey, period) + onHold);
//                    ps.setOnHold(itemKey, period, onHold);
//                    onHold = 0;
//            	}
//            	
//            }
//        }
//    }

    public int getFinalGoodCount() {
        return finalGoodCount;
    }

    public int getPeriodCount() {
        return periodCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setFinalGoodCount(int finalGoodCount) {
        this.finalGoodCount = finalGoodCount;
    }

    public void setPeriodCount(int periodCount) {
        this.periodCount = periodCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getName() {
        return this.name;
    }

    public ProblemClass getProblemClass() {
        return ProblemClass.fromItemCount(itemCount);
    }


}