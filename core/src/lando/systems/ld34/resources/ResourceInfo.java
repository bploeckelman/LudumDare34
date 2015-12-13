package lando.systems.ld34.resources;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld34.Config;
import lando.systems.ld34.uielements.ProgressBar;
import lando.systems.ld34.utils.Assets;

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


    // Center Top of panel
    Vector2 screenPos = new Vector2(Config.width/2, Config.height - 50);
    private String typeLabel = "";
    private ProgressBar amountPB;
    private ProgressBar effPB;

    public ResourceInfo(ResourceManager.Resources type){
        amountPB = new ProgressBar();
        amountPB.bounds = new Rectangle(screenPos.x - 50, screenPos.y, 120, 20);
        effPB = new ProgressBar();
        effPB.bounds = new Rectangle(screenPos.x - 80, screenPos.y, 120, 20);
        amount = 10;
        slaves = 1;
        maxAmount = 100;
        maxEfficiency = 1;
        maxSlaves = 10;
        efficiency = 1;
        this.type = type;
        switch (type){
            case STONE:
                typeLabel = "Stones";
                break;
        }
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
        efficiency += amount * .1f;
        if (efficiency >= maxEfficiency){
            efficiency = maxEfficiency;
        }
    }


    public void render(SpriteBatch batch){
        GlyphLayout layout = new GlyphLayout(Assets.font, typeLabel);
        Assets.font.draw(batch, typeLabel, amountPB.bounds.x - (layout.width + 1), amountPB.bounds.y + (amountPB.bounds.height/2) + layout.height/2);
        amountPB.bounds.y = screenPos.y - (layout.height + amountPB.bounds.height)/2;
        amountPB.fillPercent.setValue(amount / maxAmount);
        amountPB.render(batch);
        String amountText = (int)amount+"/"+maxAmount;
        layout.setText(Assets.font, amountText);
        Assets.font.draw(batch, amountText, amountPB.bounds.x + (amountPB.bounds.width /2) - layout.width/2, amountPB.bounds.y + (amountPB.bounds.height/2) + layout.height/2);
    }

}
