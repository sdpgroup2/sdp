package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvOr;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvDilate;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvErode;
import static sdp.group2.vision.ImageProcessor.newImage;

import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class RobotEntity extends Entity {
    private CvMemStorage storage = CvMemStorage.create();

    int[][] mins = new int[][] {
            new int[] {47, 60, 130}, // base plate min
//            new int[] {19, 107, 155}, // yellow min
//            new int[] {135, 20, 34}, // blue min
            new int[] {24, 70, 61}, // dot min
    };

    int[][] maxs = new int[][] {
            new int[] {60, 115, 255}, // base plate max
//            new int[] {36, 155, 255}, // yellow max
//            new int[] {200, 50, 70}, // blue max
            new int[] {53, 125, 111}, // dot max

    };

    /**
     * TODO: remove image from here
     * Thresholds the image so that only the wanted colors are white
     * and everything else is black in the resulting image.
     * @param images 3 source 1-channel images
     * @param image just for size
     * @return the resulting image
     */
    public IplImage threshold(IplImage image) {
        IplImage result = newImage(image, 1);
        IplImage temp = newImage(image, 1);

        cvInRangeS(image, cvScalar(mins[0][0], mins[0][1], mins[0][2], 0), cvScalar(maxs[0][0], maxs[0][1], maxs[0][2], 0), result);
        cvInRangeS(image, cvScalar(mins[1][0], mins[1][1], mins[1][2], 0), cvScalar(maxs[1][0], maxs[1][1], maxs[1][2], 0), temp);
        cvOr(result, temp, result, null);
//        cvInRangeS(image, cvScalar(mins[2][0], mins[2][1], mins[2][2], 0), cvScalar(maxs[2][0], maxs[2][1], maxs[2][2], 0), temp);
//        cvOr(result, temp, result, null);
//        cvInRangeS(image, cvScalar(mins[3][0], mins[3][1], mins[3][2], 0), cvScalar(maxs[3][0], maxs[3][1], maxs[3][2], 0), temp);
//        cvOr(result, temp, result, null);
        cvErode(result, result, null, 1);
        cvDilate(result, result, null, 9);
        return result;
    }

    @Override
    public IplImage detect(IplImage[] hsvImages) {
        return null;
    }
    
}
