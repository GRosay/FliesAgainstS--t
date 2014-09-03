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
public class MainMenuScreen implements Screen {
    final MainSpaceAttack game;
    final IGoogleServices gameservices;


    OrthographicCamera camera;

    Texture imgPlay;
    Texture imgScore;
    Texture imgLogo;

    Rectangle recPlay;
    Rectangle recScore;
    Rectangle recLogo;

    Vector3 touchPos;

    Sound startSound;

    public MainMenuScreen(final MainSpaceAttack gam, final IGoogleServices gameserv){
        game = gam;
        gameservices = gameserv;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 1280);

        imgPlay = new Texture(Gdx.files.internal("playbutton.png"));
        imgScore = new Texture(Gdx.files.internal("scorebutton.png"));
        imgLogo = new Texture(Gdx.files.internal("logo.png"));

        recPlay = new Rectangle();
        recPlay.width = 300;
        recPlay.height = 100;
        recPlay.x = 1024/2 - recPlay.getWidth() /2;
        recPlay.y = 1280/2 - recPlay.getHeight() /2;

        recScore = new Rectangle();
        recScore.width = 300;
        recScore.height = 100;
        recScore.x = 1024/2 - recScore.getWidth() /2;
        recScore.y = 1280/2 - recScore.getHeight() /2 - recPlay.getHeight();

        recLogo = new Rectangle();
        recLogo.width = 600;
        recLogo.height = 250;
        recLogo.x = 1024/2 - recLogo.getWidth() / 2;
        recLogo.y = 1280 - (1.5f * recLogo.getHeight());

        startSound = Gdx.audio.newSound(Gdx.files.internal("startsound.wav"));
    }

    public void render (float delta){
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(game.imgBackground, 0, 0);
        game.batch.draw(imgPlay, recPlay.x, recPlay.y);
        game.batch.draw(imgScore, recScore.x, recScore.y);
        game.batch.draw(imgLogo, recLogo.x, recLogo.y);
        game.batch.end();

        // On test si l'user a touché le menu Start ou le menu Score
        if(Gdx.input.isTouched()){
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if(recPlay.contains(touchPos.x, touchPos.y)){
                startSound.play();

                game.setScreen(new GameScreen(game, gameservices));
                dispose();
            }
            if(recScore.contains(touchPos.x, touchPos.y)){
                gameservices.showScores();
            }

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
        imgPlay.dispose();
        imgScore.dispose();
        imgLogo.dispose();
    }
}