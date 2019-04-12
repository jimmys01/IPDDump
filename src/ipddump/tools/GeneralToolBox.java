package ipddump.tools;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Jimmys Daskalakis
 */
public class GeneralToolBox {
    public String makeStringCropLast(char[] data) {
        String str=Gsm2Iso.Gsm2Iso(data);

        if (str.length()<=0) return "";
        return str.substring(0, str.length()-1);
    }

     public String makeString(char[] data) {

        return Gsm2Iso.Gsm2Iso(data);
    }
    /**
     *  Displays the type and the value of the given
     *   unknown field
     *
     *   @param type The type of field
     *   @param data The field data
     */
    public void viewIt(int type, char[] data) {
        //System.out.format("Type:%d Data String:%s length:%d\n", type, String.valueOf(data), data.length);
    }

    public void viewIt(int type, String string) {
        //System.out.format("Type %d Data String:%s\n", type, string);
    }

    public void viewItInHex(int type, char[] data) {
        System.out.format("Type %d Data Hex:%h length:%d\n", type, String.valueOf(data), data.length);
    }

    public void viewItInHex(int type, String string) {
        System.out.format("Type %d Data Hex:%h\n", type, string);
    }

    public void viewItInInt(int type, char[] data) {
        System.out.format("Type %d Data Int:%d\n", type, makeInt(data));
    }

    /**
     * Making a date is not simple, RIM doesn't use the standard
     * "seconds since the epoch". The unit is minutes, but the zero point is
     * somewhere around the start of 1900.
     */
    protected Date makeDate(char[] data) {
        long time=data[0] << 0;

        time|=data[1] << 8;
        time|=data[2] << 16;
        time|=data[3] << 24;

        // Turn into milliseconds units
        time*=60 * 1000;

        // Zero out at Jan 1, 1900
        time-=2208970740000L;

        // Make the offset be the local timezone, minus the odd 61 minutes
        int offset=TimeZone.getDefault().getOffset(time);

        offset-=61 * 60 * 1000;

        return new Date(time+offset);
    }

    protected Date makeDate2(char[] data) {
        long val0=0;

        for (int i=0; i<8; i++) {
            val0|=(long) data[i] << (i * 8);
        }

        return new Date(val0);
    }

    protected String makeDuration(int seconds) {
        if (seconds==0) {
            return "None";
        }

        int duration=seconds;
        int hours   =0;
        int minutes =0;
        int sec     =0;

        hours  =duration / 3600;
        minutes=(duration-(3600 * hours)) / 60;
        sec    =duration-((hours * 3600)+(minutes * 60));

        String stduration="";

        if (hours>0) {
            stduration=String.valueOf(hours)+":";
        }

        if (minutes<10) {
            stduration+="0";
        }

        stduration+=String.valueOf(minutes)+":";

        if (sec<10) {
            stduration+="0";
        }

        stduration+=String.valueOf(sec);

        return stduration;
    }

    protected int makeInt(char[] data) {
        int temp=-1000;

        if (data.length==4) {
            temp=data[0];
            temp|=data[1] << 8;
            temp|=data[2] << 16;
            temp|=data[3] << 24;
        } else if (data.length==8) {
            temp=data[0];
            temp|=data[1] << 8;
            temp|=data[2] << 16;
            temp|=data[3] << 24;
            temp|=data[4] << 32;
            temp|=data[5] << 40;
            temp|=data[6] << 48;
            temp|=data[7] << 56;
        } else {
            System.out.println("Invalid Int found size: "+data.length);
        }

        return temp;
    }
}
