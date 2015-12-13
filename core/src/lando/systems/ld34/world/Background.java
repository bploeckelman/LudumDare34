package lando.systems.ld34.world;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld34.Config;
import lando.systems.ld34.utils.Assets;

/**
 * Created by dsgraham on 12/12/15.
 */
public class Background {

    public int SandHeight = 90;

    public MutableFloat xOffset;
    public Background(){
        xOffset = new MutableFloat(96);
    }

    public void render (SpriteBatch batch){
        batch.draw(Assets.background, 0, 0, Config.width, Config.height, xOffset.intValue(), 0, 96, 128, false, false);
    }
}
