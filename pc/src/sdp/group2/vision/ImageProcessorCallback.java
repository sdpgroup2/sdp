package sdp.group2.vision;

import java.util.List;

import sdp.group2.util.Tuple;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;

public interface ImageProcessorCallback {
	
	public void detected(CvPoint ballCentroid, List<Tuple<CvRect, CvPoint>> yellowRobots,
			List<Tuple<CvRect, CvPoint>> blueRobots);

}
