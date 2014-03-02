package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvDilate;
import static sdp.group2.vision.ImageProcessor.newImage;


import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class DotEntity extends Entity {

    private int[] mins = new int[] {30, 51, 50};
    private int[] maxs = new int[] {45, 130, 140}; 

    @Override
    public IplImage threshold(IplImage hsvImage) {
        IplImage binaryImage = newImage(hsvImage, 1);
        cvInRangeS(hsvImage, cvScalar(mins[0], mins[1], mins[2], 0), cvScalar(maxs[0], maxs[1], maxs[2], 0), binaryImage);
        cvDilate(binaryImage, binaryImage, null, 3);
        return binaryImage;
    }
    
    public void threshold(IplImage hsvImage, IplImage binaryImage) {
        cvInRangeS(hsvImage, cvScalar(mins[0], mins[1], mins[2], 0), cvScalar(maxs[0], maxs[1], maxs[2], 0), binaryImage);
		cvDilate(binaryImage, binaryImage, null, 3);
    }

}