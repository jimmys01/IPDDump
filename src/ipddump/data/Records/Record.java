package ipddump.data.Records;

//~--- non-JDK imports --------------------------------------------------------

import ipddump.tools.GeneralToolBox;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A Record is an entry in a particular database. Each record indexes into the
 * list of databases and identifies the type of data in the fields of this
 * record.
 *
 * @author borkholder
 * @date Jan 1, 2008
 */
public abstract class Record extends GeneralToolBox {

    /**
     * The 0-based index of the database to which this field belongs.
     */
    protected final int databaseID;

    /**
     * The version of the database to which this field belongs.
     */
    protected final int databaseVersion;

    /**
     * The unique identifier of this record.
     */
    protected int uniqueID;

    /**
     * The length of the record.
     */
    protected int length;

    /**
     * A handle of the record in the database. This is an element in the
     * increasing sequence of integers within the IPD.
     */
    protected int recordDBHandle;

    /**
     * The map from the name of the field to the field value.
     */
    protected Map<String, String> fields=new HashMap<String, String>();

    //~--- constructors -------------------------------------------------------

    /**
     * Creates a new record with all provided data.
     *
     * @param dbID The database id
     * @param dbVersion The database version
     * @param uid The unique identifier of this record
     * @param recordLength The length of the record
     */
    public Record(int dbID, int dbVersion, int uid, int recordLength) {
        databaseID     =dbID;
        databaseVersion=dbVersion;
        uniqueID       =uid;
        length         =recordLength;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Adds the field to the record.
     *
     * @param type The type of field
     * @param data The field data
     */
    public abstract void addField(int type, char[] data);

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Record) {
            return uniqueID==((Record) obj).uniqueID;
        } else {
            return false;
        }
    }

    /**
     * Gets the fields contained by this record.
     *
     * @return An unmodifiable map from the name of the field to the field data
     */
    public Map<String, String> fields() {
        return Collections.unmodifiableMap(fields);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Gets the 0-based index of the database to which this field belongs.
     *
     * @return The database index
     */
    public int getDatabaseID() {
        return databaseID;
    }

    /**
     * Gets the version of the database to which this field belongs.
     *
     * @return The database version
     */
    public int getDatabaseVersion() {
        return databaseVersion;
    }

    /**
     * Gets the length of the record.
     *
     * @return The record length
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the handle of the record in the database.
     *
     * @return The record handle
     */
    public int getRecordDBHandle() {
        return recordDBHandle;
    }

    /**
     * Gets the unique identifier of this record.
     */
//    public int getUID() {
//        return uniqueID;
//    }

    //~--- methods ------------------------------------------------------------

    @Override
    public int hashCode() {
        return uniqueID;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Sets the handle of the record in the database.
     *
     * @param recordDBHandle The record handle
     */
    public void setRecordDBHandle(int recordDBHandle) {
        this.recordDBHandle=recordDBHandle;
    }

    //~--- get methods --------------------------------------------------------

    protected final String getField(String key) {
        if (fields.containsKey(key)) {
            return fields.get(key);
        } else {
            return "";
        }
    }
}
