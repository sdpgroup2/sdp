package sdp.group2.pc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import sdp.group2.communication.CommunicationService;
import sdp.group2.geometry.Point;
import sdp.group2.strategy.DefensivePlanner;
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
	private static String path;

    private CommunicationService commService;


    public MasterController(PitchType pitchPlayed) {
    	this.pitch = new Pitch(pitchPlayed);
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
        if (args.length < 3) {
            System.err.println("Not specified which team we are and what pitch we're playing");
            System.exit(1);
        }

        Thresholds.pitchName = args[2];

        try {
            ourTeam = TeamColour.valueOf(Integer.parseInt(args[0]));
           	pitchPlayed = PitchType.valueOf(Integer.parseInt(args[1]));
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        
    	try {
			Thresholds.readThresholds(Thresholds.pitchName, Integer.parseInt(args[0]));
		} catch (FileNotFoundException e) {
			System.err.println("Dang! File not found!");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        final MasterController controller = new MasterController(pitchPlayed);    
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
