package lando.systems.ld34.resources;

/**
 * Created by dsgraham on 12/12/15.
 */
public class ResourceInfo {

    public float amount;
    public float efficiency;
    public int maxAmount;
    public float maxEfficiency;
    public int slaves;
    public int maxSlaves;
    public int level;
    public ResourceManager.Resources type;


    public ResourceInfo(ResourceManager.Resources type){
        amount = 10;
        slaves = 1;
        maxAmount = 100;
        maxEfficiency = 1;
        maxSlaves = 10;
        efficiency = 1;
        this.type = type;
    }

    public void update(float dt){
        efficiency -= .01*dt;
        if (efficiency < 0) efficiency = 0;
        amount += (slaves * efficiency * dt);
        if (amount > maxAmount) amount = maxAmount;
    }

    /**
     * This will try to add slaves to this resource.  Check return value to see what was added.
     *
     * @param amount slaves to try to add
     * @return the number of slaves that were actually added
     */
    public int addSlaves(int amount){
        if (amount < 0) return 0;
        if (slaves + amount > maxSlaves){
            amount = maxSlaves - slaves;
        }
        slaves += amount;
        return amount;
    }

    public int removeSlaves(int amount){
        if (amount <= 0) return 0;
        if (amount > slaves) amount = slaves;
        slaves -= amount;
        return amount;
    }

    public int addAmount(int amount) {
        if (amount < 0) return 0;
        if (this.amount + amount > maxAmount){
            amount = (int)(maxAmount - this.amount);
        }
        this.amount += amount;
        return amount;
    }

    public boolean removeAmount(int amount){
        if (amount < 0) return false;
        if (amount > this.amount) return false;
        this.amount -= amount;
        return true;
    }

    public void addEfficiency(float amount){
        efficiency += amount;
        if (efficiency >= maxEfficiency){
            efficiency = maxEfficiency;
        }
    }

}
