#!/bin/bash -e

./crc-ssh.sh sudo tail -f /var/log/openshift-apiserver/audit.log
