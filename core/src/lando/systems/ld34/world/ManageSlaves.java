package lando.systems.ld34.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.resources.ResourceManager;
import lando.systems.ld34.uielements.ProgressBar;
import lando.systems.ld34.utils.Assets;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */
public class ManageSlaves extends Manage {

    ResourceManager resources;
    ProgressBar     availableSlavesBar;
    ProgressBar     buildingSlavesBar;
    ProgressBar     quarryingSlavesBar;
    ProgressBar     farmingSlavesBar;
    ProgressBar     choppingSlavesBar;
    Rectangle       buildAddButton;
    Rectangle       quarryAddButton;
    Rectangle       farmAddButton;
    Rectangle       chopAddButton;
    Rectangle       buildRemoveButton;
    Rectangle       quarryRemoveButton;
    Rectangle       farmRemoveButton;
    Rectangle       chopRemoveButton;
    Texture         buildAddTex, buildRemoveTex;
    Texture         quarryAddTex, quarryRemoveTex;
    Texture         farmAddTex, farmRemoveTex;
    Texture         woodAddTex, woodRemoveTex;

    int numAvailableSlaves;
    int numTotalSlaves;
    int numBuildingSlaves;
    int numBuildingSlavesMax;
    int numQuarryingSlaves;
    int numQuarryingSlavesMax;
    int numFarmingSlaves;
    int numFarmingSlavesMax;
    int numChoppingSlaves;
    int numChoppingSlavesMax;

    public ManageSlaves(Rectangle bounds) {
        super(Type.SLAVES, bounds);
        resources = LudumDare34.GameScreen.ResourceManager;
        availableSlavesBar = new ProgressBar(Assets.nice2NinePatch);
        buildingSlavesBar  = new ProgressBar(Assets.nice2NinePatch);
        quarryingSlavesBar = new ProgressBar(Assets.nice2NinePatch);
        farmingSlavesBar   = new ProgressBar(Assets.nice2NinePatch);
        choppingSlavesBar  = new ProgressBar(Assets.nice2NinePatch);
        final Color boundsColor = new Color(160f / 255f, 82f / 255f, 45f / 255f, 1f);
        availableSlavesBar .boundsColor = boundsColor;
        buildingSlavesBar  .boundsColor = boundsColor;
        quarryingSlavesBar .boundsColor = boundsColor;
        farmingSlavesBar   .boundsColor = boundsColor;
        choppingSlavesBar  .boundsColor = boundsColor;

        availableSlavesBar.bounds.set(leftMargin,
                                      barTop - 0f * (barHeight + lineSpacing),
                                      barWidth + 2f * (buttonSize + widgetPadding),
                                      barHeight);
        buildingSlavesBar  .bounds.set(leftMargin, barTop - 1f * (barHeight + lineSpacing), barWidth, barHeight);
        quarryingSlavesBar .bounds.set(leftMargin, barTop - 2f * (barHeight + lineSpacing), barWidth, barHeight);
        farmingSlavesBar   .bounds.set(leftMargin, barTop - 3f * (barHeight + lineSpacing), barWidth, barHeight);
        choppingSlavesBar  .bounds.set(leftMargin, barTop - 4f * (barHeight + lineSpacing), barWidth, barHeight);

        buildRemoveButton  = new Rectangle(leftMargin + barWidth + 1f * widgetPadding,              buildingSlavesBar.bounds.y,  buttonSize, buttonSize);
        quarryRemoveButton = new Rectangle(leftMargin + barWidth + 1f * widgetPadding,              quarryingSlavesBar.bounds.y, buttonSize, buttonSize);
        farmRemoveButton   = new Rectangle(leftMargin + barWidth + 1f * widgetPadding,              farmingSlavesBar.bounds.y,   buttonSize, buttonSize);
        chopRemoveButton   = new Rectangle(leftMargin + barWidth + 1f * widgetPadding,              choppingSlavesBar.bounds.y,  buttonSize, buttonSize);
        buildAddButton     = new Rectangle(leftMargin + barWidth + 2f * widgetPadding + buttonSize, buildingSlavesBar.bounds.y,  buttonSize, buttonSize);
        quarryAddButton    = new Rectangle(leftMargin + barWidth + 2f * widgetPadding + buttonSize, quarryingSlavesBar.bounds.y, buttonSize, buttonSize);
        farmAddButton      = new Rectangle(leftMargin + barWidth + 2f * widgetPadding + buttonSize, farmingSlavesBar.bounds.y,   buttonSize, buttonSize);
        chopAddButton      = new Rectangle(leftMargin + barWidth + 2f * widgetPadding + buttonSize, choppingSlavesBar.bounds.y,  buttonSize, buttonSize);
    }

    @Override
    public void update(float delta) {
        numTotalSlaves        = resources.getTotalSlaves();
        numAvailableSlaves    = resources.getSlaveAmount(ResourceManager.Resources.SLAVES);
        numBuildingSlaves     = resources.getSlaveAmount(ResourceManager.Resources.BUILD);
        numQuarryingSlaves    = resources.getSlaveAmount(ResourceManager.Resources.STONE);
        numFarmingSlaves      = resources.getSlaveAmount(ResourceManager.Resources.FOOD);
        numChoppingSlaves     = resources.getSlaveAmount(ResourceManager.Resources.WOOD);
        numBuildingSlavesMax  = resources.getResourceInfo(ResourceManager.Resources.BUILD).maxSlaves;
        numQuarryingSlavesMax = resources.getResourceInfo(ResourceManager.Resources.STONE).maxSlaves;
        numFarmingSlavesMax   = resources.getResourceInfo(ResourceManager.Resources.FOOD).maxSlaves;
        numChoppingSlavesMax  = resources.getResourceInfo(ResourceManager.Resources.WOOD).maxSlaves;

        availableSlavesBar .fillPercent.setValue(numAvailableSlaves / (float) numTotalSlaves);
        buildingSlavesBar  .fillPercent.setValue(numBuildingSlaves  / (float) numBuildingSlavesMax);
        quarryingSlavesBar .fillPercent.setValue(numQuarryingSlaves / (float) numQuarryingSlavesMax);
        farmingSlavesBar   .fillPercent.setValue(numFarmingSlaves   / (float) numFarmingSlavesMax);
        choppingSlavesBar  .fillPercent.setValue(numChoppingSlaves  / (float) numChoppingSlavesMax);

        // checks for when buttons should be inactive (and switch the button texture to greyed out)
        buildAddTex     = (numAvailableSlaves > 0 && numBuildingSlaves  < numBuildingSlavesMax)  ? Assets.upIconOn : Assets.upIconOff;
        quarryAddTex    = (numAvailableSlaves > 0 && numQuarryingSlaves < numQuarryingSlavesMax) ? Assets.upIconOn : Assets.upIconOff;
        farmAddTex      = (numAvailableSlaves > 0 && numFarmingSlaves   < numFarmingSlavesMax)   ? Assets.upIconOn : Assets.upIconOff;
        woodAddTex      = (numAvailableSlaves > 0 && numChoppingSlaves  < numChoppingSlavesMax)  ? Assets.upIconOn : Assets.upIconOff;
        buildRemoveTex  = (numBuildingSlaves  > 0) ? Assets.downIconOn : Assets.downIconOff;
        quarryRemoveTex = (numQuarryingSlaves > 0) ? Assets.downIconOn : Assets.downIconOff;
        farmRemoveTex   = (numFarmingSlaves   > 0) ? Assets.downIconOn : Assets.downIconOff;
        woodRemoveTex   = (numChoppingSlaves  > 0) ? Assets.downIconOn : Assets.downIconOff;

        if (!Gdx.input.justTouched()) {
            return;
        }

        final float x = Gdx.input.getX();
        final float y = LudumDare34.GameScreen.uiCamera.viewportHeight - Gdx.input.getY();
        int num = 0;
        if (buildRemoveButton.contains(x,y)) {
            num = resources.removeSlaves(ResourceManager.Resources.BUILD, 1);
            resources.addSlaves(ResourceManager.Resources.SLAVES, num);
        }
        else if (buildAddButton.contains(x,y) && resources.canAddSlaves(ResourceManager.Resources.BUILD, 1)) {
            num = resources.removeSlaves(ResourceManager.Resources.SLAVES, 1);
            resources.addSlaves(ResourceManager.Resources.BUILD, num);
        }

        else if (quarryRemoveButton.contains(x,y)) {
            num = resources.removeSlaves(ResourceManager.Resources.STONE, 1);
            resources.addSlaves(ResourceManager.Resources.SLAVES, num);
        }
        else if (quarryAddButton.contains(x,y) && resources.canAddSlaves(ResourceManager.Resources.STONE, 1)) {
            num = resources.removeSlaves(ResourceManager.Resources.SLAVES, 1);
            resources.addSlaves(ResourceManager.Resources.STONE, num);
        }

        else if (farmRemoveButton.contains(x,y)) {
            num = resources.removeSlaves(ResourceManager.Resources.FOOD, 1);
            resources.addSlaves(ResourceManager.Resources.SLAVES, num);
        }
        else if (farmAddButton.contains(x,y) && resources.canAddSlaves(ResourceManager.Resources.FOOD, 1)) {
            num = resources.removeSlaves(ResourceManager.Resources.SLAVES, 1);
            resources.addSlaves(ResourceManager.Resources.FOOD, num);
        }

        else if (chopRemoveButton.contains(x,y)) {
            num = resources.removeSlaves(ResourceManager.Resources.WOOD, 1);
            resources.addSlaves(ResourceManager.Resources.SLAVES, num);
        }
        else if (chopAddButton.contains(x,y)&& resources.canAddSlaves(ResourceManager.Resources.WOOD, 1)) {
            num = resources.removeSlaves(ResourceManager.Resources.SLAVES, 1);
            resources.addSlaves(ResourceManager.Resources.WOOD, num);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        drawHeading(batch, "Slave Job Management");
        drawAvailableRow(batch, "Available Slaves", availableSlavesBar, numAvailableSlaves, numTotalSlaves);
        drawAddRemoveRow(batch, "Building:", buildingSlavesBar,  buildAddTex,  buildRemoveTex,  buildAddButton,  buildRemoveButton,  numBuildingSlaves,  numBuildingSlavesMax);
        drawAddRemoveRow(batch, "Mining:",   quarryingSlavesBar, quarryAddTex, quarryRemoveTex, quarryAddButton, quarryRemoveButton, numQuarryingSlaves, numQuarryingSlavesMax);
        drawAddRemoveRow(batch, "Farming:",  farmingSlavesBar,   farmAddTex,   farmRemoveTex,   farmAddButton,   farmRemoveButton,   numFarmingSlaves,   numFarmingSlavesMax);
        drawAddRemoveRow(batch, "Logging:",  choppingSlavesBar,  woodAddTex,   woodRemoveTex,   chopAddButton,   chopRemoveButton,   numChoppingSlaves,  numChoppingSlavesMax);
        Assets.font.setColor(Color.WHITE);
        batch.setColor(Color.WHITE);
    }

}
