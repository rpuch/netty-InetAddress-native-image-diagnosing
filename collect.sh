#!/bin/sh

java -agentlib:native-image-agent=config-output-dir=target/config -cp `find target/deps | tr '\n' ':'`target/netty-InetAddress-native-image-diagnosing-1.0-SNAPSHOT.jar Main
