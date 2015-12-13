package lando.systems.ld34.uielements;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.world.Area;

/**
 * Created by Brian on 12/12/2015.
 */
public class AreaButton extends NavigationButton {

    public Area.Type AreaLocation;

    public static AreaButton SelectedButton;

    public AreaButton(String text, Area.Type area) {
        super(text);
        AreaLocation = area;
        Bounds = new Rectangle(0, 0, 50, 50);
    }

    @Override
    public void update(Vector3 mousePos, boolean clicked) {
        super.update(mousePos, clicked);
        Selected = (SelectedButton == this);
    }

    @Override
    public void click() {
        SelectedButton = this;
        LudumDare34.GameScreen.TransitionToArea(AreaLocation);
    }
}
