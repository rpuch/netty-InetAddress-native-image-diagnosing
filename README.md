This project demonstrats how InetAddress-related problems could be solved when
building a project using netty and netty-resolver-dns as a native image.

# Prerequisites

* Download and unpack GraalVM (I was using version 20.2.0 from here
https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-20.2.0 )
* Add its `bin` directory to your `PATH`
* Install native image builder using `gu install native-image`

# Building

```
./build.sh
```

There are two branches: `before-fixes` and `after-fixes`. Build in `before-fixes`
branch fails; build in `after-fixes` finishes successfully. The built application
can be run with `target/app`, although it does not look very fascinating.