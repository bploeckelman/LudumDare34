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

    public ManagementButton(String text, Texture image, Manage.Type screen) {
        super(text,  image);

        Screen = screen;
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
