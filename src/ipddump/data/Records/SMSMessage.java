package ipddump.data.Records;

//~--- non-JDK imports --------------------------------------------------------

import ipddump.tools.Gsm2Iso;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;
import java.util.HashMap;
import javax.naming.BinaryRefAddr;

/**
 * An SMS message is a record representing an SMS sent or received from the
 * phone.
 *
 * @author borkholder
 * @date Jan 1, 2008
 */
public class SMSMessage extends Record implements Comparable<SMSMessage> {

    /**
     * The date the SMS was sent;
     */
    protected Date sent=new Date(0);

    /**
     * The date the SMS was received.
     */
    protected Date received=new Date(0);

    /**
     * Was the SMS sent from the phone, or received?
     */
    protected Boolean wasSent=true;

    /**
     * The far number.
     */
    protected String number="ERROR";

    /**
     * The text of the SMS.
     */
    protected String decodedSMS="ERROR";
    private char[]   nonDecodedSms;

    //~--- constructors -------------------------------------------------------

    /**
     * Creates a new record with all provided data.
     *
     * @param dbID The database id
     * @param dbVersion The database version
     * @param uid The unique identifier of this record
     * @param recordLength The length of the record
     */
    public SMSMessage(int dbID, int dbVersion, int uid, int recordLength) {
        super(dbID, dbVersion, uid, recordLength);
        fields=new HashMap<String, String>();
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public void addField(int type, char[] data) {
        StringBuilder builder=null;

        switch (type) {
        case 4 :
            this.nonDecodedSms=data.clone();
            decodedSMS        =Gsm2Iso.Gsm2Iso(data);
            fields.put("text", decodedSMS);

            break;
        case 2 :
            builder=new StringBuilder();

            for (char c : data) {
                if (Character.isLetterOrDigit(c) || (c=='#') || (c=='*') || (c=='(') || (c==')') || (c=='.')
                        || (c=='-') || (c=='+') || (c==' ')) {
                    builder.append(c);
                }
            }

            number=builder.toString();
            fields.put("number", number);

            break;
        case 9 : {

//          viewIt(type, data);
            // This is a sequence number and we don't care about it for now.
            // The sequence number seems to apply only if it was sent from the
            // phone in the IPD.
            break;
        }
        case 7 : {
            System.out.println(String.format("%h", String.valueOf(data)));
            // This marks a USC2 text field
            if
                    (
                   String.format("%h", String.valueOf(data)).equalsIgnoreCase("3b3c8a9f")//greek
                || String.format("%h", String.valueOf(data)).equalsIgnoreCase("fbcca65e")// english - Chineze?
                || String.format("%h", String.valueOf(data)).equalsIgnoreCase("3b3c86de")//czech -
                || String.format("%h", String.valueOf(data)).equalsIgnoreCase("b3c56062")
                || String.format("%h", String.valueOf(data)).equalsIgnoreCase("b3c56423")//Simplified Chineze
                || String.format("%h", String.valueOf(data)).equalsIgnoreCase("1ec6995c")//??
                || String.format("%h", String.valueOf(data)).equalsIgnoreCase("b3c56062")//??
                //|| String.format("%h", String.valueOf(data)).equalsIgnoreCase("8785b25c")//?? !! no need to convert?! breaks greek, but why?
                || String.format("%h", String.valueOf(data)).equalsIgnoreCase("b3b748e1")//??
                || String.format("%h", String.valueOf(data)).equalsIgnoreCase("5392cb24")//?? from greek ipd
                || String.format("%h", String.valueOf(data)).equalsIgnoreCase("f634abe")//?? from greek ipd
                //|| String.format("%h", String.valueOf(data)).equalsIgnoreCase("e8be0")//?? Chineze
                //|| String.format("%h", String.valueOf(data)).equalsIgnoreCase("745f")//??
                || String.format("%h", String.valueOf(data)).equalsIgnoreCase("")
                //("8785b61d")english?
                //("614f7719")english?
                )
            {

                // System.out.print("UCS2: ");viewItInHex(type, data);
                if (fields.containsKey("text")) {
                    fields.remove("text");
                }

                decodedSMS=Gsm2Iso.UCS2toISO(nonDecodedSms);
                fields.put("text", decodedSMS);
            }

            break;
        }
        case 11 : {

//          viewIt(type, data);
            // This is also inconsequential, for me it's 0000 for the first sms,
            // 1000 for all the rest
            break;
        }
        case 1 :
            long val0=0;

            for (int i=13; i<21; i++) {
                val0|=(long) data[i] << ((i-13) * 8);
            }

            long val1=0;

            for (int i=21; i<29; i++) {
                val1|=(long) data[i] << ((i-21) * 8);
            }

            if (data[0]==0) {
                sent    =new Date(val0);
                received=new Date(val1);
                wasSent =true;
            } else {
                sent    =new Date(val1);
                received=new Date(val0);
                wasSent =false;
            }

            fields.put("sent", sent.toString());
            fields.put("received", received.toString());
            fields.put("sent?", wasSent.toString());

            break;
        default :

      viewIt(type, data);
        }
    }

    @Override
    public int compareTo(SMSMessage o) {
        return getSentReceivedDate().compareTo(o.getSentReceivedDate());
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Gets the far phone number.
     *
     * @return The phone number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Gets the date the SMS was received.
     *
     * @return The received date
     */
    public Date getReceived() {
        return received;
    }

    /**
     * Gets the date the SMS was sent.
     *
     * @return The sent date
     */
    public Date getSent() {
        return sent;
    }

    public Date getSentReceivedDate() {
        if (wasSent) {
            return sent;
        } else {
            return received;
        }
    }

    /**
     * Gets the text of the SMS.
     *
     * @return The SMS text
     */
    public String getText() {
        return decodedSMS;
    }

    //~--- methods ------------------------------------------------------------

    @Override
    public String toString() {
        if (wasSent()) {
            return "To: "+getNumber()+" - "+getText();
        } else {
            return "From: "+getNumber()+" - "+getText();
        }
    }

    /**
     * Was the SMS sent from the phone or received.
     *
     * @return True if sent
     */
    public boolean wasSent() {
        return wasSent;
    }
}