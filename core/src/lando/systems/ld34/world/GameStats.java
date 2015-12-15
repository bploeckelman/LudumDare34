package lando.systems.ld34.world;

import lando.systems.ld34.LudumDare34;

/**
 * Created by dsgraham on 12/14/15.
 */
public class GameStats {

    public int slavesKilledMotivating;
    public int slavesBorn;
    public int motivations;
    public int disastersTriggered;
    public int slavesKilledByDisaster;
    public int efficiencyReducesByDisaster;
    public int resourcesLostByDisaster;

    public GameStats(){
        slavesBorn = 0;
        slavesKilledMotivating = 0;
        motivations = 0;
        disastersTriggered = 0;
        slavesKilledByDisaster = 0;
        efficiencyReducesByDisaster = 0;
        resourcesLostByDisaster = 0;
    }

    public String getSlaveInfo(){
        return  "You grew your pyramid to [RED] " + LudumDare34.GameScreen.resourceManager.getPyramidHeight() + "[] tiers tall." +
                "\nYou had to motivate your slaves [RED] " + motivations + "[] times. " +
                "\nYou killed [RED] " + slavesKilledMotivating + "[] slaves through encouragement. You Monster. " +
                "\nBut you had [GREEN] " + slavesBorn + "[] slaves born during the project, so it evens out. " +
                (disastersTriggered == 0 ? "\nYour excellent management of the Pharaoh's mood kept the region [GREEN] disaster free[]! Nice work sycophant! "
                                         : "\nYour inattention to the Pharaoh's needs resulted in [RED] " + disastersTriggered + "[] disasters, next time lick those boots harder! " +
                                           "\nSpeaking of disasters, [RED] " + slavesKilledByDisaster + "[] slaves were killed by disasters, " +
                                           "efficiency was reduced [RED] " + efficiencyReducesByDisaster + "[] times by disasters, " +
                                           "and [RED] " + resourcesLostByDisaster + "[] resources were lost due to disasters... how unfortunate.") +
                "";
    }
}
