package ipddump.tools.writers;

//~--- non-JDK imports --------------------------------------------------------

import ipddump.data.InteractivePagerBackup;
import ipddump.data.Records.Task;

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
 * @author Jimmys Daskalakis
 */
public class TasksWriters extends BasicWriter {
    public TasksWriters(InteractivePagerBackup database) {
        super(database);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public int getSize() {
        if (database!=null) {
            return database.getTasks().size();
        } else {
            return 0;
        }
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param selectedRecords
     *
     * @return
     */
    @Override
    public String toCSV(int[] selectedRecords) {
        StringBuilder builder=new StringBuilder();    // fast builder!!

        builder.delete(0, builder.capacity());

        /*
         * Get all the keys since we don't know all of them ahead
         * of time.  Some fields might be duplicated several times.
         */
        Set<String> keys=new TreeSet<String>();

        for (Task record : database.getTasks()) {
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

        for (Task record : database.getTasks()) {
            if (isSelectedRecord(RecordIndex, selectedRecords) && (selectedRecords[j]<database.getTasks().size())) {
                j++;
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

                if (j>=selectedRecords.length) {
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
     * @param SelectedRecords
     *
     * @return
     */
    @Override
    public String toPlainText(int[] SelectedRecords) {
        StringBuilder tmp=new StringBuilder();

        if (database!=null) {
            int RecordIndex=0;
            int j          =0;

            for (Task record : database.getTasks()) {
                if (isSelectedRecord(RecordIndex, SelectedRecords) && (SelectedRecords[j]<database.getTasks().size())) {
                    j++;

                    Iterator iterator2=record.fields().entrySet().iterator();

                    for (Iterator iterator=iterator2; iterator2.hasNext(); ) {
                        Map.Entry entry=(Map.Entry) iterator.next();

                        tmp.append(entry.getKey()+": "+entry.getValue()+"\n");
                    }

                    tmp.append("\n");

                    if (j>=SelectedRecords.length) {
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
     * @param SelectedRecords
     *
     * @return
     */
    @Override
    public Document toXML(int[] SelectedRecords) {
        Document document=DocumentHelper.createDocument();

        // Add the root
        Element root       =document.addElement("Tasks").addAttribute("TotalTasks",
                                String.valueOf(SelectedRecords.length));
        int     RecordIndex=0;
        int     j          =0;

        for (Task record : database.getTasks()) {
            if (isSelectedRecord(RecordIndex, SelectedRecords) && (SelectedRecords[j]<database.getTasks().size())) {
                Element  message  =root.addElement("Task");
                Iterator iterator2=record.fields().entrySet().iterator();

                for (Iterator iterator=iterator2; iterator2.hasNext(); ) {
                    Map.Entry entry=(Map.Entry) iterator.next();
                    String    type =entry.getKey().toString().replaceAll(" ", "");
                    String    value=entry.getValue().toString();

                    message.addElement(type).addText(value);
                }

                j++;

                if (j>=SelectedRecords.length) {
                    break;
                }
            }

            RecordIndex++;
        }

        return createPrettyPrint(document);
    }
}
