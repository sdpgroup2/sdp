package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvRect;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.googlecode.javacv.cpp.opencv_core.CvRect;

public class Thresholds {
	
	public static Thresholds activeThresholds;
	
	public String name;
	public int[] ballMins;
	public int[] ballMaxs;
	public int[] dotMins;
	public int[] dotMaxs;
	public int[] basePlateMins;
	public int[] basePlateMaxs;
	public int[] yellowMins;
	public int[] yellowMaxs;
	public int yellowPixelsThreshold;
	public CvRect cropRect;
	public int[] rect;
	public EntityThresh[] entities = new EntityThresh[4];
	public static String pitchName;
	
    public static Thresholds readThresholds(String filename, int pitch) throws IOException, ParseException {
      
    	JSONParser parser = new JSONParser();
    	File jsonFile = new File("assets/thresholds/" + filename + ".json");
    	if (!jsonFile.exists()) {
    		if (pitch == 0) {
    			jsonFile = new File("assets/thresholds/mainPitch.json");
    		} else {
    			jsonFile = new File("assets/thresholds/sidePitch.json");
    		}
    	}
		JSONObject thresholds = (JSONObject) parser.parse(new FileReader(jsonFile));
		
		
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
		
		activeThresholds = new Thresholds(filename, ballMins, ballMaxs, dotMins, dotMaxs, baseMins, baseMaxs, yellowMins, yellowMaxs, yellowPixelsThreshold, rect);
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
	
    @SuppressWarnings("unchecked") // YOLO
	public JSONObject serialize() {
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
		
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	private static JSONArray getJSONArray(int[] intArray) {
		JSONArray jsonArray = new JSONArray();
		for (int i : intArray) {
			jsonArray.add(i);
		}
		return jsonArray;
	}
	
	public Thresholds(String name, int[] ballMins, int[] ballMaxs, int[] dotMins,
			int[] dotMaxs, int[] basePlateMins, int[] basePlateMaxs,
			int[] yellowMins, int[] yellowMaxs, int yellowPixelsThreshold,
			int[] rect) {
		this.name = name;
		this.ballMins = ballMins;
		System.out.println(ballMins[2]);
		this.ballMaxs = ballMaxs;
		this.dotMins = dotMins;
		this.dotMaxs = dotMaxs;
		System.out.println( dotMaxs[1]);
		this.basePlateMins = basePlateMins;
		this.basePlateMaxs = basePlateMaxs;
		this.yellowMins = yellowMins;
		this.yellowMaxs = yellowMaxs;
		this.yellowPixelsThreshold = yellowPixelsThreshold;
		this.rect = rect;
		System.out.printf("vals %d %d %d %d", rect[0],rect[1],rect[2],rect[3]);
		this.cropRect = cvRect(rect[0],rect[1],rect[2],rect[3]);
		
		EntityThresh ball = new EntityThresh("Ball");
		EntityThresh dot = new EntityThresh("Dot");
		EntityThresh basePlate = new EntityThresh("BasePlate");
		EntityThresh yellow = new EntityThresh("Yellow");
		
		ball.mins = ballMins;
		ball.maxs = ballMaxs;
		dot.mins = dotMins;
		dot.maxs = dotMaxs;
		basePlate.mins = basePlateMins;
		basePlate.maxs = basePlateMaxs;
		yellow.mins = yellowMins;
		yellow.maxs = yellowMaxs;
		
		entities[0] = ball;
		entities[1] = dot;
		entities[2] = basePlate;
		entities[3] = yellow;
		
	}

}
