package lando.systems.ld34.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */
public class ManagePharoah extends Manage {


    public ManagePharoah(Rectangle bounds) {
        super(Type.PHAROAH, bounds);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {
        drawHeading(batch, "Pharaoh Mood Management");
    }
}
