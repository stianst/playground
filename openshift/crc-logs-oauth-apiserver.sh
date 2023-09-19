#!/bin/bash -e

./crc-ssh.sh sudo tail -f /var/log/oauth-apiserver/audit.log
