#!/bin/sh
#First we remove all the changes in the local master, then we pull the changes
cd ~/NorthwindNode
git reset --hard origin/master
git clean -f
git pull
