#!/bin/sh
cd ..
for dir in `ls -d [^git]*`;
do
cd $dir
echo `pwd`
git push
cd ..
done
cd IMP-North-Implemented
