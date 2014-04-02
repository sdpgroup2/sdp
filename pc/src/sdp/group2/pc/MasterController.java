package sdp.group2.pc;

import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;

import sdp.group2.communication.CommandQueue;
import sdp.group2.communication.Commands;
import sdp.group2.communication.CommunicationService;
import sdp.group2.geometry.Point;
import sdp.group2.strategy.DefensivePlanner;
import sdp.group2.strategy.OffensivePlanner;
import sdp.group2.util.Constants;
import sdp.group2.util.Constants.*;
import sdp.group2.util.Tuple;
import sdp.group2.vision.ImageProcessor;
import sdp.group2.vision.Thresholds;
import sdp.group2.vision.VisionService;
import sdp.group2.vision.VisionServiceCallback;
import sdp.group2.world.Pitch;

public class MasterController implements VisionServiceCallback {

	public static boolean ENABLE_GUI = true;
    public static TeamColour ourTeam;
    public static TeamSide ourSide;
    public static PitchType pitchPlayed;
    public static boolean usingComms = true;
    public static boolean matchStarted = false;
    private Pitch pitch;
    private DefensivePlanner defPlanner;
    private OffensivePlanner offPlanner;
    private VisionService visionService;

    private CommunicationService commService;


    public MasterController(PitchType pitchPlayed) {
    	this.pitch = new Pitch(pitchPlayed);
    	this.defPlanner = new DefensivePlanner(pitch);
    	this.offPlanner = new OffensivePlanner(pitch);
        // Start the vision system
        this.visionService = new VisionService(5, this);
        if (usingComms) {
        	this.commService = new CommunicationService();
        }
    }
    
    public static int[] getPitchLines() {
    	return (pitchPlayed == PitchType.MAIN) ? Constants.MAIN_LINES : Constants.SIDE_LINES;
    }
    
    /**
     * @param mmPoint Point in millimeters
     * @return zone
     */
    public static int getZoneOfPoint(Point mmPoint) {
		int[] lines = MasterController.getPitchLines();
		if (mmPoint.x <= lines[0]) {
			return 0;
		} else if (mmPoint.x <= lines[1]) {
			return 1;
		} else if (mmPoint.x <= lines[2]) {
			return 2;
		} else {
			return 3;
		}
	}

    /**
     * Main method of the project. Arguments passed in should be:
     * TEAM_COLOUR PITCH_TYPE
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("Not specified which team we are and what pitch we're playing");
            System.exit(1);
        }

        ourTeam = (args[0].equals("yellow")) ? TeamColour.YELLOW : TeamColour.BLUE;
        ourSide = (args[1].equals("left")) ? TeamSide.LEFT : TeamSide.RIGHT;
        pitchPlayed = (args[2].equals("main")) ? PitchType.MAIN : PitchType.SIDE;
        Thresholds.pitchName = args[3];
        if (args[4].equals("false")) {
        	usingComms = false;
        }
        
    	try {
			Thresholds.readThresholds(Thresholds.pitchName, pitchPlayed);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        final MasterController controller = new MasterController(pitchPlayed);
        if (usingComms) {
	        Runtime.getRuntime().addShutdownHook(new Thread() {
	        	
	            public void run() {
	            	System.out.println("Closing");
	            	CommandQueue.add(Commands.disconnect(), Constants.ROBOT_2A_NAME);
	            	CommandQueue.add(Commands.disconnect(), Constants.ROBOT_2D_NAME);
	            }
	         });
        }
    	controller.start();
    }

    public void start() {	
        visionService.start();
        if (usingComms) {
        	commService.startRunningFromQueue();
        }
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
    	
    	ImageProcessor.heightFilter(yellowRobots);
    	ImageProcessor.heightFilter(blueRobots);
    	
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
		
		ImageProcessor.heightFilter(yellowRobots);
    	ImageProcessor.heightFilter(blueRobots);

    	if (usingComms && matchStarted) {
			offPlanner.act();
			defPlanner.act();
    	}
	}
	
	
	
    @Override
    public void onExceptionThrown(Exception e) {
    	e.printStackTrace();
    }
}
