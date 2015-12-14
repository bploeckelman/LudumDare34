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
public class AreaPyramid extends Area {

    GlyphLayout glyphLayout;
    private MotivationGame mg;


    public AreaPyramid(GameScreen gameScreen) {
        super(gameScreen, Type.PYRAMID);
        worldX = 4;

        glyphLayout = new GlyphLayout(Assets.font, "Pyramid Area");
        mg = new MotivationGame(
                LudumDare34.GameScreen.ResourceManager,
                ResourceManager.Resources.BUILD);
    }

    @Override
    public void update(float delta) {
        mg.update(delta);
    }

    @Override
    public void render(SpriteBatch batch) {
        gameScreen.Pyramid.render(batch);
        mg.render(batch);
    }
}
