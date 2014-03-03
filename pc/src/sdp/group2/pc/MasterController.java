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
import sdp.group2.world.Pitch;


public class MasterController implements VisionServiceCallback {

	public static boolean ENABLE_GUI = true;
    public static TeamColour ourTeam;
    public static PitchType pitchPlayed;
    private Pitch pitch = new Pitch();
    private DefensivePlanner defPlanner = new DefensivePlanner(pitch);
    private OffensivePlanner offPlanner;
    private VisionService visionService;
    private boolean ready = false; // true when we find all the objects

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
        while (!mc.ready) {
        	
        }
        mc.startPlanning();
    }
    
    public void start() {
        visionService.start();
    }
    
    public void startPlanning() {
		Thread planning = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					defPlanner.act();
				}
			}
		});
		
		planning.start();
    }

    @Override
    public void onPreparationFrame() {
    }

    @Override
    public void onExceptionThrown(Exception e) {
    	e.printStackTrace();
    }

	@Override
	public synchronized void update(Point ballCentroid, List<Tuple<Point, Point>> yellowRobots,
			List<Tuple<Point, Point>> blueRobots) {
		boolean maybeReady = false;
		
		// If we don't have the ball then we're not ready
		if (ballCentroid != null) {
			
			pitch.updateBallPosition(ballCentroid.toMillis());
			System.out.printf("Ball position: %s\n", ballCentroid);
			maybeReady = true;
		}
		
		// If we don't have all 4 robots, we're not ready
		if (yellowRobots.size() != 2 || blueRobots.size() != 2) {
			maybeReady = false;
		}

		// If one of the direction vectors is null we're not ready
		// Position (first of tuple) is never null
		for (Tuple<Point, Point> tuple : blueRobots) {
			System.out.printf("Blue Robot position %s\n", tuple.getFirst());
			System.out.printf("Blue Robot dot position %s\n", tuple.getSecond());
			if (tuple.getSecond() == null) {
				maybeReady = false;
			}
		}
		
		// If one of the direction vectors is null we're not ready
		// Position (first of tuple) is never null
		for (Tuple<Point, Point> tuple : yellowRobots) {
			System.out.printf("Yellow Robot position %s\n", tuple.getFirst());
			System.out.printf("Yellow Robot dot position %s\n", tuple.getSecond());
			if (tuple.getSecond() == null) {
				maybeReady = false;
			}
		}
		
		// By now we should be ready if maybe ready
		if (maybeReady) {
			pitch.updateRobots(yellowRobots, TeamColour.YELLOW);
			pitch.updateRobots(blueRobots, TeamColour.BLUE);
		}
		ready = maybeReady;
	}
}
