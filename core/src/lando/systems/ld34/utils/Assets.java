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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
    public static BitmapFont   fontSmall;
    public static BitmapFont   HUDFont;

    public static Texture testTexture;
    public static Texture whiteTexture;
    public static Texture boxTexture;
    public static Texture niceBoxTexture;
    public static Texture niceBox2Texture;
    public static Texture background;

    public static Texture managementIcon;
    public static Texture quarryIcon;
    public static Texture fieldIcon;
    public static Texture woodsIcon;
    public static Texture pyramidIcon;

    public static Texture upIconOn;
    public static Texture upIconOff;
    public static Texture downIconOn;
    public static Texture downIconOff;
    public static Texture plusIconOn;
    public static Texture plusIconOff;

    public static ArrayList<Texture> pryamidBlocks = new ArrayList<Texture>();

    public static NinePatch boxNinePatch;
    public static NinePatch niceNinePatch;
    public static NinePatch nice2NinePatch;

    public static ShaderProgram motivationBarShader;
    public static GlyphLayout glyphLayout;


    public static void load() {
        glyphLayout = new GlyphLayout();
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
        fontSmall = new BitmapFont();
        fontSmall.getData().setScale(0.95f);
        HUDFont = new BitmapFont();
        HUDFont.getData().setScale(1f);

        background = new Texture("background.png");
        testTexture = new Texture("badlogic.jpg");
        whiteTexture = new Texture("pixel.png");
        boxTexture = new Texture("box.png");
        niceBoxTexture = new Texture("ninepatch.png");
        niceBox2Texture = new Texture("ninepatch-bg.png");

        managementIcon = new Texture("management.png");
        quarryIcon = new Texture("quarry.png");
        fieldIcon = new Texture("food.png");
        woodsIcon = new Texture("woods.png");
        pyramidIcon = new Texture("pyramidIcon.png");

        pryamidBlocks.add(new Texture("pyramidBlock.png"));
        pryamidBlocks.add(new Texture("pyramidBlock2.png"));

        boxNinePatch = new NinePatch(boxTexture, 2, 2, 2, 2);
        niceNinePatch = new NinePatch(niceBoxTexture, 6, 6, 6, 6);
        nice2NinePatch = new NinePatch(niceBox2Texture, 6, 6, 6, 6);

        upIconOn = new Texture("up_orange.png");
        upIconOff = new Texture("up_white.png");
        downIconOn = new Texture("down_orange.png");
        downIconOff = new Texture("down_white.png");
        plusIconOn = new Texture("plus_orange.png");
        plusIconOff = new Texture("plus_white.png");

        motivationBarShader = compileShaderProgram(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/motivation_bar.frag"));
    }

    public static void dispose() {
        batch.dispose();
        font.dispose();
        fontSmall.dispose();
        testTexture.dispose();
        whiteTexture.dispose();
        boxTexture.dispose();
        niceBoxTexture.dispose();
        niceBox2Texture.dispose();

        managementIcon.dispose();
        quarryIcon.dispose();
        fieldIcon.dispose();
        woodsIcon.dispose();
        pyramidIcon.dispose();

        upIconOn.dispose();
        upIconOff.dispose();
        downIconOn.dispose();
        downIconOff.dispose();
        plusIconOn.dispose();
        plusIconOff.dispose();

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
