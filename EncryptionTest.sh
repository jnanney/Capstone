#!/bin/bash
i=0

if [[ $# -ne 1 ]]; then
   echo "Usage: `basename $0` <number of iterations>"
   exit 1
fi

while [[ $i -lt $1 ]]; do
   java Test hello tests/enc${i} tests/dec${i} $i > tests/result${i}
   if [[ $? -ne 0 ]]; then
      echo "Problem in iteration $i"
   fi
   i=$(( $i + 1))
done
