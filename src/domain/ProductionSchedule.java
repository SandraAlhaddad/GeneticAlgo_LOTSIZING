package domain;


public class ProductionSchedule {

    public int genome[];       // restrictions,, exeptions
    public int production[][]; 	// lot-sizes
    public int onHold[][]; 		// demand
    public int demand[][]; 		// demand
    public double tcost[][];
    public double cost[]; 		// cost per Item
 
    
    /**
     * Create a new {@link ProductionSchedule}.
     * 
     * @param itemCount
     *            the number of items
     * @param periodCount
     *            the number of periods
     */
    
    public ProductionSchedule(int itemCount, int periodCount) {
        production = new int[itemCount][periodCount];
        onHold     = new int[itemCount][periodCount];
        demand     = new int[itemCount][periodCount];
        genome     = new int[itemCount];
        tcost      = new double[itemCount][periodCount];
        cost       = new double[itemCount];
    }

    public ProductionSchedule(int[] bits, Instance instance) {
    	int itemCount   = bits.length;
    	int periodCount = instance.getPeriodCount();
    	genome          = new int[itemCount];
    	production      = new int[itemCount][periodCount];
        onHold          = new int[itemCount][periodCount];
        demand          = new int[itemCount][periodCount];
        tcost           = new double[itemCount][periodCount];
        cost            = new double[itemCount];
        
        for(int i=0;i<bits.length;i++){
        		genome[i] = bits[i];
        }
        instance.decodeMatrix(this);
    }
    
   
    public Double getCostSum() {
        double ret = 0;
        for (int i = 0; i < cost.length; i++) {
            ret += cost[i];
        }
        return ret;
    }
    
    
    public Double getCostsForItem(int itemKey) {
        return cost[itemKey - 1];
    }

    public void setCostsForItem(int itemKey, double costs) {
        cost[itemKey - 1] = costs;
    }

    public void setDemandForPeriod(int itemKey, int period, int amount) {
        this.demand[itemKey - 1][period - 1] = amount;
    }

    public int getDemandForPeriod(int itemKey, int period) {
        return this.demand[itemKey - 1][period - 1];
    }

    public void setProduction(int itemKey, int period, int amount) {
        this.production[itemKey - 1][period - 1] = amount;
    }

    public void setOnHold(int itemKey, int period, int amount) {
        this.onHold[itemKey - 1][period - 1] = amount;
    }

    public int getProduction(int itemKey, int period) {
        return this.production[itemKey - 1][period - 1];
    }

    public int getOnHold(int itemKey, int period) {
        return this.onHold[itemKey - 1][period - 1];
    }

	public int getGenome(int itemKey) {
		return genome[itemKey - 1];
	}
	public void setGenome(int[] genome) {
		this.genome = genome;
	}

    
}
