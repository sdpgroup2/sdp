package sdp.group2.pc;

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
    private static String pitchName;
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

        pitchName = args[2];
        try {
            ourTeam = TeamColour.valueOf(Integer.parseInt(args[0]));
           	pitchPlayed = PitchType.valueOf(Integer.parseInt(args[1]));
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        
    	try {
			Thresholds.activeThresholds = readThresholds(pitchName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

    
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				close();
			}		
		}));
        
        final MasterController controller = new MasterController(pitchPlayed);    
        controller.start();
  
    }
    
    public static Thresholds readThresholds(String filename) throws FileNotFoundException, IOException, ParseException {
    	JSONParser parser = new JSONParser();
    	Path currentRelativePath = Paths.get("");
    	path = currentRelativePath.toAbsolutePath().toString();
		JSONObject thresholds = (JSONObject) parser.parse(new FileReader(path+ "/assets/thresholds/" +filename + ".json"));
		
		JSONObject ball = (JSONObject) thresholds.get("ball");
		int[] ballMins = getIntArray((JSONArray) ball.get("mins"));
		int[] ballMaxs = getIntArray((JSONArray)  ball.get("maxs"));
		JSONObject dot = (JSONObject) thresholds.get("dot");
		int[] dotMins = getIntArray((JSONArray) dot.get("mins"));
		int[] dotMaxs = getIntArray((JSONArray) dot.get("maxs"));
		JSONObject basePlate = (JSONObject) thresholds.get("baseplate");
		int[] baseMins = getIntArray((JSONArray)basePlate.get("mins"));
		int[] baseMaxs = getIntArray((JSONArray) basePlate.get("maxs"));
		JSONObject yellow = (JSONObject) thresholds.get("yellow");
		int[] yellowMins = getIntArray((JSONArray) yellow.get("mins"));
		int[] yellowMaxs = getIntArray((JSONArray)  yellow.get("maxs"));
		Long yellowPixelsThresholdLong = (Long) yellow.get("yellowThresh");
		int yellowPixelsThreshold = Integer.valueOf(yellowPixelsThresholdLong.intValue());
		JSONObject boundingRect = (JSONObject) thresholds.get("boundingrect");
		int[] rect = getIntArray((JSONArray) boundingRect.get("rect"));
		
		Thresholds activeThresholds = new Thresholds(filename, ballMins, ballMaxs, dotMins, dotMaxs, baseMins, baseMaxs, yellowMins, yellowMaxs, yellowPixelsThreshold, rect);
		return activeThresholds;
		
    }
    
    private static int[] getIntArray(JSONArray jsonArray){
    	int[] intArray = new int[jsonArray.size()];
    	for (int i = 0; i < jsonArray.size(); i++) {
    		Long val = (Long) jsonArray.get(i);
    		intArray[i] = Integer.valueOf(val.intValue());
    	}
		return intArray;
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
	
	public static void close() {
		System.out.println("CLOSE CALLED!!!!!!!!!!!!!!!!!");
		JSONObject obj = new JSONObject();
		
		JSONObject ball = new JSONObject();
		JSONArray mins = new JSONArray();
		mins = getJSONArray(Thresholds.activeThresholds.ballMins);
		ball.put("mins", mins);
		
		JSONArray maxs = new JSONArray();
		maxs = getJSONArray(Thresholds.activeThresholds.ballMaxs);
		ball.put("maxs", maxs);
		obj.put("ball", ball);
		
		JSONObject dot = new JSONObject();
		mins = new JSONArray();
		mins = getJSONArray(Thresholds.activeThresholds.dotMins);
		dot.put("mins", mins);
		maxs = new JSONArray();
		maxs = getJSONArray(Thresholds.activeThresholds.dotMaxs);
		dot.put("maxs", maxs);
		obj.put("dot", dot);
		
		JSONObject baseplate = new JSONObject();
		mins = new JSONArray();
		mins = getJSONArray(Thresholds.activeThresholds.basePlateMins);
		baseplate.put("mins", mins);
		maxs = new JSONArray();
		maxs = getJSONArray(Thresholds.activeThresholds.basePlateMaxs);
		baseplate.put("maxs", maxs);
		obj.put("baseplate", baseplate);
		
		JSONObject yellow = new JSONObject();
		mins = new JSONArray();
		mins = getJSONArray(Thresholds.activeThresholds.yellowMins);
		yellow.put("mins", mins);
		maxs = new JSONArray();
		
		maxs = getJSONArray(Thresholds.activeThresholds.yellowMaxs);
		yellow.put("maxs", maxs);
		
		yellow.put("yellowThresh", Thresholds.activeThresholds.yellowPixelsThreshold);
		obj.put("yellow", yellow);
		
		JSONArray rect = new JSONArray();
		rect = getJSONArray(Thresholds.activeThresholds.rect);
		
		JSONObject boundingRect = new JSONObject();
		boundingRect.put("rect", rect);
		obj.put("boundingrect", boundingRect);

		try {
			 
			FileWriter file = new FileWriter(path + "/assets/thresholds/" + pitchName + ".json");
			file.write(obj.toString());
			file.flush();
			file.close();
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private static JSONArray getJSONArray(int[] intArray) {
		JSONArray jsonArray = new JSONArray();
		for (int i : intArray) {
			jsonArray.add(i);
		}
		return jsonArray;
	}
    @Override
    public void onExceptionThrown(Exception e) {
    	e.printStackTrace();
		close();
    }
}
