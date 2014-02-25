package sdp.group2.vision;


import sdp.group2.util.Constants;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;


public class RobotEntity implements Detectable {
    private Constants.PitchType pitchType;

    int[][] mins = new int[][] {
            new int[] {45, 75, 102}, // base plate min

    };

    int[][] maxs = new int[][] {
            new int[] {100, 114, 140}, // base plate max

    };

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
        IplImage channel = ImageProcessor.newImage(image, 1);
        IplImage temp1 = ImageProcessor.newImage(image, 1);

        for (int m = 0; m < mins.length; m++) {
            for (int i = 0; i < 3 ; ++i) {
                int min = mins[m][i];
                int max = maxs[m][i];

                if (m == 0 && i == 0) {
                    if (min < 0) {
                        // This is a workaround for hue because it's circular
                        cvThreshold(images[i], channel, 180 + min, 255.0, CV_THRESH_BINARY);
                        cvThreshold(images[i], channel, max, 255.0, CV_THRESH_BINARY_INV);
                    } else {
                        cvInRangeS(images[i], cvScalar(min, 0, 0, 0), cvScalar(max, 0, 0, 0), channel);
                    }
                } else {
                    cvInRangeS(images[i], cvScalar(min, 0, 0, 0), cvScalar(max, 0, 0, 0), temp1);
                    cvAnd(channel, temp1, channel, null);
                }
            }
        }
        // Erode here to remove all the small white pixel chunks
        cvErode(channel, channel, null, 1);
        return channel;
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
