package sdp.group2.vision;


import com.googlecode.javacv.cpp.opencv_core.IplImage;

import sdp.group2.util.Constants;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static sdp.group2.vision.ImageProcessor.newImage;


public class RobotEntity implements Detectable {
    private Constants.PitchType pitchType;

    int[][] mins = new int[][] {
            new int[] {45, 70, 130}, // base plate min
            new int[] {15, 70, 180}, // yellow min
            new int[] {135, 20, 34}, // blue min
            new int[] {40, 5, 35}, // dot min
    };

    int[][] maxs = new int[][] {
            new int[] {65, 115, 190}, // base plate max
            new int[] {45, 150, 255}, // yellow max
            new int[] {200, 50, 70}, // blue max
            new int[] {110, 54, 58}, // dot max

    };
    

//    private int[] mins = new int[]{0, 0, 0};
//    private int[] maxs = new int[]{100, 255, 255};
//
//    private int[] mins = new int[]{45, 70, 130};
//    private int[] maxs = new int[]{65, 115, 190};

    public RobotEntity(Constants.PitchType pitchType) {
        this.pitchType = pitchType;
    }

    /**
     * TODO: remove image from here
     * Thresholds the image so that only the wanted colors are white
     * and everything else is black in the resulting image.
     * @param images 3 source 1-channel images
     * @param image just for size
     * @return the resulting image
     */
    public IplImage threshold(IplImage[] images, IplImage image) {
        IplImage result = newImage(image, 1);
        IplImage temp = newImage(image, 1);

        cvInRangeS(image, cvScalar(mins[0][0], mins[0][1], mins[0][2], 0), cvScalar(maxs[0][0], maxs[0][1], maxs[0][2], 0), result);
        cvInRangeS(image, cvScalar(mins[1][0], mins[1][1], mins[1][2], 0), cvScalar(maxs[1][0], maxs[1][1], maxs[1][2], 0), temp);
        cvOr(result, temp, result, null);
        cvInRangeS(image, cvScalar(mins[2][0], mins[2][1], mins[2][2], 0), cvScalar(maxs[2][0], maxs[2][1], maxs[2][2], 0), temp);
        cvOr(result, temp, result, null);
        cvInRangeS(image, cvScalar(mins[3][0], mins[3][1], mins[3][2], 0), cvScalar(maxs[3][0], maxs[3][1], maxs[3][2], 0), temp);
        cvOr(result, temp, result, null);
//        cvErode(channel, channel, null, 1);
//        cvDilate(channel, channel, null, 4);
        return result;
    }

    @Override
    public IplImage detect(IplImage[] hsvImages) {
        return null;
    }

    @Override
    public IplImage findBlobs(IplImage binaryImage, int numOfBlobs) {
        return null;
    }
}
