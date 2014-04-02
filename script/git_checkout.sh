#!/bin/bash

if [ $# -eq 0 ]; then  		### if(arvc == 0)
	echo "Insert repo url"	###	printf("...");
	exit 1
fi

mkdir vers_tmp # crea cartella 
cd vers_tmp

git clone $1 tmp_proj # scarica checkout $1 = url
if ! [ $? -eq 0 ]; then		### $? contiene il return value di git clone
	exit 1
fi
cd `ls`
echo "<root>" > ../log.xml

git log --pretty=format:"<entry><hash>%h</hash><name>%an</name><email>%ae</email><date>%cd</date><message>%B</message></entry>"  --date=local  --date-order --reverse >> ../log.xml
# crea xml
echo -e "\n</root>" >> ../log.xml # append
exit 0
