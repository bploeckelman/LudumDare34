package lando.systems.ld34.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.utils.Assets;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */
public class ManagePharoah extends Manage {

    GlyphLayout glyphLayout;

    public ManagePharoah(Rectangle bounds) {
        super(Type.PHAROAH, bounds);
        glyphLayout = new GlyphLayout(Assets.font, "Pharoah Mood Management");
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {
        Assets.font.setColor(Color.RED);
        Assets.font.draw(batch,
                         "Pharoah Mood Management",
                         bounds.x + bounds.width / 2f - glyphLayout.width / 2f,
                         bounds.y + bounds.height - glyphLayout.height);
        Assets.font.setColor(Color.WHITE);
    }
}
