#!/bin/sh

for dir in `ls -d */`;
do
cd $dir

echo --------------------------------------------------------------------------------
echo cd $dir
echo git add apiary.apib
echo git commit

git add apiary.apib
git commit -m "called from script" apiary.apib
cd ..
done

echo --------------------------------------------------------------------------------

