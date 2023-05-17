package domain;

import java.util.Collection;
import java.util.Vector;

/**
 * Class representing one node of the Gozintograph.

 */
public class Item {

    private Vector<Item> predecessors;
    private Vector<Item> successors;
    private int demand[];
    private int key;
    private int level;
    private double holdingCosts;
    private double productionCosts;
    
    public Item(int key, int periods) {
        this.key = key;
        this.demand = new int[periods];
        this.predecessors = new Vector<Item>();
        this.successors = new Vector<Item>();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Integer getKey() {
        return key;
    }

    public double getHoldingCosts() {
        return holdingCosts;
    }

    public double getProductionCosts() {
        return productionCosts;
    }

    public void setHoldingCosts(double holdingCosts) {
        this.holdingCosts = holdingCosts;
    }

    public void setProductionCosts(double productionCosts) {
        this.productionCosts = productionCosts;
    }

    /**
     * Set the demand for a period.
     * @param demand the demand for this period
     * @param period the period, first one is 1
     */
    public void setDemandForPeriod(int demand, int period) {
        this.demand[period - 1] = demand;
    }

    /**
     * Get the demand for a period.
     * @param period the period, first one is 1
     * @return the demand for this period
     */
    public int getDemandForPeriod(int period) {
        return demand[period - 1];
    }

    public void addSuccessor(Item successor) {
        successors.add(successor);
    }

    public void addPredecessor(Item predecessor) {
        predecessors.add(predecessor);
    }

    public Collection<Item> getPredecessors() {
        return predecessors;
    }

    public Collection<Item> getSuccessors() {
        return successors;
    }

    
    public void determineItemLevel() {
        int lvl = 0;
        for (Item successor : successors) {
            if (successor.getLevel() >= lvl) {
                lvl = successor.getLevel() + 1;
            }
        }
        this.level = lvl;

        for (Item pre : predecessors) {
            pre.determineItemLevel();
        }
    }
    
    public boolean isFinalGood(){
        return getSuccessors().size() == 0;
    }
}
