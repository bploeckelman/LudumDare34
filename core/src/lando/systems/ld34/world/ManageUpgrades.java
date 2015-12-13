package lando.systems.ld34.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.resources.ResourceInfo;
import lando.systems.ld34.resources.ResourceManager;
import lando.systems.ld34.uielements.ProgressBar;
import lando.systems.ld34.utils.Assets;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */
public class ManageUpgrades extends Manage {

    ResourceManager resources;
    ProgressBar     availableWoodBar;
    ProgressBar     availableStoneBar;
    ProgressBar     availableStoneSlavesBar;
    ProgressBar     availableFoodBar;
    ProgressBar     availableFoodSlavesBar;
    ProgressBar     availableForestBar;
    ProgressBar     availableForestSlavesBar;
    ProgressBar     availableBuildingSlavesBar;
    Rectangle       stoneUpgradeButton;
    Rectangle       foodUpgradeButton;
    Rectangle       forestUpgradeButton;
    Rectangle       buildingUpgradeButton;
    Texture         buildUpgradeTex;
    Texture         stoneUpgradeTex;
    Texture         foodUpgradeTex;
    Texture         forestUpgradeTex;
    int             woodResourceLevel;
    int             stoneResourceLevel;
    int             foodResourceLevel;
    int             buildingResourceLevel;
    int             availableWood;
    int             availableWoodMax;
    int             availableStone;
    int             availableStoneMax;
    int             availableStoneSlaves;
    int             availableStoneSlavesMax;
    int             availableFood;
    int             availableFoodMax;
    int             availableForestSlaves;
    int             availableForestSlavesMax;
    int             availableFoodSlaves;
    int             availableFoodSlavesMax;
    int             availableBuildSlaves;
    int             availableBuildSlavesMax;

    final float widgetPadding = 10f;
    final float buttonSize    = 20f;

    public ManageUpgrades(Rectangle bounds) {
        super(Type.UPGRADES, bounds);
        resources = LudumDare34.GameScreen.ResourceManager;

        availableWoodBar           = new ProgressBar(Assets.nice2NinePatch);
        availableStoneBar          = new ProgressBar(Assets.nice2NinePatch);
        availableFoodBar           = new ProgressBar(Assets.nice2NinePatch);
        availableForestBar         = new ProgressBar(Assets.nice2NinePatch);
        availableBuildingSlavesBar = new ProgressBar(Assets.nice2NinePatch);
        availableStoneSlavesBar    = new ProgressBar(Assets.nice2NinePatch);
        availableFoodSlavesBar     = new ProgressBar(Assets.nice2NinePatch);
        availableForestSlavesBar   = new ProgressBar(Assets.nice2NinePatch);

        final Color boundsColor = new Color(160f / 255f, 82f / 255f, 45f / 255f, 1f);
        availableWoodBar.boundsColor           = boundsColor;
        availableStoneBar.boundsColor          = boundsColor;
        availableStoneSlavesBar.boundsColor    = boundsColor;
        availableFoodBar.boundsColor           = boundsColor;
        availableFoodSlavesBar.boundsColor     = boundsColor;
        availableForestBar.boundsColor         = boundsColor;
        availableForestSlavesBar.boundsColor   = boundsColor;
        availableBuildingSlavesBar.boundsColor = boundsColor;

        availableWoodBar           .fillColor.set(0f, 0.5f, 0f, 1f);
        availableStoneBar          .fillColor.set(0f, 0.5f, 0f, 1f);
        availableFoodBar           .fillColor.set(0f, 0.5f, 0f, 1f);
        availableForestBar         .fillColor.set(0f, 0.5f, 0f, 1f);
        availableBuildingSlavesBar .fillColor.set(0f, 0.5f, 0f, 1f);
        availableStoneSlavesBar    .fillColor.set(0f, 0.5f, 0f, 1f);
        availableFoodSlavesBar     .fillColor.set(0f, 0.5f, 0f, 1f);
        availableForestSlavesBar   .fillColor.set(0f, 0.5f, 0f, 1f);

        final float barWidth = bounds.width / 3f;
        final float leftMargin = bounds.x + bounds.width - barWidth - 2f * (buttonSize + widgetPadding) - 20f;
        final float barHeight     = 20f;
        final float lineSpacing   = 10f;
        final float widgetPadding = 10f;
        final float buttonSize    = 20f;
        final float barTop = bounds.y + bounds.height - 100f;
        final float barWidth1 = (barWidth + 1.0f * buttonSize + widgetPadding) / 2f;
        availableWoodBar          .bounds.set(leftMargin,             barTop - 0f * (barHeight + lineSpacing), barWidth  + 2.0f *(buttonSize + widgetPadding), barHeight);
        availableBuildingSlavesBar.bounds.set(leftMargin,             barTop - 1f * (barHeight + lineSpacing), barWidth1 * 2.0f, barHeight);
        availableStoneBar         .bounds.set(leftMargin,             barTop - 2f * (barHeight + lineSpacing), barWidth1, barHeight);
        availableStoneSlavesBar   .bounds.set(leftMargin + barWidth1, barTop - 2f * (barHeight + lineSpacing), barWidth1, barHeight);
        availableFoodBar          .bounds.set(leftMargin,             barTop - 3f * (barHeight + lineSpacing), barWidth1, barHeight);
        availableFoodSlavesBar    .bounds.set(leftMargin + barWidth1, barTop - 3f * (barHeight + lineSpacing), barWidth1, barHeight);
        availableForestBar        .bounds.set(leftMargin,             barTop - 4f * (barHeight + lineSpacing), barWidth1, barHeight);
        availableForestSlavesBar  .bounds.set(leftMargin + barWidth1, barTop - 4f * (barHeight + lineSpacing), barWidth1, barHeight);

        buildingUpgradeButton = new Rectangle(leftMargin + barWidth + 4.0f * widgetPadding, availableBuildingSlavesBar.bounds.y, buttonSize, buttonSize);
        stoneUpgradeButton    = new Rectangle(leftMargin + barWidth + 4.0f * widgetPadding, availableStoneBar.bounds.y,    buttonSize, buttonSize);
        foodUpgradeButton     = new Rectangle(leftMargin + barWidth + 4.0f * widgetPadding, availableFoodBar.bounds.y,     buttonSize, buttonSize);
        forestUpgradeButton   = new Rectangle(leftMargin + barWidth + 4.0f * widgetPadding, availableForestBar.bounds.y,   buttonSize, buttonSize);
    }

    @Override
    public void update(float delta) {
        final ResourceInfo woodResource  = resources.getResourceInfo(ResourceManager.Resources.WOOD);
        final ResourceInfo stoneResource = resources.getResourceInfo(ResourceManager.Resources.STONE);
        final ResourceInfo foodResource  = resources.getResourceInfo(ResourceManager.Resources.FOOD);
        final ResourceInfo buildResource = resources.getResourceInfo(ResourceManager.Resources.BUILD);

        woodResourceLevel         = woodResource.level;
        stoneResourceLevel        = stoneResource.level;
        foodResourceLevel         = foodResource.level;
        buildingResourceLevel     = buildResource.level;
        availableWood             = MathUtils.floor(woodResource.amount);
        availableStone            = MathUtils.floor(stoneResource.amount);
        availableFood             = MathUtils.floor(foodResource.amount);
        availableBuildSlaves      = MathUtils.floor(buildResource.slaves);
        availableStoneSlaves      = MathUtils.floor(stoneResource.slaves);
        availableFoodSlaves       = MathUtils.floor(foodResource.slaves);
        availableForestSlaves     = MathUtils.floor(woodResource.slaves);
        availableWoodMax          = woodResource.maxAmount;
        availableStoneMax         = stoneResource.maxAmount;
        availableFoodMax          = foodResource.maxAmount;
        availableBuildSlavesMax   = buildResource.maxSlaves;
        availableStoneSlavesMax   = stoneResource.maxSlaves;
        availableFoodSlavesMax    = foodResource.maxSlaves;
        availableForestSlavesMax  = woodResource.maxSlaves;

        availableWoodBar           .fillPercent.setValue(availableWood  / (float) availableWoodMax);
        availableStoneBar          .fillPercent.setValue(availableStone / (float) availableStoneMax);
        availableFoodBar           .fillPercent.setValue(availableFood  / (float) availableFoodMax);
        availableForestBar         .fillPercent.setValue(availableWood  / (float) availableWoodMax);
        availableBuildingSlavesBar .fillPercent.setValue(availableBuildSlaves  / (float) availableBuildSlavesMax);
        availableStoneSlavesBar    .fillPercent.setValue(availableStoneSlaves  / (float) availableStoneSlavesMax);
        availableFoodSlavesBar     .fillPercent.setValue(availableFoodSlaves   / (float) availableFoodSlavesMax);
        availableForestSlavesBar   .fillPercent.setValue(availableForestSlaves / (float) availableForestSlavesMax);

        buildUpgradeTex  = resources.canUpgrade(ResourceManager.Resources.BUILD) ? Assets.plusIconOn : Assets.plusIconOff;
        stoneUpgradeTex  = resources.canUpgrade(ResourceManager.Resources.STONE) ? Assets.plusIconOn : Assets.plusIconOff;
        foodUpgradeTex   = resources.canUpgrade(ResourceManager.Resources.FOOD)  ? Assets.plusIconOn : Assets.plusIconOff;
        forestUpgradeTex = resources.canUpgrade(ResourceManager.Resources.WOOD)  ? Assets.plusIconOn : Assets.plusIconOff;

        if (!Gdx.input.justTouched()) {
            return;
        }

        final float x = Gdx.input.getX();
        final float y = LudumDare34.GameScreen.uiCamera.viewportHeight - Gdx.input.getY();
        if (stoneUpgradeButton.contains(x,y) && resources.canUpgrade(ResourceManager.Resources.STONE)) {
            stoneResource.upgradeLevel();
        }
        else if (foodUpgradeButton.contains(x,y) && resources.canUpgrade(ResourceManager.Resources.FOOD)) {
            foodResource.upgradeLevel();
        }
        else if (forestUpgradeButton.contains(x,y) && resources.canUpgrade(ResourceManager.Resources.WOOD)) {
            woodResource.upgradeLevel();
        }
        else if (buildingUpgradeButton.contains(x,y) && resources.canUpgrade(ResourceManager.Resources.BUILD)) {
            buildResource.upgradeLevel();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Assets.fontSmall.setColor(Color.WHITE);

        drawHeading(batch, "Resource Upgrade Management");
        drawAvailableRow(batch, "Available Wood:", availableWoodBar, availableWood, availableWoodMax);

        // too lazy to extract a method for this single oddball row
        glyphLayout.setText(Assets.font, "Building (level " + buildingResourceLevel + "):");
        final float x = MathUtils.floor(availableBuildingSlavesBar.bounds.x - glyphLayout.width - widgetPadding);
        final float y = availableBuildingSlavesBar.bounds.y + availableBuildingSlavesBar.bounds.height / 2f + glyphLayout.height / 2f;
        availableBuildingSlavesBar.render(batch);
        batch.draw(buildUpgradeTex, buildingUpgradeButton.x, buildingUpgradeButton.y, buttonSize, buttonSize);
//        final float n = availableBuildSlaves / (float) availableBuildSlavesMax;
//        Assets.font.setColor(1f - n, n, 0f, 1f);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, "Building (level " + buildingResourceLevel + "):", x, y);
        String buildText = availableBuildSlaves + "/" + availableBuildSlavesMax;
        glyphLayout.setText(Assets.fontSmall, buildText);
        Assets.fontSmall.draw(batch, buildText,
                         availableBuildingSlavesBar.bounds.x + availableBuildingSlavesBar.bounds.width / 2f - glyphLayout.width / 2f,
                         availableBuildingSlavesBar.bounds.y + availableBuildingSlavesBar.bounds.height / 2f + glyphLayout.height / 2f);

        drawUpgrade2ColRow(batch, "Mining",  stoneResourceLevel, availableStoneBar,  availableStoneSlavesBar,  stoneUpgradeTex,  stoneUpgradeButton,  availableStone, availableStoneMax, availableStoneSlaves,  availableStoneSlavesMax);
        drawUpgrade2ColRow(batch, "Farming", foodResourceLevel,  availableFoodBar,   availableFoodSlavesBar,   foodUpgradeTex,   foodUpgradeButton,   availableFood,  availableFoodMax,  availableFoodSlaves,   availableFoodSlavesMax);
        drawUpgrade2ColRow(batch, "Logging", woodResourceLevel,  availableForestBar, availableForestSlavesBar, forestUpgradeTex, forestUpgradeButton, availableWood,  availableWoodMax,  availableForestSlaves, availableForestSlavesMax);

        batch.setColor(Color.WHITE);
        Assets.font.setColor(Color.WHITE);
        Assets.fontSmall.setColor(Color.WHITE);
    }

}
