package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_32F;
import static com.googlecode.javacv.cpp.opencv_core.cvCopy;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_core.cvScalarAll;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.awt.image.BufferedImage;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class ImageProcessor {

	private IplImage image;
	private static String dirRoot    = ".";
	private static String assetsFolder = dirRoot + "/assets";
		
	public ImageProcessor(BufferedImage inputImage) {

		image = IplImage.createFrom(inputImage);
	}

	public void undistort(long seq) {
		IplImage mapx, mapy = null;
		CvMat intrinsics = new CvMat(cvLoad(assetsFolder + "/Intrinsics.yml"));
	    CvMat distortion = new CvMat(cvLoad(assetsFolder + "/Distortion.yml"));
	    
	    if( intrinsics == null || distortion == null ) {
	           System.err.println("Can't open distortion info.");
	    } else {
	            mapx = IplImage.create(cvGetSize(image), IPL_DEPTH_32F, 1);
	            mapy = IplImage.create(cvGetSize(image), IPL_DEPTH_32F, 1);
	            cvInitUndistortMap(intrinsics, distortion, mapx, mapy);
		
		IplImage temp = newImage(image, 3);
	    if( mapx == null || mapy == null ) {
	            return;
	    } else {
	            cvCopy( image, temp );
	            cvRemap( temp, image, mapx, mapy, CV_INTER_LINEAR | CV_WARP_FILL_OUTLIERS, cvScalarAll(0) );
	    	}
	    }
	     
	    cvSaveImage(assetsFolder + "/images/IMG" + seq + ".jpg",image);
	}

    public void undistortAlt() {
        IplImage mapx, mapy = null;
        CvMat cameraMatrix = new CvMat(cvLoad(assetsFolder + "/CameraMatrix.yml"));
        CvMat distCoeffs = new CvMat(cvLoad(assetsFolder + "/DistCoeffs.yml"));

        if( cameraMatrix == null || distCoeffs == null ) {
            System.err.println("Can't open distortion info.");
        } else {
            mapx = IplImage.create(cvGetSize(image), IPL_DEPTH_32F, 1);
            mapy = IplImage.create(cvGetSize(image), IPL_DEPTH_32F, 1);
            cvInitUndistortMap(cameraMatrix, distCoeffs, mapx, mapy);

            IplImage temp = newImage(image, 3);
            if( mapx == null || mapy == null ) {
                return;
            } else {
                cvCopy( image, temp );
                cvRemap( temp, image, mapx, mapy, CV_INTER_LINEAR | CV_WARP_FILL_OUTLIERS, cvScalarAll(0) );
            }
        }

        ImageViewer imageViewer = new ImageViewer();
        imageViewer.showImage(image);
    }
	
    private static IplImage newImage( IplImage img, int channels ) {
        return IplImage.create( cvGetSize(img), img.depth(), channels );
    }
	
}
