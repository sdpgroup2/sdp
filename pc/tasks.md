Tasks to be done before the Frendlies
========================================

These are the tasks that we should aim to accomplish before the next assesment, 26/2.


### Team stuff

 1. We shouldn't be working on our own branches with our own copies of certain classes and then try to merge them. We are working on one project so when you want to work on something someone else is working on, contact them and let them know. Commit and push often so people know what you are doing. Don't develop locally and then try to merge your stuff after few days of work.

 1. If you don't know what to do or don't know how something works, ask someone. Even better, find something that interests you and let other people know/get their opinion. There's a ton of stuff we need to do and not knowing how everything works shouldn't be an excuse anymore. There isn't anyone who will tell you what to do.


### Vision

 1. Improve the vision recognition algorithms so that they perform reliably. This includes possibly start using the OpenCV library (the sooner the better). Possible candidates are: [findContours](http://docs.opencv.org/doc/tutorials/imgproc/shapedescriptors/find_contours/find_contours.html), [convexHull](http://docs.opencv.org/doc/tutorials/imgproc/shapedescriptors/hull/hull.html) and so on.

 1. Stop storing three variants of the image from the video feed. We are currently storing an RGB representation, an HSB Representation and a BufferedImage. Instead we should store only one - ideally only the RGB array or the BufferedImage bundled in a class with the following methods:
    - `<Wrapper> getRGB(int x, int y)` - returns the RGB representation of the pixel at coordinates x and y
    - `<Wrapper> getHSB(int x, int y)` - returns the HSB representation of the pixel at coordinates x and y
    - `int get(int x, y)` - returns the int value at coordinates x and y

    The getRGB and getHSB methods should return some Wrapper class that can enclose all three values (java.awt.Color seems like an ideal candidate, maybe subclass so we have more HSB methods). This will consume less memory. Both of these classes should be unit-tested - a lot will depend on them.

 1. Remove BlueRobotCluster, YellowRobotCluster, RobotCluster, BallCluster etc. This will result in us having lot less 'special' classes and more clarity where the object is modified. These can all just be replaced by one cluster with different constructor calls and the constants abstracted into Constants.java, e.g.:
    -  `Cluster blueRobotCluster = new Cluster("Blue Robots", ROBOT.MIN_WIDTH, ROBOT.MIN_HEIGHT, ROBOT.MAX_WIDTH, ROBOT.MAX_HEIGHT, BLUE_ROBOT.MIN_COLOR, BLUE_ROBOT.MAX_COLOR)`.

 1. Rename getImportantRects of Cluster class to something more reasonable and less annoying, e.g. two methods:
    - `Rect getBestRect()` - gives you the most important rectangle
    - `List<Rect> getBestRects(int n)` - gives you the `n` most important rectangles (ideally sorted by their "importance").

 1. Enable running the GUI at all times. Currently, the GUI and the Service are two separate things. Really the GUI should be a component of the service, ready to be enabled when we need to debug and disabled when we don't (e.g. for the matches). This could be accomplished by adding a VisionGUI variable to the `VisionService` which could be initialised based on if some parameter is set. Furthermore, we should move all the updating of the `VisionGUI` into a method `updateGUI` in `VisionService` and call that method only if the aforementioned parameter is set. This method **must** to be asynchronous, maybe utilising [SwingWorker](http://download.oracle.com/javase/6/docs/api/javax/swing/SwingWorker.html).


### Planner

 1. Research what previous teams did in terms of algorithms - how to pick the next action and so on.

 2. Get some basic idea about the state of the pitch. Implement functions such as:
    - `foeAttackerHasBall()`, `foeDefenderHasBall()`, etc.
    - `isDefenderBlocked()`, `isAttackerBlocked()` - when not possible to kick the ball with a horizontal kick

 3. Add some basic actions for the robots:
    - Defender:
        1. Mirror enemy
        2. Run up and down in front of the goal
    - Attacker:
        1. Try to score immediately - turn until facing the goal and then kick
        2. Go around defender - go left or right until not blocked by their defender and then kick


### Robot Design

 1. Research what to spend money on. I know some other teams are already shopping. Things that may be worth buying are:
    - better motor for kicking
    - better comms device (not sure what the current delay is)
    - those wheels that allow you to move in all directions

 1. The robot should be able to pick up the ball and roam around with it prepared to shoot when it's facing the goal. That means it will require some mechanism that can grab the ball and kick it (not neccessarily with one motor - we have some money to spend).

 1. It needs to be more stable. Currently it could fall over if you pushed it strongly enough. Four wheels should give us more stability than three.
