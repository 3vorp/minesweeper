#!/usr/bin/env bash

# copy all static assets immediately
cp -r assets $HOME/.config/evorp/minesweeper

javac ./evorp/minesweeper/Minesweeper.java ./evorp/**/*.java -d ./build

# for whatever reason jar has to be invoked inside the folder it's compiling (why is this a thing)
cd build
jar cfe ../Minesweeper.jar evorp.minesweeper.Minesweeper ./evorp/minesweeper/Minesweeper.class evorp/minesweeper/**/*.class
cd ..

# remove build files
rm -r build/

chmod +x Minesweeper.jar
