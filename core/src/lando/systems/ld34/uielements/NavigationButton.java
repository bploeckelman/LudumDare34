package lando.systems.ld34.uielements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.utils.Assets;

/**
 * Created by Brian on 12/12/2015.
 */
public abstract class NavigationButton {

    public Rectangle Bounds;
    public Texture Image;
    public String Text;
    public String ToolTipText;
    private GlyphLayout _glyphLayout;

    public boolean Highlighted;
    public boolean Selected;

    public float SelectionThickness = 3f;

    public float efficiencyLevel = 1;
    private double _alertTime = 0;
    private Color _alertColor;

    public NavigationButton(String text, Texture image, String tooltipText) {
        Text = text;
        Image = image;
        ToolTipText = tooltipText;

        Bounds = new Rectangle(0, 0, 30, 30);
        _glyphLayout = new GlyphLayout(Assets.font, text);
    }

    public void update(float delta, Vector3 mousePos, boolean clicked) {
        Highlighted = Bounds.contains(mousePos.x, mousePos.y);
        if (Highlighted){
            LudumDare34.GameScreen.hudManager.showTooltip(ToolTipText);
        }

        float warningLevel = 0.4f;

        if (efficiencyLevel > warningLevel) {
            _alertTime = 0f;
        } else {
            _alertTime += delta;

            Color red = new Color(1f, 0f, 0f, 1f);
            _alertColor = new Color(1f, 1f, 0f, 1f);

            float level = (efficiencyLevel / warningLevel);

            _alertColor.lerp(red, (1 - level));

            float flashSpeed = 4 + ((1 - level) *4);

            float flash =(float) ((Math.sin(flashSpeed * _alertTime) + 1) /2);
            _alertColor.lerp(ImageColor, flash);
        }

        if (Highlighted && !Selected && clicked) {
            click();
        }
    }

    protected Color ImageColor = new Color(1, 1, 1, 1);

    public void render(SpriteBatch batch) {
        boolean highlight = Highlighted || Selected;

        if (highlight) {
            batch.setColor(175f/255f, 238f/255f, 238f/255f, 0.4f);
            batch.draw(Assets.whiteTexture, Bounds.x, Bounds.y, Bounds.width, Bounds.height);
        }

        if (Image != null) {
            if (_alertTime > 0) {
                batch.setColor(_alertColor);
            } else {
                batch.setColor(ImageColor);
            }
            batch.draw(Image, Bounds.x, Bounds.y, Bounds.width, Bounds.height);
        } else {
            batch.setColor(highlight ? Color.YELLOW : Color.BLACK);
            if (Text.equals("Pharaoh") && LudumDare34.GameScreen.currentAnger > 70) {
                batch.setColor(Color.RED);
            }
            //batch.draw(Assets.whiteTexture, Bounds.x, Bounds.y, Bounds.width, Bounds.height);
            Assets.nice2NinePatch.draw(batch, Bounds.x, Bounds.y, Bounds.width, Bounds.height);

            Assets.font.setColor(highlight ? Color.YELLOW : Color.WHITE);

            Assets.font.draw(batch,
                    Text,
                    Bounds.x + (Bounds.width - _glyphLayout.width) / 2,
                    Bounds.y + (Bounds.height + _glyphLayout.height) / 2);
        }

//        if (Selected && (Image != null)) {
//           renderSelected(batch);
//        }

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
