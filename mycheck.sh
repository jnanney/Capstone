#!/bin/bash

i=0
if [[ $# -ne 2 ]]; then
   echo "Usage: `basename $0` <originalFile> <number of iterations>"
   exit 1
fi
while [[ $i -lt $2 ]]; do
   result=`diff tests/dec${i} $1`
   if [[ $result != "" ]]; then
      echo "in iteration $i result is $result"
   fi
   i=$(( $i + 1))
done
exit 0
