package lando.systems.ld34.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.screens.GameScreen;
import lando.systems.ld34.utils.Assets;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */
public class ManageSlaves extends Manage {

    GlyphLayout glyphLayout;

    public ManageSlaves(Rectangle bounds) {
        super(Type.SLAVES, bounds);
        glyphLayout = new GlyphLayout(Assets.font, "Slave Job Management");
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {
        Assets.font.setColor(Color.RED);
        Assets.font.draw(batch,
                         "Slave Job Management",
                         bounds.x + bounds.width / 2f - glyphLayout.width / 2f,
                         bounds.y + bounds.height - glyphLayout.height);
        Assets.font.setColor(Color.WHITE);
    }
}
