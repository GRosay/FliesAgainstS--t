package ch.gaspardrosay.spaceattack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by rosay on 20.08.14.
 */
public class GameScreen implements Screen, InputProcessor {

    // Variables de la classe
    final MainSpaceAttack game;

    private Texture imgBottom;
    private Texture flyImage;
    private Texture flyRedImage;
    private Texture flyKillImage;
    private Texture backgroundScore;

    private Rectangle recBottom;
    private Rectangle recScore;

    private OrthographicCamera camera;

    private Array<Rectangle> flyRectangles;
    private Array<Rectangle> flyRedRectangles;
    private Array<Rectangle> flyKilleds;
    private Array<Integer> noChances;

    private long lastFlyAppear;
    private long timeBeforeVitesse;
    private long lastAugmentation;
    private long timeElapsed;
    private long timeAtStart;

    private int iFactor = 1;
    private int iRed;
    private int iBlue;
    private int iGreen;
    private int iNbPassee;
    private int iScore;

    private BitmapFont font;

    private double secondes;

    private Music flySound;
    private Music flySound2;

    private Sound sproutchSound;
    private Sound eatenSound;
    private Sound flushSound;

    private String strScore;

    private boolean pause;

    private Map<Integer,TouchInfo> touches = new HashMap<Integer,TouchInfo>();
    final IGoogleServices gameservices;


    class TouchInfo {
        public float touchX = 0;
        public float touchY = 0;
        public boolean touched = false;
    }

    // Classes d'initialisation
    public GameScreen (final MainSpaceAttack gam, final IGoogleServices gameserv) {
        this.game = gam;
        gameservices = gameserv;
        pause = false;
        timeBeforeVitesse = 1000000000L;
        lastAugmentation = 0;
        Gdx.app.log("Temps depuis le début", ""+ TimeUtils.nanoTime());
        timeElapsed = 0;
        iNbPassee = 0;
        lastFlyAppear = (long)timeAtStart + (long)3000000000f;
        timeAtStart = TimeUtils.nanoTime();


        recBottom = new Rectangle();
        recBottom.width = 461;
        recBottom.height = 141;
        recBottom.x = 1024 /2 - 461 /2;
        recBottom.y = 1280;

        imgBottom = new Texture(Gdx.files.internal("cacaframe1.png"));
        flyImage = new Texture(Gdx.files.internal("ennemy.png"));
        flyRedImage = new Texture(Gdx.files.internal("redennemy.png"));
        flyKillImage = new Texture(Gdx.files.internal("flykilled.png"));
        backgroundScore = new Texture(Gdx.files.internal("scoreBackground.png"));

        recScore = new Rectangle();
        recScore.width = 200;
        recScore.height = 80;
        recScore.x = 50;
        recScore.y = 1230 - recScore.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 1280);

        flyRectangles = new Array<Rectangle>();
        flyRedRectangles = new Array<Rectangle>();
        flyKilleds = new Array<Rectangle>();

        flySound = Gdx.audio.newMusic(Gdx.files.internal("flysound1.mp4"));
        flySound2 = Gdx.audio.newMusic(Gdx.files.internal("flysound2.mp4"));
        sproutchSound = Gdx.audio.newSound(Gdx.files.internal("sproutch.wav"));
        eatenSound = Gdx.audio.newSound(Gdx.files.internal("eaten.mp4"));
        flushSound = Gdx.audio.newSound(Gdx.files.internal("flush.mp4"));

        flySound.setLooping(true);
        flySound2.setLooping(true);

        noChances = new Array<Integer>();
        noChances.add(MathUtils.random(1, 100));

        iScore = 0;
        strScore = ""+iScore;
        Gdx.input.setInputProcessor(this);

        for(int i = 0; i < 5; i++){
            touches.put(i, new TouchInfo());
        }

    }

    public void show(){
        flySound.play();
        flySound2.play();
    }
    public void pause(){
        flySound.stop();
        flySound2.stop();
        pause = true;
        game.setScreen(new MainMenuScreen(game, gameservices));
        dispose();

    }
    public void resize (int width, int height){

    }

    /** Called when this screen is no longer the current screen for a {@link com.badlogic.gdx.Game}. */
    public void hide (){
        flySound.stop();
        flySound2.stop();
    }
    public void resume(){
        pause = false;
    }

    @Override
    public void render (float delta) {
        camera.update();
        if(pause == false) {
            game.batch.setProjectionMatrix(camera.combined);
            if (recBottom.y > 0) {
                recBottom.y -= 1500 * Gdx.graphics.getDeltaTime();
            }

            Gdx.gl.glClearColor(0 / 255f, 0 / 255f, 0 / 255f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if (iNbPassee < 3) {


                timeElapsed = TimeUtils.nanoTime() - timeAtStart;
                secondes = (double) timeElapsed / 1000000000.0;

                // Génération aléatoire des mouches
                if ((TimeUtils.nanoTime() - lastFlyAppear)  > (MathUtils.random(300000000f, 1000000000f) - ((iFactor*iFactor)*1000))){
                    int iRandom = MathUtils.random(1, 1000);
                    boolean bRed = false;
                    for (Integer i : noChances) {
                        if (i == iRandom) {
                            bRed = true;
                        }
                    }
                    if (bRed == true) {
                        spawnRedFly();
                    } else {
                        spawnFly();
                    }
                }

                // Augmentation de la vitesse de génération
                if (TimeUtils.nanoTime() - lastAugmentation > timeBeforeVitesse) {
                    lastAugmentation = TimeUtils.nanoTime();
                    iFactor+=3;
                    noChances.add(MathUtils.random(1, 1000));
                }

                //////////////////////////////////////////////////////
                //
                // On test si l'écran a été touché
                //
                for (int i = 0; i < 5; i++) { // 20 is max number of touch points
                    if (touches.get(i).touched) {
                        Vector3 touchPos = new Vector3(touches.get(i).touchX, touches.get(i).touchY, 0);
                        camera.unproject(touchPos);
                        Gdx.app.debug("Un doigt ici:", "x=>" + touchPos.x + " y=>" + touchPos.y);
                        fCheckKilled(touchPos.x, touchPos.y);
                    }
                }
                //////////////////////////////////////////////////////

                //////////////////////////////////////////////////////
                // On bouge chaque mouche (si touchée on supprime)
                Iterator<Rectangle> iterFly = flyRectangles.iterator();
                while (iterFly.hasNext()) {
                    Rectangle recFly = iterFly.next();
                    recFly.y -= (MathUtils.random(100, 300)) * Gdx.graphics.getDeltaTime();
                    recFly.x -= (MathUtils.random(-100, 100)) * Gdx.graphics.getDeltaTime();
                    if (recFly.x < 0 + recFly.getWidth()) {
                        recFly.x = recFly.getWidth();
                    }
                    if (recFly.x > 1024 - recFly.getWidth()) {
                        recFly.x = 1024 - recFly.getWidth();
                    }
                    if (recFly.y + recFly.height < 0) {
                        iterFly.remove();
                        eatenSound.play();
                        iNbPassee++;
                        imgBottom = new Texture(Gdx.files.internal("cacaframe" + (iNbPassee + 1) + ".png"));
                    }

                }
                Iterator<Rectangle> iterRedFly = flyRedRectangles.iterator();
                while (iterRedFly.hasNext()) {
                    Rectangle recFly = iterRedFly.next();
                    recFly.y -= (MathUtils.random(200, 400)) * Gdx.graphics.getDeltaTime();
                    recFly.x -= (MathUtils.random(-10, 10)) * Gdx.graphics.getDeltaTime();
                    if (recFly.x < 0 + recFly.getWidth()) {
                        recFly.x = recFly.getWidth();
                    }
                    if (recFly.x > 1024 - recFly.getWidth()) {
                        recFly.x = 1024 - recFly.getWidth();
                    }

                    if (recFly.y + recFly.height < 0) {
                        iterRedFly.remove();
                        eatenSound.play();
                        iNbPassee++;
                        imgBottom = new Texture(Gdx.files.internal("cacaframe" + (iNbPassee + 1) + ".png"));
                    }

                }
                //////////////////////////////////////////////////////
            } else {
                // On a perdu...
                imgBottom = new Texture(Gdx.files.internal("cacamort.png"));
                flushSound.play();
                game.setScreen(new GameOverScreen(game, gameservices, iScore));
                dispose();
            }

            // On prépare le score pour affichage
            strScore = "" + iScore;


            // On dessine les Sprites sur la Spritegame.batch
            game.batch.begin();
            game.batch.draw(game.imgBackground, 0, 0);
            game.batch.draw(backgroundScore, recScore.x, recScore.y);
            game.batch.draw(imgBottom, recBottom.x, recBottom.y);
            for (Rectangle recFly : flyKilleds) {
                game.batch.draw(flyKillImage, recFly.x, recFly.y);
            }
            for (Rectangle recFly : flyRectangles) {
                game.batch.draw(flyImage, recFly.x, recFly.y);
            }
            for (Rectangle recFly : flyRedRectangles) {
                game.batch.draw(flyRedImage, recFly.x, recFly.y);
            }
            game.font.setColor(Color.BLACK);
            game.font.draw(game.batch, "" + strScore, ((50 + (recScore.getWidth() / 2)) - game.font.getBounds(strScore).width / 2), (recScore.y + recScore.getHeight() / 2) + game.font.getBounds(strScore).height / 2);
            game.batch.end();
        }

    }

    @Override
    public void dispose(){
        flyImage.dispose();
        imgBottom.dispose();
        flyRedImage.dispose();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /*
        spawnFly()
        Génère une mouche à des coordonnées X aléatoires et Y fixe (haut de l'écran)

     */
    private void spawnFly(){
        Rectangle recFly = new Rectangle();
        recFly.x = MathUtils.random(0, 1024-125);
        recFly.y = 1280;
        recFly.width = 125;
        recFly.height = 66;
        flyRectangles.add(recFly);
        lastFlyAppear = TimeUtils.nanoTime();

    }
    private void spawnRedFly(){
        Rectangle recFly = new Rectangle();
        recFly.x = MathUtils.random(0, 1024-125);
        recFly.y = 1280;
        recFly.width = 125;
        recFly.height = 66;
        flyRedRectangles.add(recFly);
        lastFlyAppear = TimeUtils.nanoTime();

    }

    private void fCheckKilled(float x, float y){

        Iterator<Rectangle> temp = flyRectangles.iterator();
        while(temp.hasNext()){
            Rectangle recFly = temp.next();
            if (recFly.contains(x, y)) {
                temp.remove();
                recFly.height = 50;
                recFly.width = 50;
                iScore++;
                sproutchSound.play();
                flyKilleds.add(recFly);
            }
        }

        Iterator<Rectangle> temp2 = flyRedRectangles.iterator();
        while(temp2.hasNext()){
            Rectangle recFly = temp2.next();
            if (recFly.contains(x, y)) {
                temp2.remove();
                recFly.height = 66+2;
                recFly.width = 125;
                flyRectangles.add(recFly);
            }
        }

    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = screenY;
            touches.get(pointer).touched = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;
            touches.get(pointer).touched = false;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }

}