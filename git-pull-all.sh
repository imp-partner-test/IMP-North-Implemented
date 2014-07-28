#!/bin/sh

for dir in `ls -d */`;
do
cd $dir
echo `pwd`
git pull
git checkout master
cd ..
done
