package lando.systems.ld34.uielements;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.utils.Assets;

/**
 * Brian Ploeckelman created on 12/12/2015.
 */
public class ProgressBar {

    private final NinePatch    ninePatch;
    public        Rectangle    bounds;
    public        MutableFloat fillPercent;
    public        Color        boundsColor;
    public        Color        fillColor;

    public ProgressBar() {
        ninePatch = Assets.boxNinePatch;
        bounds = new Rectangle();
        fillPercent = new MutableFloat(0.0f);
        boundsColor = new Color(0f, 0f, 0f, 1f);
        fillColor = new Color(0f, 1f, 0f, 1f);
        Tween.to(fillPercent, -1, 3)
                .target(1f)
                .repeatYoyo(-1, 0)
                .start(Assets.tween);
    }

    public void render(SpriteBatch batch) {
        batch.setColor(fillColor);
        batch.draw(Assets.whiteTexture,
                   bounds.x + ninePatch.getLeftWidth(),
                   bounds.y + ninePatch.getBottomHeight(),
                   (bounds.width - ninePatch.getLeftWidth() - ninePatch.getRightWidth()) * fillPercent.floatValue(),
                   bounds.height - ninePatch.getTopHeight() - ninePatch.getBottomHeight());

        batch.setColor(boundsColor);
        ninePatch.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);

        batch.setColor(Color.WHITE);
    }

}