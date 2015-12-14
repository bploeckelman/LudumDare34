package lando.systems.ld34.resources;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectMap;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.screens.NavigationLayout;
import lando.systems.ld34.world.Area;


/**
 * Created by dsgraham on 12/12/15.
 * Stop showing a warning....
 */
public class ResourceManager {

    public enum Resources { BUILD, STONE, WOOD, FOOD, GOLD, SLAVES }

    private ObjectMap<Resources, ResourceInfo> resources;
    public int nextSlaveFoodAmount = 0;
    public int totalSlaves = 0;

    public ResourceManager(){
        resources = new ObjectMap<Resources, ResourceInfo>();
        resources.put(Resources.BUILD, new ResourceInfo(Resources.BUILD));
        resources.put(Resources.STONE, new ResourceInfo(Resources.STONE));
        resources.put(Resources.WOOD, new ResourceInfo(Resources.WOOD));
        resources.put(Resources.FOOD, new ResourceInfo(Resources.FOOD));
        resources.put(Resources.GOLD, new ResourceInfo(Resources.GOLD));
        resources.put(Resources.SLAVES, new ResourceInfo(Resources.SLAVES));
    }


    public void update(float dt) {
        nextSlaveFoodAmount = getFoodForNextSlave();
        totalSlaves = getTotalSlaves();
        for (ResourceInfo resource : resources.values()) {
            resource.update(dt);
        }

        // bind this tightly like doug's butt hole before he went to prison
        NavigationLayout.AreaButtons.get(Area.Type.MGMT).efficiencyLevel = 1f - (LudumDare34.GameScreen.currentAnger / 100f);
        NavigationLayout.AreaButtons.get(Area.Type.WOODS).efficiencyLevel = resources.get(Resources.WOOD).efficiency;
        NavigationLayout.AreaButtons.get(Area.Type.FIELD).efficiencyLevel = resources.get(Resources.FOOD).efficiency;
        NavigationLayout.AreaButtons.get(Area.Type.QUARRY).efficiencyLevel = resources.get(Resources.STONE).efficiency;
        NavigationLayout.AreaButtons.get(Area.Type.PYRAMID).efficiencyLevel = resources.get(Resources.BUILD).efficiency;
    }

    /**
     * Remove an amount of resources from a store
     * @param type resource to remove
     * @param amount amount to remove
     * @return whether it was able to remove the resources
     */
    public boolean removeResource(Resources type, float amount){
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

    public boolean canAddSlaves(Resources type, int amount){
        ResourceInfo info = resources.get(type);
        return info.maxSlaves - info.slaves >= amount;
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

    public int getFoodForNextSlave(){
        int totalSlaves = getTotalSlaves();
        return (totalSlaves * (totalSlaves +1 ))/2;
    }

    public float getWhipTargetRange(Resources type){
        return .05f + (.05f * resources.get(type).skilledWorkers);
    }

    public float getWhipFalloffRange(Resources type){
        return resources.get(type).getWhipFalloff();
    }

    public float getEfficiency(Resources type){
        return resources.get(type).efficiency;
    }

    public void addEfficiency(Resources type, float motivationAmount){
        resources.get(type).addEfficiency(motivationAmount);
    }

    public ResourceInfo getResourceInfo(Resources type) {
        return resources.get(type);
    }

    public void render(Resources type, SpriteBatch batch){
        resources.get(type).render(batch);
    }

    public boolean canUpgrade(Resources type){
        return resources.get(Resources.WOOD).amount > resources.get(type).woodToUpgade();
    }

    public boolean canTrade(Resources type) {
        return resources.get(type).amount >= resources.get(type).costToTrade();
    }

    public void upgradeResource(Resources type){
        resources.get(type).upgradeLevel();
    }

    public void tradeResource(Resources type) {
        if (!canTrade(type)) return;
        if (removeResource(type, resources.get(type).costToTrade())) {
            addAmount(Resources.GOLD, 1);
        }
    }

    public static Resources getRandomDisasterResource() {
        int rand = MathUtils.random(1, 5);
        switch (rand) {
            case 1: return Resources.BUILD;
            case 2: return Resources.STONE;
            case 3: return Resources.WOOD;
            case 4: return Resources.FOOD;
            case 5: default: return Resources.SLAVES;
        }
    }

}
