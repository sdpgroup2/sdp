package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvAnd;
import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvNot;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_core.cvSplit;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvDilate;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvErode;

import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class BallEntity extends Entity {

    private static IplImage[] hsvImages = new IplImage[3];
    private static IplImage binaryImage;
    
    static {
        CvRect cropRect = Thresholds.activeThresholds.cropRect;
        for (int i = 0; i < 3; ++i) {
            hsvImages[i] = IplImage.create(cvSize(cropRect.width(), cropRect.height()), 8, 1);
        }
        binaryImage = IplImage.create(cvSize(cropRect.width(), cropRect.height()), 8, 1);
    }

//    @Override
    public IplImage threshold(IplImage hsvImage) {
        // Split 3-channel image into 3 1-channel images
        cvSplit(hsvImage, hsvImages[0], hsvImages[1], hsvImages[2], null);

        int[] mins = Thresholds.activeThresholds.ballMins;
        int[] maxs = Thresholds.activeThresholds.ballMaxs;
        
        // The stuff below could be a loop but this is cleaner I think
        
        // Hue
    	cvInRangeS(hsvImages[0], cvScalar(maxs[0], 0, 0, 0), cvScalar(180 + mins[0], 0, 0, 0), binaryImage);
    	cvNot(binaryImage, binaryImage);
    	// Saturation
    	cvInRangeS(hsvImages[1], cvScalar(mins[1], 0, 0, 0), cvScalar(maxs[1], 0, 0, 0), hsvImages[1]);
        cvAnd(binaryImage, hsvImages[1], binaryImage, null);
        // Value
    	cvInRangeS(hsvImages[2], cvScalar(mins[2], 0, 0, 0), cvScalar(maxs[2], 0, 0, 0), hsvImages[2]);
        cvAnd(binaryImage, hsvImages[2], binaryImage, null);
        
        // Erode here to remove all the small white pixel chunks
        cvErode(binaryImage, binaryImage, null, 2);
        cvDilate(binaryImage, binaryImage, null, 2);
        return binaryImage;
    }
    
}
