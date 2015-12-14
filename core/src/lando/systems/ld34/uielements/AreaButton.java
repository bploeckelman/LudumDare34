package lando.systems.ld34.uielements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.screens.NavigationLayout;
import lando.systems.ld34.world.Area;

/**
 * Created by Brian on 12/12/2015.
 */
public class AreaButton extends NavigationButton {

    public Area.Type AreaLocation;

    public static AreaButton SelectedButton;

    public AreaButton(String text, Texture image, Area.Type area, String tooltip) {
        super(text, image, tooltip);
        AreaLocation = area;
    }

    @Override
    public void update(float delta, Vector3 mousePos, boolean clicked) {
        super.update(delta, mousePos, clicked);
        Selected = (SelectedButton == this);
    }

    @Override
    public void click() {
        SelectedButton = this;
        LudumDare34.GameScreen.TransitionToArea(AreaLocation);
        NavigationLayout.CurrentArea = AreaLocation;
    }
}
