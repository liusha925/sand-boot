#!/bin/bash
#
# chkconfig:   - 20 80
# description: tail log.
# author:liusha

APP_NAME=graphic
LOG_PATH=$APP_HOME/logs/$APP_NAME.out
tail -100f ./$LOG_PATH