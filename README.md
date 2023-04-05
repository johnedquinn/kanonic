# Kanonic

Kanonic is a fast LR(1) parser generator operating on the JVM.

While Java is understood to be the language of the software industry, there hasn't been an adequate parser generator
operating on the JVM that accomplishes one of the main pre-requisites for companies: speed. This project aims to tackle
this problem by leveraging the speed and expressiveness of LR(1) grammars.

## About

Check out [Kanonic's Documentation](https://github.com/johnedquinn/kanonic/wiki) for information regarding the Kanonic
syntax, example usage, code generation, and more!

## Status

*This project is a work-in-progress and should be considered experimental.*

## Building

```shell
./gradlew clean assemble
```

## Using the Tool

First, you'll need to install the `kanonic` command:
```shell
./gradlew :kanonic-tool:install
```

Then, run the command on a Kanonic file:
```shell
./kanonic-tool/build/install/kanonic-tool/bin/kanonic kanonic.knc
```

Please see the [documentation](https://github.com/johnedquinn/kanonic/wiki) for more information!
