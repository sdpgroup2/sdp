package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvAnd;
import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvOr;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvSplit;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_THRESH_BINARY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_THRESH_BINARY_INV;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvDilate;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvErode;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvThreshold;
import static sdp.group2.vision.ImageProcessor.newImage;

import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class BallEntity extends Entity {

    private int[] mins = new int[]{-10, 92, 140};
    private int[] maxs = new int[]{10, 256, 256};
    private static IplImage[] hsvImages = new IplImage[3];

    private CvMemStorage storage = CvMemStorage.create();


    public IplImage threshold(IplImage image) {
        for (int i = 0; i < 3; ++i) {
            hsvImages[i] = newImage(image, 1);
        }
        // Split 3-channel image into 3 1-channel images
        cvSplit(image, hsvImages[0], hsvImages[1], hsvImages[2], null);
        IplImage channel = newImage(image, 1);
        IplImage temp1 = newImage(image, 1);

        for (int i = 0; i < 3; ++i) {
            int min = mins[i];
            int max = maxs[i];

            if (i == 0) {
                if (min < 0) {
                    // This is a workaround for hue because it's circular
                    cvThreshold(hsvImages[i], channel, 180 + min, 255.0, CV_THRESH_BINARY);
                    cvThreshold(hsvImages[i], temp1, max, 255.0, CV_THRESH_BINARY_INV);
                    cvOr(temp1, channel, channel, null);
                } else {
                    cvInRangeS(hsvImages[i], cvScalar(min, 0, 0, 0), cvScalar(max, 0, 0, 0), channel);
                }
            } else {
                cvInRangeS(hsvImages[i], cvScalar(min, 0, 0, 0), cvScalar(max, 0, 0, 0), temp1);
                cvAnd(channel, temp1, channel, null);
            }
        }

        // Erode here to remove all the small white pixel chunks
        cvErode(channel, channel, null, 3);
        cvDilate(channel, channel, null, 1);
        return channel;
    }


	@Override
	public IplImage detect(IplImage[] hsvImages) {
		// TODO Auto-generated method stub
		return null;
	}
}
