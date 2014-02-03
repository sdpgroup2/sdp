V4L4J
=====

If you're working on vision, the library to use seems to be V4L4J. I've already
placed it in the project so it should work fine. If you want to install it and
test it yourself - outside of the project - though, there's a forum topic that
explains how:

[HOWTO: Viewing/Capturing the camera feed using V4L4J](https://www.forums.ed.ac.uk/viewtopic.php?f=641&t=7182&sid=f477593c9a7a411719ac0c5b2326dd0a)

There are a couple of other things you need to do that aren't immediately
obvious.

1.  Just before you run 'ant test-gui', you have to use the following console
    command: `export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/afs/inf.ed.ac.uk/user/sXX/sXXXXXXX/Downloads/v4l4j-0.8.10`.
    Make sure to replace the 'X's with the appropriate numbers for your home
    directory. Alternatively, you can add the command into your ~/.bashrc or
    ~/.bash_profile files and it will get applied every time you start a new
    terminal session. Thus you don't need to re-run the command every time.

2.  Once test-gui has opened a window, change the value of the **Inputs** option
    from 'Composite0' to 'S-Video'.

Using V4L4J with Eclipse (in Linux)
===================================
Projects using V4L4J won't work unless you `export LD_LIBRARY_PATH` as
mentioned above. To do this within Eclipse:

1.  Right click on the project, go to **Run As** then click
    **Run Configurations...**.

2.  Go to the **Environment** tab and add click the **New** button

3.  Set the name to `LD_LIBRARY_PATH` and the value to `{project_loc}/lib`

Now it should run properly.

How the current vision system works
===================================

At the moment, every time a new video frame is handed to the program:

1.  Every pixel is tested by each cluster. A cluster is just a set of pixels
    that pass a certain test. So if the pixel is red, that coordinate is placed
    into the BallCluster, etc.

2.  Within each cluster, the pixels are split into disjoint regions. All
    pixels touching each other are in a set together.

3.  For each region of pixels, a rectangle is returned which gives us the
    size and position of every object on the pitch.

That's a fairly abstract description so:

PixelCluster
------------
BallCluster, RobotCluster, PitchLines and PitchSection are all examples of
this. Most of the functionality is within AbstractPixelCluster, so look there
to really understand it.

The most important function is:
`public boolean testPixel(int x, int y, Color color)`

This will call the colorTest function (which differs between different kinds
of cluster) to determine whether the pixel should be included in the cluster.
If so, it adds that pixel and returns true.

To get the rectangles of objects in the cluster use:
`public List<Rect> getRects(int minLength, int maxLength,
                            int minBreadth, int maxBreadth,
                            float minFill, float maxFill)`

This will return rectangles for every region of pixels in the cluster. Using
the min/max length/breadth parameters, you can filter out rectangles which are
too big or small. The minFill and maxFill refer to how much of the rectangle
is filled with pixels. The value 0.5 for example means a rectangle which is
half-full of pixels.

VisionSystem
------------
This class sets up the window that displays the video feed and also
processes the image.

It creates a SkyCam object (which is just a simple wrapper around V4L4J's
VideoDevice) and starts taking video input with `skyCam.startVision(this)`.

Once that function has been called, whenever a new frame of video is
available, the `nextFrame` function is automatically called.

This function does the following:

1.  Gets an image from the video frame.
2.  Loads the colour values into an array (which is faster than checking the
    image itself for every pixel).
3.  Calls `processImage`.
4.  Shows the image in the window.

The `processImage` function does the following:

1.  Clears all the clusters (because we don't care about pixels from last
    frame.)
2.  Loops through all the pixels, putting them into the clusters by calling
    `testPixel` for each one.
3.  From the clusters, finds the locations of the robots and the ball.
4.  Does nothing else, because we haven't written any AI yet.

Hopefully this was a helpful description of the vision system so far. If you
think something is unclear here, feel free to edit it to make it moreso.

The GUI
-------
If you run VisionSystem as main, you'll get a window with the camera feed.
To change the parameters of the vision, click one of the clusters from the menu
at the top right, then adjust the sliders for the minimum and maximum HSB
colours. After that, click the update button to see how well the new values
work.

Note that these changes aren't permanent, they'll reset when you restart the
program. To permanently change them, you'll have to change the constructor in
the cluster's class.
