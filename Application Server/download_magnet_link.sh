#!/bin/bash
## Note that the magnet link must have quotes around it. 

echo "Running the bash script"

transmission-remote -a "$1" -s 

echo "Bash script completed"

