package sdp.group2.pc;

import java.util.List;

import sdp.group2.communication.CommunicationService;
import sdp.group2.geometry.Point;
import sdp.group2.geometry.PointSet;
import sdp.group2.strategy.DefensivePlanner;
import sdp.group2.strategy.OffensivePlanner;
import sdp.group2.util.Constants;
import sdp.group2.util.Constants.PitchType;
import sdp.group2.util.Constants.TeamColour;
import sdp.group2.util.Tuple;
import sdp.group2.vision.VisionService;
import sdp.group2.vision.VisionServiceCallback;
import sdp.group2.world.Pitch;


public class MasterController implements VisionServiceCallback {

	public static boolean ENABLE_GUI = true;
    public static TeamColour ourTeam;
    public static PitchType pitchPlayed;
    private Pitch pitch;
    private DefensivePlanner defPlanner = new DefensivePlanner(pitch);
    private OffensivePlanner offPlanner;
    private VisionService visionService;
    private CommunicationService commService;

    public MasterController() {
    	// Create a pitch
    	PointSet pitchPoints = new PointSet();
    	pitchPoints.add(new Point(101, 94));
    	pitchPoints.add(new Point(66, 164));
    	pitchPoints.add(new Point(67, 311));
    	pitchPoints.add(new Point(100, 377));
    	pitchPoints.add(new Point(546, 382));
    	pitchPoints.add(new Point(584, 315));
		  pitchPoints.add(new Point(588, 170));
		  pitchPoints.add(new Point(554, 100));
		  PointSet[] zonePoints = new PointSet[4];
		  for (int i = 0; i < zonePoints.length; i++) {
				zonePoints[i] = new PointSet();
			}
		  zonePoints[0].add(new Point(157,89));
		  zonePoints[0].add(new Point(101,94));
		  zonePoints[0].add(new Point(66,164));
		  zonePoints[0].add(new Point(67,311));
		  zonePoints[0].add(new Point(100,377));
		  zonePoints[0].add(new Point(155,382));
		  
		  zonePoints[1].add(new Point(206,88));
		  zonePoints[1].add(new Point(306,88));
		  zonePoints[1].add(new Point(204,383));
		  zonePoints[1].add(new Point(301,385));
		  
		  zonePoints[2].add(new Point(356,90));
		  zonePoints[2].add(new Point(453,94));
		  zonePoints[2].add(new Point(350,385));
		  zonePoints[2].add(new Point(447,385));
		  
		  zonePoints[3].add(new Point(502,95));
		  zonePoints[3].add(new Point(554,100));
		  zonePoints[3].add(new Point(588,170));
		  zonePoints[3].add(new Point(584,315));
		  zonePoints[3].add(new Point(546,382));
		  zonePoints[3].add(new Point(495,384));
    	this.pitch = new Pitch();
        // Start the vision system
        this.visionService = new VisionService(5, this);
//        this.commService = new CommunicationService(Constants.ROBOT_2D_NAME);
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
            ourTeam = TeamColour.valueOf(Integer.parseInt(args[0]));
            pitchPlayed = PitchType.valueOf(Integer.parseInt(args[1]));
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        
        final MasterController controller = new MasterController();    
        controller.start();
  
    }
    
    public void start() {
        visionService.start();
//        commService.startRunningFromQueue();
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
		if (ballCentroid != null) {
			pitch.updateBallPosition(ballCentroid.toMillis());
		}
		pitch.updateRobots(yellowRobots, TeamColour.YELLOW);
		pitch.updateRobots(blueRobots, TeamColour.BLUE);
		System.out.println();
//		defPlanner.act();
	}
}
