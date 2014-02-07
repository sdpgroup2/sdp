package group2.sdp.ai;

/** Contains representation of a state from a team point of view.
 *  Hence, the same for ally and enemy. */
public class Team
{
    private boolean ally;
    private Plane defenseZone, attackZone;
    private Robot defenseRobot, attackRobot;
    // add planes for defence, attack enhanced by robot information
    
    public Team(boolean ally)
    { this.ally = ally; }
    
    public boolean isAlly()
    { return ally; }
}
