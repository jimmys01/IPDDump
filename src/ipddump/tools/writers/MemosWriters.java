/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package ipddump.tools.writers;

//~--- non-JDK imports --------------------------------------------------------

import ipddump.data.InteractivePagerBackup;
import ipddump.data.Records.Memo;

import org.dom4j.*;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@gmail.com
 */
public class MemosWriters extends BasicWriter {
    public MemosWriters(InteractivePagerBackup database) {
        super(database);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the total of the Memo messages
     *
     *
     * @return
     */
    public int getSize() {
        if (database!=null) {
            return database.getMemos().size();
        } else {
            return 0;
        }
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Get the cvs of the parsed Memo's
     *
     *
     * @return
     */
    public String toCSV(int[] selectedMemos) {
        StringBuilder temp=new StringBuilder();    // fast builder!!

        temp.delete(0, temp.capacity());
        temp.append("Title,Memo\n");

        int RecordIndex=0;
        int j          =0;

        for (Memo record : database.getMemos()) {
            if (isSelectedRecord(RecordIndex, selectedMemos) && (selectedMemos[j]<database.getMemos().size())) {
                temp.append(record.getTitle()+","+record.getMemo()+"\n");
                j++;

                if (j>=selectedMemos.length) {
                    break;
                }
            }

            RecordIndex++;
        }

        return temp.toString();
    }

    /**
     * Get a represantation of the parsed Memo's
     * in plain text
     *
     *
     * @param database
     * @param MemoselectedRows
     *
     * @return
     */
    public String toPlainText(int[] MemoselectedRows) {
        StringBuilder tmp=new StringBuilder();

        if (database!=null) {
            int RecordIndex=0;
            int j          =0;

            for (Memo record : database.getMemos()) {
                if (isSelectedRecord(RecordIndex, MemoselectedRows)
                        && (MemoselectedRows[j]<database.getMemos().size())) {
                    String title=record.getTitle();
                    String memo =record.getMemo();

                    tmp.append("Title: "+title+"\nMemo:\n"+memo+"\n\n");
                    j++;

                    if (j>=MemoselectedRows.length) {
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
     * Get the XML of the parsed Memo's
     *
     * @param database
     * @param selectedMemos
     *
     * @return
     */
    public Document toXML(int[] selectedMemos) {
        Document document=DocumentHelper.createDocument();

        // Add the root
        Element root       =document.addElement("Memos").addAttribute("TotalMemos",
                                String.valueOf(selectedMemos.length));
        int     RecordIndex=0;
        int     j          =0;

        for (Memo record : database.getMemos()) {
            if (isSelectedRecord(RecordIndex, selectedMemos) && (selectedMemos[j]<database.getMemos().size())) {
                Element message=root.addElement("MemoMessage");

                message.addElement("Title").addText(record.getTitle());
                message.addElement("Memo").addText(record.getMemo());
                j++;

                if (j>=selectedMemos.length) {
                    break;
                }
            }

            RecordIndex++;
        }

        return createPrettyPrint(document);
    }
}
