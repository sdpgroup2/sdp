package sdp.group2.vision;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_imgproc.*;
import sdp.group2.geometry.Point;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static sdp.group2.util.Constants.PitchType;
import static sdp.group2.vision.ImageProcessor.newImage;


public class BallEntity implements Detectable {

    private int[] mins = new int[]{-10, 92, 140};
    private int[] maxs = new int[]{10, 256, 256};



    private PitchType pitchType;
    private CvMemStorage storage = CvMemStorage.create();

    public BallEntity(PitchType pitchType) {
        this.pitchType = pitchType;
    }

    // TODO: do we need image here?
    public IplImage threshold(IplImage[] images, IplImage image) {
        IplImage channel = newImage(image, 1);
        IplImage temp1 = newImage(image, 1);

        for (int i = 0; i < 3; ++i) {
            int min = mins[i];
            int max = maxs[i];

            if (i == 0) {
                if (min < 0) {
                    // This is a workaround for hue because it's circular
                    cvThreshold(images[i], channel, 180 + min, 255.0, CV_THRESH_BINARY);
                    cvThreshold(images[i], temp1, max, 255.0, CV_THRESH_BINARY_INV);
                    cvOr(temp1, channel, channel, null);
                } else {
                    cvInRangeS(images[i], cvScalar(min, 0, 0, 0), cvScalar(max, 0, 0, 0), channel);
                }
            } else {
                cvInRangeS(images[i], cvScalar(min, 0, 0, 0), cvScalar(max, 0, 0, 0), temp1);
                cvAnd(channel, temp1, channel, null);
            }
        }

        // Erode here to remove all the small white pixel chunks
        cvErode(channel, channel, null, 3);
        cvDilate(channel, channel, null, 1);
        return channel;
    }

    public Point segment(IplImage channel) {
        CvSeq contours = new CvSeq(null);
//        cvCanny(channel, channel, 100, 100, 3);
        cvFindContours(channel, storage, contours, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
        if (contours.isNull() || contours.total() == 0) {
            return null;
        }
        CvSeq firstContour = contours.h_next();
        CvMoments moments = new CvMoments();
        cvMoments(firstContour, moments, 0);
        Point point = new Point(moments.m10() / moments.m00(), moments.m01() / moments.m00());
        return point;
    }

    @Override
    public IplImage detect(IplImage[] hsvImages) {
        //IplImage binImage = threshold(hsvImages);
        //findBlobs(binImage, 1);
        return null;
    }

    @Override
    public IplImage findBlobs(IplImage binaryImage, int numOfBlobs) {
        Blobs regions = new Blobs();
        regions.BlobAnalysis(
                binaryImage,                // image
                -1, -1,                     // ROI start col, row
                -1, -1,                     // ROI cols, rows
                1,                          // border (0 = black; 1 = white)
                0);                         // minarea
        regions.PrintRegionData();
        return null;
    }
}
