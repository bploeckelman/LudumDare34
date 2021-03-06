package lando.systems.ld34.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.screens.GameScreen;
import lando.systems.ld34.utils.Assets;

/**
 * Created by dsgraham on 12/14/15.
 */
public class AreaGameOver extends Area {

    Rectangle bounds;
    private int currentPage;
    private float inputDelay;

    public AreaGameOver(GameScreen gameScreen) {
        super(gameScreen, Type.PYRAMID);
        worldX = 4;

        float w = gameScreen.uiCamera.viewportWidth / 1.4f;
        float h = gameScreen.uiCamera.viewportHeight / 2.4f;
        float x = 10;
        float y = gameScreen.uiCamera.viewportHeight  -  (h + 10) ;
        bounds = new Rectangle(x, y, w, h);
        currentPage = 0;
        inputDelay = 3f;

    }

    @Override
    public void update(float delta) {
        inputDelay -= delta;
        if (inputDelay <= 0 && Gdx.input.justTouched()) {
            currentPage++;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(Color.WHITE);

        gameScreen.Pyramid.render(batch);
        batch.setColor(62f / 255, 42f / 255, 0, 1f);
        batch.draw(Assets.whiteTexture, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);
        Assets.niceNinePatch.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);

        if (currentPage == 0) {
            float currentY = bounds.y + bounds.height - 10;
            Assets.glyphLayout.setText(Assets.font, "Congratulations! You have completed the Pyramid.", Color.WHITE, bounds.width - 20, Align.center, true);
            Assets.font.draw(batch, Assets.glyphLayout, bounds.x + 10, currentY);
            currentY -= Assets.glyphLayout.height + 15;
            Assets.glyphLayout.setText(Assets.font, GameScreen.stats.getSlaveInfo(), Color.WHITE, bounds.width - 20, Align.left, true);
            Assets.font.draw(batch, Assets.glyphLayout, bounds.x + 10, currentY);

        } else {
            float currentY = bounds.y + bounds.height - 30;
            String credits = "Made for Ludum Dare 34 by:\n\nBrian Ploeckelman\nDoug Graham\nBrian Rossman\nIan McNamara\nLuke Bain\n\n[#999999FF] No pixels were harmed in the making of this game[]";
            Assets.glyphLayout.setText(Assets.font, credits, Color.WHITE, bounds.width - 20, Align.center, true);
            Assets.font.draw(batch, Assets.glyphLayout, bounds.x + 10, currentY);

        }

    }

}
