package lando.systems.ld34.uielements;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.utils.Assets;
import lando.systems.ld34.world.Area;

/**
 * Created by Brian on 12/12/2015.
 */
public abstract class NavigationButton {

    public Rectangle Bounds;
    public Texture Image;
    public String Text;
    private GlyphLayout _glyphLayout;

    public NavigationButton(String text) {
        Text = text;
        _glyphLayout = new GlyphLayout(Assets.font, text);
    }

    public void render(SpriteBatch batch) {
        batch.setColor(Color.GOLD);
        batch.draw(Assets.whiteTexture, Bounds.x, Bounds.y, Bounds.width, Bounds.height);

        if (Image != null) {
            batch.draw(Image, Bounds.x, Bounds.y, Bounds.width, Bounds.height);
        } else {
            Assets.font.setColor(Color.BLACK);
            Assets.font.draw(batch,
                    _glyphLayout,
                    Bounds.x + (Bounds.width - _glyphLayout.width)/2,
                    Bounds.y + (Bounds.height - _glyphLayout.height)/2);
        }

        Assets.font.setColor(Color.WHITE);
        batch.setColor(Color.WHITE);
    }

    public abstract void click();
}
