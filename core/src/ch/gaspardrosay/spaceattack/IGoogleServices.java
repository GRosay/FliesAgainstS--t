package ch.gaspardrosay.spaceattack;

/**
 * Created by rosay on 25.08.14.
 */
public interface IGoogleServices
{
    public void signIn();
    public void signOut();
    public void rateGame();
    public void submitScore(long score);
    public void showScores();
    public boolean isSignedIn();
    public void getAchievement(String strAchievement);
}