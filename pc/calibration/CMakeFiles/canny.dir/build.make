# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 2.8

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list

# Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/local/Cellar/cmake/2.8.12.2/bin/cmake

# The command to remove a file.
RM = /usr/local/Cellar/cmake/2.8.12.2/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The program to use to edit the cache.
CMAKE_EDIT_COMMAND = /usr/local/Cellar/cmake/2.8.12.2/bin/ccmake

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /Users/mark/sdp/pc/calibration

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /Users/mark/sdp/pc/calibration

# Include any dependencies generated for this target.
include CMakeFiles/canny.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/canny.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/canny.dir/flags.make

CMakeFiles/canny.dir/canny.cpp.o: CMakeFiles/canny.dir/flags.make
CMakeFiles/canny.dir/canny.cpp.o: canny.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /Users/mark/sdp/pc/calibration/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object CMakeFiles/canny.dir/canny.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/canny.dir/canny.cpp.o -c /Users/mark/sdp/pc/calibration/canny.cpp

CMakeFiles/canny.dir/canny.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/canny.dir/canny.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /Users/mark/sdp/pc/calibration/canny.cpp > CMakeFiles/canny.dir/canny.cpp.i

CMakeFiles/canny.dir/canny.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/canny.dir/canny.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /Users/mark/sdp/pc/calibration/canny.cpp -o CMakeFiles/canny.dir/canny.cpp.s

CMakeFiles/canny.dir/canny.cpp.o.requires:
.PHONY : CMakeFiles/canny.dir/canny.cpp.o.requires

CMakeFiles/canny.dir/canny.cpp.o.provides: CMakeFiles/canny.dir/canny.cpp.o.requires
	$(MAKE) -f CMakeFiles/canny.dir/build.make CMakeFiles/canny.dir/canny.cpp.o.provides.build
.PHONY : CMakeFiles/canny.dir/canny.cpp.o.provides

CMakeFiles/canny.dir/canny.cpp.o.provides.build: CMakeFiles/canny.dir/canny.cpp.o

# Object files for target canny
canny_OBJECTS = \
"CMakeFiles/canny.dir/canny.cpp.o"

# External object files for target canny
canny_EXTERNAL_OBJECTS =

canny: CMakeFiles/canny.dir/canny.cpp.o
canny: CMakeFiles/canny.dir/build.make
canny: /usr/local/lib/libopencv_videostab.2.4.7.dylib
canny: /usr/local/lib/libopencv_video.2.4.7.dylib
canny: /usr/local/lib/libopencv_ts.a
canny: /usr/local/lib/libopencv_superres.2.4.7.dylib
canny: /usr/local/lib/libopencv_stitching.2.4.7.dylib
canny: /usr/local/lib/libopencv_photo.2.4.7.dylib
canny: /usr/local/lib/libopencv_ocl.2.4.7.dylib
canny: /usr/local/lib/libopencv_objdetect.2.4.7.dylib
canny: /usr/local/lib/libopencv_nonfree.2.4.7.dylib
canny: /usr/local/lib/libopencv_ml.2.4.7.dylib
canny: /usr/local/lib/libopencv_legacy.2.4.7.dylib
canny: /usr/local/lib/libopencv_imgproc.2.4.7.dylib
canny: /usr/local/lib/libopencv_highgui.2.4.7.dylib
canny: /usr/local/lib/libopencv_gpu.2.4.7.dylib
canny: /usr/local/lib/libopencv_flann.2.4.7.dylib
canny: /usr/local/lib/libopencv_features2d.2.4.7.dylib
canny: /usr/local/lib/libopencv_core.2.4.7.dylib
canny: /usr/local/lib/libopencv_contrib.2.4.7.dylib
canny: /usr/local/lib/libopencv_calib3d.2.4.7.dylib
canny: /usr/local/lib/libopencv_nonfree.2.4.7.dylib
canny: /usr/local/lib/libopencv_ocl.2.4.7.dylib
canny: /usr/local/lib/libopencv_gpu.2.4.7.dylib
canny: /usr/local/lib/libopencv_photo.2.4.7.dylib
canny: /usr/local/lib/libopencv_objdetect.2.4.7.dylib
canny: /usr/local/lib/libopencv_legacy.2.4.7.dylib
canny: /usr/local/lib/libopencv_video.2.4.7.dylib
canny: /usr/local/lib/libopencv_ml.2.4.7.dylib
canny: /usr/local/lib/libopencv_calib3d.2.4.7.dylib
canny: /usr/local/lib/libopencv_features2d.2.4.7.dylib
canny: /usr/local/lib/libopencv_highgui.2.4.7.dylib
canny: /usr/local/lib/libopencv_imgproc.2.4.7.dylib
canny: /usr/local/lib/libopencv_flann.2.4.7.dylib
canny: /usr/local/lib/libopencv_core.2.4.7.dylib
canny: CMakeFiles/canny.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX executable canny"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/canny.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/canny.dir/build: canny
.PHONY : CMakeFiles/canny.dir/build

CMakeFiles/canny.dir/requires: CMakeFiles/canny.dir/canny.cpp.o.requires
.PHONY : CMakeFiles/canny.dir/requires

CMakeFiles/canny.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/canny.dir/cmake_clean.cmake
.PHONY : CMakeFiles/canny.dir/clean

CMakeFiles/canny.dir/depend:
	cd /Users/mark/sdp/pc/calibration && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /Users/mark/sdp/pc/calibration /Users/mark/sdp/pc/calibration /Users/mark/sdp/pc/calibration /Users/mark/sdp/pc/calibration /Users/mark/sdp/pc/calibration/CMakeFiles/canny.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/canny.dir/depend
