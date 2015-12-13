package lando.systems.ld34.uielements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld34.utils.Assets;

/**
 * Created by Brian on 12/12/2015.
 */
public abstract class NavigationButton {

    public Rectangle Bounds;
    public Texture Image;
    public String Text;
    private GlyphLayout _glyphLayout;

    public boolean Highlighted;
    public boolean Selected;

    public float SelectionThickness = 3f;

    public NavigationButton(String text) {
        Text = text;

        Bounds = new Rectangle(0, 0, 50, 50);
        _glyphLayout = new GlyphLayout(Assets.font, text);
    }

    public void update(Vector3 mousePos, boolean clicked) {
        Highlighted = Bounds.contains(mousePos.x, mousePos.y);

        if (Highlighted && !Selected && clicked) {
            click();
        }
    }

    public void render(SpriteBatch batch) {
        boolean highlight = Highlighted || Selected;

        batch.setColor(highlight ? Color.GOLD : Color.YELLOW);

        batch.draw(Assets.whiteTexture, Bounds.x, Bounds.y, Bounds.width, Bounds.height);

        if (Image != null) {
            batch.draw(Image, Bounds.x, Bounds.y, Bounds.width, Bounds.height);
        } else {
            Assets.font.setColor(Color.BLACK);
            Assets.font.draw(batch,
                    Text,
                    Bounds.x + (Bounds.width - _glyphLayout.width)/2,
                    Bounds.y + (Bounds.height + _glyphLayout.height)/2);
        }

        if (Selected) {
           renderSelected(batch);
        }

        Assets.font.setColor(Color.WHITE);
        batch.setColor(Color.WHITE);
    }

    protected void renderSelected(SpriteBatch batch) {
        batch.setColor(Color.RED);
        float offset = SelectionThickness + 1;
        Assets.boxNinePatch.draw(batch, Bounds.x - offset, Bounds.y - offset,
                Bounds.width + (offset * 2), Bounds.height + (offset * 2));
    }

    public abstract void click();
}
