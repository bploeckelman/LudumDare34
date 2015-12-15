package lando.systems.ld34.uielements;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld34.Config;
import lando.systems.ld34.world.Area;
import lando.systems.ld34.world.Manage;

/**
 * Created by dsgraham on 12/14/15.
 */
public class TutorialInfo {
    public String text;
    public Area.Type area;
    public Rectangle hightlightBounds;
    public Vector2 pos;
    public int wrapWidth;
    public Manage.Type mgmtScreen;

    public TutorialInfo(String text, Area.Type area, Rectangle bounds){
        this(text, area, null, new Vector2(Config.width/2, Config.height/2), 250, bounds);
    }

    public TutorialInfo(String text, Area.Type area, Manage.Type mgmtScreen, Vector2 centerPos, int wrapWidth, Rectangle bounds){
        this.text = text + "\n\n[#999999xALPHAx]Click to Continue\nEscape to Cancel Tutorial[]";
        this.area = area;
        this.pos = centerPos;
        this.hightlightBounds = bounds;
        this.wrapWidth = wrapWidth;
        this.mgmtScreen = mgmtScreen;
    }

}
