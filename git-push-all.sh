#!/bin/sh

for dir in `ls -d */`;
do
cd $dir

echo --------------------------------------------------------------------------------
echo cd $dir
echo git push

git config --global push.default simple

git push
cd ..
done

echo --------------------------------------------------------------------------------




#rawdir=${dir%/}
#echo `pwd`
#path1='https://imp-partner-test:Imp2013\!@github.com/imp-partner-test/'
#path2='.git'
#gitrep=$path1$rawdir$path2
