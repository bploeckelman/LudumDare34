package lando.systems.ld34.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
    GlyphLayout     glyphLayout;
    ProgressBar     availableSlavesBar;
    ProgressBar     buildingSlavesBar;
    ProgressBar     quarryingSlavesBar;
    ProgressBar     farmingSlavesBar;
    ProgressBar     choppingSlavesBar;

    float barWidth;
    float barHeight;
    float lineHeight;
    float lineSpacing;
    float widgetPadding;
    float buttonSize;

    public ManageSlaves(Rectangle bounds) {
        super(Type.SLAVES, bounds);
        resources = LudumDare34.GameScreen.ResourceManager;
        glyphLayout = new GlyphLayout();
        availableSlavesBar = new ProgressBar();
        buildingSlavesBar = new ProgressBar();
        quarryingSlavesBar = new ProgressBar();
        farmingSlavesBar = new ProgressBar();
        choppingSlavesBar = new ProgressBar();

        barWidth = bounds.width / 4f;
        barHeight = 20f;
        lineHeight = 20f;
        lineSpacing = 10f;
        widgetPadding = 10f;
        buttonSize = 20f;
        final float barTop = bounds.y + bounds.height - 100f;
        final float leftMargin = bounds.x + bounds.width - barWidth - 2f * (buttonSize + widgetPadding);
        availableSlavesBar .bounds.set(leftMargin, barTop - 0f * (barHeight + lineSpacing), barWidth, barHeight);
        buildingSlavesBar  .bounds.set(leftMargin, barTop - 1f * (barHeight + lineSpacing), barWidth, barHeight);
        quarryingSlavesBar .bounds.set(leftMargin, barTop - 2f * (barHeight + lineSpacing), barWidth, barHeight);
        farmingSlavesBar   .bounds.set(leftMargin, barTop - 3f * (barHeight + lineSpacing), barWidth, barHeight);
        choppingSlavesBar  .bounds.set(leftMargin, barTop - 4f * (barHeight + lineSpacing), barWidth, barHeight);
    }

    @Override
    public void update(float delta) {
        availableSlavesBar .fillPercent.setValue(resources.getSlaveAmount(ResourceManager.Resources.SLAVES) / (float) resources.getTotalSlaves());
        buildingSlavesBar  .fillPercent.setValue(resources.getSlaveAmount(ResourceManager.Resources.BUILD)  / (float) resources.getResourceInfo(ResourceManager.Resources.BUILD).maxSlaves);
        quarryingSlavesBar .fillPercent.setValue(resources.getSlaveAmount(ResourceManager.Resources.STONE)  / (float) resources.getResourceInfo(ResourceManager.Resources.STONE).maxSlaves);
        farmingSlavesBar   .fillPercent.setValue(resources.getSlaveAmount(ResourceManager.Resources.FOOD)   / (float) resources.getResourceInfo(ResourceManager.Resources.FOOD).maxSlaves);
        choppingSlavesBar  .fillPercent.setValue(resources.getSlaveAmount(ResourceManager.Resources.WOOD)   / (float) resources.getResourceInfo(ResourceManager.Resources.WOOD).maxSlaves);
    }

    @Override
    public void render(SpriteBatch batch) {
        float x,y;

        Assets.font.setColor(Color.RED);
        glyphLayout.setText(Assets.font, "Slave Job Management");
        x = bounds.x + bounds.width / 2f - glyphLayout.width / 2f;
        y = bounds.y + bounds.height - glyphLayout.height;
        Assets.font.draw(batch, "Slave Job Management", x, y);

        Assets.font.setColor(Color.BLACK);
        glyphLayout.setText(Assets.font, "Available Slaves:");
        x = MathUtils.floor(availableSlavesBar.bounds.x - glyphLayout.width - widgetPadding);
        y = availableSlavesBar.bounds.y + availableSlavesBar.bounds.height / 2f + glyphLayout.height / 2f;
        Assets.font.draw(batch, "Available Slaves:", x, y);
//        availableSlavesBar.bounds.x = x + glyphLayout.width + widgetPadding;
        availableSlavesBar.render(batch);
        // TODO: draw -/+ buttons

        glyphLayout.setText(Assets.font, "Build:");
//        x = MathUtils.floor(bounds.x + (bounds.width / 2f) - (glyphLayout.width + barWidth) / 2f);
        x = MathUtils.floor(buildingSlavesBar.bounds.x - glyphLayout.width - widgetPadding);
        y = buildingSlavesBar.bounds.y + buildingSlavesBar.bounds.height / 2f + glyphLayout.height / 2f;
        Assets.font.draw(batch, "Build:", x, y);
//        buildingSlavesBar.bounds.x = x + glyphLayout.width + widgetPadding;
        buildingSlavesBar.render(batch);
        // TODO: draw -/+ buttons

        glyphLayout.setText(Assets.font, "Quarry:");
//        x = MathUtils.floor(bounds.x + (bounds.width / 2f) - (glyphLayout.width + barWidth) / 2f);
        x = MathUtils.floor(quarryingSlavesBar.bounds.x - glyphLayout.width - widgetPadding);
        y = quarryingSlavesBar.bounds.y + quarryingSlavesBar.bounds.height / 2f + glyphLayout.height / 2f;
        Assets.font.draw(batch, "Quarry:", x, y);
//        quarryingSlavesBar.bounds.x = x + glyphLayout.width + widgetPadding;
        quarryingSlavesBar.render(batch);
        // TODO: draw -/+ buttons

        glyphLayout.setText(Assets.font, "Farm:");
//        x = MathUtils.floor(bounds.x + (bounds.width / 2f) - (glyphLayout.width + barWidth) / 2f);
        x = MathUtils.floor(farmingSlavesBar.bounds.x - glyphLayout.width - widgetPadding);
        y = farmingSlavesBar.bounds.y + farmingSlavesBar.bounds.height / 2f + glyphLayout.height / 2f;
        Assets.font.draw(batch, "Farm:", x, y);
//        farmingSlavesBar.bounds.x = x + glyphLayout.width + widgetPadding;
        farmingSlavesBar.render(batch);
        // TODO: draw -/+ buttons

        glyphLayout.setText(Assets.font, "Chop:");
//        x = MathUtils.floor(bounds.x + (bounds.width / 2f) - (glyphLayout.width + barWidth) / 2f);
        x = MathUtils.floor(choppingSlavesBar.bounds.x - glyphLayout.width - widgetPadding);
        y = choppingSlavesBar.bounds.y + choppingSlavesBar.bounds.height / 2f + glyphLayout.height / 2f;
        Assets.font.draw(batch, "Chop:", x, y);
//        choppingSlavesBar.bounds.x = x + glyphLayout.width + widgetPadding;
        choppingSlavesBar.render(batch);
        // TODO: draw -/+ buttons

        Assets.font.setColor(Color.WHITE);
        batch.setColor(Color.WHITE);
    }
}
