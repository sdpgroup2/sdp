package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.IplImage;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import sdp.group2.geometry.Point;
import sdp.group2.geometry.Rect;


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
    Point findBlobs(IplImage binaryImage, int numOfBlobs);

    IplImage threshold(IplImage[] hsvImages, IplImage image);

    void drawBlobs(IplImage binaryImage, IplImage outputImage, int numOfBlobs);
    
    //IplImage findCentroid(IplImage binaryImage);

    //IplImage findBoundingBox(IplImage binaryImage);

}
