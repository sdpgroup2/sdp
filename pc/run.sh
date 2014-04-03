#/bin/bash

echo -n "Yellow [Y/n]?"
read ANS1
if [ "$ANS1" = "n" ]; then
  echo "Blue team";
  TEAM="blue";
else
  echo "Yellow team";
  TEAM="yellow";
fi

echo -n "Side pitch[Y/n]?"
read ANS2
if [ "$ANS2" = "n" ]; then
  echo "Main pitch";
  PITCH="main";
else
  echo "Side pitch";
  PITCH="side";
fi

echo -n "Playing on the left[Y/n]?"
read ANS3
if [ "$ANS3" == "n" ]; then
  echo "Playing on the right";
  SIDE="right";
else
  echo "Playing on the left";
  SIDE="left";
fi

PC=`hostname | cut -d '.' -f 1`;
echo -n "Using $PC computer.\n"

echo "Starting...\n"
CMD="ant run -Dteam=$TEAM -Dpitch=$PITCH -Dpc=$PC -Dside=$SIDE"
echo "Running: $CMD"
$CMD
