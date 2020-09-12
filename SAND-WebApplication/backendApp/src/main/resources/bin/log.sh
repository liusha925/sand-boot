#!/bin/bash
#
# author:liusha
# description: Console log.

APP_HOME=`pwd`
APP_NAME=backendApp
LOG_PATH=${APP_HOME}/logs/${APP_NAME}.out
tail -100f ${LOG_PATH}