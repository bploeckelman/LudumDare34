package lando.systems.ld34.uielements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import lando.systems.ld34.Config;
import lando.systems.ld34.utils.Assets;
import lando.systems.ld34.utils.Utils;

/**
 * Created by dsgraham on 12/13/15.
 */
public class NotificationWindow {
    public float ttl;
    public Rectangle rect;
    public Texture icon;
    public String text;
    public float targetY;
    private float moveSpeed = 50f;

    public NotificationWindow(Texture icon, String text, float y){
        this.icon = icon;
        this.text = text;
        targetY = y;
        ttl = 5;
        Assets.glyphLayout.setText(Assets.HUDFont, text, Color.WHITE, 180, Align.center, true);
        this.rect = new Rectangle(1, targetY, 199, Assets.glyphLayout.height + 20);
    }

    public void update(float dt){
        ttl -= dt;
        float trueTarget = targetY;
        if (rect.y != trueTarget){
            float dist = moveSpeed * dt;
            if (dist > rect.y - trueTarget){
                rect.y = trueTarget;
            } else {
                rect.y -=dist;
            }
        }
    }

    public void render(SpriteBatch batch, boolean hovered){
        float alpha = Utils.clamp(ttl, 0, 1);
        if (hovered) alpha = Math.min(alpha, .4f);
        Color c = new Color(1,1,1,alpha);
        Assets.nice2NinePatch.setColor(c);
        Assets.HUDFont.setColor(c);
//        Assets.glyphLayout.setText(Assets.HUDFont, text);
        Assets.glyphLayout.setText(Assets.HUDFont, text, c, 180, Align.center, true);

        Assets.nice2NinePatch.draw(batch, rect.x, rect.y, rect.width, rect.height);
//        Assets.HUDFont.draw(batch, text, rect.x + rect.width/2 - Assets.glyphLayout.width/2, rect.y + (rect.height+Assets.glyphLayout.height)/2 );
        Assets.HUDFont.draw(batch, Assets.glyphLayout, rect.x + 10, rect.y + (rect.height - 10));

        Assets.nice2NinePatch.setColor(Color.WHITE);
        Assets.HUDFont.setColor(Color.WHITE);
    }
}
