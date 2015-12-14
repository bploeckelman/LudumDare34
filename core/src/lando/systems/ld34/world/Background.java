package lando.systems.ld34.world;

import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.Config;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.screens.GameScreen;
import lando.systems.ld34.utils.Assets;

/**
 * Created by dsgraham on 12/12/15.
 */
public class Background {

    public int SandHeight = 90;

    public MutableFloat xOffset;
    private int vTop;
    private int height;
    private float scale;
    private Rectangle sunRect;
    private Rectangle moonRect;
    public Color eclipseColor;
    public Color darkColor;

    public Background(){
        xOffset = new MutableFloat(0);
        float aspect = (float)Config.width/Config.height;
        scale = Config.width / (Assets.background.getWidth()/8f);

        height = (int)(Config.height/scale);
        vTop = Assets.background.getHeight() - height;
        sunRect = new Rectangle(0, 320, 80, 80);
        moonRect = new Rectangle(-100, 320, 84, 84);
        eclipseColor = new Color(1,1,1,1);
        darkColor = new Color(.4f, .4f, .6f, 1);
    }

    public void render (SpriteBatch batch){
        float percentComplete = LudumDare34.GameScreen.gameTimer / GameScreen.gameLength;
        sunRect.x = (Config.width * 4) - (xOffset.floatValue() * scale) + 550;
        float moonWorldPos = MathUtils.lerp(10, (Config.width * 4) + 550, percentComplete);
        moonRect.x = moonWorldPos - (xOffset.floatValue() * scale);
        moonRect.y = 320 + (MathUtils.sinDeg(percentComplete * 270 - 90) * 100);


        Rectangle overlap = new Rectangle();
        Intersector.intersectRectangles(moonRect, sunRect, overlap);
        float eclipsePercent = overlap.area() / sunRect.area();
        eclipseColor.r = 1;
        eclipseColor.g = 1;
        eclipseColor.b = 1;
        eclipseColor.lerp(darkColor, eclipsePercent);

        batch.setColor(eclipseColor);

        batch.draw(Assets.background, 0, 0, Config.width, Config.height, xOffset.intValue(), vTop, Assets.background.getWidth()/8, height, false, false);

        batch.setColor(Color.WHITE);

        batch.draw(Assets.sunTexture, sunRect.x, sunRect.y, sunRect.width, sunRect.height);
        batch.draw(Assets.moonTexture, moonRect.x, moonRect.y, moonRect.width, moonRect.height);

    }
}
