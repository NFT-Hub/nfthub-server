#!/bin/bash

currentPath=$(pwd)

phase=prod

echo start to deploy $phase phase

echo git tag sync
git tag -l | xargs git tag -d
git fetch

echo tag list last 5
git tag | sort -V | tail -5

latesttag=$(git tag | sort -V | tail -1)
echo input target tag to deploy [$latesttag]
read targetTag
targetTag=${targetTag:-$latesttag}
echo target tag is $targetTag

git checkout -t origin/$phase | git checkout $phase
git reset --hard $targetTag
git push -f

cd $currentPath