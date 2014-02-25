package sdp.group2.vision;

import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public interface Detectable {

    /**
     * Runs the source images through an algorithm and returns
     * a binary image with only the things to be detected
     * set to 1 and everything else set to 0.
     *
     * @param hsvImages 3 1-channel (H, S, V) images
     * @return resulting binary image
     */
    IplImage detect(IplImage[] hsvImages);

    /**
     * Finds a given number of blobs on the binary image.
     *
     * @param binaryImage image to be searched
     * @param numOfBlobs  number of blobs to look for
     * @return
     */
    CvSeq findContours(IplImage binaryImage);

    void drawContours(IplImage binaryImage, IplImage outputImage);
    
    //IplImage findCentroid(IplImage binaryImage);

    //IplImage findBoundingBox(IplImage binaryImage);

}
