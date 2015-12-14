package lando.systems.ld34.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld34.Config;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.uielements.NotificationWindow;
import lando.systems.ld34.utils.Assets;

/**
 * Created by dsgraham on 12/13/15.
 */
public class HUDManager {

    Array<NotificationWindow> notifications;

    private String toolTipString;
    private String lastToolTipString;
    private float toolTipDelay;

    private static final float TooltipDelay = .5f;

    public HUDManager(){
        notifications = new Array<NotificationWindow>();
        tooltipRect = new Rectangle();
        lastToolTipString = null;

    }

    public void showTooltip(String msg){
        toolTipString = msg;
    }

    public void addNotification(String msg){
        float y = Config.height;
        if (notifications.size > 0){
            y = notifications.get(notifications.size-1).targetY - 50;
        }
        notifications.add(new NotificationWindow(null, msg, y));
    }

    public void update(float dt){
        NotificationWindow item;
        toolTipDelay -= dt;
        int len = notifications.size;
        for (int i = len; --i >= 0;) {
            item = notifications.get(i);
            item.targetY = Config.height - (i * 50);
            item.update(dt);

            if (item.ttl <= 0) {
                notifications.removeIndex(i);

            }
        }
    }

    private Rectangle tooltipRect;
    public void render(SpriteBatch batch){
        float mouseX = LudumDare34.GameScreen.mouseScreenPos.x;
        float mouseY = LudumDare34.GameScreen.mouseScreenPos.y;
        for (NotificationWindow not : notifications) {
            not.render(batch, not.rect.contains(mouseX, mouseY));
        }
        if (toolTipString != null && toolTipDelay <= 0){

            Assets.glyphLayout.setText(Assets.HUDFont, toolTipString, Color.WHITE, 100, Align.center, true);
            tooltipRect.width = 120;
            tooltipRect.height = Assets.glyphLayout.height + 20;
            if(mouseX < Config.width/2) {
                tooltipRect.x = mouseX + 10;
            } else {
                tooltipRect.x = mouseX - tooltipRect.width - 10;
            }
            if(mouseY < Config.height/2) {
                tooltipRect.y = mouseY + 10;
            } else {
                tooltipRect.y = mouseY - tooltipRect.height - 10;
            }

            Assets.nice2NinePatch.draw(batch, tooltipRect.x, tooltipRect.y, tooltipRect.width, tooltipRect.height);
            Assets.HUDFont.draw(batch, Assets.glyphLayout, tooltipRect.x + 10, tooltipRect.y + (tooltipRect.height - 10));

        }

        if (toolTipString == null || !toolTipString.equals(lastToolTipString)) {
            toolTipDelay = TooltipDelay;
        }

        lastToolTipString = toolTipString;
        toolTipString = null;
    }
}
