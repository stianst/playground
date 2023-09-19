#!/bin/bash -e

IP=$(crc ip)
ssh -i ~/.crc/machines/crc/id_ecdsa core@$IP "$@"
