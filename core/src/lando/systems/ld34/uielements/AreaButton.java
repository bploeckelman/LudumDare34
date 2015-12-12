package lando.systems.ld34.uielements;

import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.world.Area;

/**
 * Created by Brian on 12/12/2015.
 */
public class AreaButton extends NavigationButton {

    public Area.Type AreaLocation;

    public AreaButton(String text, Area.Type area) {
        super(text);
        AreaLocation = area;
        Bounds = new Rectangle(0, 0, 100, 50);
    }

    @Override
    public void click() {
        LudumDare34.GameScreen.TransitionToArea(AreaLocation);
    }
}
