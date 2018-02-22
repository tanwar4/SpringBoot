package challenge.Model;

public class UserFollower {
    private String handle;
    private String popularFollower;
    private int popularityScore;

    public int getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(int popularityScore) {
        this.popularityScore = popularityScore;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPopularFollower() {
        return popularFollower;
    }

    public void setPopularFollower(String popularFollower) {
        this.popularFollower = popularFollower;
    }
}
