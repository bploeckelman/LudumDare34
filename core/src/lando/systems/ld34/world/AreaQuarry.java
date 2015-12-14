package lando.systems.ld34.world;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.motivation.MotivationGame;
import lando.systems.ld34.resources.ResourceManager;
import lando.systems.ld34.screens.GameScreen;
import lando.systems.ld34.utils.Assets;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */
public class AreaQuarry extends Area {

    GlyphLayout glyphLayout;

    private MotivationGame mg;

    public AreaQuarry(GameScreen gameScreen) {
        super(gameScreen, Type.QUARRY);
        worldX = 2;
        glyphLayout = new GlyphLayout(Assets.font, "Quarry Area");
        mg = new MotivationGame(
                LudumDare34.GameScreen.resourceManager,
                ResourceManager.Resources.STONE);
    }

    @Override
    public void update(float delta) {
        mg.update(delta);
    }

    @Override
    public void render(SpriteBatch batch) {
        gameScreen.resourceManager.render(resourceType, batch);
        mg.render(batch);
    }

}
