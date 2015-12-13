package lando.systems.ld34.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
    GlyphLayout     glyphLayout;
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
        glyphLayout = new GlyphLayout();

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
        final float leftMargin = bounds.x + bounds.width - barWidth - 2f * (buttonSize + widgetPadding) - 40f;
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

        buildUpgradeTex  = resources.canUpgrade(ResourceManager.Resources.BUILD) ? Assets.upIconOn : Assets.upIconOff;
        stoneUpgradeTex  = resources.canUpgrade(ResourceManager.Resources.STONE) ? Assets.upIconOn : Assets.upIconOff;
        foodUpgradeTex   = resources.canUpgrade(ResourceManager.Resources.FOOD)  ? Assets.upIconOn : Assets.upIconOff;
        forestUpgradeTex = resources.canUpgrade(ResourceManager.Resources.WOOD)  ? Assets.upIconOn : Assets.upIconOff;

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
        float x,y,n;

        Assets.fontSmall.setColor(Color.WHITE);

        glyphLayout.setText(Assets.font, "Upgrade Management");
        x = bounds.x + bounds.width / 2f - glyphLayout.width / 2f;
        y = bounds.y + bounds.height - glyphLayout.height;
        Assets.batch.setColor(160f / 255f, 82f / 255f, 45f / 255f, 1f);
        final float marginx = 30f;
        final float marginy = 15f;
        Assets.nice2NinePatch.draw(batch, x - marginx, y - marginy - glyphLayout.height, glyphLayout.width + 2f * marginx, glyphLayout.height + 2f * marginy);
        Assets.batch.setColor(1f, 1f, 1f, 1f);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, "Upgrade Management", x, y);

        glyphLayout.setText(Assets.font, "Available Wood:");
        x = MathUtils.floor(availableWoodBar.bounds.x - glyphLayout.width - widgetPadding);
        y = availableWoodBar.bounds.y + availableWoodBar.bounds.height / 2f + glyphLayout.height / 2f;
        availableWoodBar.render(batch);
        n = availableWood / (float) availableWoodMax;
        Assets.font.setColor(1f - n, n, 0f, 1f);
        Assets.font.draw(batch, "Available Wood:", x, y);
        String woodText = availableWood + "/" + availableWoodMax;
        glyphLayout.setText(Assets.fontSmall, woodText);
        Assets.fontSmall.draw(batch, woodText,
                              availableWoodBar.bounds.x + availableWoodBar.bounds.width / 2f - glyphLayout.width / 2f,
                              availableWoodBar.bounds.y + availableWoodBar.bounds.height / 2f + glyphLayout.height / 2f);

        glyphLayout.setText(Assets.font, "Building (level " + buildingResourceLevel + "):");
        x = MathUtils.floor(availableBuildingSlavesBar.bounds.x - glyphLayout.width - widgetPadding);
        y = availableBuildingSlavesBar.bounds.y + availableBuildingSlavesBar.bounds.height / 2f + glyphLayout.height / 2f;
        availableBuildingSlavesBar.render(batch);
        batch.draw(buildUpgradeTex, buildingUpgradeButton.x, buildingUpgradeButton.y, buttonSize, buttonSize);
//        n = availableBuildSlaves / (float) availableBuildSlavesMax;
//        Assets.font.setColor(1f - n, n, 0f, 1f);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, "Building (level " + buildingResourceLevel + "):", x, y);
        String buildText = availableBuildSlaves + "/" + availableBuildSlavesMax;
        glyphLayout.setText(Assets.fontSmall, buildText);
        Assets.fontSmall.draw(batch, buildText,
                         availableBuildingSlavesBar.bounds.x + availableBuildingSlavesBar.bounds.width / 2f - glyphLayout.width / 2f,
                         availableBuildingSlavesBar.bounds.y + availableBuildingSlavesBar.bounds.height / 2f + glyphLayout.height / 2f);

        glyphLayout.setText(Assets.font, "Mining (level " + stoneResourceLevel + "):");
        x = MathUtils.floor(availableStoneBar.bounds.x - glyphLayout.width - widgetPadding);
        y = availableStoneBar.bounds.y + availableStoneBar.bounds.height / 2f + glyphLayout.height / 2f;
        availableStoneBar.render(batch);
        availableStoneSlavesBar.render(batch);
        batch.draw(stoneUpgradeTex, stoneUpgradeButton.x, stoneUpgradeButton.y, buttonSize, buttonSize);
//        n = availableStone / (float) availableStoneMax;
//        Assets.font.setColor(1f - n, n, 0f, 1f);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, "Mining (level " + stoneResourceLevel + "):", x, y);
        String stoneText = availableStone + "/" + availableStoneMax;
        glyphLayout.setText(Assets.fontSmall, stoneText);
        Assets.fontSmall.draw(batch, stoneText,
                         availableStoneBar.bounds.x + availableStoneBar.bounds.width / 2f - glyphLayout.width / 2f,
                         availableStoneBar.bounds.y + availableStoneBar.bounds.height / 2f + glyphLayout.height / 2f);
        String stoneSlavesText = availableStoneSlaves + "/" + availableStoneSlavesMax;
        glyphLayout.setText(Assets.fontSmall, stoneSlavesText);
        Assets.fontSmall.draw(batch, stoneSlavesText,
                              availableStoneSlavesBar.bounds.x + availableStoneSlavesBar.bounds.width / 2f - glyphLayout.width / 2f,
                              availableStoneSlavesBar.bounds.y + availableStoneSlavesBar.bounds.height / 2f + glyphLayout.height / 2f);

        glyphLayout.setText(Assets.font, "Farming (level " + foodResourceLevel + "):");
        x = MathUtils.floor(availableFoodBar.bounds.x - glyphLayout.width - widgetPadding);
        y = availableFoodBar.bounds.y + availableFoodBar.bounds.height / 2f + glyphLayout.height / 2f;
        availableFoodBar.render(batch);
        availableFoodSlavesBar.render(batch);
        batch.draw(foodUpgradeTex, foodUpgradeButton.x, foodUpgradeButton.y, buttonSize, buttonSize);
//        n = availableFood / (float) availableFoodMax;
//        Assets.font.setColor(1f - n, n, 0f, 1f);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, "Farming (level " + foodResourceLevel + "):", x, y);
        String foodText = availableFood + "/" + availableFoodMax;
        glyphLayout.setText(Assets.fontSmall, foodText);
        Assets.fontSmall.draw(batch, foodText,
                         availableFoodBar.bounds.x + availableFoodBar.bounds.width / 2f - glyphLayout.width / 2f,
                         availableFoodBar.bounds.y + availableFoodBar.bounds.height / 2f + glyphLayout.height / 2f);
        String foodSlavesText = availableFoodSlaves + "/" + availableFoodSlavesMax;
        glyphLayout.setText(Assets.fontSmall, foodSlavesText);
        Assets.fontSmall.draw(batch, foodSlavesText,
                              availableFoodSlavesBar.bounds.x + availableFoodSlavesBar.bounds.width / 2f - glyphLayout.width / 2f,
                              availableFoodSlavesBar.bounds.y + availableFoodSlavesBar.bounds.height / 2f + glyphLayout.height / 2f);

        glyphLayout.setText(Assets.font, "Logging (level " + woodResourceLevel + "):");
        x = MathUtils.floor(availableForestBar.bounds.x - glyphLayout.width - widgetPadding);
        y = availableForestBar.bounds.y + availableForestBar.bounds.height / 2f + glyphLayout.height / 2f;
        availableForestBar.render(batch);
        availableForestSlavesBar.render(batch);
        batch.draw(forestUpgradeTex, forestUpgradeButton.x, forestUpgradeButton.y, buttonSize, buttonSize);
//        n = availableWood / (float) availableWoodMax;
//        Assets.font.setColor(1f - n, n, 0f, 1f);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, "Logging (level " + woodResourceLevel + "):", x, y);
        String forestText = availableWood + "/" + availableWoodMax;
        glyphLayout.setText(Assets.fontSmall, forestText);
        Assets.fontSmall.draw(batch, forestText,
                         availableForestBar.bounds.x + availableForestBar.bounds.width / 2f - glyphLayout.width / 2f,
                         availableForestBar.bounds.y + availableForestBar.bounds.height / 2f + glyphLayout.height / 2f);
        String forestSlavesText = availableForestSlaves + "/" + availableForestSlavesMax;
        glyphLayout.setText(Assets.fontSmall, forestSlavesText);
        Assets.fontSmall.draw(batch, forestSlavesText,
                              availableForestSlavesBar.bounds.x + availableForestSlavesBar.bounds.width / 2f - glyphLayout.width / 2f,
                              availableForestSlavesBar.bounds.y + availableForestSlavesBar.bounds.height / 2f + glyphLayout.height / 2f);

        batch.setColor(Color.WHITE);
        Assets.font.setColor(Color.WHITE);
        Assets.fontSmall.setColor(Color.WHITE);
    }
}
