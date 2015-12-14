package lando.systems.ld34.world;

/**
 * Created by dsgraham on 12/14/15.
 */
public class GameStats {

    public int slavesKilledMotivating;
    public int slavesBorn;
    public int motivations;

    public GameStats(){
        slavesBorn = 0;
        slavesKilledMotivating = 0;
        motivations = 0;
    }

    public String getSlaveInfo(){
        return  "You had to motivate your slaves " + motivations + " times. " +
                "You Monster. You killed " + slavesKilledMotivating + " slaves through encouragement. " +
                "But you had " + slavesBorn + " slaves born during the project.";
    }
}
