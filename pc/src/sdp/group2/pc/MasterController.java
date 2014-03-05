package sdp.group2.pc;

import java.util.List;

import sdp.group2.communication.CommunicationService;
import sdp.group2.geometry.Point;
import sdp.group2.geometry.PointSet;
import sdp.group2.strategy.DefensivePlanner;
import sdp.group2.strategy.OffensivePlanner;
import sdp.group2.strategy.SimpleDefensivePlanner;
import sdp.group2.util.Constants;
import sdp.group2.util.Constants.PitchType;
import sdp.group2.util.Constants.TeamColour;
import sdp.group2.util.Tuple;
import sdp.group2.vision.Thresholds;
import sdp.group2.vision.VisionService;
import sdp.group2.vision.VisionServiceCallback;
import sdp.group2.world.Pitch;


public class MasterController implements VisionServiceCallback {

	public static boolean ENABLE_GUI = true;
    public static TeamColour ourTeam;
    public static PitchType pitchPlayed;
    private Pitch pitch;
    private SimpleDefensivePlanner defPlanner;
    private OffensivePlanner offPlanner;
    private VisionService visionService;
    private CommunicationService commService;

    public MasterController() {
    	this.pitch = sdp.group2.simulator.Constants.getDefaultPitch();
    	this.defPlanner = new SimpleDefensivePlanner(pitch);
        // Start the vision system
        this.visionService = new VisionService(5, this);
        this.commService = new CommunicationService(Constants.ROBOT_2A_NAME);
    }

    /**
     * Main method of the project. Arguments passed in should be:
     * TEAM_COLOUR PITCH_TYPE
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Not specified which team we are and what pitch we're playing");
            System.exit(1);
        }
        try { 
            Integer.parseInt(args[0]); 
            Integer.parseInt(args[1]);
        } catch(NumberFormatException e) { 
        	System.err.println("Not integer parameters supplied");
            System.exit(1);
        }
        try {
            ourTeam = TeamColour.valueOf(Integer.parseInt(args[0]));
            pitchPlayed = PitchType.valueOf(Integer.parseInt(args[1]));
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        
        // Sets the thresholds to be used based on pitch type.
        if (pitchPlayed == Constants.PitchType.MAIN) {
        	Thresholds.activeThresholds = Thresholds.mainPitchThresholds;
        } else {
        	Thresholds.activeThresholds = Thresholds.sidePitchThresholds;
        }
        Thresholds.activeThresholds = Thresholds.nightMainPitchThresholds;
        
        final MasterController controller = new MasterController();    
        controller.start();
  
    }
    
    public void start() {	
        visionService.start();
        commService.startRunningFromQueue();
    }

    @Override
    public void onPreparationFrame() {
    }

    @Override
    public void onExceptionThrown(Exception e) {
    	e.printStackTrace();
    }

	@Override
	public void update(Point ballCentroid, List<Tuple<Point, Point>> yellowRobots,
			List<Tuple<Point, Point>> blueRobots) {
		
		pitch.updateRobots(yellowRobots, TeamColour.YELLOW);
		pitch.updateRobots(blueRobots, TeamColour.BLUE);
		
		if (ballCentroid == null) {
			return;
		}
		
		pitch.updateBallPosition(ballCentroid.toMillis());
		
		for (int i = 0; i < 4; i++) {
			if (pitch.getZone(i).contains(ballCentroid)) {
				System.out.println(ballCentroid);
				System.out.println(i);
			}
		}
		
		defPlanner.act();
	}
}
