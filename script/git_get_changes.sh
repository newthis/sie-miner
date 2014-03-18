#!/bin/bash

if [ $# -lt 1 ]; then # if(argc <= 1)
	echo "Insert Hash1 Hash2" # printf("insert ...)
	exit 1
fi

cd vers_tmp/tmp_proj

if ! [ $? -eq 0 ]; then	
	exit 1

fi
git diff $1 $2 # fa il diff tra commit nuovo e vecchio

if ! [ $? -eq 0 ]; then
	exit 1
fi

exit 0
