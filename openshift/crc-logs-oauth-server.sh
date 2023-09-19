#!/bin/bash -e

./crc-ssh.sh sudo tail -f /var/log/oauth-server/audit.log
