package sdp.group2.vision;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.WindowConstants;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

/* Class taken from SDP group 9 2012*/


/** VERY thin wrapper around CanvasFrame. This allows images to be shown using the showImage method(). */
public class ImageViewer extends CanvasFrame {
        private static final long serialVersionUID = -8624000383921431065L;
        
        private ArrayList< NewBufferedImageListener > buffListeners;
        private ArrayList< NewIplImageListener >      iplListeners;
        
        public interface NewBufferedImageListener {
                public void newBufferedImage( Graphics2D graphics );
        }
        
        public interface NewIplImageListener {
                public void newIplImage( IplImage image );
        }

        public ImageViewer() {
                super( "Video Feed" );
                setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
                
                buffListeners = new ArrayList<ImageViewer.NewBufferedImageListener>();
                iplListeners  = new ArrayList<ImageViewer.NewIplImageListener>();
        }
        
        public void addNewBufferedImageListener( NewBufferedImageListener newBufferedImageListener ) {
                buffListeners.add( newBufferedImageListener );
        }
        
        public void addNewIplImageListener( NewIplImageListener newIplImageListener ) {
                iplListeners.add( newIplImageListener );
        }
        
        public void removeNewBufferedImageListener( NewBufferedImageListener newBufferedImageListener ) {
                buffListeners.remove( newBufferedImageListener );
        }
        
        public void removeNewIplImageListener( NewIplImageListener newIplImageListener ) {
                iplListeners.remove( newIplImageListener );
        }
        
        public void showImage( IplImage iplImage ) {
                if( iplImage != null ) {
                        for( NewIplImageListener l : iplListeners )
                                l.newIplImage( iplImage );
                        
                        BufferedImage buffImg = iplImage.getBufferedImage();
                        Graphics2D    g       = buffImg.createGraphics();
                        
                        for( NewBufferedImageListener l : buffListeners )
                                l.newBufferedImage( g );
                        
                        if( buffImg.getWidth() != getCanvas().getWidth() || buffImg.getHeight() != getCanvas().getHeight() ) {
                                getCanvas().setSize( buffImg.getWidth(), buffImg.getHeight() );
                                pack();
                        }
                        
                        super.showImage( buffImg );
                }
        }
        
        public void showImage( BufferedImage buffImg ) {
                if( buffImg != null ) {
                        Graphics2D g = buffImg.createGraphics();
                        
                        IplImage iplImage = IplImage.createFrom( buffImg );
                        
                        for( NewIplImageListener l : iplListeners )
                                l.newIplImage( iplImage );
                        
                        buffImg = iplImage.getBufferedImage();
                        
                        for( NewBufferedImageListener l : buffListeners )
                                l.newBufferedImage( g );
                        
                        if( buffImg.getWidth() != getCanvas().getWidth() || buffImg.getHeight() != getCanvas().getHeight() ) {
                                getCanvas().setSize( buffImg.getWidth(), buffImg.getHeight() );
                                pack();
                        }
                        iplImage.release();
                        iplImage = null;
                        super.showImage( buffImg );
                }
        }
        
}