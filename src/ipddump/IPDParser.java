package ipddump;

//~--- non-JDK imports --------------------------------------------------------

import ipddump.data.InteractivePagerBackup;
import ipddump.data.Records.*;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileInputStream;
import java.io.IOException;

import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Parse an IPD file and populate a {@link InteractivePagerBackup} with the
 * records.
 *
 * @author borkholder
 * @date Jan 13, 2008
 */
public class IPDParser {

    /**
     * The name/path of the file to parse.
     */
    protected final String fileName;

    /**
     *   Stores the lase valid batabase id that was parced.
     */
    private int     lastValidDBid    =-1;
    private boolean debugingEnabled  =false;
    private int     lastValidDBHandle=0;
    private int     lastfieldLength  =-1;
    private boolean valuePeeking     =false;
    private int     oldDatabaseHandle=1;
    private FileInputStream  input;
    private StringBuilder stringBuilder = new StringBuilder();
    private boolean useAdrrBookAllDB=true;
    private InteractivePagerBackup database         =null;
    private boolean                isDatabaseEncrypded   = false;
    float fcsize=0;
    float fcposition=0;
    private long FcPosAtRecordLength;

    //~--- constant enums -----------------------------------------------------

    /**
     * Specifies the state of the parser, that is, the current part of the IPD
     * that the parser is reading.
     *
     * @author borkholder
     * @date Jan 13, 2008
     */
    protected enum ReadingState {

        /**
         * The file identification header - "Inter@ctive Pager Backup/Restore File".
         */
        HEADER(-1,-1),

        /**
         * The character used to for line feeds in text fields - 0x0A.
         */
        LINEFEED(-1,-1),

        /**
         * The IPD file version - 0x02.
         */
        VERSION(-1,-1),

        /**
         * The number of databases in this file - 2 bytes in big endian.
         */
        DATA_BASE_COUNT(-1,-1),

        /**
         * The byte that separates database names - 0x00.
         */
        DATA_BASE_NAME_SEPARATOR(-1,-1),

        /**
         * The length of the next specified database name - 2 bytes in little
         * endian. The length includes the terminating null of the name string.
         */
        DATA_BASE_NAME_LENGTH(-1,-1),

        /**
         * The name of the database - size as specified by the preceeding database
         * name length.
         */
        DATA_BASE_NAME(-1,-1),

        /**
         * The 0-based index of the database that contains this record - 2 bytes
         * little endian.
         */
        DATA_BASE_ID(-1,-1),

        /**
         * The length of the record in number of bytes that follow this value - 4
         * bytes little endian.
         */
        RECORD_LENGTH(-1,-1),

        /**
         * The version of the database to which this record belongs - 1 byte.
         */
        RECORD_DB_VERSION(-1,-1),

        /**
         * A handle of the record within the database (this is a increasing sequence
         * of integers with the IPD) - 2 bytes little endian.
         */
        DATA_BASE_RECORD_HANDLE(-1,-1),

        /**
         * The unique ID of the record within the database - 4 bytes.
         */
        RECORD_UNIQUE_ID(-1,-1),

        /**
         * The length of the field data in number of bytes - 2 bytes little endian.
         */
        FIELD_LENGTH(-1,-1),

        /**
         * The type of the field within the record - 1 byte.
         */
        FIELD_TYPE(-1,-1),

        /**
         * The field data - as long as is specified by the preceeding length.
         */
        FIELD_DATA(-1,-1);

        long startOfOffset;
        long endOfOffset;

        private ReadingState(long startOfOffset, long endOfOffset) {
        this.startOfOffset=startOfOffset;
        this.endOfOffset=endOfOffset;
        }

        public long[] getOffset(){
            long[] j= {this.startOfOffset, this.endOfOffset};
        return j;
        }

        public void setOffset(long startOfOffset, long endOfOffset){
            if (startOfOffset!=-1){
            this.startOfOffset=startOfOffset;}

                    if (endOfOffset!=-1){
            this.endOfOffset=endOfOffset-1;
       //    System.out.format("%25s: %9d  %9d\n",super.name(),this.startOfOffset,this.endOfOffset);
            }
        }
        
    }

    //~--- constructors -------------------------------------------------------

    /**
     * Creates a new IPDParser that will parse the file at the given path.
     *
     * @param fileName The path of the file to parse
     */
    public IPDParser(String fileName) {
        this.fileName=fileName;
    }
    //~--- methods ------------------------------------------------------------

    public void enableDebuging() {
        debugingEnabled=true;
    }

    public void enableValuePeeking() {
        valuePeeking=true;
    }
        int                    recordRead       =0;
        int                    fieldLength      =0;
        int                    sumfieldLength   =0;
        FileChannel fc=null;
    /**
     * Parses the provided IPD file into an {@link InteractivePagerBackup}.
     *
     * @return A new InteractivePagerBackup representing the IPD file
     * @throws IOException Any outor in reading the IPD file
     */
    public InteractivePagerBackup parse() throws IOException {
try{
        // Temporary variables used in parsing
        char                   lineFeed         =0;
        int                    numberOfDatabases=0;
        int                    dbNameLength     =0;
        int                    recordDBVersion  =0;
        int                    databaseHandle   =0;
        int                    fieldType        =0;
        int                    dbID             =0;
        int                    recordLength     =0;
//        int                    uid              =0;
        Record                 record           =null;
        
        ReadingState           state            =ReadingState.HEADER;


        if (fileName.endsWith(".bbb")){
        //do unziping actions here
        }

        input=new FileInputStream(fileName);
        fc=input.getChannel();
        fcsize=fc.size();

            // Start reading in the header state
            while (fc.position()<fc.size()) {
                switch (state) {
                case HEADER :
                    //ReadingState.HEADER.setOffset(fc.position(),-1);
                    for (int i=0; i<"Inter@ctive Pager Backup/Restore File".length(); i++) {
                        input.read();
                    }
                    
                    if (input.read()==' '){
                    isDatabaseEncrypded=true;
                    input.skip(10);
                    //decrypting code goes here
                    }
                    else {
                        input.skip(-1);
                    }

                    state=ReadingState.LINEFEED;
                    ReadingState.HEADER.setOffset(-1,fc.position());
                    break;
                case LINEFEED :
                    ReadingState.LINEFEED.setOffset(fc.position(),-1);
                    lineFeed=(char) input.read();
                    state   =ReadingState.VERSION;
                    ReadingState.LINEFEED.setOffset(-1,fc.position());
                    break;
                case VERSION :
                    ReadingState.VERSION.setOffset(fc.position(),-1);
                    database=new InteractivePagerBackup(input.read(), lineFeed, isDatabaseEncrypded);

                                if (debugingEnabled) {
                                    database.enableDebuging();
                                    System.out.print("Version: "+database.getVersion());
                                    System.out.println(String.format(" -- LineFeed: %h", database.getLineFeed()));
                                }

                                if (valuePeeking) {
                                    database.enableValuePeeking();
                                }

                    state=ReadingState.DATA_BASE_COUNT;
                    ReadingState.VERSION.setOffset(-1,fc.position());
                    break;
                case DATA_BASE_COUNT :
                    ReadingState.DATA_BASE_COUNT.setOffset(fc.position(),-1);
                    numberOfDatabases=input.read() << 8;
                    numberOfDatabases|=input.read();
                    
                    state            =ReadingState.DATA_BASE_NAME_SEPARATOR;
                    ReadingState.DATA_BASE_COUNT.setOffset(-1,fc.position());
                    break;

                // Just eat it because we know the terminating null will be in
                // the name anyway
                case DATA_BASE_NAME_SEPARATOR :
                    ReadingState.DATA_BASE_NAME_SEPARATOR.setOffset(fc.position(),-1);
                        input.read();
                        


                    state=ReadingState.DATA_BASE_NAME_LENGTH;
                    ReadingState.DATA_BASE_NAME_SEPARATOR.setOffset(-1,fc.position());
                    break;
                case DATA_BASE_NAME_LENGTH :
                    ReadingState.DATA_BASE_NAME_LENGTH.setOffset(fc.position(),-1);
                    dbNameLength=input.read();
                    dbNameLength|=input.read() << 8;
                    
                    state       =ReadingState.DATA_BASE_NAME;
                    ReadingState.DATA_BASE_NAME_LENGTH.setOffset(-1,fc.position());
                    break;
                case DATA_BASE_NAME :
                    ReadingState.DATA_BASE_NAME.setOffset(fc.position(),-1);
                    StringBuilder buffer=new StringBuilder();

//                     Read everything but the terminating null
                    for (int i=0; i<dbNameLength-1; i++) {
                        buffer.append((char) input.read());
                        
                    }

                    database.addDatabase(buffer.toString());

                    // Eat null/separator
                    input.read();
                    

                    if (database.getDatabaseNames().size()<numberOfDatabases) {
                        state=ReadingState.DATA_BASE_NAME_LENGTH;
                    } else {
                        state=ReadingState.DATA_BASE_ID;
                    }
                    ReadingState.DATA_BASE_NAME.setOffset(-1,fc.position());
                    break;
                case DATA_BASE_ID :
                   ReadingState.DATA_BASE_ID.setOffset(fc.position(),-1);
                    int temp;
                    temp=dbID;
                    dbID =input.read();
                    dbID |=input.read() << 8;

                    if (dbID==65535)dbID=database.getDatabaseNames().size()-1;
                                           if (((dbID>database.getDatabaseNames().size()) || dbID<0)&& debugingEnabled)
                                           {
                        //                       System.out.println(String.format("----- GOT ERROR -----dbID: hex: %4h dec: %5d --Old DBid: hex: %4h dec: %5d",dbID,dbID,temp,temp));
                                               //input.skip(-tries);
                                               //state=ReadingState.DATA_BASE_ID;
                                               //break;
                                           }
                   
                    state=ReadingState.RECORD_LENGTH;
                    ReadingState.DATA_BASE_ID.setOffset(-1,fc.position());
                    break;
                case RECORD_LENGTH :
                    ReadingState.RECORD_LENGTH.setOffset(fc.position(),-1);
                    recordLength=input.read();
                    recordLength|=input.read() << 8;
                    recordLength|=input.read() << 16;
                    recordLength|=input.read() << 24;


                    recordRead  =0;
                    FcPosAtRecordLength= fc.position();
                    state       =ReadingState.RECORD_DB_VERSION;
                    ReadingState.RECORD_LENGTH.setOffset(-1,fc.position());
                    break;
                case RECORD_DB_VERSION :
                    ReadingState.RECORD_DB_VERSION.setOffset(fc.position(),-1);
                    recordRead++;
                    recordDBVersion=input.read();
                    
                    state=ReadingState.DATA_BASE_RECORD_HANDLE;
                    ReadingState.RECORD_DB_VERSION.setOffset(-1,fc.position());
                    break;
                case DATA_BASE_RECORD_HANDLE :
                    ReadingState.DATA_BASE_RECORD_HANDLE.setOffset(fc.position(),-1);
                    databaseHandle=input.read();
                    databaseHandle|=input.read() << 8;
                    
                    recordRead    +=2;
                    state         =ReadingState.RECORD_UNIQUE_ID;

                                        if (debugingEnabled) {

                                            // System.out.println("DATABASERECORDHANDLE: "+databaseHandle);
                                            if (databaseHandle!=oldDatabaseHandle) {
                                                System.out.println("Database Handle Error! old dbHandle: "+(oldDatabaseHandle)
                                                                   +" new dbHandle: "+databaseHandle);
                                                oldDatabaseHandle=databaseHandle+1;
                                            } else {
                                                oldDatabaseHandle+=1;
                                            }
                                        }
                    ReadingState.DATA_BASE_RECORD_HANDLE.setOffset(-1,fc.position());
                    break;
                case RECORD_UNIQUE_ID :
                    ReadingState.RECORD_UNIQUE_ID.setOffset(fc.position(),-1);
                    //Uid is ussles for this apps perpose
//                    uid       =input.read();
//                    uid       |=input.read() << 8;
//                    uid       |=input.read() << 16;
//                    uid       |=input.read() << 24;
                    input.skip(4);
                    recordRead+=4;

                    if (dbID<database.getDatabaseNames().size() && dbID>0) {
                        lastValidDBid    =dbID;
                        lastValidDBHandle=databaseHandle;
                    }

                    record=database.createRecord(dbID, recordDBVersion, recordLength);
                    record.setRecordDBHandle(databaseHandle);
                    state=ReadingState.FIELD_LENGTH;
                    ReadingState.RECORD_UNIQUE_ID.setOffset(-1,fc.position());
                    break;
                case FIELD_LENGTH :
                    ReadingState.FIELD_LENGTH.setOffset(fc.position(),-1);
                    fieldLength=input.read();
                    fieldLength|=input.read() << 8;
                    sumfieldLength+=fieldLength;
                    recordRead +=2;
                    state      =ReadingState.FIELD_TYPE;
                    ReadingState.FIELD_LENGTH.setOffset(-1,fc.position());
                    break;
                case FIELD_TYPE :
                    ReadingState.FIELD_TYPE.setOffset(fc.position(),-1);
                    fieldType=input.read();

                    recordRead++;
                    state=ReadingState.FIELD_DATA;
                    ReadingState.FIELD_TYPE.setOffset(-1,fc.position());
                    break;
                case FIELD_DATA :
                    ReadingState.FIELD_DATA.setOffset(fc.position(),-1);


            //                    good
                                 if (fieldLength>recordLength){
            //                        System.out.println("\n------------read: "+recordRead+" length: "+recordLength);

                                    if (fc.position()-recordLength-recordRead>0){
                                    fieldLength=fieldLength-recordRead;
            //                            System.out.println("field Length Error Found");
                                    }
                                 }

                    char[] dataBuffer=new char[fieldLength];
                    for (int i=0; i<fieldLength; i++) {
                        dataBuffer[i]=(char) input.read();
                    }

                                if ((dbID>database.getDatabaseNames().size() || dbID<0) && debugingEnabled) {
                                    database.setErrorFlag();

                                    String dbname=String.valueOf(lastValidDBid);

                                    if (lastValidDBid>=0) {
                                        dbname=database.getDatabaseNames().get(lastValidDBid);
                                    }
                                    System.out.format(fieldLength+"Problematic dbIndex: hex: %4h dec: %5d "
                                                      +"database Size: %3d -- Last valid DBid:%3d Name: %s -- ", dbID, dbID,
                                                          database.getDatabaseNames().size(), lastValidDBid, dbname);
                                    System.out.println("\n    Last Valid BD handle: "+lastValidDBHandle+" this BD handle: "
                                                       +databaseHandle);
                                    System.out.println("    Last Valid Field length: "+lastfieldLength+" this Field length: "
                                                       +fieldLength);

                                } else {
                                    lastfieldLength  =fieldLength;
                                    lastValidDBid    =dbID;
                                    lastValidDBHandle=databaseHandle;
                                }

                    record.addField(fieldType, dataBuffer);
                    recordRead+=fieldLength;



//                    good
                    if (recordRead>recordLength){

                        //System.out.println("\n------------read: "+recordRead+" length: "+recordLength);

                        long prevPos=fc.position();

                        if (fc.position()-recordLength-recordRead>0){

                        input.skip(recordLength-recordRead);
                        }

                        //System.out.println("Record Length Error Found");
                    }

                    fcposition=fc.position();
                    if (recordRead<recordLength) {
                        state=ReadingState.FIELD_LENGTH;
                    } else 
//                        if (recordRead==recordLength)
                        {

                        input.skip(FcPosAtRecordLength+recordLength-fc.position());
                        state=ReadingState.DATA_BASE_ID;
                    }
                    ReadingState.FIELD_DATA.setOffset(-1,fc.position());
                    break;
                }
            }
        

        if (debugingEnabled) {
            for (int i=0; i<database.getDatabaseNames().size(); i++) {
                System.out.print(i+": "+database.getDatabaseNames().get(i)+", ");
            }

            System.out.println("");
        }

        database.organize();
        
        fcsize=0;
        input.close();
        return database;
        } catch (Exception ex){
                ex.printStackTrace();
                fcposition = fcsize;
                input.close();
                return database;
        }


    }

    
   synchronized public int getCompletedPersentage(){
        try{
                if (fcsize!=0){
            return (int)((float)fcposition / fcsize*100.0);}
                else return -1;
        } catch (Exception ex) {
            Logger.getLogger(IPDParser.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
    }
}
