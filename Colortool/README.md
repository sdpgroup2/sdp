This is a little tool to make it slightly easier to get the threshold colors
for the vision system.

To run
======

In this folder, use the command:

python colortool.py <filename>

Where <filename> is the name of an image file.

To use
======

Select regions with the mouse buttons.

Left mouse button to start a polygon selection.  
Right mouse button to end a polygon selection.  

Right mouse button to start a circle selection.  
Any mouse button to end a circle selection.  

After selecting a region, in the terminal, you will see the minimum and maximum
colors, int the correct format: 180 is the maximum hue, 255 is the maximum
saturation and value.

Use **CTRL+Z** to undo. Use **BACKSPACE** to clear the selections, and use
**RETURN** or any other key to print the colors to the terminal again.
