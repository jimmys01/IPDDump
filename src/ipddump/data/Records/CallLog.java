package ipddump.data.Records;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@gmail.com
 * @created Jun 20, 2009
 */
public class CallLog extends Record implements Comparable<CallLog> {
    private Date date;

    //~--- constructors -------------------------------------------------------

    /**
     * Creates a new record with all provided data.
     *
     * @param dbID
     *          The database id
     * @param dbVersion
     *          The database version
     * @param uid
     *          The unique identifier of this record
     * @param recordLength
     *          The length of the record
     */
    public CallLog(int dbID, int dbVersion, int uid, int recordLength) {
        super(dbID, dbVersion, uid, recordLength);
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public void addField(int type, char[] data) {
        switch (type) {
        case 1 :
            break;    // always the same
        case 6 :
            break;    // always the same
        case 8 :
            break;    // always the same
        case 16 :
            break;    // always the same
        case 10 :

            // viewItInInt(type, data);
            // Probably nothing special
            break;
        case 11 :

            // viewItInInt(type, data);
            // Probably nothing special
            break;
        case 13 :

            // viewItInInt(type, data);
            // Probably nothing special
            break;
        case 2 :
            if (makeInt(data)==0) {
                fields.put("Status", "Received Call");
            } else if (makeInt(data)==1) {
                fields.put("Status", "Placed Call");
            } else if (makeInt(data)==2) {
                fields.put("Status", "Placed, Not Answered");
            } else if (makeInt(data)==3) {
                fields.put("Status", "Missed Call");    // Diference?
            }

            break;
        case 31 :
            fields.put("Name", makeStringCropLast(data));

            break;
        case 3 : {
            String duration=makeDuration(makeInt(data));

            fields.put("Duration", duration);

            break;
        }
        case 4 : {
            date=makeDate2(data);
            fields.put("Date", date.toString());

            break;
        }
        case 12 : {
            fields.put("Number", makeStringCropLast(data));

            break;
        }
        default :

        // viewItInInt(type, data);
        }
    }

    @Override
    public int compareTo(CallLog o) {
        return getDate().compareTo(o.getDate());
    }

    //~--- get methods --------------------------------------------------------

    public Date getDate() {
        return date;
    }

    public String getDuration() {
        return getField("Duration");
    }

    public String getName() {
        if (getField("Name").equals("")) {
            if (!getField("Number").equals("")) {
                return getNumber();
            } else {
                return "Unknown Name";
            }
        }

        return getField("Name");
    }

    public String getNameAndNumber() {
        if (getField("Name").equals("")) {
            return "Number: "+getNumber();
        }

        return "Name: "+getField("Name")+"\nNumber: "+getNumber();
    }

    public String getNumber() {
        if (getField("Number").equals("")) {
            return "Unknown Number";
        } else {
            return getField("Number");
        }
    }

    public String getStatus() {
        return getField("Status");
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public String toString() {
        return fields.toString();
    }
}
