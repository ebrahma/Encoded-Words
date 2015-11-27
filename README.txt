Given a short list of encoded (but alphabetized) words, CodeSort.java will alphetize and list of encoded words created from the same encoding (even if that list is longer and contains encoded words not includied in the original list). CodeSort performs this sorting using topological sort. Sample input files mallSorted.txt and unsorted1.txt for a sample run are included.

Code sort expects three comandline arguments:
1) small sorted file, which contains a list of alphabetized, encoded words
2) unsorted file, which contains the list of unordered, encoded words
3) sorted file, which is the file to which CodeSort outputs the alphabetized list of encoded words

Sample Run:
java CodeSort smallSorted.txt unsorted1.txt sorted1.txt



