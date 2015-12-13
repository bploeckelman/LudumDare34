package lando.systems.ld34.world;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld34.screens.GameScreen;
import lando.systems.ld34.utils.Assets;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */
public class AreaMgmt extends Area {

    GlyphLayout glyphLayout;

    public AreaMgmt(GameScreen gameScreen) {
        super(gameScreen, Type.MGMT);
        worldX = 4;
        glyphLayout = new GlyphLayout(Assets.font, "Management Area");
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {
        //batch.setColor(135f / 255f, 206f / 255f, 250f / 255f, 1f);
        //batch.draw(Assets.background, 0, 0, gameScreen.camera.viewportWidth, gameScreen.camera.viewportHeight);

        Assets.font.setColor(0f, 0f, 0f, 1f);
        Assets.font.draw(batch,
                         "Management Area",
                         gameScreen.camera.viewportWidth  / 2f - glyphLayout.width  / 2f,
                         gameScreen.camera.viewportHeight / 2f - glyphLayout.height / 2f);

        Assets.font.setColor(1f, 1f, 1f, 1f);

        batch.setColor(1f, 1f, 1f, 1f);
    }
}
