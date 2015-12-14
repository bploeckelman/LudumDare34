package lando.systems.ld34.world;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.uielements.ProgressBar;
import lando.systems.ld34.utils.Assets;
import lando.systems.ld34.utils.Utils;

public abstract class Manage {

    public enum Type { WORKERS, SLAVES, PHAROAH, UPGRADES, RESOURCES }

    protected static float barWidth;
    protected static float barHeight;
    protected static float lineHeight;
    protected static float lineSpacing;
    protected static float widgetPadding;
    protected static float buttonSize;
    protected static float barTop;
    protected static float leftMargin;

    protected final Type        type;
    protected       GlyphLayout glyphLayout;
    public          Rectangle   bounds;
    private         Color       color;

    public Manage(Type type, Rectangle bounds) {
        this.type = type;
        this.bounds = bounds;
        this.glyphLayout = new GlyphLayout();
        this.color = new Color();
        Manage.barWidth      = bounds.width / 3f;
        Manage.barHeight     = 20f;
        Manage.lineHeight    = 20f;
        Manage.lineSpacing   = 10f;
        Manage.widgetPadding = 10f;
        Manage.buttonSize    = 20f;
        Manage.barTop        = bounds.y + bounds.height - 100f;
        Manage.leftMargin    = bounds.x + bounds.width - barWidth - 2f * (buttonSize + widgetPadding) - 20f;
    }

    public abstract void update(float delta);

    public abstract void render(SpriteBatch batch);

    protected void drawHeading(SpriteBatch batch, String headingText) {
        glyphLayout.setText(Assets.font, headingText);
        final float x = bounds.x + bounds.width / 2f - glyphLayout.width / 2f;
        final float y = bounds.y + bounds.height - glyphLayout.height - 10f;
        final float marginx = 30f;
        final float marginy = 15f;

        Assets.batch.setColor(160f / 255f, 82f / 255f, 45f / 255f, 1f);
        Assets.nice2NinePatch.draw(batch,
                                   x - marginx,
                                   y - marginy - glyphLayout.height,
                                   glyphLayout.width  + 2f * marginx,
                                   glyphLayout.height + 2f * marginy);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, headingText, x, y);

        Assets.batch.setColor(Color.WHITE);
    }

    protected void drawAvailableRow(SpriteBatch batch, String labelText, ProgressBar availableBar, int numAvailable, int numTotal) {
        glyphLayout.setText(Assets.font, labelText);
        final float x = MathUtils.floor(availableBar.bounds.x - glyphLayout.width - widgetPadding);
        final float y = availableBar.bounds.y + availableBar.bounds.height / 2f + glyphLayout.height / 2f;
        final float n = availableBar.fillPercent.floatValue();

        Utils.hsvToRgb(n * 120f / 365f, 1.0f, 1.0f, color);
        availableBar.fillColor.set(color);
        availableBar.render(batch);

//        if (numTotal >= 0) Assets.font.setColor(1f - n, n, 0, 1f);
//        else               Assets.font.setColor(Color.WHITE);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, labelText, x, y);

        final String availableText = numAvailable + (numTotal >= 0 ? "/" + numTotal : "");
        glyphLayout.setText(Assets.fontSmall, availableText);
        Assets.fontSmall.setColor(Color.WHITE);
        Assets.fontSmall.draw(batch, availableText,
                              availableBar.bounds.x + availableBar.bounds.width / 2f - glyphLayout.width / 2f,
                              availableBar.bounds.y + availableBar.bounds.height / 2f + glyphLayout.height / 2f);
    }

    protected void drawAddRemoveRow(SpriteBatch batch,
                                    String labelText,
                                    ProgressBar progressBar,
                                    Texture addTexture,
                                    Texture removeTexture,
                                    Rectangle addButton,
                                    Rectangle removeButton,
                                    int numItems,
                                    int numItemsMax) {
        glyphLayout.setText(Assets.font, labelText);
        final float x = MathUtils.floor(progressBar.bounds.x - glyphLayout.width - widgetPadding);
        final float y = progressBar.bounds.y + progressBar.bounds.height / 2f + glyphLayout.height / 2f;
        final float n = numItems / (float) numItemsMax;

        Utils.hsvToRgb(n * 120f / 365f, 1.0f, 1.0f, color);
        progressBar.fillColor.set(color);
        progressBar.render(batch);
        batch.draw(removeTexture, removeButton.x, removeButton.y, buttonSize, buttonSize);
        batch.draw(addTexture,    addButton.x,    addButton.y,    buttonSize, buttonSize);

        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, labelText, x, y);

        String buildingText = numItems + "/" + numItemsMax;
        glyphLayout.setText(Assets.fontSmall, buildingText);
        Assets.fontSmall.setColor(Color.WHITE);
        Assets.fontSmall.draw(batch, buildingText,
                              progressBar.bounds.x + progressBar.bounds.width / 2f - glyphLayout.width / 2f,
                              progressBar.bounds.y + progressBar.bounds.height / 2f + glyphLayout.height / 2f);
    }

    protected void drawUpgrade1ColRow(SpriteBatch batch,
                                      String labelText,
                                      ProgressBar progressBar,
                                      Texture upgradeTexture,
                                      Rectangle upgradeButton,
                                      int availableItems,
                                      int availableItemsMax) {
        glyphLayout.setText(Assets.font, labelText);
        final float x = MathUtils.floor(progressBar.bounds.x - glyphLayout.width - widgetPadding);
        final float y = progressBar.bounds.y + progressBar.bounds.height / 2f + glyphLayout.height / 2f;
        final float n = availableItems / (float) availableItemsMax;

        Utils.hsvToRgb(n * 120f / 365f, 1.0f, 1.0f, color);
        progressBar.fillColor.set(color);
        progressBar.render(batch);
        batch.draw(upgradeTexture, upgradeButton.x, upgradeButton.y, buttonSize, buttonSize);

//        Assets.font.setColor(1f - n, n, 0f, 1f);
//        if (availableItemsMax >= 0) Assets.font.setColor(1f - n, n, 0, 1f);
//        else                        Assets.font.setColor(Color.WHITE);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, labelText, x, y);

        final String itemText = availableItems + (availableItemsMax >= 0 ? "/" + availableItemsMax : "");
        glyphLayout.setText(Assets.fontSmall, itemText);
        Assets.fontSmall.draw(batch, itemText,
                              progressBar.bounds.x + progressBar.bounds.width / 2f - glyphLayout.width / 2f,
                              progressBar.bounds.y + progressBar.bounds.height / 2f + glyphLayout.height / 2f);
    }

    protected void drawUpgrade2ColRow(SpriteBatch batch,
                                      String labelText,
                                      int resourceLevel,
                                      ProgressBar progressBar,
                                      ProgressBar slavesProgressBar,
                                      Texture upgradeTexture,
                                      Rectangle upgradeButton,
                                      int availableItems,
                                      int availableItemsMax,
                                      int availableSlaves,
                                      int availableSlavesMax) {
        glyphLayout.setText(Assets.font, labelText + " (level " + resourceLevel + "):");
        final float x = MathUtils.floor(progressBar.bounds.x - glyphLayout.width - widgetPadding);
        final float y = progressBar.bounds.y + progressBar.bounds.height / 2f + glyphLayout.height / 2f;
        final float n1 = availableItems / (float) availableItemsMax;

        Utils.hsvToRgb(n1 * 120f / 365f, 1.0f, 1.0f, color);
        progressBar.fillColor.set(color);
        progressBar.render(batch);

        final float n2 = availableSlaves / (float) availableSlavesMax;
        Utils.hsvToRgb(n2 * 120f / 365f, 1.0f, 1.0f, color);
        slavesProgressBar.fillColor.set(color);
        slavesProgressBar.render(batch);

        batch.draw(upgradeTexture, upgradeButton.x, upgradeButton.y, buttonSize, buttonSize);

        //        Assets.font.setColor(1f - n, n, 0f, 1f);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, labelText + " (level " + resourceLevel + "):", x, y);

        final String itemText = availableItems + "/" + availableItemsMax;
        glyphLayout.setText(Assets.fontSmall, itemText);
        Assets.fontSmall.draw(batch, itemText,
                              progressBar.bounds.x + progressBar.bounds.width / 2f - glyphLayout.width / 2f,
                              progressBar.bounds.y + progressBar.bounds.height / 2f + glyphLayout.height / 2f);

        final String slavesText = availableSlaves + "/" + availableSlavesMax;
        glyphLayout.setText(Assets.fontSmall, slavesText);
        Assets.fontSmall.draw(batch, slavesText,
                              slavesProgressBar.bounds.x + slavesProgressBar.bounds.width / 2f - glyphLayout.width / 2f,
                              slavesProgressBar.bounds.y + slavesProgressBar.bounds.height / 2f + glyphLayout.height / 2f);
    }

    protected void drawAddWorkerRow(SpriteBatch batch,
                                    String rowLabel,
                                    ProgressBar progressBar,
                                    Texture addTexture,
                                    Rectangle addButton,
                                    int numWorkers,
                                    int nextWorkerCost) {
        glyphLayout.setText(Assets.font, rowLabel);
        final float x = MathUtils.floor(progressBar.bounds.x - glyphLayout.width - widgetPadding);
        final float y = progressBar.bounds.y + progressBar.bounds.height / 2f + glyphLayout.height / 2f;

        progressBar.render(batch);
        batch.draw(addTexture, addButton.x, addButton.y, buttonSize, buttonSize);

        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, rowLabel, x, y);
        final String buildText = "" + numWorkers;
        glyphLayout.setText(Assets.fontSmall, buildText);
        Assets.fontSmall.draw(batch, buildText,
                              progressBar.bounds.x + progressBar.bounds.width / 2f - glyphLayout.width / 2f,
                              progressBar.bounds.y + progressBar.bounds.height / 2f + glyphLayout.height / 2f);

        final String nextCostLabel =  "$" + nextWorkerCost;
        glyphLayout.setText(Assets.font, nextCostLabel);
        Assets.font.draw(batch, nextCostLabel, addButton.x + addButton.width + widgetPadding, addButton.y + addButton.height / 2f + glyphLayout.height / 2f);
    }

}
