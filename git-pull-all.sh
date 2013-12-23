#!/bin/sh
cd ..
for dir in `ls -d [^git]*`;
do
cd $dir
echo `pwd`
git pull
cd ..
done
cd IMP-North-Implemented
