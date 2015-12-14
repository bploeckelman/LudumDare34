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
        this.rect = new Rectangle(Config.width - 200, targetY - 50, 199, 40);
    }

    public void update(float dt){
        ttl -= dt;
        float trueTarget = targetY - 50;
        if (rect.y != trueTarget){
            float dist = moveSpeed * dt;
            if (dist > trueTarget - rect.y){
                rect.y = trueTarget;
            } else {
                rect.y +=dist;
            }
        }
    }

    public void render(SpriteBatch batch, boolean hovered){
        float alpha = Utils.clamp(ttl, 0, 1);
        if (hovered) alpha = Math.min(alpha, .4f);
        Assets.nice2NinePatch.setColor(new Color(1,1,1,alpha));
        Assets.HUDFont.setColor(new Color(1,1,1,alpha));
//        Assets.glyphLayout.setText(Assets.HUDFont, text);
        Assets.glyphLayout.setText(Assets.HUDFont, text, Color.WHITE, 180, Align.center, true);

        Assets.nice2NinePatch.draw(batch, rect.x, rect.y, rect.width, rect.height);
//        Assets.HUDFont.draw(batch, text, rect.x + rect.width/2 - Assets.glyphLayout.width/2, rect.y + (rect.height+Assets.glyphLayout.height)/2 );
        Assets.HUDFont.draw(batch, Assets.glyphLayout, rect.x + 10, rect.y + (rect.height - 10));

        Assets.nice2NinePatch.setColor(Color.WHITE);
        Assets.HUDFont.setColor(Color.WHITE);
    }
}
