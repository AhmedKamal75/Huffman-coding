#!/bin/bash

# Check if two arguments are provided
if [ "$#" -ne 2 ]; then
    echo "Usage: ./compare_files.sh file1.txt file2.txt"
    exit 1
fi

# Use the 'diff' command to compare the files
diff -q "$1" "$2" > /dev/null

# Check the exit status of the 'diff' command
if [ "$?" -eq 0 ]
then
    echo "The files are the same."
else
    echo "The files are different."
fi
