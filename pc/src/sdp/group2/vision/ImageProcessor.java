package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_32F;
import static com.googlecode.javacv.cpp.opencv_core.cvCopy;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_core.cvScalarAll;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_WARP_FILL_OUTLIERS;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvInitUndistortMap;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvRemap;

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

	public void undistort() {
	    
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
	    
	    cvSaveImage(assetsFolder + "/UndistortedImage.jpg",image);
	}
	
    private static IplImage newImage( IplImage img, int channels ) {
        return IplImage.create( cvGetSize(img), img.depth(), channels );
    }
	
}
