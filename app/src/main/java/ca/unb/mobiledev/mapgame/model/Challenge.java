package ca.unb.mobiledev.mapgame.model;

public class Challenge {

    private String solution;
    private String hint;
    private int rewardAmount;


    public Challenge(String solution, String hint, int rewardAmount) {
        this.solution = solution;
        this.hint = hint;
        this.rewardAmount = rewardAmount;
    }

    private String getSolution()
    {
        return this.solution;
    }

    private String getHint()
    {
        return this.hint;
    }

    private int getRewardAmount()
    {
        return this.rewardAmount;
    }
}

