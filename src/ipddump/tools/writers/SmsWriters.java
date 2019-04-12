package ipddump.tools.writers;

//~--- non-JDK imports --------------------------------------------------------

import ipddump.data.InteractivePagerBackup;
import ipddump.data.Records.SMSMessage;

import ipddump.tools.Finder;

import org.dom4j.*;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@yahoo.gr
 */
public class SmsWriters extends BasicWriter {
    private boolean resolveNames=false;
    private Finder  contactFinder;

    //~--- constructors -------------------------------------------------------

    public SmsWriters(InteractivePagerBackup database, boolean resolveNames) {
        super(database);
        this.resolveNames=resolveNames;
        contactFinder    =new Finder(super.database);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the total of the SMS messages
     *
     *
     * @return
     */
    @Override
    public int getSize() {
        if (database!=null) {
            return database.getSMSRecords().size();
        } else {
            return 0;
        }
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Get the cvs of the parsed SMS's
     *
     *
     * @return
     */
    @Override
    public String toCSV(int[] selectedMessages) {
        StringBuilder temp=new StringBuilder();    // fast builder!!

        temp.delete(0, temp.capacity());
        temp.append("sent,received,sent?,far number,text\n");

        int RecordIndex=0;
        int j          =0;

        for (SMSMessage record : database.getSMSRecords()) {
            if (isSelectedRecord(RecordIndex, selectedMessages)
                    && (selectedMessages[j]<database.getSMSRecords().size())) {
                String Name="";

                if (resolveNames) {
                    Name=contactFinder.findContactByPhoneNumber(record.getNumber());
                } else {
                    Name=record.getNumber();
                }

                temp.append(record.getSent().toString()+","+record.getReceived().toString()+","
                            +record.wasSent()+","+Name+",\""+record.getText()+"\"\n");
                j++;

                if (j>=selectedMessages.length) {
                    break;
                }
            }

            RecordIndex++;
        }

        return temp.toString();
    }

    /**
     * Get a represantation of the parsed SMS's
     * in plain text
     *
     *
     * @param database
     * @param SMSselectedRows
     *
     * @return
     */
    @Override
    public String toPlainText(int[] SMSselectedRows) {
        StringBuilder tmp=new StringBuilder();

        tmp.append("");

        if (database!=null) {
            int RecordIndex=0;
            int j          =0;

            for (SMSMessage record : database.getSMSRecords()) {
                if (isSelectedRecord(RecordIndex, SMSselectedRows)
                        && (SMSselectedRows[j]<database.getSMSRecords().size())) {
                    String number  =record.getNumber();
                    String text    =record.getText();
                    String sent    =record.getSent().toString();
                    String recieved=record.getReceived().toString();
                    String Name    ="";

                    if (resolveNames) {
                        Name=contactFinder.findContactByPhoneNumber(number);
                    } else {
                        Name=number;
                    }

                    if (!record.wasSent()) {
                        tmp.append("From: "+Name+"\nTo: My Phone"+"\nSent: "+sent+"\nReceived: "+recieved+"\nText:\n"
                                   +text+"\n\n");
                    } else {
                        tmp.append("From: My Phone"+"\nTo: "+Name+"\nSent: "+sent+"\nReceived: "+recieved+"\nText:\n"
                                   +text+"\n\n");
                    }

                    j++;

                    if (j>=SMSselectedRows.length) {
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
     * Get the XML of the parsed SMS's
     *
     * @param database
     * @param selectedMessages
     *
     * @return
     */
    @Override
    public Document toXML(int[] selectedMessages) {
        String   sSent   ="";
        Document document=DocumentHelper.createDocument();

        // Add the root
        Element root=document.addElement("SMSmessages").addAttribute("TotalSMS",
                                         String.valueOf(selectedMessages.length));
        int RecordIndex=0;
        int j          =0;

        for (SMSMessage record : database.getSMSRecords()) {
            if (isSelectedRecord(RecordIndex, selectedMessages)
                    && (selectedMessages[j]<database.getSMSRecords().size())) {
                if (record.wasSent()) {
                    sSent="true";
                } else {
                    sSent="false";
                }

                String Name="";

                if (resolveNames) {
                    Name=contactFinder.findContactByPhoneNumber(record.getNumber());
                } else {
                    Name=record.getNumber();
                }

//              System.out.println(record.getUID()+","+record.getSent()+","+record.getReceived()+","+record.wasSent()+","
//              +record.getNumber()+",\""+record.getText()+"\"");
                Element message=root.addElement("SmsMessage");

                // Create the document
                // Add the "sentDate" element
                message.addElement("sentDate").addText(record.getSent().toString());

                // Add the "receivedDate" element
                message.addElement("receivedDate").addText(record.getReceived().toString());

                // Add the "sent?" element
                message.addElement("wasSent").addText(sSent);

                // Add the "to" element
                message.addElement("to").addText(Name);

                // Add the "text" element
                message.addElement("text").addText(record.getText()+"\n");
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
