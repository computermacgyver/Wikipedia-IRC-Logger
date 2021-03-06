Wikipedia-IRC-Logger
====================

Wikipedia-IRC-Logger

This code is Java code based on [sample code](http://oreilly.com/pub/h/1966) to connect to the Wikipedia IRC stream of recent changes and dump the output to text files. It was used to monitor the recent changes to Wikipedia in multiple language editions and the results published in [a recent article](http://arxiv.org/abs/1312.0976).

The code creates one file per day with a filename in the format of irc-logger--yyyy-mm-dd--mm--ss.txt. Filenames are rotated around midnight localtime, but may be slightly later if the IRC channel is unusually quiet. The datetime each message is sent is recorded from the system clock and prepended to the line followed by a tab.

Adjust which channels (projects) you want to record in [SimpleLogger.java](https://github.com/computermacgyver/Wikipedia-IRC-Logger/blob/master/src/us/hale/scott/wikipedia/irc/logger/SimpleLogger.java). Compile and run WikipediaLogger.java to start capturing.

[RecentChange.java](https://github.com/computermacgyver/Wikipedia-IRC-Logger/blob/master/src/us/hale/scott/wikipedia/irc/model/RecentChange.java) provides example code to parse lines from the files. 

See the related [mapreduce code](https://github.com/computermacgyver/Wikipedia-IRC-MapReduce) for hadoop mappers and reducers. Wikipedia has further information about [the recent changes IRC stream](http://meta.wikimedia.org/wiki/IRC/Channels#Raw_feeds).

If you use this code in support of an academic publication, please cite:

    Hale, S. A. (2014). Multilinguals and Wikipedia Editing. http://arxiv.org/abs/1312.0976

This code is released under the [GPLv2 license](http://www.gnu.org/licenses/gpl-2.0.html). Please [contact me](http://www.scotthale.net/blog/?page_id=9) if you wish to use the code in ways that the GPLv2 license does not permit.
