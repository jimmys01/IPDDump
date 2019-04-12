# IPDDump
Blackberry .ipd files parser. Re uploaded to github from google code archives.


The ipddump is a utility that enables the user to navigate thought and extract records from a Blackberry backup, which is an IPD (Inter@ctive Pager Backup) file. Its goal is to be able to extract and export all types of records into customized open text formats as well to edit records like service books and contacts.


ipddump is a project aimed at providing a library for reading the [http://na.blackberry.com/eng/devjournals/resources/journals/jan_2006/ipd_file_format.jsp Inter@ctive Pager Backup] (IPD) file format commonly used by RIM on the [http://blackberry.com/ Blackberry] smartphones.

== History ==

I started ipddump because I wanted to pull my SMS messages off my phone and back them up in an open format.  But there are no good free solutions out there.  The only ones I've found are [http://www.blackberryforums.com/aftermarket-software/38695-sms_borer-export-sms-data-backup-file-ipd.html SMS Borer] which has significant limitations, and [http://www.processtext.com/abcblackberry.html ABC Amber] which has many features, but is not free.



This a 100% absolutely, totally free software.



==Trouble Shooting== Ipd's:
1.Connect your Blackberry and open the Desktop Manager.
2.Click to the Backup and Restore
3.Click Advanced
4.Select the databases Sms Messages, Call Logs, Memos, Tasks, Time Zones, Adrress Book and Adrress Book All
5.Press the << button.
6.Press file and then Save As... then save the ipd
7.Open this .ipd with the ipddump


Run Instructions:
Either double click the .jar file or if that wont work
type in your cmd java -jar "IpdDump 0.3 RC2.jar"
If that wont work either then you must install a new version of Java JRE



Anyone who likes to be a part of this projects development is welcomed!


Any references of the IpdDump or to its code in scientific papers or journals please sent me a copy of it.
