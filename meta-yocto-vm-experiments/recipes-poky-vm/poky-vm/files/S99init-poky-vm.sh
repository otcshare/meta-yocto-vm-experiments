#!/bin/sh

if [ "$(cat /sys/class/net/eth0/operstate)" != "up" ]; then
  echo "Starting eth0."
  udhcpc
fi

ifconfig 

#mount -t ext4 -o discard /dev/sdb /mnt/yocto-tmp
#chown -R chef:chef /mnt/yocto-tmp

#if [ -d/opt/rdt-server ]; then
#  cd /opt/rdt-server
#  perl ./daemon.pl &
#fi


