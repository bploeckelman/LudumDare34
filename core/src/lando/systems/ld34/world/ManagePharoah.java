package lando.systems.ld34.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.resources.ResourceManager;
import lando.systems.ld34.screens.GameScreen;
import lando.systems.ld34.uielements.ProgressBar;
import lando.systems.ld34.utils.Assets;
import lando.systems.ld34.utils.SoundManager;
import lando.systems.ld34.utils.Utils;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */
public class ManagePharoah extends Manage {

    private static final int minAngerForSacrifice = 5;

    ResourceManager resources;
    GameScreen      game;
    ProgressBar     pharaohMoodBar;
    ProgressBar     buttonBackgroundBar;
    Rectangle       sacrificeButton;
    Color           color;
    boolean         canSacrifice = false;

    public ManagePharoah(Rectangle bounds) {
        super(Type.PHAROAH, bounds);
        resources = LudumDare34.GameScreen.resourceManager;
        game = LudumDare34.GameScreen;

        pharaohMoodBar = new ProgressBar(Assets.nice2NinePatch);
        buttonBackgroundBar = new ProgressBar(Assets.nice2NinePatch);

        final Color boundsColor = new Color(160f / 255f, 82f / 255f, 45f / 255f, 1f);
        pharaohMoodBar.boundsColor = boundsColor;
        pharaohMoodBar.fillColor.set(0f, 0.5f, 0f, 1f);
        pharaohMoodBar.bounds.set(leftMargin - 30f,
                                  barTop + 25f - Assets.pharaohTexture.getHeight(),
                                  barWidth + 2.0f * (buttonSize + widgetPadding),
                                  barHeight);
        final float w = barWidth * 1.5f;
        final float h = barHeight * 1.25f;
        buttonBackgroundBar.bounds.set(pharaohMoodBar.bounds.x + pharaohMoodBar.bounds.width / 2f - w / 2f,
                                       pharaohMoodBar.bounds.y - h - widgetPadding,
                                       w, h);
        sacrificeButton = new Rectangle(buttonBackgroundBar.bounds);
        color = new Color();
    }

    @Override
    public void update(float delta) {
        pharaohMoodBar.fillPercent.setValue(game.currentAnger / 100f);

        canSacrifice = (resources.getResourceInfo(ResourceManager.Resources.SLAVES).slaves > 0)
                       && game.currentAnger > minAngerForSacrifice;

        final float x = Gdx.input.getX();
        final float y = LudumDare34.GameScreen.uiCamera.viewportHeight - Gdx.input.getY();
        if (sacrificeButton.contains(x,y)) {
            if (canSacrifice) game.hudManager.showTooltip("Sacrifice a slave to reduce Pharaoh's anger");
            else              game.hudManager.showTooltip("Must have 1 slave available to sacrifice");
        }

        if (!Gdx.input.justTouched()) {
            return;
        }

        if (sacrificeButton.contains(x,y) && canSacrifice) {
            resources.getResourceInfo(ResourceManager.Resources.SLAVES).slaves--;
            game.currentAnger = 0;
            game.addNotification("Pharaoh has been appeased,\nfor now...");
            SoundManager.chosenWisely.play(0.2f);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        final float n = 1f - pharaohMoodBar.fillPercent.floatValue();
        Utils.hsvToRgb(n * 120f / 365f, 1.0f, 1.0f, color);

        drawHeading(batch, "Pharaoh Mood Management");
        float x = bounds.x + bounds.getWidth() / 2f - Assets.pharaohTexture.getWidth() / 2f + 12f;
        float y = bounds.y + bounds.getHeight() / 2f - Assets.pharaohTexture.getHeight() / 2f + 75f;
        batch.setColor(color);
        batch.draw(Assets.pharaohTexture, x, y);
        batch.setColor(Color.WHITE);

        glyphLayout.setText(Assets.font, "Anger:");
        x = MathUtils.floor(pharaohMoodBar.bounds.x - glyphLayout.width - widgetPadding);
        y = pharaohMoodBar.bounds.y + pharaohMoodBar.bounds.height / 2f + glyphLayout.height / 2f;// - Assets.pharaohTexture.getHeight() - widgetPadding;
        pharaohMoodBar.fillColor.set(color);
        pharaohMoodBar.render(batch);
        if (canSacrifice) {
            batch.setColor(1f, 1f, 1f, 1f);
            buttonBackgroundBar.boundsColor.set(1f, 1f, 1f, 1f);
        } else {
            batch.setColor(0.5f, 0.5f, 0.5f, 1f);
            buttonBackgroundBar.boundsColor.set(0.5f, 0.5f, 0.5f, 1f);
        }
        buttonBackgroundBar.render(batch);
        batch.setColor(1f, 1f, 1f, 1f);
        buttonBackgroundBar.boundsColor.set(1f, 1f, 1f, 1f);

        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, "Anger:", x, y);

        final String availableText = MathUtils.floor(game.currentAnger) + "%";
        glyphLayout.setText(Assets.fontSmall, availableText);
        Assets.fontSmall.setColor(Color.WHITE);
        Assets.fontSmall.draw(batch, availableText,
                              pharaohMoodBar.bounds.x + pharaohMoodBar.bounds.width / 2f - glyphLayout.width / 2f,
                              pharaohMoodBar.bounds.y + pharaohMoodBar.bounds.height / 2f + glyphLayout.height / 2f);

        if (canSacrifice) {
            Assets.font.setColor(1f, 1f, 1f, 1f);
        } else {
            Assets.font.setColor(0.5f, 0.5f, 0.5f, 1f);
        }
        final String sacrificeText = "Sacrifice a Slave";
        glyphLayout.setText(Assets.font, sacrificeText);
        Assets.font.draw(batch, sacrificeText,
                         sacrificeButton.x + sacrificeButton.width / 2f - glyphLayout.width / 2f,
                         sacrificeButton.y + sacrificeButton.height  / 2f + glyphLayout.height / 2f);
    }
}
