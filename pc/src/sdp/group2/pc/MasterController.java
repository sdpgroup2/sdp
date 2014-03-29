package sdp.group2.pc;

import static com.googlecode.javacv.cpp.opencv_core.cvRect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashMap;
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
import sdp.group2.util.Debug;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MasterController implements VisionServiceCallback {

	public static boolean ENABLE_GUI = true;
    public static TeamColour ourTeam;
    public static PitchType pitchPlayed;
    private Pitch pitch;
    private DefensivePlanner defPlanner;
//    private OffensivePlanner offPlanner;
    private VisionService visionService;
    private CommunicationService commService;
    private static String pitchName;
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
        pitchName = args[1];
        try {
            ourTeam = TeamColour.valueOf(Integer.parseInt(args[0]));
 
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        
        
		
//        // Sets the thresholds to be used based on pitch type.
//        if (pitchPlayed == Constants.PitchType.MAIN) {
////        	Thresholds.activeThresholds = Thresholds.mainPitchThresholds;
        	try {
				Thresholds.activeThresholds = readThresholds(pitchName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
//        } else {
//        	Thresholds.activeThresholds = Thresholds.hikuaiThresholds;
//        }
        // Remove this if I have forgotten to:
//        Thresholds.activeThresholds = Thresholds.torosayThresholds;
        Debug.log("Using thresholds: %s", Thresholds.activeThresholds.name);
        
        
        final MasterController controller = new MasterController();    
        controller.start();
  
    }
    
    public static Thresholds readThresholds(String filename) throws FileNotFoundException, IOException, ParseException {
    	JSONParser parser = new JSONParser();
		JSONObject thresholds = (JSONObject) parser.parse(new FileReader(filename + ".json"));
		JSONObject ball = (JSONObject) thresholds.get("ball");
		int[] ballMins = (int[]) ball.get("mins");
		int[] ballMaxs = (int[])  ball.get("maxs");
		JSONObject dot = (JSONObject) thresholds.get("dot");
		int[] dotMins = (int[]) dot.get("mins");
		int[] dotMaxs = (int[]) dot.get("maxs");
		JSONObject basePlate = (JSONObject) thresholds.get("baseplate");
		int[] baseMins = (int[]) basePlate.get("mins");
		int[] baseMaxs = (int[]) basePlate.get("maxs");
		JSONObject yellow = (JSONObject) thresholds.get("yellow");
		int[] yellowMins = (int[]) yellow.get("mins");
		int[] yellowMaxs = (int[]) yellow.get("maxs");
		int yellowPixelsThreshold = (Integer) thresholds.get("yellowThresh");
		int[] rect = (int[]) yellow.get("rect");
		Thresholds activeThresholds = new Thresholds(filename, ballMins, ballMaxs, dotMins, dotMaxs, baseMins, baseMaxs, yellowMins, yellowMaxs, yellowPixelsThreshold, rect);
		return activeThresholds;
		
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
	
	public void close() {
		JSONObject obj = new JSONObject();
		
		JSONObject ball = new JSONObject();
		JSONArray mins = new JSONArray();
		mins.addAll(Arrays.asList(Thresholds.activeThresholds.ballMins));
		ball.put("mins", mins);
		JSONArray maxs = new JSONArray();
		maxs.addAll(Arrays.asList(Thresholds.activeThresholds.ballMaxs));
		ball.put("maxs", maxs);
		obj.put("ball", ball);
		
		JSONObject dot = new JSONObject();
		mins = new JSONArray();
		mins.addAll(Arrays.asList(Thresholds.activeThresholds.dotMins));
		dot.put("mins", mins);
		maxs = new JSONArray();
		maxs.addAll(Arrays.asList(Thresholds.activeThresholds.dotMaxs));
		dot.put("maxs", maxs);
		obj.put("dot", dot);
		
		JSONObject baseplate = new JSONObject();
		mins = new JSONArray();
		mins.addAll(Arrays.asList(Thresholds.activeThresholds.basePlateMins));
		baseplate.put("mins", mins);
		maxs = new JSONArray();
		maxs.addAll(Arrays.asList(Thresholds.activeThresholds.basePlateMaxs));
		baseplate.put("maxs", maxs);
		obj.put("baseplate", baseplate);
		
		JSONObject yellow = new JSONObject();
		mins = new JSONArray();
		mins.addAll(Arrays.asList(Thresholds.activeThresholds.yellowMins));
		yellow.put("mins", mins);
		maxs = new JSONArray();
		
		maxs.addAll(Arrays.asList(Thresholds.activeThresholds.yellowMaxs));
		yellow.put("maxs", maxs);
		obj.put("yellow", yellow);
		
		obj.put("yellowThresh", Thresholds.activeThresholds.yellowPixelsThreshold);
		
		JSONArray rect = new JSONArray();
		rect.addAll(Arrays.asList(Thresholds.activeThresholds.rect));
		obj.put("rect", rect);

		try {
			 
			FileWriter file = new FileWriter("/assets/thresholds/" + pitchName + ".json");
			file.write(obj.toJSONString());
			file.flush();
			file.close();
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @Override
    public void onExceptionThrown(Exception e) {
    	e.printStackTrace();
    	close();
    }
}
