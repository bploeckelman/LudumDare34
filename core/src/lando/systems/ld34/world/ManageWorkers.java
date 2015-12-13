package lando.systems.ld34.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.resources.ResourceInfo;
import lando.systems.ld34.resources.ResourceManager;
import lando.systems.ld34.uielements.ProgressBar;
import lando.systems.ld34.utils.Assets;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */
public class ManageWorkers extends Manage {

    ResourceManager resources;
    ProgressBar     buildersBar;
    ProgressBar     minersBar;
    ProgressBar     farmersBar;
    ProgressBar     loggersBar;
    Rectangle       addBuilderButton;
    Rectangle       addMinerButton;
    Rectangle       addFarmerButton;
    Rectangle       addLoggerButton;
    Texture         addBuilderTexture;
    Texture         addMinerTexture;
    Texture         addFarmerTexture;
    Texture         addLoggerTexture;

    int numBuilders;
    int numMiners;
    int numFarmers;
    int numLoggers;
    int nextBuilderCost;
    int nextMinerCost;
    int nextFarmerCost;
    int nextLoggerCost;

    public ManageWorkers(Rectangle bounds) {
        super(Type.WORKERS, bounds);
        resources = LudumDare34.GameScreen.ResourceManager;
        buildersBar = new ProgressBar(Assets.nice2NinePatch);
        minersBar = new ProgressBar(Assets.nice2NinePatch);
        farmersBar = new ProgressBar(Assets.nice2NinePatch);
        loggersBar = new ProgressBar(Assets.nice2NinePatch);

        final Color boundsColor = new Color(160f / 255f, 82f / 255f, 45f / 255f, 1f);
        buildersBar .boundsColor = boundsColor;
        minersBar   .boundsColor = boundsColor;
        farmersBar  .boundsColor = boundsColor;
        loggersBar  .boundsColor = boundsColor;

        buildersBar .bounds.set(leftMargin, barTop - 0f * (barHeight + lineSpacing), barWidth, barHeight);
        minersBar   .bounds.set(leftMargin, barTop - 1f * (barHeight + lineSpacing), barWidth, barHeight);
        farmersBar  .bounds.set(leftMargin, barTop - 2f * (barHeight + lineSpacing), barWidth, barHeight);
        loggersBar  .bounds.set(leftMargin, barTop - 3f * (barHeight + lineSpacing), barWidth, barHeight);

        addBuilderButton = new Rectangle(leftMargin + barWidth + 1f * widgetPadding, buildersBar.bounds.y, buttonSize, buttonSize);
        addMinerButton   = new Rectangle(leftMargin + barWidth + 1f * widgetPadding, minersBar.bounds.y,   buttonSize, buttonSize);
        addFarmerButton  = new Rectangle(leftMargin + barWidth + 1f * widgetPadding, farmersBar.bounds.y,  buttonSize, buttonSize);
        addLoggerButton  = new Rectangle(leftMargin + barWidth + 1f * widgetPadding, loggersBar.bounds.y,  buttonSize, buttonSize);
    }

    @Override
    public void update(float delta) {
        final ResourceInfo buildResource = resources.getResourceInfo(ResourceManager.Resources.BUILD);
        final ResourceInfo woodResource  = resources.getResourceInfo(ResourceManager.Resources.WOOD);
        final ResourceInfo stoneResource = resources.getResourceInfo(ResourceManager.Resources.STONE);
        final ResourceInfo foodResource  = resources.getResourceInfo(ResourceManager.Resources.FOOD);
        final ResourceInfo goldResource  = resources.getResourceInfo(ResourceManager.Resources.GOLD);

        numBuilders = buildResource.skilledWorkers;
        numMiners   = stoneResource.skilledWorkers;
        numFarmers  = foodResource.skilledWorkers;
        numLoggers  = woodResource.skilledWorkers;

        nextBuilderCost = buildResource.costOfNextSkilled();
        nextMinerCost   = stoneResource.costOfNextSkilled();
        nextFarmerCost  = foodResource.costOfNextSkilled();
        nextLoggerCost  = woodResource.costOfNextSkilled();

        final boolean canBuyBuilder = goldResource.amount >= nextBuilderCost;
        final boolean canBuyMiner   = goldResource.amount >= nextMinerCost;
        final boolean canBuyFarmer  = goldResource.amount >= nextFarmerCost;
        final boolean canBuyLogger  = goldResource.amount >= nextLoggerCost;

        addBuilderTexture = canBuyBuilder ? Assets.plusIconOn : Assets.plusIconOff;
        addMinerTexture   = canBuyMiner   ? Assets.plusIconOn : Assets.plusIconOff;
        addFarmerTexture  = canBuyFarmer  ? Assets.plusIconOn : Assets.plusIconOff;
        addLoggerTexture  = canBuyLogger  ? Assets.plusIconOn : Assets.plusIconOff;

        if (!Gdx.input.justTouched()) {
            return;
        }

        final float x = Gdx.input.getX();
        final float y = LudumDare34.GameScreen.uiCamera.viewportHeight - Gdx.input.getY();
        if      (addBuilderButton.contains(x,y) && canBuyBuilder) buildResource.upgradeSkilledWorker();
        else if (addMinerButton.contains(x,y)   && canBuyMiner)   stoneResource.upgradeSkilledWorker();
        else if (addFarmerButton.contains(x,y)  && canBuyFarmer)  foodResource.upgradeSkilledWorker();
        else if (addLoggerButton.contains(x,y)  && canBuyLogger)  woodResource.upgradeSkilledWorker();
    }

    @Override
    public void render(SpriteBatch batch) {
        drawHeading(batch, "Skilled Worker Management");
        drawAddWorkerRow(batch, "Builders:", buildersBar, addBuilderTexture, addBuilderButton, numBuilders, nextBuilderCost);
        drawAddWorkerRow(batch, "Miners:",   minersBar,   addMinerTexture,   addMinerButton,   numMiners,   nextMinerCost);
        drawAddWorkerRow(batch, "Farmers:",  farmersBar,  addFarmerTexture,  addFarmerButton,  numFarmers,  nextFarmerCost);
        drawAddWorkerRow(batch, "Loggers:",  loggersBar,  addLoggerTexture,  addLoggerButton,  numLoggers,  nextLoggerCost);
    }

}
