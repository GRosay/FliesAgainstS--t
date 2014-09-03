package ch.gaspardrosay.spaceattack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.logging.Logger;

public class MainSpaceAttack extends Game{
    public SpriteBatch batch;
    public BitmapFont font;
    public FreeTypeFontGenerator generator;
    public FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    public Texture imgBackground;
    public static IGoogleServices googleServices;
    public class TouchInfo {
        public float touchX = 0;
        public float touchY = 0;
        public boolean touched = false;
    }

    public MainSpaceAttack(IGoogleServices googleServices){
        super();
        MainSpaceAttack.googleServices = googleServices;
    }

    public void create(){
        batch = new SpriteBatch();

        imgBackground = new Texture(Gdx.files.internal("background-toilet.png"));

        generator = new FreeTypeFontGenerator(Gdx.files.internal("font.TTF"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 72;
        font = generator.generateFont(parameter);
        font.setColor(Color.RED);
        switch (Gdx.app.getType()) {
            case iOS:
                // HTML5 specific code
                this.setScreen(new MainMenuScreen(this, googleServices));

                break;
            default:
                // Other platforms specific code
                this.setScreen(new AndroidSplashScreen(this, googleServices));

                break;
        }
    }

    public void render(){
        super.render();

    }

    public void dispose(){
        batch.dispose();
        font.dispose();
    }
}





