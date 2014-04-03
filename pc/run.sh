#/bin/bash

echo -n "Yellow [Y/n]?"
read ANS1
if [ "$ANS1" = "n" ]; then
  echo "Blue team";
  TEAM=1;
else
  echo "Yellow team";
  TEAM=0;
fi

echo -n "Side pitch[Y/n]?"
read ANS2
if [ "$ANS2" = "n" ]; then
  echo "Main pitch";
  PITCH=0;
else
  echo "Side pitch";
  PITCH=1;
fi

echo -n "Playing on the left[Y/n]?"
read ANS3
if [ "$ANS3" == "n" ]; then
  echo "Playing on the right";
  SIDE=1;
else
  echo "Playing on the left";
  SIDE=0;
fi

PC=`hostname | cut -d '.' -f 1`;
echo -n "Using $PC computer.\n"

echo "Starting...\n"
ant run -Dteam=$TEAM -Dpitch=$PITCH -Dpc=$PC -Dside=$SIDE
