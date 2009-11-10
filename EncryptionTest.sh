#!/bin/bash
i=0
while [[ $i -lt 100 ]]; do
   java Test tests/enc${i} tests/dec${i} > tests/result${i}
   i=$(( $i + 1))
done
