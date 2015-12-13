package lando.systems.ld34.uielements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.world.Manage;

/**
 * Created by Brian on 12/12/2015.
 */
public class ManagementButton extends NavigationButton {

    public static ManagementButton SelectedButton;

    public Manage.Type Screen;

    public ManagementButton(String text, Manage.Type screen, String tooltip) {
        super(text, null, tooltip);

        Screen = screen;
        Bounds.width = 100;
        Bounds.height = 25;

    }
    @Override
    public void update(Vector3 mousePos, boolean clicked) {
        super.update(mousePos, clicked);
        Selected = (SelectedButton == this);
    }

    @Override
    public void click() {
        SelectedButton = this;
        LudumDare34.GameScreen.ShowManagementScreen(Screen);
    }
}
