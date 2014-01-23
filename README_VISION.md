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
    command: `export LD_LIBRARY_PATH=/afs/inf.ed.ac.uk/user/sXX/sXXXXXXX/Downloads/v4l4j-0.8.10`  
    Make sure to replace the 'X's with the appropriate numbers for your home
    directory.

2.  Once test-gui has opened a window, change the value of the **Inputs** option
    from 'Composite0' to 'S-Video'.
