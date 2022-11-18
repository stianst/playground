while :; do psql -U keycloak -d keycloak <<< 'select pid,state,query from pg_stat_activity order by pid;' > out.log; sleep 0.5; clear; cat out.log | grep -v '\---' | grep -v 'pid  |'; done
