package lando.systems.ld34.resources;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld34.Config;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.screens.GameScreen;
import lando.systems.ld34.uielements.ProgressBar;
import lando.systems.ld34.utils.Assets;
import lando.systems.ld34.utils.SoundManager;
import lando.systems.ld34.utils.Utils;

/**
 * Created by dsgraham on 12/12/15.
 */
public class ResourceInfo {

    private static float effDecay = .01f;
    private static float stoneToBlocks = .1f;

    private Array<String> pyramidPuns;

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
    Vector2 screenPos = new Vector2(Config.width/2 - 90, Config.height - 50);
    private String typeLabel = "";
    public  ProgressBar bgPB;
    private ProgressBar amountPB;
    private ProgressBar effPB;
    private ProgressBar slavePB;
    private Color color;

    public ResourceInfo(ResourceManager.Resources type){
        pyramidPuns = new Array<String>();
        pyramidPuns.add("Where there is a [#FFFF00xALPHAx] whip there is a way.[]");
        pyramidPuns.add("I once had to bury a guy in the desert.  I tried to apologize but he said [#FFFF00xALPHAx] 'I understand'[]");
        pyramidPuns.add("We once had a sand dune in our Army.  He [#FFFF00xALPHAx] 'deserted'[]");
        pyramidPuns.add("It's hard making a straight line here.  Let's [#FFFF00xALPHAx] 'try angle'[]");
        pyramidPuns.add("I played tuba in a marching band.  She played trumpet.  We were both terrible but we had [#FFFF00xALPHAx] 'toot in common'[].");
        pyramidPuns.add("What do the great pyramids have in common with dentures?  They're both in [#FFFF00xALPHAx] geezer[].");
        pyramidPuns.add("It's hard to befriend a mummy cause they're always so wrapped up with themselves.");
        pyramidPuns.add("I don't think I have a swimming problem, but my shrink says I'm in [#FFFF00xALPHAx] da Nile[].");
        pyramidPuns.add("I like to take walks amidst the reeds by the river... it's quite lovely to [#FFFF00xALPHAx] 'peer amid'[] them.");
        pyramidPuns.add("Where there is a Whip there is a way.");



        bgPB     = new ProgressBar(Assets.nice2NinePatch);
        amountPB = new ProgressBar(Assets.nice2NinePatch);
        effPB    = new ProgressBar(Assets.nice2NinePatch);
        slavePB  = new ProgressBar(Assets.nice2NinePatch);
        final float bgw = 220f;
        final float bgh = 120f;
        bgPB     .bounds.set(screenPos.x - 80f, screenPos.y - 80f, bgw, bgh);
        amountPB .bounds.set(screenPos.x, screenPos.y, 120, 20);
        effPB    .bounds.set(screenPos.x, screenPos.y - 30, 120, 20);
        slavePB  .bounds.set(screenPos.x, screenPos.y - 60, 120, 20);
        final Color boundsColor = new Color(160f / 255f, 82f / 255f, 45f / 255f, 1f);
        bgPB     .boundsColor.set(1f, 1f, 1f, 1f);
        amountPB .boundsColor = boundsColor;
        effPB    .boundsColor = boundsColor;
        slavePB  .boundsColor = boundsColor;
        color = new Color();
        amount = 10;
        slaves = 1;
        maxAmount = 100;
        maxEfficiency = 1;
        minEfficiency = 0;
        maxSlaves = 4;
        efficiency = 1;
        skilledWorkers = 0;
        level = 1;
        this.type = type;
        switch (type){
            case STONE:
                typeLabel = "Stones";
                efficiency = .7f;
                break;
            case WOOD:
                typeLabel = "Wood";
                efficiency = .8f;
                break;
            case FOOD:
                typeLabel = "Food";
                efficiency = .6f;
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
        if (slaves > 0) efficiency -= effDecay*dt;
        if (efficiency < 0) efficiency = 0;

        switch (type){
            case SLAVES:
                int nextSlave = LudumDare34.GameScreen.resourceManager.nextSlaveFoodAmount;
                if (LudumDare34.GameScreen.resourceManager.removeResource(ResourceManager.Resources.FOOD, nextSlave)){
                    LudumDare34.GameScreen.addNotification("Slave Born");
                    SoundManager.babyCry.play(0.1f);
                    GameScreen.stats.slavesBorn++;
                    slaves++;
                }
                break;
            case BUILD:
                float stones = (slaves * efficiency * dt);
                if (LudumDare34.GameScreen.resourceManager.removeResource(ResourceManager.Resources.STONE, stones)) {
                    amount += stones * stoneToBlocks;
                }
                if (amount > maxAmount) amount = maxAmount;
                if (amount == maxAmount){
                    //TODO: Add Pun to screen here
                    int height = LudumDare34.GameScreen.resourceManager.getPyramidHeight();
                    if (height > 2) {
                        String pun = pyramidPuns.get(height % pyramidPuns.size);
                        LudumDare34.GameScreen.addNotification("New Pyramid Tier.\n\n" + pun);
                    }
                    lastMaxAmount = (height * (height+1))/2;
                    height++;
                    maxAmount = (height * (height+1))/2;
                }
                break;
            default:
                maxAmount = 50 * (level);
                amount += ((slaves + (5 * skilledWorkers)) * efficiency * dt);
                if (amount > maxAmount) amount = maxAmount;
        }

    }

    public boolean upgradeSkilledWorker(){
        int gold = costOfNextSkilled();
        if (LudumDare34.GameScreen.resourceManager.removeResource(ResourceManager.Resources.GOLD, gold)){
            skilledWorkers++;
            return true;
        }
        return false;
    }

    public int costOfNextSkilled(){
        return (skilledWorkers+1) * 10;
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
        return (level * (level+1))/2*5;
    }

    public int costToTrade() {
        return 10;
    }

    public boolean upgradeLevel(){
        int wood = woodToUpgade();
        if (LudumDare34.GameScreen.resourceManager.removeResource(ResourceManager.Resources.WOOD, wood)){
            level++;
            maxSlaves = level * 4;
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
        efficiency += amount * .5f;
        if (efficiency >= maxEfficiency){
            efficiency = maxEfficiency;
        }
    }

    public float getWhipFalloff(){
        float val = (1.2f - ((efficiency - minEfficiency) / (maxEfficiency - minEfficiency))) * .3f;
        return Utils.clamp(val, 0, 1);
    }


    public void render(SpriteBatch batch){
        float n;

        bgPB.render(batch);

        GlyphLayout layout = new GlyphLayout(Assets.font, typeLabel);
        Assets.font.draw(batch, typeLabel, amountPB.bounds.x - (layout.width + 1), amountPB.bounds.y + (amountPB.bounds.height / 2) + layout.height / 2);
        n = (amount - lastMaxAmount) / (float) (maxAmount - lastMaxAmount);
        Utils.hsvToRgb(n * 120f / 365f, 1.0f, 1.0f, color);
        amountPB.fillColor.set(color);
        amountPB.fillPercent.setValue(n);
        amountPB.render(batch);
        String amountText = (int)amount+"/"+maxAmount;
        layout.setText(Assets.font, amountText);
        Assets.font.draw(batch, amountText, amountPB.bounds.x + (amountPB.bounds.width / 2) - layout.width / 2, amountPB.bounds.y + (amountPB.bounds.height / 2) + layout.height / 2);

        layout.setText(Assets.font, "Efficiency");
        Assets.font.draw(batch, "Efficiency", effPB.bounds.x - (layout.width + 1), effPB.bounds.y + (effPB.bounds.height / 2) + layout.height / 2);
        Utils.hsvToRgb(efficiency * 120f / 365f, 1.0f, 1.0f, color);
        effPB.fillColor.set(color);
        effPB.fillPercent.setValue(efficiency);
        effPB.render(batch);
        amountText = (int)(efficiency*100)+"%";
        layout.setText(Assets.font, amountText);
        Assets.font.draw(batch, amountText, effPB.bounds.x + (effPB.bounds.width / 2) - layout.width / 2, effPB.bounds.y + (effPB.bounds.height / 2) + layout.height / 2);

        layout.setText(Assets.font, "Slaves");
        Assets.font.draw(batch, "Slaves", slavePB.bounds.x - (layout.width + 1), slavePB.bounds.y + (slavePB.bounds.height / 2) + layout.height / 2);
        n = slaves / (float) maxSlaves;
        Utils.hsvToRgb(n * 120f / 365f, 1.0f, 1.0f, color);
        slavePB.fillColor.set(color);
        slavePB.fillPercent.setValue(n);
        slavePB.render(batch);
        amountText = (slaves)+"/"+maxSlaves;
        layout.setText(Assets.font, amountText);
        Assets.font.draw(batch, amountText, slavePB.bounds.x + (slavePB.bounds.width / 2) - layout.width / 2, slavePB.bounds.y + (slavePB.bounds.height / 2) + layout.height / 2);


    }

}
