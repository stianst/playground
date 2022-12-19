FLAKES=""
SEP=""
for dir in $(find -type d -name surefire-reports); do
    for i in $(grep -l '<flakyFailure' $dir/TEST-*.xml); do
        FLAKES="$FLAKES$SEP$i"
        SEP=$'\n'
    done
done

echo $FLAKES
