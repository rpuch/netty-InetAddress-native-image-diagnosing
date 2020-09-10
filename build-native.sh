#!/bin/sh

native-image --no-server --no-fallback -H:+TraceClassInitialization -H:ConfigurationFileDirectories=target/config \
--allow-incomplete-classpath \
--initialize-at-build-time=io.netty \
-cp `find target/deps | tr '\n' ':'`target/netty-InetAddress-native-image-diagnosing-1.0-SNAPSHOT.jar Main target/app

