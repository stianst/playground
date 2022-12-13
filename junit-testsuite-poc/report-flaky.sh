for dir in `find -type d -name surefire-reports`; do
    for i in `ls $dir/*.txt`; do
      if ( grep '<<< FAILURE!' $i &>/dev/null ); then
        TEST=`echo $i | sed 's/.txt//g' | sed 's/.*\.//g'`
        LOG=`cat $i`
        
        RUN="https://github.com/stianst/playground/actions/runs/3683855481"
        TITLE="Flaky test: $TEST"
        BODY="$RUN"$'\n'"\`\`\`"$'\n'"$LOG"$'\n'"\`\`\`"
        ISSUE=`gh issue list --search "$TITLE in:title" --json number --jq .[].number`
    
        if [ "$ISSUE" == "" ]; then
          echo gh issue create -t "$TITLE" -b "$BODY" -l "bug,flaky-test"
        else
          echo gh issue comment $ISSUE -b "$BODY"
        fi
      fi
    done
  done
