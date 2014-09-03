package ch.gaspardrosay.spaceattack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


/**
 * Created by rosay on 21.08.14.
 */
public class GameOverScreen implements Screen {

    final MainSpaceAttack game;
    final IGoogleServices gameservices;
    OrthographicCamera camera;

    int fScore;
    int highscore;

    boolean bNewBest;

    String strScore;
    String strHighscore;

    Texture imgGameOver;
    Texture backgroundScore;
    Texture backgroundHighScore;
    Texture bottomFrame;

    Rectangle recGameOver;
    Rectangle recScore;
    Rectangle recHighScore;
    Rectangle recBottom;


    Vector3 touchPos;

    public GameOverScreen(final MainSpaceAttack gam, final IGoogleServices gameserv, int fScor){
        this.game = gam;
        gameservices = gameserv;
        fScore = fScor;
        bNewBest = false;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 1280);

        imgGameOver = new Texture(Gdx.files.internal("gameover.png"));
        backgroundHighScore = new Texture(Gdx.files.internal("highscore.png"));
        bottomFrame = new Texture(Gdx.files.internal("flieswin.png"));
        recGameOver = new Rectangle();
        recGameOver.width = 600;
        recGameOver.height = 250;
        recGameOver.x = 1024/2 - recGameOver.getWidth()/2;
        recGameOver.y = 1280 - 250 - recGameOver.getHeight();

        recScore = new Rectangle();
        recScore.width = 200;
        recScore.height = 80;
        recScore.x = 1024/2 - recScore.getWidth()/2;
        recScore.y = 1280/2 - recScore.getHeight()/2;

        recHighScore = new Rectangle();
        recHighScore.width = 200;
        recHighScore.height = 80;
        recHighScore.x = 1024/2 - recHighScore.getWidth()/2;
        recHighScore.y = 1280/2 - recHighScore.getHeight()/2 - recScore.getHeight() - 50;

        recBottom = new Rectangle();
        recBottom.width = 400;
        recBottom.height = 276;
        recBottom.x = 1024/2 - recBottom.getWidth()/2;
        recBottom.y = 0;

        //Read
        FileHandle file = Gdx.files.local("assets/highscore.txt");
        if(file.exists()){
            highscore = Integer.valueOf(file.readString());
        }
        //Write
        if(fScore > highscore){
            file.writeString(Integer.toString(fScore ),false);
            highscore = fScore;
            bNewBest = true;
            backgroundScore = new Texture(Gdx.files.internal("newhighscore.png"));
        }
        else{
            backgroundScore = new Texture(Gdx.files.internal("scoreBackground.png"));
        }

        strScore = ""+fScore;
        strHighscore = ""+highscore;
        gameservices.submitScore(fScore);
        if(fScore > 200){
            gameservices.getAchievement("CgkI6rG73c8XEAIQAg");
        }
        if(fScore > 300){
            gameservices.getAchievement("CgkI6rG73c8XEAIQBA");
        }
        if(fScore > 500){
            gameservices.getAchievement("CgkI6rG73c8XEAIQAw");
        }
    }

    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    public void render (float delta){
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        if(Gdx.input.isTouched()){
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

            camera.unproject(touchPos);

            game.setScreen(new MainMenuScreen(game, gameservices));
            dispose();
        }

        game.batch.begin();
        game.batch.draw(game.imgBackground, 0, 0);
        game.batch.draw(bottomFrame,recBottom.x, recBottom.y);
        game.batch.draw(backgroundScore, recScore.x, recScore.y);
        game.batch.draw(backgroundHighScore, recHighScore.x, recHighScore.y);
        game.batch.draw(imgGameOver, recGameOver.x, recGameOver.y);

        game.font.setColor(Color.BLACK);
        game.font.draw(game.batch, "" + strScore, ((recScore.x + recScore.getWidth() / 2) - game.font.getBounds(strScore).width / 2), (recScore.y + recScore.getHeight() / 2) + game.font.getBounds(strScore).height / 2);
        game.font.draw(game.batch, "" + strHighscore, ((recHighScore.x + recHighScore.getWidth() / 2) - game.font.getBounds(strHighscore).width / 2), (recHighScore.y + recHighScore.getHeight() / 2) + game.font.getBounds(strHighscore).height / 2);
        game.font.setColor(Color.WHITE);

        game.font.draw(game.batch, "Score: " , ((recScore.x) - game.font.getBounds("Score: ").width - 20),(recScore.y +recScore.getHeight()/2) + game.font.getBounds(strScore).height/2);
        game.font.draw(game.batch, "Best: " , ((recHighScore.x) - game.font.getBounds("Best: ").width - 20),(recHighScore.y +recHighScore.getHeight()/2) + game.font.getBounds(strHighscore).height/2);
        if(bNewBest == true){
            game.font.draw(game.batch, "NEW " , ((recHighScore.x + (recHighScore.getWidth() + 20)) ),(recHighScore.y +recHighScore.getHeight()/2) + game.font.getBounds(strHighscore).height/2);
        }

        game.batch.end();
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
        imgGameOver.dispose();
    }
}
