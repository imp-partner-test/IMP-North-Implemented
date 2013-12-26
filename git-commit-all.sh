#!/bin/sh
cd ..
for dir in `ls -d [^git]*`;
do
cd $dir
echo `pwd`
git commit -m "called from script" apiary.apib
cd ..
done
cd IMP-North-Implemented
