package ipddump.data.Records;

/**
 * This record is here for any records that this utility doesn't handle yet.
 *
 * @author borkholder
 * @date Jan 1, 2008
 */
public class DummyRecord extends Record {

    // List<String> fields=new ArrayList<String>();
    private boolean valuePeeking=false;

    //~--- constructors -------------------------------------------------------

    /**
     * Creates a new record with all provided data.
     *
     * @param dbID The database id
     * @param dbVersion The database version
     * @param uid The unique identifier of this record
     * @param recordLength The length of the record
     */
    public DummyRecord(int dbID, int dbVersion, int uid, int recordLength) {
        super(dbID, dbVersion, uid, recordLength);
    }

    //~--- methods ------------------------------------------------------------

    public void addField(int type, char[] data) {

        // fields.add(type+"|"+ makeString(data));
        if (valuePeeking) {
            viewIt(type, data);
        }
    }

    public Record disableValuePeeking() {
        valuePeeking=false;

        return this;
    }

    public Record enableValuePeeking() {
        valuePeeking=true;

        return this;
    }
}
