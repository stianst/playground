#!/bin/bash -e

FILE=$(basename $1).html

echo "<html><body>" > $FILE
curl -s "$1"  | grep '<p><strong><a href="' >> $FILE
echo "</body></html>" >> $FILE

google-chrome $FILE
