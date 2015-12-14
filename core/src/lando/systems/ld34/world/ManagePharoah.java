package lando.systems.ld34.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.resources.ResourceManager;
import lando.systems.ld34.screens.GameScreen;
import lando.systems.ld34.uielements.ProgressBar;
import lando.systems.ld34.utils.Assets;
import lando.systems.ld34.utils.Utils;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */
public class ManagePharoah extends Manage {

    ResourceManager resources;
    GameScreen      game;
    ProgressBar     pharaohMoodBar;
    Color           color;

    public ManagePharoah(Rectangle bounds) {
        super(Type.PHAROAH, bounds);
        resources = LudumDare34.GameScreen.resourceManager;
        game = LudumDare34.GameScreen;

        pharaohMoodBar = new ProgressBar(Assets.nice2NinePatch);
        final Color boundsColor = new Color(160f / 255f, 82f / 255f, 45f / 255f, 1f);
        pharaohMoodBar.boundsColor = boundsColor;
        pharaohMoodBar.fillColor.set(0f, 0.5f, 0f, 1f);
        pharaohMoodBar.bounds.set(leftMargin - 30f,
                                  barTop + 25f - Assets.pharaohTexture.getHeight(),
                                  barWidth + 2.0f * (buttonSize + widgetPadding),
                                  barHeight);
        color = new Color();
    }

    @Override
    public void update(float delta) {
        pharaohMoodBar.fillPercent.setValue(game.currentAnger / 100f);
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

        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, "Anger:", x, y);

        final String availableText = MathUtils.floor(game.currentAnger) + "%";
        glyphLayout.setText(Assets.fontSmall, availableText);
        Assets.fontSmall.setColor(Color.WHITE);
        Assets.fontSmall.draw(batch, availableText,
                              pharaohMoodBar.bounds.x + pharaohMoodBar.bounds.width / 2f - glyphLayout.width / 2f,
                              pharaohMoodBar.bounds.y + pharaohMoodBar.bounds.height / 2f + glyphLayout.height / 2f);
    }
}
