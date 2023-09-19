#!/bin/bash -e

./crc-ssh.sh sudo tail -f /var/log/kube-apiserver/audit.log
