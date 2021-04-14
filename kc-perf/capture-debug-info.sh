#!/bin/bash

while (true); do
	for i in `seq 1 3`; do
		NODE=$1

		DATE=`date +%F-%H-%M-%S`

		mkdir -p debug-info

		PID=`jps -v | grep sso-7.4-$NODE | cut -f 1 -d ' '`

		curl -s http://localhost:8180/auth/realms/cache/stats > debug-info/$DATE--cache-stats-$NODE
		jstack $PID > debug-info/$DATE--threads-$NODE
		jmap -dump:file=debug-info/$DATE--heap-$NODE.hprof $PID
		gzip debug-info/$DATE--heap-$NODE.hprof
	done
	
	sleep 30m
done
