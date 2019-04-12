package ipddump.data.Records;

/**
 * A memo is a record with title and text.
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@gmail.com
 * @date Jun 20, 2009
 */
public class Memo extends Record implements Comparable<Memo> {
    protected String text ="";
    protected String title="ERROR";

    //~--- constructors -------------------------------------------------------

    /**
     * Creates a new record with all provided data.
     *
     * @param dbID The database id
     * @param dbVersion The database version
     * @param uid The unique identifier of this record
     * @param recordLength The length of the record
     */
    public Memo(int dbID, int dbVersion, int uid, int recordLength) {
        super(dbID, dbVersion, uid, recordLength);
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public void addField(int type, char[] data) {
        switch (type) {
        case 1 :
            title=makeStringCropLast(data);
            fields.put("Title", title);

            break;
        case 2 :
            text=makeStringCropLast(data);
            fields.put("Memo", text);

            break;
        case 3 :
            break;
        default :
        }
    }

    @Override
    public int compareTo(Memo o) {
        return getTitle().compareTo(o.getTitle());
    }

    //~--- get methods --------------------------------------------------------

    public String getMemo() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public String toString() {
        return getTitle()+": "+getMemo();
    }
}
