package ipddump.tools.writers;

//~--- non-JDK imports --------------------------------------------------------

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@gmail.com
 */
public class FileWriters {

    /**
     * Method description
     *
     *
     * @param filename
     * @param stringToWrite
     *
     * @return
     */
    public boolean writeTextToFile(String filename, String stringToWrite, String fileExtension) {
        try {
            filename=filename.substring(0, filename.lastIndexOf('.'));

            if (!filename.toLowerCase().endsWith(fileExtension.toLowerCase())) {
                filename=filename+fileExtension;
            }

            System.out.println("\n->Writing "+filename);

            return writeStringToFile(filename, stringToWrite);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());

            return false;
        }
    }

    /**
     * Writes the Xml into a file.
     * The diference of the method writeBytes2File is that
     * this one makes the xml 'pretty' and then it writes it.
     *
     *
     * @param path
     * @param document
     *
     * @throws IOException
     */
    public boolean writeXMLtoFile(String filename, Document document) {
        filename=filename.substring(0, filename.lastIndexOf('.'));

        String fileExtension=".xml";

        if (!filename.toLowerCase().endsWith(fileExtension.toLowerCase())) {
            filename=filename+fileExtension;
        }

        System.out.println("\n->Writing "+filename);

        // Make a pretty output
        OutputFormat format=OutputFormat.createPrettyPrint();

        format.setEncoding("UNICODE");

        // format.setTrimText(true);
//      Save it
        XMLWriter writer;

        try {
            writer=new XMLWriter(new FileWriter(filename), format);
            writer.write(document);
            writer.close();

            return true;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());

            return false;
        }
    }

    /**
     * Method description
     *
     *
     * @param pathfile
     * @param content
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static boolean writeStringToFile(String pathfile, String content) {
        BufferedWriter fos;
        String         strFilePath=pathfile;
        String         strContent =content;

        try {

//          System.out.println(strFilePath);
            File out=new File(strFilePath);

            if (!out.exists()) {
                out.createNewFile();
                fos=new BufferedWriter(new FileWriter(out));
            } else {
                fos=new BufferedWriter(new FileWriter(out));
            }

//          FileOutputStream fos = new FileOutputStream(strFilePath);
            fos.write(strContent);

//          fos.write(strContent.getBytes());
            fos.close();

            return true;
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException : "+ex);

            return false;
        } catch (IOException ioe) {
            System.out.println("IOException : "+ioe);

            return false;
        }
    }
}
