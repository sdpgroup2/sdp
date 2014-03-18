package sdp.group2.pc;

import java.util.List;

import sdp.group2.communication.CommunicationService;
import sdp.group2.geometry.Point;
import sdp.group2.strategy.DefensivePlanner;
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
    private DefensivePlanner defPlanner;
//    private OffensivePlanner offPlanner;
    private VisionService visionService;
    private CommunicationService commService;

    public MasterController() {
    	this.pitch = sdp.group2.simulator.Constants.getDefaultPitch();
    	this.defPlanner = new DefensivePlanner(pitch);
//    	this.offPlanner = new EvenSimplerAttackingPlanner(pitch);
        // Start the vision system
        this.visionService = new VisionService(5, this);
        this.commService = new CommunicationService();
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
//        Thresholds.activeThresholds = Thresholds.nightMainPitchThresholds;
//        Thresholds.activeThresholds = Thresholds.spongeBobSquarePitch;
        
        final MasterController controller = new MasterController();    
        controller.start();
  
    }
    
    public void start() {	
        visionService.start();
        commService.startRunningFromQueue();
    }

    // Sorry
    public static void tupleOfPointsToMillis(Tuple<Point, Point> tuple) {
    	Point first = tuple.getFirst();
    	Point second = tuple.getSecond();
    	if (first != null) { first.toMillis(); }
    	if (second != null) { second.toMillis(); }
    }
    
    /**
     * Callback ran after the vision service has found all the neccessary things.
     */
    @Override
    public void prepared(Point ballCentroid, List<Tuple<Point, Point>> yellowRobots,
    		List<Tuple<Point, Point>> blueRobots) {
		// The toMillis is in-place. We change to millis so from now on everything
		// is in millis. Also nothing should be null in this otherwise
    	// we wouldn't be prepared, right?
    	for (Tuple<Point, Point> tuple : blueRobots) {
    		tupleOfPointsToMillis(tuple);
		}
    	for (Tuple<Point, Point> tuple : yellowRobots) {
    		tupleOfPointsToMillis(tuple);
		}
    	pitch.initialise(ballCentroid.toMillis(), yellowRobots, blueRobots);
    }

    /**
     * Callback ran every frame after the image was processed after preparation.
     */
	@Override
	public void update(Point ballCentroid, List<Tuple<Point, Point>> yellowRobots,
			List<Tuple<Point, Point>> blueRobots) {
		
		pitch.updateRobots(yellowRobots, blueRobots);
		for (Tuple<Point, Point> tuple : blueRobots) {
			tupleOfPointsToMillis(tuple);
		}
    	for (Tuple<Point, Point> tuple : yellowRobots) {
    		tupleOfPointsToMillis(tuple);
		}
		if (ballCentroid != null) {
			pitch.updateBallPosition(ballCentroid.toMillis());
		}
		
		defPlanner.act();
//		offPlanner.act();
	}
	

    @Override
    public void onExceptionThrown(Exception e) {
    	e.printStackTrace();
    }
}
