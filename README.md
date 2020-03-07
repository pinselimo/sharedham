# Shared Ham

Shared Ham is the Java-Library that let's you connect and work with a HamP-Free server.
HamP-Free is a free remote control MP3 jukebox based on mpg321. It reads your music library and communicates over TCP. Discovery is based on a UDP protocol. It is written in Haskell, thus the "Ham".

## Remote control

The accompanying Android app will be released in the near future,
this is its Java library to handle the communication with HamP-Free.

## Disclaimer

This was a fun project to discover Haskell's boundaries and advantages in highly IO-/sideeffect-bound scenarios.
It is poorly maintained.

## Installation

Just bind to this Java package in your code like:

~~~java
import plakolb.sharedham.*;
~~~

### HamP-Free

To install HamP-Free go to its accompanying repository and follow the instructions.
