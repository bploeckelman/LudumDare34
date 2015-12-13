package lando.systems.ld34.world;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld34.screens.GameScreen;
import lando.systems.ld34.utils.Assets;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */
public class AreaPyramid extends Area {

    GlyphLayout glyphLayout;

    public AreaPyramid(GameScreen gameScreen) {
        super(gameScreen, Type.PYRAMID);
        worldX = 5;
        glyphLayout = new GlyphLayout(Assets.font, "Pyramid Area");
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(244f / 255f, 164f / 255f, 96f / 255f, 1f);
        //batch.draw(Assets.whiteTexture, 0, 0, gameScreen.camera.viewportWidth, gameScreen.camera.viewportHeight);

        Assets.font.setColor(0f, 0f, 0f, 1f);
        Assets.font.draw(batch,
                         "Pyramid Area",
                         gameScreen.camera.viewportWidth  / 2f - glyphLayout.width  / 2f,
                         gameScreen.camera.viewportHeight / 2f - glyphLayout.height / 2f);
        Assets.font.setColor(1f, 1f, 1f, 1f);

        batch.setColor(1f, 1f, 1f, 1f);
    }
}
