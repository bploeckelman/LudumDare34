package lando.systems.ld34.utils;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lando.systems.ld34.utils.accessors.*;

import java.util.ArrayList;

/**
 * Brian Ploeckelman created on 9/10/2015.
 */
public class Assets {
    public static TweenManager tween;
    public static SpriteBatch  batch;
    public static BitmapFont   font;

    public static Texture testTexture;
    public static Texture whiteTexture;
    public static Texture boxTexture;
    public static Texture background;

    public static Texture managementIcon;
    public static Texture quarryIcon;
    public static Texture fieldIcon;
    public static Texture woodsIcon;

    public static Texture slavesIcon;
    public static Texture pharoahIcon;
    public static Texture workersIcon;
    public static Texture upgradesIcon;
    public static Texture resourcesIcon;


    public static ArrayList<Texture> pryamidBlocks = new ArrayList<Texture>();

    public static NinePatch boxNinePatch;


    public static void load() {
        if (tween == null) {
            tween = new TweenManager();
            Tween.setCombinedAttributesLimit(4);
            Tween.registerAccessor(Color.class, new ColorAccessor());
            Tween.registerAccessor(Rectangle.class, new RectangleAccessor());
            Tween.registerAccessor(Vector2.class, new Vector2Accessor());
            Tween.registerAccessor(Vector3.class, new Vector3Accessor());
            Tween.registerAccessor(OrthographicCamera.class, new CameraAccessor());
        }

        batch = new SpriteBatch();
        font = new BitmapFont();

        background = new Texture("background.png");
        testTexture = new Texture("badlogic.jpg");
        whiteTexture = new Texture("pixel.png");
        boxTexture = new Texture("box.png");

        managementIcon = new Texture("management.png");
        quarryIcon = new Texture("quarry.png");
        fieldIcon = new Texture("food.png");
        woodsIcon = new Texture("woods.png");

        slavesIcon = new Texture("slaves.png");
        workersIcon = new Texture("workers.png");
        pharoahIcon = new Texture("paroah.png");
        upgradesIcon = new Texture("upgrades.png");
        resourcesIcon = new Texture("resources.png");

        pryamidBlocks.add(new Texture("pyramidBlock.png"));
        pryamidBlocks.add(new Texture("pyramidBlock2.png"));

        boxNinePatch = new NinePatch(boxTexture, 2, 2, 2, 2);
    }

    public static void dispose() {
        batch.dispose();
        font.dispose();
        testTexture.dispose();
        whiteTexture.dispose();
        boxTexture.dispose();

        managementIcon.dispose();
        quarryIcon.dispose();
        fieldIcon.dispose();
        woodsIcon.dispose();

        slavesIcon.dispose();
        workersIcon.dispose();
        pharoahIcon.dispose();
        upgradesIcon.dispose();
        resourcesIcon.dispose();

        for (Texture block : pryamidBlocks) {
            block.dispose();
        }
    }

    private static ShaderProgram compileShaderProgram(FileHandle vertSource, FileHandle fragSource) {
        ShaderProgram.pedantic = false;
        final ShaderProgram shader = new ShaderProgram(vertSource, fragSource);
        if (!shader.isCompiled()) {
            throw new GdxRuntimeException("Failed to compile shader program:\n" + shader.getLog());
        }
        else if (shader.getLog().length() > 0) {
            Gdx.app.debug("SHADER", "ShaderProgram compilation log:\n" + shader.getLog());
        }
        return shader;
    }
}
