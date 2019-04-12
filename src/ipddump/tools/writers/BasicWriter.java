package ipddump.tools.writers;

//~--- non-JDK imports --------------------------------------------------------

import ipddump.data.InteractivePagerBackup;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.StringWriter;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@gmail.com
 */
abstract public class BasicWriter {
    protected InteractivePagerBackup database;

    //~--- constructors -------------------------------------------------------

    public BasicWriter(InteractivePagerBackup database) {
        this.database=database;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    abstract public int getSize();

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param database
     */
    public void setDatabase(InteractivePagerBackup database) {
        this.database=database;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public String toCSV() {
        return toCSV(getAllRecords());
    }

    /**
     * Method description
     *
     *
     * @param totalnumber
     *
     * @return
     */
    abstract public String toCSV(int[] selectedRecords);

    /**
     * Method description
     *
     *
     * @return
     */
    public String toPlainText() {
        return toPlainText(getAllRecords());
    }

    /**
     * Method description
     *
     *
     * @param totalnumber
     *
     * @return
     */
    abstract public String toPlainText(int[] SelectedRecords);

    /**
     * Method description
     *
     *
     * @return
     */
    public Document toXML() {
        return toXML(getAllRecords());
    }

    /**
     * Method description
     *
     *
     * @param totalnumber
     *
     * @return
     */
    abstract public Document toXML(int[] SelectedRecords);

    /**
     * Method description
     *
     *
     * @param document
     *
     * @return
     */
    protected Document createPrettyPrint(Document document) {
        OutputFormat format=OutputFormat.createPrettyPrint();

        format.setEncoding("UNICODE");

        XMLWriter    writer;
        StringWriter str=new StringWriter();

        writer=new XMLWriter(str, format);

        try {
            writer.write(document);
            writer.close();
            document=DocumentHelper.parseText(str.toString());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (DocumentException ex) {
            System.out.println(ex.getMessage());
        }

        return document;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Fills a int array with the instructions
     * for 'choosing' all the records from a database
     *
     * @return
     */
    protected int[] getAllRecords() {
        int[] allRecords=new int[getSize()];

        for (int i=0; i<allRecords.length; i++) {
            allRecords[i]=i;
        }

        return allRecords;
    }

    protected boolean isSelectedRecord(int curentIndex, int[] selectedRecord) {
        for (int i=0; i<selectedRecord.length; i++) {
            if (curentIndex==selectedRecord[i]) {
                return true;
            }
        }

        return false;
    }
}
