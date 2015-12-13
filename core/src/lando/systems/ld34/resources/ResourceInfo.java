package lando.systems.ld34.resources;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld34.Config;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.uielements.ProgressBar;
import lando.systems.ld34.utils.Assets;
import lando.systems.ld34.utils.Utils;

/**
 * Created by dsgraham on 12/12/15.
 */
public class ResourceInfo {

    private static float effDecay = .01f;
    private static float stoneToBlocks = .1f;

    public float amount;
    public float efficiency;
    public int maxAmount;
    public int lastMaxAmount;
    public float maxEfficiency;
    public float minEfficiency;
    public int slaves;
    public int maxSlaves;
    public int level;
    public int skilledWorkers;
    public ResourceManager.Resources type;


    // Center Top of panel
    Vector2 screenPos = new Vector2(Config.width/2, Config.height - 50);
    private String typeLabel = "";
    private ProgressBar amountPB;
    private ProgressBar effPB;
    private ProgressBar slavePB;

    public ResourceInfo(ResourceManager.Resources type){
        amountPB = new ProgressBar();
        amountPB.bounds = new Rectangle(screenPos.x, screenPos.y, 120, 20);
        effPB = new ProgressBar();
        effPB.bounds = new Rectangle(screenPos.x, screenPos.y - 30, 120, 20);
        slavePB = new ProgressBar();
        slavePB.bounds = new Rectangle(screenPos.x, screenPos.y - 60, 120, 20);
        amount = 10;
        slaves = 1;
        maxAmount = 100;
        maxEfficiency = 1;
        minEfficiency = 0;
        maxSlaves = 5;
        efficiency = 1;
        skilledWorkers = 0;
        level = 1;
        this.type = type;
        switch (type){
            case STONE:
                typeLabel = "Stones";
                break;
            case WOOD:
                typeLabel = "Wood";
                break;
            case FOOD:
                typeLabel = "Food";
                break;
            case BUILD:
                typeLabel = "Blocks";
                amount = 1;
                maxAmount = 1;
                lastMaxAmount = 0;
                break;
            case GOLD:
                slaves = 0;
                amount = 0;
                efficiency = 0;
                break;
            case SLAVES:
                maxSlaves = 10000000;
                slaves = 5;
                break;
        }
    }

    public void update(float dt){
        efficiency -= effDecay*dt;
        if (efficiency < 0) efficiency = 0;

        switch (type){
            case SLAVES:
                int nextSlave = LudumDare34.GameScreen.ResourceManager.nextSlaveFoodAmount;
                if (LudumDare34.GameScreen.ResourceManager.removeResource(ResourceManager.Resources.FOOD, nextSlave)){
                    slaves++;
                }
                break;
            case BUILD:
                float stones = (slaves * efficiency * dt);
                if (LudumDare34.GameScreen.ResourceManager.removeResource(ResourceManager.Resources.STONE, stones)) {
                    amount += stones * stoneToBlocks;
                }
                if (amount > maxAmount) amount = maxAmount;
                if (amount == maxAmount){
                    //TODO: Add Pun to screen here
                    int height = LudumDare34.GameScreen.ResourceManager.getPyramidHeight();
                    lastMaxAmount = (height * (height+1))/2;
                    height++;
                    maxAmount = (height * (height+1))/2;
                }
                break;
            default:
                maxAmount = 50 * (level * level);
                amount += (slaves * efficiency * dt);
                if (amount > maxAmount) amount = maxAmount;
        }

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

    public int woodToUpgade(){
        return (level * (level+1))/2*10;
    }

    public boolean upgradeLevel(){
        int wood = woodToUpgade();
        if (LudumDare34.GameScreen.ResourceManager.removeResource(ResourceManager.Resources.WOOD, wood)){
            level++;
            maxSlaves = level * 5;
            return true;
        }
        return false;
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

    public boolean removeAmount(float amount){
        if (amount < 0) return false;
        if (amount > this.amount) return false;
        this.amount -= amount;
        return true;
    }

    public void addEfficiency(float amount){
        efficiency += amount * .3f;
        if (efficiency >= maxEfficiency){
            efficiency = maxEfficiency;
        }
    }

    public float getWhipFalloff(){
        return Utils.clamp((.8f - ((efficiency - minEfficiency) / (maxEfficiency - minEfficiency))) * .2f, 0, 1);
    }


    public void render(SpriteBatch batch){
        GlyphLayout layout = new GlyphLayout(Assets.font, typeLabel);
        Assets.font.draw(batch, typeLabel, amountPB.bounds.x - (layout.width + 1), amountPB.bounds.y + (amountPB.bounds.height / 2) + layout.height / 2);
        amountPB.fillPercent.setValue((amount-lastMaxAmount) / (maxAmount-lastMaxAmount));
        amountPB.render(batch);
        String amountText = (int)amount+"/"+maxAmount;
        layout.setText(Assets.font, amountText);
        Assets.font.draw(batch, amountText, amountPB.bounds.x + (amountPB.bounds.width / 2) - layout.width / 2, amountPB.bounds.y + (amountPB.bounds.height / 2) + layout.height / 2);

            layout.setText(Assets.font, "Efficiency");
        Assets.font.draw(batch, "Efficiency", effPB.bounds.x - (layout.width + 1), effPB.bounds.y + (effPB.bounds.height / 2) + layout.height / 2);
        effPB.fillPercent.setValue(efficiency);
        effPB.render(batch);
        amountText = (int)(efficiency*100)+"%";
        layout.setText(Assets.font, amountText);
        Assets.font.draw(batch, amountText, effPB.bounds.x + (effPB.bounds.width / 2) - layout.width / 2, effPB.bounds.y + (effPB.bounds.height / 2) + layout.height / 2);

        layout.setText(Assets.font, "Slaves");
        Assets.font.draw(batch, "Slaves", slavePB.bounds.x - (layout.width + 1), slavePB.bounds.y + (slavePB.bounds.height / 2) + layout.height / 2);
        slavePB.fillPercent.setValue((float)slaves/maxSlaves);
        slavePB.render(batch);
        amountText = (slaves)+"/"+maxSlaves;
        layout.setText(Assets.font, amountText);
        Assets.font.draw(batch, amountText, slavePB.bounds.x + (slavePB.bounds.width / 2) - layout.width / 2, slavePB.bounds.y + (slavePB.bounds.height / 2) + layout.height / 2);


    }

}
