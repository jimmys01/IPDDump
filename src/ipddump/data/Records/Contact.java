package ipddump.data.Records;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.nio.Buffer;
import java.util.HashMap;

/**
 * A contact is a record representing contact information stored in the address
 * book.
 *
 * @author borkholder
 * @date Jun 6, 2009
 */
public class Contact extends Record implements Comparable<Contact> {
    private boolean enableAdrressBookAllType=false;
    private Image   image;
    private static int countAdrrBookAllrecords=0;

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
    public Contact(int dbID, int dbVersion, int uid, int recordLength) {
        super(dbID, dbVersion, uid, recordLength);
        fields = new HashMap<String, String>();
    }

    enum Field {
        Last(111), Nick(86), Email(1), WorkFax(3), HomeFax(20), Work_Phone(6), Work_Phone2(16), Home_Phone(17),Home_Phone2(17), Mobile_Phone(8),Mobile_Phone2(19),
        Pager(9), PIN(10), Other_Number(18), Name(32), Company(33), Work_Address(35, 36), Work_City(38), Work_State(39),
        Work_Postcode(40), Work_Country(41), Job_Title(42), Webpage(54), Title(55),

        /* These are the Category tags. Null data if no tags. */
        Categories(59), Home_Address(61, 62), User(65, 66, 67, 68), Home_City(69), Home_State(70), Home_Postcode(71),
        Home_Country(72), Contact_Image(77), Notes(64), Birthday(82), Anniversary(83),

        /* This is always the same 8 characters */
        Unknown_8_Chars(false, 84),

        /* Always 4 characters, date? */
        Unknown_4_Chars(false, 85), Google_Talk(90),Yahoo(91);

        int[]   indexes;
        boolean supported;

        Field(int... indexes) {
            this(true, indexes);
        }

        Field(boolean supported, int... indexes) {
            this.indexes   = indexes;
            this.supported = supported;
        }

        public boolean accept(int code) {
            if (supported) {
                for (int index : indexes) {
                    if (index == code) {
                        return true;
                    }
                }
            }

            return false;
        }

        @Override
        public String toString() {
            return super.toString().replace('_', ' ');
        }
    }

    private void doFields(int type, char[] data) {
        for (Field field : Field.values()) {
            if (field.accept(type)) {
                if (field == Field.Contact_Image) {
                    image = decodeBase64(data);
                } else if (field == Field.Categories) {

                    // Microsoft Outlook Style. If sepated by commas then the CSV brakes.
                    addField(field, makeStringCropLast(data).replace(',', ';'));
                } else if (field == Field.Name && enableAdrressBookAllType) {

                    // Microsoft Outlook Style. If sepated by commas then the CSV brakes.
                    addField(field, makeString(data));
                } else {
                    addField(field, makeStringCropLast(data));
                }
            } 
        }

    }

    public void enableAdrressBookAllType() {
        countAdrrBookAllrecords++;
        enableAdrressBookAllType = true;
    }

     public int getAdrrBookAllrecordsCount(){
    return countAdrrBookAllrecords;
    }

      public void setAdrrBookAllrecordsToZero(){
     countAdrrBookAllrecords=0;
    }

    public void addField(Field key, String value) {
        String keyName = key.toString();

        if (key == Field.Name) {
            String name = fields.get(keyName);

            if (name == null) {
                fields.put(keyName, value);
            } else {
                fields.put(keyName, name + " " + value);
            }
        } else if (fields.containsKey(keyName)) {
            int index = 2;

            while (fields.containsKey(keyName + index)) {
                index++;
            }

            fields.put(keyName + index, value);
        } else {
            fields.put(keyName, value);
        }
    }

    @Override
    public void addField(int type, char[] data) {
        if (!enableAdrressBookAllType) {
            doFields(type, data);
        } else {
            switch (type) {
            case 2 : {
                System.out.println("Type: " + type + " --Data: " + makeString(data.clone()) + " size: " + data.length);

                break;
            }

            case 3 : {

                // Seems Always to be Default
                System.out.println("Type: " + type + " --Data: " + makeString(data.clone()) + " size: " + data.length);

                break;
            }

            case 5 : {
                System.out.println("Type: " + type + " --Data: " + makeString(data.clone()) + " size: " + data.length);

                break;
            }

            case 10 : {
                type   = 32;
                length = data[0];

                int pointer = 2;

                for (pointer = 2; pointer + length <= data.length; pointer += 0) {
                    System.out.format("\n--type: %h - %d Length: %h - %d Data: ", type, type, length, length);
                    System.out.print(String.valueOf(data.clone()).substring(pointer, length + pointer));
                    pointer += 0;
                    parseTypes(type, data.clone(), pointer, length);
                    pointer += length;

                     System.out.println("--pointer: "+pointer);
                    if (pointer + length < data.length && data[pointer] == 0) {

                        length = data[pointer + 1];
                        length |= data[pointer + 2] << 8;
                        System.out.println("pointer data=0 , getting length "+length);
                        System.out.println("--pointer: "+pointer);
                        if (length <255) {
                            type=111;
                        } else {type    = -1;}
                        pointer += 4;
                    } else if (pointer < data.length-3) {
                        length  = data[pointer];
                        type    = data[pointer + 2];
                        pointer += 3;
                        System.out.println("--pointer: "+pointer);
                    }
                }

                parseTypes(type, data.clone(), pointer, length);
                System.out.println((pointer + length) +" "+ data.length);
            }
            
            }
        }
    }

    private void parseTypes(int type, char[] data, int pointer, int length) {
        switch (type) {

            
            case -1 : {
            //Image type
                break;
            }

        case 111 : {
            if (((length + pointer) < data.length) && (length + pointer>0)) {

                if (!(String.valueOf(data).substring(pointer, length + pointer)).equals("ÿÿÿÿÿÿÿÿ"))
                doFields(type,(String.valueOf(data).substring(pointer, length + pointer)).toCharArray());
            }

            break;
        }

        default : {
            if (length + pointer <= data.length) {    // unknown Error-Bug?
                doFields(type, (String.valueOf(data).substring(pointer, length + pointer).toCharArray()));
            }

            break;
        }
        }
    }

    @Override
    public int compareTo(Contact o) {
        return getName().compareTo(o.getName());
    }

    public String getAnniversary() {
        return getField(Field.Anniversary);
    }

    public String getBirthday() {
        return getField(Field.Birthday);
    }

    public String getCategories() {
        return getField(Field.Categories);
    }

    public String getCompany() {
        return getField(Field.Company);
    }

    public String getEmail() {
        return getField(Field.Email);
    }

    public String getGoogleTalk() {
        return getField(Field.Google_Talk);
    }

    public String getHomeAddress() {
        return getField(Field.Home_Address);
    }

    public String getHomeCity() {
        return getField(Field.Home_City);
    }

    public String getLastName() {
        return getField(Field.Last); 
    }

    public String getHomeCountry() {
        return getField(Field.Home_Country);
    }

    public String getHomePhone() {
        if (!getField(Field.Home_Phone).equals(""))
        return getField(Field.Home_Phone);
        else return getField(Field.Home_Phone2);
    }

    public String getHomePhone2() {
        return getField(Field.Home_Phone2);
    }

    public String getHomePostcode() {
        return getField(Field.Home_Postcode);
    }

    public String getHomeState() {
        return getField(Field.Home_State);
    }

    public Image getImage() {
        return image;
    }

    public String getJobTitle() {
        return getField(Field.Job_Title);
    }

        public String getMobilePhone() {
        if (!getField(Field.Mobile_Phone).equals(""))
        return getField(Field.Mobile_Phone);
        else return getField(Field.Mobile_Phone2);
    }

        public String getMobilePhone2() {
        return getField(Field.Mobile_Phone2);
    }

        public String getYahoo() {
        return getField(Field.Yahoo);
    }

    public String getName() {

        if (enableAdrressBookAllType){
        return getField(Field.Name).substring(1)+" "+getField(Field.Last);
        }else{
        return getField(Field.Name)+" "+getField(Field.Last);
    }
    }

    public String getNotes() {
        return getField(Field.Notes);
    }

    public String getOtherNumber() {
        return getField(Field.Other_Number);
    }

    public String getPIN() {
        return getField(Field.PIN);
    }

    public String getPager() {
        return getField(Field.Pager);
    }

    public String getTitle() {
        return getField(Field.Title);
    }

    public String getUser() {
        return getField(Field.User);
    }

    public String getWebpage() {
        return getField(Field.Webpage);
    }

    public String getWorkAddress() {
        return getField(Field.Work_Address);
    }

    public String getWorkCity() {
        return getField(Field.Work_City);
    }

    public String getWorkCountry() {
        return getField(Field.Work_Country);
    }

    public String getWorkFax() {
        return getField(Field.WorkFax);
    }

    public String getHomeFax() {
        return getField(Field.HomeFax);
    }


        public String getWorkPhone() {
        if (!getField(Field.Work_Phone).equals(""))
        return getField(Field.Work_Phone);
        else return getField(Field.Work_Phone2);
    }


    public String getWorkPhone2() {
        return getField(Field.Work_Phone);
    }

    public String getWorkPostcode() {
        return getField(Field.Work_Postcode);
    }

    public String getWorkState() {
        return getField(Field.Work_State);
    }

    @Override
    public String toString() {
        return getName();
    }

    private Image decodeBase64(char[] sb) {

        // maybe this is the correct way to display the image?
        byte[] buffer_decode = new byte[sb.length];

        for (int i = 0; i < sb.length; i++) {
            buffer_decode[i] = (byte) sb[i];
        }

        return Toolkit.getDefaultToolkit().createImage(buffer_decode);
    }

    private String getField(Field field) {
        String key = field.toString();

        if (fields.containsKey(key)) {
            return fields.get(key);
        } else {
            return "";
        }
    }
            public boolean isAdrrBookAllDB() {
       return enableAdrressBookAllType;
    }
}
