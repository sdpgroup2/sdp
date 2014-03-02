package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_32F;
import static com.googlecode.javacv.cpp.opencv_core.cvConvertScale;
import static com.googlecode.javacv.cpp.opencv_core.cvCopy;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_core.cvMerge;
import static com.googlecode.javacv.cpp.opencv_core.cvRect;
import static com.googlecode.javacv.cpp.opencv_core.cvScalarAll;
import static com.googlecode.javacv.cpp.opencv_core.cvSetImageROI;
import static com.googlecode.javacv.cpp.opencv_core.cvSplit;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2HSV;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_HSV2BGR;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_WARP_FILL_OUTLIERS;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvEqualizeHist;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvInitUndistortMap;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvRemap;
import static com.googlecode.javacv.cpp.opencv_imgproc.medianBlur;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import sdp.group2.util.Tuple;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class ImageProcessor {

    private static final int MEDIAN_FILTER_SIZE = 3; // must be odd and > 1
    private static final String ASSETS_FOLDER = "./assets";
    
    // Matrices used for remapping
    CvMat cameraMatrix = new CvMat(cvLoad(ASSETS_FOLDER + "/CameraMatrix.yml"));
    CvMat distCoeffs = new CvMat(cvLoad(ASSETS_FOLDER + "/DistCoeffs.yml"));
    
    private static ImageViewer imageViewer = new ImageViewer();
    private static ImageViewer[] entityViewers = new ImageViewer[2];

    private static CvRect cropRect; // Cropping rectangle
    private static IplImage temp; // Temporary image used for processing
    private static IplImage image; // Processing image
    private static BallEntity ballEntity; // Ball thresholding
    private static RobotEntity robotEntity; // Robot thresholding
    
    CvPoint ballCentroid;
    List<Tuple<CvRect, CvPoint>> robotTuples = new ArrayList<Tuple<CvRect, CvPoint>>();

    public ImageProcessor() {
        ballEntity = new BallEntity();
        robotEntity = new RobotEntity();
        entityViewers[0] = new ImageViewer();
        entityViewers[1] = new ImageViewer();
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
    	cvSetImageROI(image, cropRect);
    	IplImage cropped = newImage(image, 3);
    	IplImage cropped2 = newImage(image, 3);
    	//Copy original image (only ROI) to the cropped image
    	image = cropped;
    	temp = cropped2;
//        cvSetImageROI(image, cropRect);
        // needs to be set on temp as well for future filters and stuff
//        cvSetImageROI(temp, cropRect);
    }

    /**
     * DON'T USE THIS
     * Normalises the image.
     *
     * @param image image to be normalised
     * @return 
     */
   
    private static void normalize(IplImage image) {
    	cvCvtColor(image, image, CV_BGR2HSV);
    	IplImage[] channels = new IplImage[3];
    	for (int i = 0; i < 1; i++) {
			channels[i] = newImage(image, 1);
		}
    	cvSplit(image, channels[0], channels[1], channels[2], null);
    	for (int i = 0; i < 1; i++) {
    		cvEqualizeHist(channels[i], channels[i]);
		}
    	cvMerge(channels[0], channels[1], channels[2], null, image);
    	cvCvtColor(image, image, CV_HSV2BGR);
//        cvNormalize(image, image);
//        cvNormalize(image, image, 0, 255, CV_MINMAX, null);
    		
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
     * Detects the objects on the image
     *
     * @param image image to be inspected
     * @param temp  temporary image
     */
    private void detect(IplImage image, IplImage temp) {
        IplImage channel = newImage(image, 1);
        // Convert from BGR to HSV
        cvCvtColor(image, temp, CV_BGR2HSV);
        
        channel = ballEntity.threshold(temp);
//        ballEntity.drawContours(channel, image, 10);
        ballCentroid = ballEntity.findCentroid(channel);
        if (channel != null) {
        	entityViewers[0].showImage(channel, BufferedImage.TYPE_BYTE_INDEXED);
        }
        
        channel = robotEntity.threshold(temp);
        robotTuples = robotEntity.detectRobots(channel);
        if (channel != null) {
        	entityViewers[1].showImage(channel, BufferedImage.TYPE_BYTE_INDEXED);
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
     * Creates a new IplImage same size as the source image with a given ROI
     *
     * @param img      source image
     * @param roiRect ROI rectangle
     * @param channels number of channels
     * @return newly created image
     */
    public static IplImage newImage(IplImage img, CvRect roiRect, int channels) {
        IplImage image = newImage(img, channels);
        cvSetImageROI(image, roiRect);
        return image;
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
        cvConvertScale(image, image, 2, 0);
        filter(image);
        detect(image, temp);
        imageViewer.showImage(image, BufferedImage.TYPE_3BYTE_BGR);
    }

}
