package ipddump.data;

//~--- non-JDK imports --------------------------------------------------------

import ipddump.data.Records.*;

import ipddump.tools.Finder;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * The InteractivePagerBackup represents a single IPD file and provides an easy
 * way to generate records based on the initial record data.
 * </p>
 * <p>
 * In general, in the ipddump project IPD refers to the <em>file</em> and
 * <code>InteractivePagerBackup </code> refers to the <em>datastructure</em>
 * representing the file.
 * </p>
 *
 * @author borkholder
 * @date Jan 1, 2008
 */
 public class InteractivePagerBackup {

    /**
     * Reports If there were Errors while parsing
     */
    private InteractivePagerBackup database              = this;
    private boolean                errorFlag             = false;
    private boolean                debugingEnabled       = false;
    private boolean                valuePeeking          = false;
    private boolean                useAdrrBookAllDB      = true;
    private boolean                isDatabaseEncrypded   = false;
    private boolean                otherLibsFlag         = false;

    public boolean isIsDatabaseEncrypded() {
        return isDatabaseEncrypded;
    }

    /**
     * The set of contacts.
     */
    private final List<Calendar> calendar;

    /**
     * The set of Phone Call Logs.
     */
    private final List<CallLog> callLogs;

    /**
     * The set of contacts.
     */
    private final List<Contact> contacts;

    /**
     * The list of databases, or rather the names of the databases.
     */
    private final List<String> databases;

    /**
     * The character used as the line feed.
     */
    private char lineFeed;

    /**
     * The set of Memos.
     */
    private final List<Memo> memos;

    /**
     * The set of SMS messages.
     */
    private final List<SMSMessage> smsRecords;

    /**
     * The set of Tasks Entries.
     */
    private final List<Task> tasks;

    /**
     * The set of TimeZones.
     */
    private final List<BBTimeZone> timeZones;

    /**
     * The version of the IPD.
     */
    private final int version;

    /**
     * Creates a new database.
     *
     * @param version The IPD version
     * @param lineFeed The line feed character
     */
     public InteractivePagerBackup(int version, char lineFeed, boolean isDatabaseEncrypded) {
        this.version  = version;
        this.lineFeed = lineFeed;
        this.isDatabaseEncrypded = isDatabaseEncrypded;
        databases     = new ArrayList<String>();
        smsRecords    = new ArrayList<SMSMessage>();
        contacts      = new ArrayList<Contact>();
        tasks         = new ArrayList<Task>();
        memos         = new ArrayList<Memo>();
        timeZones     = new ArrayList<BBTimeZone>();
        callLogs      = new ArrayList<CallLog>();
        calendar      = new ArrayList<Calendar>();
    }

    /**
     * Adds a new database to the list of contained databases.
     *
     * @param name The name of the database to add
     */
    public void addDatabase(String name) {
        databases.add(name);
    }

    /**
     * Gets the collection of contacts.
     *
     * @return An unmodifiable collection of contacts
     */
    public Collection<Contact> contacts() {
        return Collections.<Contact>unmodifiableCollection(contacts);
    }

    /**
     * Creates a new {@link Record} to represent the type of data for the database
     * given by the dbIndex value.
     *
     * @param dbIndex The index of the database that this record will be in
     * @param version The version of the database to which this record belongs
     * @param uid The unique identifier of the Record
     * @param length The length of the Record in the data
     * @return A new Record
     */
    public Record createRecord(int dbIndex, int version, int length) {
        int uid = 0;

        if ((dbIndex >= databases.size()) || (dbIndex < 0)) {
            if (valuePeeking && debugingEnabled) {
                System.out.println("-------dbID: " + dbIndex + "-------");

                return new DummyRecord(dbIndex, version, uid, length).enableValuePeeking();
            } else if (debugingEnabled) {
                return new DummyRecord(dbIndex, version, uid, length).disableValuePeeking();
            } else {
                return new DummyRecord(dbIndex, version, uid, length).disableValuePeeking();
            }
        } else if ("SMS Messages".equals(databases.get(dbIndex))) {
            SMSMessage record = new SMSMessage(dbIndex, version, uid, length);

            smsRecords.add(record);

            return record;
        } else if ("Address Book".equals(databases.get(dbIndex))) {
            Contact record = new Contact(dbIndex, version, uid, length);

            contacts.add(record);

            return record;
        } else if ("Address Book - All".equals(databases.get(dbIndex))) {
            Contact record = new Contact(dbIndex, version, uid, length);

            record.enableAdrressBookAllType();
            contacts.add(record);

            return record;
        } else if ("Memos".equals(databases.get(dbIndex))) {
            ipddump.data.Records.Memo record = new ipddump.data.Records.Memo(dbIndex, version, uid, length);

            memos.add(record);

            return record;
        } else if ("Tasks".equals(databases.get(dbIndex))) {
            Task record = new Task(dbIndex, version, uid, length);

            tasks.add(record);

            return record;
        } else if ("Time Zones".equals(databases.get(dbIndex))) {
            BBTimeZone record = new BBTimeZone(dbIndex, version, uid, length);

            timeZones.add(record);

            return record;
        } else if ("Phone Call Logs".equals(databases.get(dbIndex))) {
            CallLog record = new CallLog(dbIndex, version, uid, length);

            callLogs.add(record);

            return record;

//          } else if ("Calendar".equals(databases.get(dbIndex))) {
//              Calendar record=new Calendar(dbIndex, version, uid, length);
//
//              distinguishRecord(dbIndex);
//              calendar.add(record);
//
//              return record;
        } else {

            

            if (valuePeeking && debugingEnabled &&!otherLibsFlag) {
                distinguishRecord(dbIndex);

                return new DummyRecord(dbIndex, version, uid, length).enableValuePeeking();
            } else if (debugingEnabled && !otherLibsFlag) {
                return new DummyRecord(dbIndex, version, uid, length).disableValuePeeking();
            }else {
                return new DummyRecord(dbIndex, version, uid, length).disableValuePeeking();
            }


        }
    }

    public void enableDebuging() {
        debugingEnabled = true;
    }

    public void enableValuePeeking() {
        valuePeeking = true;
    }

    /**
     * Gets the collection of the Calendar.
     *
     * @return An unmodifiable collection of Calendar records
     */
    public Collection<Calendar> getCalendar() {
        return Collections.unmodifiableCollection(calendar);
    }

    /**
     * Gets the collection of the Phone Call Logs.
     *
     * @return An unmodifiable collection of Phone Call Logs records
     */
    public Collection<CallLog> getCallLogs() {
        return Collections.unmodifiableCollection(callLogs);
    }

    /**
     * Gets the list of database names that have been added so far.
     *
     * @return An unmodifiable list of database names
     */
    public List<String> getDatabaseNames() {
        return Collections.<String>unmodifiableList(databases);
    }

    /**
     * @return the lineFeed
     */
    public char getLineFeed() {
        return lineFeed;
    }

    /**
     * Gets the collection of memos.
     *
     * @return An unmodifiable collection of memos
     */
    public Collection<Memo> getMemos() {
        return Collections.<Memo>unmodifiableCollection(memos);
    }

    /**
     * Gets the collection of SMS records.
     *
     * @return An unmodifiable collection of SMS records
     */
    public Collection<SMSMessage> getSMSRecords() {
        return Collections.<SMSMessage>unmodifiableCollection(smsRecords);
    }

    /**
     * Gets the collection of task records.
     *
     * @return An unmodifiable collection of task records
     */
    public Collection<Task> getTasks() {
        return Collections.unmodifiableCollection(tasks);
    }

    /**
     * Gets the collection of the Time Zones records.
     *
     * @return An unmodifiable collection of Time Zones records
     */
    public Collection<BBTimeZone> getTimeZones() {
        return Collections.unmodifiableCollection(timeZones);
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    public void organize() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Collections.sort(memos);
                Collections.sort(smsRecords);
                Collections.sort(tasks);
            }
        };
        Thread t = new Thread(r);

        t.setDaemon(false);
        t.start();

        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                Collections.sort(contacts);
                Collections.sort(timeZones);
                Collections.sort(calendar);
            }
        };
        Thread t1 = new Thread(r1);

        t1.setDaemon(false);
        t1.start();

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                Collections.sort(callLogs);

                Finder finder = new Finder(database);

                for (Task recordt : database.tasks) {
                    String name = finder.findTimeZoneByID(recordt.getTimeZone());

                    recordt.setTimeZoneName(name);
                }
            }
        };
        Thread t2 = new Thread(r2);

        t2.setDaemon(false);
        t2.start();
    }

    public void setErrorFlag() {
        errorFlag = true;
    }

    public boolean wereErrors() {
        return errorFlag;
    }

    private void distinguishRecord(int dbIndex) {
        System.out.println("----New " + getDatabaseNames().get(dbIndex) + "----");
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
