package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvAnd;
import static com.googlecode.javacv.cpp.opencv_core.cvSetImageROI;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_THRESH_BINARY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_THRESH_BINARY_INV;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvThreshold;

import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class Entity {

    private int[] maxs;
    private int[] mins;
    private int around = 50;

    public Entity(int[] mins, int[] maxs) {
    	this.maxs = maxs;
    	this.mins = mins;
    }
    
    public IplImage threshold(IplImage[] images, IplImage image, CvRect cropRect) {

        // For each pixel property (hue, saturation or value), find the pixels that lie
        // between the entity limits for that property.
        IplImage channel = ImageProcessor.newImage(image, 1);
        IplImage temp1 = ImageProcessor.newImage(image, 1);
        
        for( int i = 0; i < 3 ; ++ i ) {
//                cvSetImageROI( images[ i ], cropRect );
//                cvSetImageROI( channel,  cropRect );
//                cvSetImageROI( temp1,    cropRect );
                
                IplImage hsvi = images[ i ];
                
                int min = mins[i];
                int max = maxs[i];
                
                if( i == 0 ) {
                        cvThreshold( hsvi, channel, min, 255.0, CV_THRESH_BINARY );
                } else {
                        cvThreshold( hsvi, temp1, min, 255.0, CV_THRESH_BINARY );
                        cvAnd( channel, temp1, channel, null );
                }
                
//                cvThreshold( hsvi, temp1, max, 255.0, CV_THRESH_BINARY_INV );
//                cvAnd( channel, temp1, channel, null );
        }
        
        return channel;
    }

}
