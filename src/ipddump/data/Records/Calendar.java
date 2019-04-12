package ipddump.data.Records;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@gmail.com
 * @created Jun 20, 2009
 */
public class Calendar extends Record implements Comparable<Calendar> {

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
    public Calendar(int dbID, int dbVersion, int uid, int recordLength) {
        super(dbID, dbVersion, uid, recordLength);
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public void addField(int type, char[] data) {
        switch (type) {
        case 1 :
            int reacurance=data[0];

            viewItInHex(type, data);

            switch (reacurance) {
            case 0x2a :
                fields.put("Reacurance", "Yearly");

                break;
            case 0x61 :
                fields.put("Reacurance", "None");

                break;
            }

            System.out.println(getField("Reacurance"));

            break;
        case 2 :
            fields.put("Name", makeStringCropLast(data));
            System.out.println(getField("Name"));

            break;
        case 42 :
            break;

//      case 43 :
//          ;
//          fields.put("Date1", makeDate2(data).toString());
//          System.out.println(getField("Date1"));
//
//          break;
//
//      case 44 :
//          fields.put("Date2", makeDate2(data).toString());
//          System.out.println(getField("Date2"));
//
//          break;
        case 6 :
            fields.put("StartDate", new Date(((makeInt(data))+5256000) * 60 * 10000).toString());
            viewItInInt(type, data);
            System.out.println(getField("StartDate"));

            break;
        case 7 :
            fields.put("EndDate",
                       new Date((long) (makeInt(data)) * 60 * 1000-2208970740000L-28 * 61 * 60 * 1000).toString());
            viewItInInt(type, data);
            System.out.println(getField("EndDate"));

            break;
        case 255 :
            break;
        case 18 :
            break;
        case 40 :
            break;
        default :
            viewItInHex(type, data);
        }
    }

    @Override
    public int compareTo(Calendar o) {
        return 0;    // getCalendar().compareTo(o.getCalendar());
    }

    //~--- get methods --------------------------------------------------------

    public String getCategories() {
        return getField("Categories").replaceAll(",", ";");
    }

    public String getDue() {
        return getField("Due");
    }

    public String getName() {
        return getField("Name");
    }

    public String getNotes() {
        return getField("Notes");
    }

    public String getPriority() {
        if (getField("Priority").equals("")) {
            return "Normal";
        }

        return getField("Priority");
    }

    public String getReminder() {
        if (getField("Reminder").equals("")) {
            return "None";
        }

        return getField("Reminder");
    }

    public String getStatus() {
        return getField("Status");
    }

    public String getTimeZone() {
        return getField("TimeZone");
    }

    //~--- set methods --------------------------------------------------------

    public void setTimeZoneName(String name) {
        fields.put("TimeZone", name);
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public String toString() {
        return fields.toString();
    }
}
