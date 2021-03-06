# Liner #

## Description ##
A server that can serve very fast a specified line from a huge file.

## Protocol ##
- LINE n : returns specified line (lines are numbered from 1) from a file in the given format:
    * if n is a valid line number: "OK\n" and then n-th line
    * otherwise: "ERROR\n"
- QUIT: disconnect client
- SHUTDOWN: shutdown the server

## Example usage ##
1. Configure system.properties if you wish

2. Build (and install needed software):
$ ./build.sh

3. Run a server:
$ ./run.sh fileName

4. Run a test client:
$ nc localhost 6789

## Implementation details ##
The server processes a specified file during start and creates an index as
binary file with offsets to each line of the original file. The index size is
8 bytes x line count. Then it starts handling clients in parallel using a thread
pool. One thread is used to serve one client. Getting a line is done in 2 steps
and requires 2 disk read for short lines (finding a line in an index and reading
a line from the file). For bigger lines an operating system must make multiple
reads.

## Assumptions ##
* Maximum line length is <= 2GB (Integer.MAX_VALUE)
* There is enough free space on disk for an index file. The index file can be
  even bigger then original file in case of very short lines (for example empty lines).
* Server start can be slow (the index is built before clients can connect)
 
## Performance ##
Performance tests were executed for files with a size from few bytes to 100 GB.
Random requests were generated by up to 100 simultaneous running client threads.

### Requests per second ###
Even a huge number of requests per second doesn't break a performance of the system.
A performance degradation is prevented by a limited thread pool.
For details and measurements please look at test-performance.sh and PerformanceLinerTest.

### Size of file ###
The system is scalable for a growing file size. In theory, it can handle a file
with any size which can be stored in a file system. After reaching "a cache limit",
increasing file size has no influence on speed of GET commands, but only on a system
start (building an index). The time of building index increases linear with a file
size increase. There is one catch. System caches are used for small enough files.
There are almost no disk reads, a data is taken from RAM and a number of handled requests per
second is very high (tens of thousands per sec.). Then it starts to lack a space in
the cache and disk reads are done, which drastically decrease performance in terms of
req/s. Then almost each request require 2 disk reads, and each read takes few ms for
magnetic disk drives. Thus the system is capable to serve about 100 req/s when using
only one drive.

## Alternatives ##
Take an existing database, create table Line(nr, text) with index on nr.
Insert all lines to the database and next query by nr.

## Possible extensions ##
* Mount other drive if exists and then we have at least 2 possibilities:
  - put a copy of file and index on the second drive and create 2 TextFile instances
  - put a half of file and index on the first drive and rest on the other one

* Faster file processing and creating index
  The current algorithm is not so bad, because it is not so far from a hardware
  capabilities (measured with hdparm). Albeit parallel processing can potentially
  improve performance. One thread can be used for disk operations and others
  for processing.
  
* Multiple instances of TextFile 

## On-line resources ##
* http://nadeausoftware.com/articles/2008/02/java_tip_how_read_files_quickly

## Libraries and tools ##
* Java 8
* Maven 3
* Junit 4, Mockito

## TODO ##
* consider using netty lib
* RAMTextFile for small files
* improve tests

## Development ##

### Check newer versions of dependencies

    $ mvn versions:display-dependency-updates
