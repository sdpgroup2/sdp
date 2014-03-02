package sdp.group2.pc;

import java.util.List;

import sdp.group2.geometry.Point;
import sdp.group2.strategy.DefensivePlanner;
import sdp.group2.strategy.OffensivePlanner;
import sdp.group2.util.Constants.PitchType;
import sdp.group2.util.Constants.TeamColour;
import sdp.group2.util.Tuple;
import sdp.group2.vision.VisionService;
import sdp.group2.vision.VisionServiceCallback;


public class MasterController implements VisionServiceCallback {

    //private Pitch pitch;
    public static TeamColour ourTeam;
    public static PitchType pitchPlayed;
    private DefensivePlanner defPlanner;
    private OffensivePlanner offPlanner;
    private VisionService visionService;

    public MasterController() {
        // Start the vision system
        this.visionService = new VisionService(5, this);
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
        MasterController mc = new MasterController();
        mc.start();
    }

    public void start() {
        visionService.start();
    }

    @Override
    public void onPreparationFrame() {

    }

    @Override
    public void onExceptionThrown(Exception e) {

    }

	@Override
	public void update(Point ballCentroid,
			List<Tuple<Point, Point>> yellowRobots,
			List<Tuple<Point, Point>> blueRobots) {
		// TODO Auto-generated method stub
		
	}
}
