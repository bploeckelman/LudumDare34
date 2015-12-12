package lando.systems.ld34.resources;

import com.badlogic.gdx.utils.ObjectMap;


/**
 * Created by dsgraham on 12/12/15.
 */
public class ResourceManager {

    public enum Resources {STONE, WOOD, FOOD, GOLD, SLAVES}

    private ObjectMap<Resources, ResourceInfo> resources;

    public ResourceManager(){
        resources = new ObjectMap<Resources, ResourceInfo>();
        resources.put(Resources.STONE, new ResourceInfo(Resources.STONE));
        resources.put(Resources.WOOD, new ResourceInfo(Resources.WOOD));
        resources.put(Resources.FOOD, new ResourceInfo(Resources.FOOD));
        resources.put(Resources.GOLD, new ResourceInfo(Resources.GOLD));
        resources.put(Resources.SLAVES, new ResourceInfo(Resources.SLAVES));
        resources.get(Resources.GOLD).efficiency = 0;
        resources.get(Resources.SLAVES).efficiency = 0;
        resources.get(Resources.SLAVES).maxSlaves = 1000;
    }

    public void update(float dt) {
        for (ResourceInfo resource : resources.values()){
            resource.update(dt);
        }
    }

    /**
     * Remove an amount of resources from a store
     * @param type resource to remove
     * @param amount amount to remove
     * @return whether it was able to remove the resources
     */
    public boolean removeResource(Resources type, int amount){
        return resources.get(type).removeAmount(amount);
    }

    public float getAmount(Resources type){
        return resources.get(type).amount;
    }

    public int addAmount(Resources type, int amount){
        return resources.get(type).addAmount(amount);
    }

    public int getSlaveAmount(Resources type){
        return resources.get(type).slaves;
    }

    public int addSlaves(Resources type, int amount){
        return resources.get(type).addSlaves(amount);
    }

    public int removeSlaves(Resources type, int amount){
        return resources.get(type).removeSlaves(amount);
    }
}
