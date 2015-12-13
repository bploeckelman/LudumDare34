package lando.systems.ld34.uielements;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld34.LudumDare34;
import lando.systems.ld34.world.Area;

/**
 * Created by Brian on 12/12/2015.
 */
public class PyramidButton extends AreaButton {

    public Rectangle Boundary;

    public PyramidButton(Rectangle boundary) {
        super("Pyramid", Area.Type.PYRAMID);

        Boundary = boundary;
        Bounds = new Rectangle(0, 0, 50, 0);
    }

    @Override
    public void update(Vector3 mousePos, boolean clicked) {
        super.update(mousePos, clicked);
        setBounds(LudumDare34.GameScreen.ResourceManager.getPyramidHeight());
    }

    private void setBounds(int pyramidHeight) {
        Bounds.height = pyramidHeight * 10;
        Bounds.x = (int)(Boundary.x + (Boundary.width - Bounds.width) / 2);
        Bounds.y = (int)(Boundary.y + (Boundary.height - Bounds.height) / 2);
    }
}
