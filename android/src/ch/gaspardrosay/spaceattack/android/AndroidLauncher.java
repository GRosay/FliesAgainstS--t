package ch.gaspardrosay.spaceattack.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.zwsihxtacrai.AdController;

import ch.gaspardrosay.spaceattack.ActionResolver;
import ch.gaspardrosay.spaceattack.IGoogleServices;
import ch.gaspardrosay.spaceattack.MainSpaceAttack;


public class AndroidLauncher extends AndroidApplication implements IGoogleServices,ActionResolver {

    private GameHelper _gameHelper;
    private final static int REQUEST_CODE_UNUSED = 9002;

    public AdController ad;

    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        // Create the GameHelper.
        _gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        _gameHelper.enableDebugLog(false);

        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
        {
            @Override
            public void onSignInSucceeded()
            {
            }

            @Override
            public void onSignInFailed()
            {
            }
        };

        _gameHelper.setup(gameHelperListener);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
		initialize(new MainSpaceAttack(this), config);
        ad = new AdController(this.getContext(), "212563197");
        ad.loadAd();

    }

    @Override
    public void signIn()
    {
        try
        {
            runOnUiThread(new Runnable()
            {
                //@Override
                public void run()
                {
                    _gameHelper.beginUserInitiatedSignIn();
                }
            });
        }
        catch (Exception e)
        {
            Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void signOut()
    {
        try
        {
            runOnUiThread(new Runnable()
            {
                //@Override
                public void run()
                {
                    _gameHelper.signOut();
                }
            });
        }
        catch (Exception e)
        {
            Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void rateGame()
    {
// Replace the end of the URL with the package of your game
        String str ="https://play.google.com/store/apps/details?id=ch.gaspardrosay.spaceattack.android";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    }

    @Override
    public void submitScore(long score)
    {
        if (isSignedIn() == true)
        {
            Games.Leaderboards.submitScore(_gameHelper.getApiClient(), getString(R.string.leaderboard_id), score);
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(_gameHelper.getApiClient(), getString(R.string.leaderboard_id)), REQUEST_CODE_UNUSED);
        }
        else
        {
// Maybe sign in here then redirect to submitting score?
        }
    }

    @Override
    public void showScores()
    {
        if (isSignedIn() == true)
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(_gameHelper.getApiClient(), getString(R.string.leaderboard_id)), REQUEST_CODE_UNUSED);
        else
        {
// Maybe sign in here then redirect to showing scores?
        }
    }

    @Override
    public void getAchievement(String strAchievementKey){
        if (isSignedIn() == true)
        {
            Games.Achievements.unlock(_gameHelper.getApiClient(), strAchievementKey);
        }
        else
        {
// Maybe sign in here then redirect to submitting score?
        }
    }

    @Override
    public boolean isSignedIn()
    {
        return _gameHelper.isSignedIn();
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        _gameHelper.onStart(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        _gameHelper.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        _gameHelper.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void showAd() {
        this.runOnUiThread(new Runnable() {

            public void run() {
                ad.showElements();
            }

        });

    }


    @Override
    public void hideAd() {

        this.runOnUiThread(new Runnable() {

            public void run() {
                ad.hideElements();
            }

        });

    }
}
