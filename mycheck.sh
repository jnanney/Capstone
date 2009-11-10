#!/bin/bash

i=0
if [[ $# -ne 1 ]]; then
   echo "Usage: `basename $0` <originalFile>"
   exit 1
fi
while [[ $i -lt 100 ]]; do
   result=`diff tests/dec${i} $1`
   if [[ $result != "" ]]; then
      echo "in iteration $i result is $result"
   fi
   i=$(( $i + 1))
done
