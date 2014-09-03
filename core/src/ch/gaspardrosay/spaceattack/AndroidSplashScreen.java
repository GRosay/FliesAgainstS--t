package ch.gaspardrosay.spaceattack;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by rosay on 20.08.14.
 */
public class AndroidSplashScreen implements Screen {
    final MainSpaceAttack game;
    final IGoogleServices gameservices;
    OrthographicCamera camera;
    Texture imSplash;
    float fTime;

    public AndroidSplashScreen(final MainSpaceAttack gam, final IGoogleServices gameserv){
        game = gam;
        gameservices = gameserv;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 1280);
        imSplash = new Texture(Gdx.files.internal("splashscreen.png"));
        fTime = TimeUtils.nanoTime();
    }

    public void render (float delta){
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(imSplash, 1024/2-460/2, 1280/2-575/2);
        game.batch.end();

        if(TimeUtils.nanoTime() > fTime+ 3000000000f){
            game.setScreen(new MainMenuScreen(game,gameservices));
            dispose();
        }
        else{
            Gdx.app.log("Temps depuis le début", ""+ TimeUtils.nanoTime());
        }

    }

    /** @see com.badlogic.gdx.ApplicationListener#resize(int, int) */
    public void resize (int width, int height){

    }

    /** Called when this screen becomes the current screen for a {@link com.badlogic.gdx.Game}. */
    public void show (){

    }

    /** Called when this screen is no longer the current screen for a {@link com.badlogic.gdx.Game}. */
    public void hide (){

    }

    /** @see com.badlogic.gdx.ApplicationListener#pause() */
    public void pause (){

    }

    /** @see com.badlogic.gdx.ApplicationListener#resume() */
    public void resume (){

    }

    /** Called when this screen should release all resources. */
    public void dispose (){
        imSplash.dispose();
    }
}