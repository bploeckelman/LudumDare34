package lando.systems.ld34.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld34.Config;
import lando.systems.ld34.uielements.NotificationWindow;

/**
 * Created by dsgraham on 12/13/15.
 */
public class HUDManager {

    Array<NotificationWindow> notifications;

    public HUDManager(){
        notifications = new Array<NotificationWindow>();

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

    public void render(SpriteBatch batch){
        for (NotificationWindow not : notifications){
            not.render(batch);
        }
    }
}
