package ipddump.data.Records;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

/**
 *
 * @author Jimmys Daskalakis, jimdaskalakis01@gmail.com
 */
public class HistoryRecord implements Comparable<HistoryRecord> {
    private Date   date=new Date(0);
    private String text="";

    //~--- constructors -------------------------------------------------------

    public HistoryRecord(Date date, String text) {
        this.date=date;
        this.text=text;
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public int compareTo(HistoryRecord o) {
        return getDate().compareTo(o.getDate());
    }

    //~--- get methods --------------------------------------------------------

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }
}
