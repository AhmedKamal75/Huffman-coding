#!/bin/bash

# Check if two arguments are provided
if [ "$#" -ne 2 ]; then
    echo "Usage: ./compare_files.sh file1.txt file2.txt"
    exit 1
fi

# Generate SHA-256 hashes of the files
hash1=$(sha256sum "$1" | awk '{ print $1 }')
hash2=$(sha256sum "$2" | awk '{ print $1 }')

# Compare the hashes
if [ "$hash1" = "$hash2" ]
then
    echo "The files are the same."
else
    echo "The files are different."
fi
