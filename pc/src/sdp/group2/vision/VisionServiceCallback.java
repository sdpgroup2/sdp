package sdp.group2.vision;


public interface VisionServiceCallback {

    public void onPreparationFrame();

	public void onExceptionThrown(Exception e);

}
