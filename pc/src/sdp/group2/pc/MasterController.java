package sdp.group2.pc;

import sdp.group2.geometry.Rect;
import sdp.group2.vision.Image;
import sdp.group2.vision.VisionService;
import sdp.group2.vision.VisionServiceCallback;
import sdp.group2.vision.clusters.BallCluster;
import sdp.group2.vision.clusters.RobotBaseCluster;


public class MasterController implements VisionServiceCallback {

    //public static TeamColor ourTeam;
    //public static PitchType pitchPlayed;
    //private Pitch pitch;
    private VisionService visionService;

    public MasterController() {
        // Start the vision system
        this.visionService = new VisionService(5, this);
        visionService.start();
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

    public static void main(String[] args) {
        new MasterController();
        //if (args.length < 2) {
        //	System.out.println("Not specified which team we are and what pitch we're playing");
        //	System.exit(0);
        //}
        //ourTeam = Integer.parseInt(args[0]) == Constants.YELLOW_TEAM ? TeamColor.YELLOW : TeamColor.BLUE;
        //pitchPlayed = Integer.parseInt(args[1]) == Constants.MAIN_PITCH ? PitchType.MAIN : PitchType.SIDE;
        //MasterController mc = new MasterController();
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
