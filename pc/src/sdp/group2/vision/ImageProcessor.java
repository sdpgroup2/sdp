package sdp.group2.vision;

import com.googlecode.javacv.cpp.opencv_core.CvArr;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.awt.image.BufferedImage;

import sdp.group2.geometry.Point;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;


public class ImageProcessor {

    private static final int MEDIAN_FILTER_SIZE = 3; // must be odd and > 1
    private static final String ASSETS_FOLDER = "./assets";
    CvMat cameraMatrix = new CvMat(cvLoad(ASSETS_FOLDER + "/CameraMatrix.yml"));
    // Matrices used for remapping
    CvMat distCoeffs = new CvMat(cvLoad(ASSETS_FOLDER + "/DistCoeffs.yml"));
    private static ImageViewer imageViewer = new ImageViewer();
    private static ImageViewer[] entityViewers = new ImageViewer[1];

    private static CvRect cropRect; // Cropping rectangle
    private static IplImage temp; // Temporary image used for processing
    private static IplImage[] hsvImages = new IplImage[3]; // each hsv channel stored
    private static IplImage image;
    private static Entity[] entities = new Entity[1];

    public ImageProcessor() {
        // Ball
    	entities[0] = new Entity(new int[] {-10, 92, 140}, new int[] {10, 256, 256});
//        entities[1]= new Entity(new int[] {45, 75, 102}, new int[] {100, 114, 140});
        entityViewers[0] = new ImageViewer();
//        entityViewers[1] = new ImageViewer();
        cropRect = cvRect(30, 80, 590, 315);    
    }

    /**
     * Un-distorts the image. Cannot be done in-place.
     *
     * @param image        image to be un-distorted
     * @param temp         temporary image
     * @param cameraMatrix camera matrix input from calibration
     * @param distCoeffs   distortion coefficients input from calibration
     */
    public static void undistort(IplImage image, IplImage temp, CvMat cameraMatrix, CvMat distCoeffs) {
        if (cameraMatrix == null || distCoeffs == null) {
            System.err.println("Can't open distortion info.");
        } else {
            IplImage remapX = IplImage.create(cvGetSize(image), IPL_DEPTH_32F, 1);
            IplImage remapY = IplImage.create(cvGetSize(image), IPL_DEPTH_32F, 1);

            // Initialise the undistortion map
            cvInitUndistortMap(cameraMatrix, distCoeffs, remapX, remapY);

            if (remapX != null && remapY != null) {
                cvCopy(image, temp);
                // Apply the undistortion map
                cvRemap(temp, image, remapX, remapY, CV_INTER_LINEAR | CV_WARP_FILL_OUTLIERS, cvScalarAll(0));
            }
        }
    }

    /**
     * Sets the region of interest (ROI) of the image.
     *
     * @param image    image to be set
     * @param cropRect region of interest
     */
    private static void crop(IplImage image, CvRect cropRect) {
        // TODO: do we need these checks? cropRect is final and size of image doesn't change
        cvSetImageROI(image, cropRect);
        // needs to be set on temp as well for future filters and stuff
        cvSetImageROI(temp, cropRect);
    }

    /**
     * Normalises the image.
     *
     * @param image image to be normalised
     * @return 
     */
   
    private static void normalize(IplImage image) {
    	cvCvtColor(image, image, CV_BGR2HSV);
    	IplImage[] channels = new IplImage[3];
    	for (int i = 1; i < channels.length; i++) {
			channels[i] = newImage(image, 1);
		}
    	cvSplit(image, channels[0], channels[1], channels[2], null);
    	for (int i = 1; i < channels.length; i++) {
    		cvEqualizeHist(channels[i], channels[i]);
		}
    	cvMerge(channels[0], channels[1], channels[2], null, image);
    	cvCvtColor(image, image, CV_HSV2BGR);
//        cvNormalize(image, image);
    }

    /**
     * Filters the image with the median blur. This should remove noise
     *
     * @param image image to be filtered
     */
    private static void filter(IplImage image) {
        medianBlur(image, image, MEDIAN_FILTER_SIZE);
    }

    /**
     * TODO: Try doing this in-place
     * Detects the objects on the image
     *
     * @param image image to be inspected
     * @param temp  temporary image
     */
    private static void detect(IplImage image, IplImage temp) {
//        IplImage channel = null;
    	Point point = null;
        cvCvtColor(image, temp, CV_BGR2HSV);
        for (int i = 0; i < 3; ++i) {
            hsvImages[i] = newImage(temp, 1);
        }
        cvSplit(temp, hsvImages[0], hsvImages[1], hsvImages[2], null);
        for (int i = 0; i < entities.length; i++) {
            point = entities[i].threshold(hsvImages, temp, cropRect);
            if (point != null) {
            	cvRectangle(image, cvPoint((int) point.x - 10, (int) point.y - 10), cvPoint((int) point.x + 10, (int) point.y + 10), cvScalar(0, 0, 255, 0), 1, 8, 0);
            	entityViewers[i].showImage(image, BufferedImage.TYPE_3BYTE_BGR);
            }
		}
    }

    /**
     * Creates a new IplImage same size as the source image.
     *
     * @param img      source image
     * @param channels number of channels
     * @return newly created image
     */
    public static IplImage newImage(IplImage img, int channels) {
        return IplImage.create(cvGetSize(img), img.depth(), channels);
    }

    /**
     * Processes the image.
     *
     * @param inputImage image to be processed
     */

    public void process(BufferedImage inputImage) {
        image = IplImage.createFrom(inputImage);
        temp = newImage(image, 3);
        undistort(image, temp, cameraMatrix, distCoeffs);
        crop(image, cropRect);
//        normalize(image);
        filter(image);
//        detect(image, temp);
        imageViewer.showImage(image, BufferedImage.TYPE_3BYTE_BGR);
    }

}
