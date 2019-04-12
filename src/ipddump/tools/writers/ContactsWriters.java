package ipddump.tools.writers;

//~--- non-JDK imports --------------------------------------------------------

import ipddump.data.InteractivePagerBackup;
import ipddump.data.Records.Contact;

import org.dom4j.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@gmail.com
 */
public class ContactsWriters extends BasicWriter {
    public ContactsWriters(InteractivePagerBackup database) {
        super(database);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the total of the SMS messages
     *
     *
     * @return
     */
    public int getSize() {
        if (database!=null) {
            return database.contacts().size();
        } else {
            return 0;
        }
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param selectedContacts
     *
     * @return
     */
    public String toCSV(int[] selectedContacts) {
        StringBuilder builder=new StringBuilder();    // fast builder!!

        /*
         * Get all the keys since we don't know all of them ahead
         * of time.  Some fields might be duplicated several times.
         */
        Set<String> keys=new TreeSet<String>();

        for (Contact record : database.contacts()) {
            keys.addAll(record.fields().keySet());
        }

        int          RecordIndex=0;
        int          j          =0;
        List<String> names      =new ArrayList<String>(keys);
        boolean      first      =true;

        for (String name : names) {
            if (first) {
                first=false;
            } else {
                builder.append(",");
            }

            builder.append(name);
        }

        builder.append("\n");

        for (Contact record : database.contacts()) {
            if (isSelectedRecord(RecordIndex, selectedContacts) && (selectedContacts[j]<database.contacts().size())) {
                first=true;

                Map<String, String> fields=record.fields();

                for (String name : names) {
                    if (first) {
                        first=false;
                    } else {
                        builder.append(",");
                    }

                    String value=fields.get(name);

                    if (value!=null) {
                        builder.append(value);
                    }
                }

                builder.append("\n");
                j++;

                if (j>=selectedContacts.length) {
                    break;
                }
            }

            RecordIndex++;
        }

        return builder.toString();
    }

    /**
     * Method description
     *
     *
     * @param selectedContacts
     *
     * @return
     */
    public String toPlainText(int[] selectedContacts) {
        StringBuilder tmp=new StringBuilder();

        if (database!=null) {
            int RecordIndex=0;
            int j          =0;

            for (Contact record : database.contacts()) {
                if (isSelectedRecord(RecordIndex, selectedContacts)
                        && (selectedContacts[j]<database.contacts().size())) {
                    Iterator iterator2=record.fields().entrySet().iterator();

                    for (Iterator iterator=iterator2; iterator2.hasNext(); ) {
                        Map.Entry entry=(Map.Entry) iterator.next();

                        tmp.append(entry.getKey()+": "+entry.getValue()+"\n");
                    }

                    tmp.append("\n");
                    j++;

                    if (j>=selectedContacts.length) {
                        break;
                    }
                }

                RecordIndex++;
            }

            return tmp.toString();
        }

        return tmp.toString();
    }

    /**
     * Method description
     *
     *
     * @param selectedMessages
     *
     * @return
     */
    public Document toXML(int[] selectedMessages) {
        Document document=DocumentHelper.createDocument();

        // Add the root
        Element root=document.addElement("Contacts").addAttribute("TotalContacts",
                                         String.valueOf(selectedMessages.length));
        int RecordIndex=0;
        int j          =0;

        for (Contact record : database.contacts()) {
            if (isSelectedRecord(RecordIndex, selectedMessages) && (selectedMessages[j]<database.contacts().size())) {
                Element  message  =root.addElement("Contact");
                Iterator iterator2=record.fields().entrySet().iterator();

                for (Iterator iterator=iterator2; iterator2.hasNext(); ) {
                    Map.Entry entry=(Map.Entry) iterator.next();
                    String    type =entry.getKey().toString().replaceAll(" ", "");
                    String    value=entry.getValue().toString();

                    message.addElement(type).addText(value);
                }

                j++;

                if (j>=selectedMessages.length) {
                    break;
                }
            }

            RecordIndex++;
        }

        return createPrettyPrint(document);
    }
}
