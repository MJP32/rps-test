#!/bin/sh

host=$1
port=$2
sb=$3

##directory where jar file is located    
dir=.

##jar file name
jar_name=casino-0.1-SNAPSHOT.jar
main_class=/classes/com.tw.casino.connection.netty.PlayerClient

## Permform some validation on input arguments, one example below
if [ -z "$1" ] || [ -z "$2" ] || [-z "$3"]; then
        echo "Missing arguments, exiting.."
        echo "Usage : $0 host port starting_balance_as-double"
        exit 1
fi

java -cp jar_name main_class host port sb