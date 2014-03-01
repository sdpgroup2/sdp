package sdp.group2.pc;

import sdp.group2.geometry.Rect;
import sdp.group2.strategy.DefensivePlanner;
import sdp.group2.strategy.OffensivePlanner;
import sdp.group2.util.Constants.PitchType;
import sdp.group2.util.Constants.TeamColour;
import sdp.group2.vision.Image;
import sdp.group2.vision.VisionService;
import sdp.group2.vision.VisionServiceCallback;
import sdp.group2.vision.clusters.BallCluster;
import sdp.group2.vision.clusters.RobotBaseCluster;


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

    //public void onPreparationReady(PitchLinesCluster lines, PitchSectionCluster sections,
    //		BallCluster ballCluster, YellowRobotCluster yellowCluster, BlueRobotCluster blueCluster) {
    //this.pitch = new Pitch(lines, sections);
    //Ball ball = new Ball(ballCluster.getImportantRects().get(0));
    //pitch.addBall(ball);
    //List<Robot> blueRobots = new ArrayList<Robot>();
    //List<Robot> yellowRobots = new ArrayList<Robot>();
    //for (Rect rect : blueCluster.getImportantRects().subList(0, 2)) {
    //	blueRobots.add(new Robot(rect));
    //}
    //for (Rect rect : yellowCluster.getImportantRects().subList(0, 2)) {
    //	yellowRobots.add(new Robot(rect));
    //}
    //pitch.addRobots(blueRobots, yellowRobots, ourTeam);
    //}

    public void start() {
        visionService.start();
    }

    @Override
    public boolean onPreparationReady(Image currentImage, BallCluster ballCluster, RobotBaseCluster robotBaseCluster, Rect pitchRect, Rect[] sectionRects) {
        return false;
    }

    @Override
    public void onFrameGrabbed(Image currentImage) {

    }

    @Override
    public void onImageFiltered(Image currentImage) {

    }

    @Override
    public void onPreparationFrame() {

    }

    @Override
    public void onImageProcessed(Image currentImage, BallCluster ballCluster, RobotBaseCluster robotBaseCluster) {

    }

    @Override
    public void onExceptionThrown(Exception e) {

    }
}
