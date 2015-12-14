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
    private int vTop;
    private int height;

    public Background(){
        xOffset = new MutableFloat(0);
        float aspect = (float)Config.width/Config.height;
        float scale = Config.width / (Assets.background.getWidth()/8f);

        height = (int)(Config.height/scale);
        vTop = Assets.background.getHeight() - height;
    }

    public void render (SpriteBatch batch){
        batch.draw(Assets.background, 0, 0, Config.width, Config.height, xOffset.intValue(), vTop, Assets.background.getWidth()/8, height, false, false);
    }
}
