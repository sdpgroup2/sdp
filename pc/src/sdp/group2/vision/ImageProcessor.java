package sdp.group2.vision;

import com.googlecode.javacv.cpp.opencv_core.*;

import java.awt.image.BufferedImage;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;


public class ImageProcessor {

    private static final int MEDIAN_FILTER_SIZE = 3; // must be odd and > 1
    private static final String ASSETS_FOLDER = "./assets";

    // Matrices used for remapping
    CvMat distCoeffs = new CvMat(cvLoad(ASSETS_FOLDER + "/DistCoeffs.yml"));
    CvMat cameraMatrix = new CvMat(cvLoad(ASSETS_FOLDER + "/CameraMatrix.yml"));

    private static ImageViewer imageViewer = new ImageViewer();
    private static ImageViewer imageViewer2 = new ImageViewer();

    private static CvRect cropRect; // Cropping rectangle
    private static IplImage temp; // Temporary image used for processing
    private static IplImage[] hsvImages = new IplImage[3]; // each hsv channel stored
    private static IplImage image;
	private static Entity[] entities = new Entity[1];

    public ImageProcessor() {
    	// Ball
    	entities[0] = new Entity(new int[] {-10, 92, 140}, new int[] {10, 255, 255});
        cropRect = cvRect(30, 80, 590, 315);    
    }

    /**
     * TODO: Try doing this in-place
     * Un-distorts the image.
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
     * TODO: Try doing this in-place
     * Sets the region of interest (ROI) of the image.
     *
     * @param image    image to be set
     * @param cropRect region of interest
     */
    private static void crop(IplImage image, CvRect cropRect) {
        // TODO: do we need these checks? cropRect is final and size of image doesn't change
//        if (cropRect.width() + cropRect.x() > image.width()) {
//            cropRect.width(image.width() - cropRect.x());
//        }
//        if (cropRect.height() + cropRect.y() > image.height()) {
//            cropRect.height(image.height() - cropRect.y());
//        }
        cvSetImageROI(image, cropRect);
        // needs to be set on temp as well for future filters and stuff
        cvSetImageROI(temp, cropRect);
    }

    /**
     * TODO: Try doing this in-place
     * Normalises the image.
     *
     * @param image image to be normalised
     */
    private static void normalize(IplImage image) {
        cvNormalize(image, image);
    }

    /**
     * TODO: Try doing this in-place
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
    	IplImage channel = null;
        cvCvtColor(image, temp, CV_BGR2HSV);
        for (int i = 0; i < 3; ++i) {
            hsvImages[i] = newImage(temp, 1);
        }
        cvSplit(temp, hsvImages[0], hsvImages[1], hsvImages[2], null);
        for (Entity ent : entities) {
        	channel = ent.threshold(hsvImages, temp, cropRect);
        }      
        
        imageViewer.showImage(channel, BufferedImage.TYPE_BYTE_INDEXED);
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
     * @param inputImage image to be processed
     */
    public void process(BufferedImage inputImage) {
        image = IplImage.createFrom(inputImage);
        temp = newImage(image, 3);
        undistort(image, temp, cameraMatrix, distCoeffs);
        crop(image, cropRect);        
        //normalize(image);
        filter(image);
        detect(image,temp);
        imageViewer2.showImage(image, BufferedImage.TYPE_3BYTE_BGR);
    }

}
