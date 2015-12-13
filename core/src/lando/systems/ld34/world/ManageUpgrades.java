package lando.systems.ld34.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
    ProgressBar     availableFoodBar;
    ProgressBar     availableForestBar;
    ProgressBar     availableBuildingBar;
    Rectangle       stoneUpgradeButton;
    Rectangle       foodUpgradeButton;
    Rectangle       forestUpgradeButton;
    Rectangle       buildingUpgradeButton;
    int             woodResourceLevel;
    int             stoneResourceLevel;
    int             foodResourceLevel;
    int             buildingResourceLevel;
    int             availableWood;
    int             availableWoodMax;
    int             availableStone;
    int             availableStoneMax;
    int             availableFood;
    int             availableFoodMax;
    int             availableBuildSlaves;
    int             availableBuildSlavesMax;

    final float widgetPadding = 10f;
    final float buttonSize    = 20f;

    public ManageUpgrades(Rectangle bounds) {
        super(Type.UPGRADES, bounds);
        resources = LudumDare34.GameScreen.ResourceManager;
        glyphLayout = new GlyphLayout();

        availableWoodBar     = new ProgressBar(Assets.nice2NinePatch);
        availableStoneBar    = new ProgressBar(Assets.nice2NinePatch);
        availableFoodBar     = new ProgressBar(Assets.nice2NinePatch);
        availableForestBar   = new ProgressBar(Assets.nice2NinePatch);
        availableBuildingBar = new ProgressBar(Assets.nice2NinePatch);

        final Color boundsColor = new Color(160f / 255f, 82f / 255f, 45f / 255f, 1f);
        availableWoodBar.boundsColor     = boundsColor;
        availableStoneBar.boundsColor    = boundsColor;
        availableFoodBar.boundsColor     = boundsColor;
        availableForestBar.boundsColor   = boundsColor;
        availableBuildingBar.boundsColor = boundsColor;

        final float barWidth = bounds.width / 4f;
        final float leftMargin = bounds.x + bounds.width - barWidth - 2f * (buttonSize + widgetPadding) - 40f;
        final float barHeight     = 20f;
        final float lineSpacing   = 10f;
        final float widgetPadding = 10f;
        final float buttonSize    = 20f;
        final float barTop = bounds.y + bounds.height - 100f;
        availableWoodBar    .bounds.set(leftMargin, barTop - 0f * (barHeight + lineSpacing), barWidth + 2.0f *(buttonSize + widgetPadding), barHeight);
        availableBuildingBar.bounds.set(leftMargin, barTop - 1f * (barHeight + lineSpacing), barWidth + 1.0f * buttonSize + widgetPadding,  barHeight);
        availableStoneBar   .bounds.set(leftMargin, barTop - 2f * (barHeight + lineSpacing), barWidth + 1.0f * buttonSize + widgetPadding,  barHeight);
        availableFoodBar    .bounds.set(leftMargin, barTop - 3f * (barHeight + lineSpacing), barWidth + 1.0f * buttonSize + widgetPadding,  barHeight);
        availableForestBar  .bounds.set(leftMargin, barTop - 4f * (barHeight + lineSpacing), barWidth + 1.0f * buttonSize + widgetPadding,  barHeight);

        buildingUpgradeButton = new Rectangle(leftMargin + barWidth + 4.0f * widgetPadding, availableBuildingBar.bounds.y, buttonSize, buttonSize);
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

        woodResourceLevel       = woodResource.level;
        stoneResourceLevel      = stoneResource.level;
        foodResourceLevel       = foodResource.level;
        buildingResourceLevel   = buildResource.level;
        availableWood           = MathUtils.floor(woodResource.amount);
        availableStone          = MathUtils.floor(stoneResource.amount);
        availableFood           = MathUtils.floor(foodResource.amount);
        availableBuildSlaves    = MathUtils.floor(buildResource.slaves);
        availableWoodMax        = woodResource.maxAmount;
        availableStoneMax       = stoneResource.maxAmount;
        availableFoodMax        = foodResource.maxAmount;
        availableBuildSlavesMax = buildResource.maxSlaves;

        availableWoodBar    .fillPercent.setValue(availableWood  / (float) availableWoodMax);
        availableStoneBar   .fillPercent.setValue(availableStone / (float) availableStoneMax);
        availableFoodBar    .fillPercent.setValue(availableFood  / (float) availableFoodMax);
        availableForestBar  .fillPercent.setValue(availableWood  / (float) availableWoodMax);
        availableBuildingBar.fillPercent.setValue(availableBuildSlaves / (float) availableBuildSlavesMax);

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

        glyphLayout.setText(Assets.font, "Building (level " + buildingResourceLevel + "):");
        x = MathUtils.floor(availableBuildingBar.bounds.x - glyphLayout.width - widgetPadding);
        y = availableBuildingBar.bounds.y + availableBuildingBar.bounds.height / 2f + glyphLayout.height / 2f;
        availableBuildingBar.render(batch);
        batch.draw(Assets.upIconOn, buildingUpgradeButton.x, buildingUpgradeButton.y, buttonSize, buttonSize);
//        n = availableBuildSlaves / (float) availableBuildSlavesMax;
//        Assets.font.setColor(1f - n, n, 0f, 1f);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, "Building (level " + buildingResourceLevel + "):", x, y);
        String buildText = availableBuildSlaves + "/" + availableBuildSlavesMax;
        glyphLayout.setText(Assets.font, buildText);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, buildText,
                         availableBuildingBar.bounds.x + availableBuildingBar.bounds.width / 2f - glyphLayout.width / 2f,
                         availableBuildingBar.bounds.y + availableBuildingBar.bounds.height / 2f + glyphLayout.height / 2f);

        glyphLayout.setText(Assets.font, "Mining (level " + stoneResourceLevel + "):");
        x = MathUtils.floor(availableStoneBar.bounds.x - glyphLayout.width - widgetPadding);
        y = availableStoneBar.bounds.y + availableStoneBar.bounds.height / 2f + glyphLayout.height / 2f;
        availableStoneBar.render(batch);
        batch.draw(Assets.upIconOn, stoneUpgradeButton.x, stoneUpgradeButton.y, buttonSize, buttonSize);
//        n = availableStone / (float) availableStoneMax;
//        Assets.font.setColor(1f - n, n, 0f, 1f);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, "Mining (level " + stoneResourceLevel + "):", x, y);
        String stoneText = availableStone + "/" + availableStoneMax;
        glyphLayout.setText(Assets.font, stoneText);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, stoneText,
                         availableStoneBar.bounds.x + availableStoneBar.bounds.width / 2f - glyphLayout.width / 2f,
                         availableStoneBar.bounds.y + availableStoneBar.bounds.height / 2f + glyphLayout.height / 2f);

        glyphLayout.setText(Assets.font, "Farming (level " + foodResourceLevel + "):");
        x = MathUtils.floor(availableFoodBar.bounds.x - glyphLayout.width - widgetPadding);
        y = availableFoodBar.bounds.y + availableFoodBar.bounds.height / 2f + glyphLayout.height / 2f;
        availableFoodBar.render(batch);
        batch.draw(Assets.upIconOn, foodUpgradeButton.x, foodUpgradeButton.y, buttonSize, buttonSize);
//        n = availableFood / (float) availableFoodMax;
//        Assets.font.setColor(1f - n, n, 0f, 1f);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, "Farming (level " + foodResourceLevel + "):", x, y);
        String foodText = availableFood + "/" + availableFoodMax;
        glyphLayout.setText(Assets.font, foodText);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, foodText,
                         availableFoodBar.bounds.x + availableFoodBar.bounds.width / 2f - glyphLayout.width / 2f,
                         availableFoodBar.bounds.y + availableFoodBar.bounds.height / 2f + glyphLayout.height / 2f);

        glyphLayout.setText(Assets.font, "Logging (level " + woodResourceLevel + "):");
        x = MathUtils.floor(availableForestBar.bounds.x - glyphLayout.width - widgetPadding);
        y = availableForestBar.bounds.y + availableForestBar.bounds.height / 2f + glyphLayout.height / 2f;
        availableForestBar.render(batch);
        batch.draw(Assets.upIconOn, forestUpgradeButton.x, forestUpgradeButton.y, buttonSize, buttonSize);
//        n = availableWood / (float) availableWoodMax;
//        Assets.font.setColor(1f - n, n, 0f, 1f);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, "Logging (level " + woodResourceLevel + "):", x, y);
        String forestText = availableWood + "/" + availableWoodMax;
        glyphLayout.setText(Assets.font, forestText);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, forestText,
                         availableForestBar.bounds.x + availableForestBar.bounds.width / 2f - glyphLayout.width / 2f,
                         availableForestBar.bounds.y + availableForestBar.bounds.height / 2f + glyphLayout.height / 2f);


        batch.setColor(Color.WHITE);
        Assets.font.setColor(Color.WHITE);
    }
}
