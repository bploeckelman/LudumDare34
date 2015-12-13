package lando.systems.ld34.resources;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectMap;


/**
 * Created by dsgraham on 12/12/15.
 * Stop showing a warning....
 */
public class ResourceManager {

    public enum Resources {BUILD, STONE, WOOD, FOOD, GOLD, SLAVES}

    private ObjectMap<Resources, ResourceInfo> resources;

    public ResourceManager(){
        resources = new ObjectMap<Resources, ResourceInfo>();
        resources.put(Resources.BUILD, new ResourceInfo(Resources.BUILD));
        resources.put(Resources.STONE, new ResourceInfo(Resources.STONE));
        resources.put(Resources.WOOD, new ResourceInfo(Resources.WOOD));
        resources.put(Resources.FOOD, new ResourceInfo(Resources.FOOD));
        resources.put(Resources.GOLD, new ResourceInfo(Resources.GOLD));
        resources.put(Resources.SLAVES, new ResourceInfo(Resources.SLAVES));
        resources.get(Resources.GOLD).efficiency = 0;
        resources.get(Resources.GOLD).slaves = 0;
        resources.get(Resources.SLAVES).efficiency = 0;
        resources.get(Resources.SLAVES).maxSlaves = 1000;
        resources.get(Resources.SLAVES).slaves = 10;
    }

    private float _greatTemporaryFieldName;

    public void update(float dt) {
        for (ResourceInfo resource : resources.values()) {
            resource.update(dt);
        }

        // add a block every .5 seconds
        _greatTemporaryFieldName += dt;
        if (_greatTemporaryFieldName > .5) {
            _greatTemporaryFieldName -= .5;
            addAmount(Resources.BUILD, 1);
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

    public int getTotalSlaves(){
        int totalSlaves = 0;
        for (ResourceInfo r : resources.values()){
            totalSlaves += r.slaves;
        }
        return totalSlaves;
    }

    public int getPyramidHeight(){
        return (int) ((Math.sqrt((8.0 * resources.get(Resources.BUILD).amount) + 1) - 1)  / 2.0);
    }

    public float getWhipTargetRange(Resources type){
        return .05f;
    }

    public float getWhipFalloffRange(Resources type){
        return .1f;
    }

    public float getEfficiency(Resources type){
        return resources.get(type).efficiency;
    }

    public void addEfficiency(Resources type, float motivationAmount){
        resources.get(type).addEfficiency(motivationAmount);
    }

    public void render(Resources type, SpriteBatch batch){
        resources.get(type).render(batch);
    }
}
