#!/bin/sh

rootDir=$(cd `dirname $0`; pwd)

echo $rootDir

rm -rf $rootDir/builds

mkdir -p $rootDir/builds

buildProj() {
	projName=$1
	cd $rootDir/$projName
	mvn package
	cp $rootDir/$projName/target/*.jar $rootDir/builds/
}

buildProj base
buildProj common
buildProj dbproxy
buildProj game
buildProj gate
buildProj gamemanager

