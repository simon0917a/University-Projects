//Li Ming Chun Simon 25017659D
//BlueJ

public class VideoGame extends Item
{
    // instance variables - replace the example below with your own
    private String platform;
    private int numberOfPlayers;
    
    public VideoGame(String theTitle, String theplatform, int Players, int time)
    {
        super(theTitle, time);
        platform = theplatform;
        numberOfPlayers = Players;
    }
    
    public void print() {
        System.out.println("VideoGame");
        super.print();
        System.out.println("Platform: " + platform);
        System.out.println("No. of players: " + numberOfPlayers);
    }

}