#!/bin/bash
#
# author:liusha
# description: Console log.

APP_NAME=backendApp
LOG_HOME=/home/${APP_NAME}/logs
LOG_PATH=${LOG_HOME}/${APP_NAME}.out
tail -100f ${LOG_PATH}
