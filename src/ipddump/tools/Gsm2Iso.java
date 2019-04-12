package ipddump.tools;
/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@gmail.com
 */
public class Gsm2Iso {

    /**
     * GSM 03.38 to ISO standart converter
     *
     *
     * @param data
     */
    public static String Gsm2Iso(char[] data) {
        for (int i=0; i<data.length; i++) {
            if (data[i]==0x40) {
                data[i]='@';
            } else if (data[i]==0x00) {
                data[i]='@';
            } else if (data[i]==0x80) {
                data[i]='€';
            } else if (data[i]==0x01) {
                data[i]='£';
            } else if (data[i]==0x02) {
                data[i]='$';
            } else if (data[i]==0x03) {
                data[i]='¥';
            } else if (data[i]==0x04) {
                data[i]='è';
            } else if (data[i]==0x05) {
                data[i]='é';
            } else if (data[i]==0x06) {
                data[i]='ù';
            } else if (data[i]==0x07) {
                data[i]='ì';
            } else if (data[i]==0x08) {
                data[i]='ò';
            } else if (data[i]==0x09) {
                data[i]='Ç';
            } else if (data[i]==0x0A) {
                data[i]='\n';
            } else if (data[i]==0x0B) {
                data[i]='Ø';
            } else if (data[i]==0x0C) {
                data[i]='ø';
            } else if (data[i]==0x0D) {
                data[i]='?';
            } else if (data[i]==0x0E) {
                data[i]='Å';
            } else if (data[i]==0x0F) {
                data[i]='å';
            } else if (data[i]==0x10) {
                data[i]='Δ';

                // System.out.println("Delta");
            } else if (data[i]==0x11) {
                data[i]='_';
            } else if (data[i]==0x12) {
                data[i]='Φ';

                // System.out.println("Fi");
            } else if (data[i]==0x13) {
                data[i]='Γ';

                // System.out.println("Gamma");
            } else if (data[i]==0x14) {
                data[i]='Λ';

                // System.out.println("Lamda");
            } else if (data[i]==0x15) {
                data[i]='Ω';

                // System.out.println("Omega");
            } else if (data[i]==0x16) {
                data[i]='Π';

                // System.out.println("Pi");
            } else if (data[i]==0x17) {
                data[i]='Ψ';

                // System.out.println("Psi");
            } else if (data[i]==0x18) {
                data[i]='Σ';

                // System.out.println("Sigma");
            } else if (data[i]==0x19) {
                data[i]='Θ';

                // System.out.println("Thita");
            } else if (data[i]==0x1A) {
                data[i]='Ξ';

                // System.out.println("Ksi");
            } else if (data[i]==0x1B) {

                // data[i]='';//<ESC>
                // System.out.println("esc character");
            } else if (data[i]==0x1C) {
                data[i]='Æ';
            } else if (data[i]==0x1D) {
                data[i]='æ';
            } else if (data[i]==0x1E) {

                // data[i]='';//??
            } else if (data[i]==0x1F) {
                data[i]='É';
            } else if (data[i]==0x20) {
                data[i]=' ';
            } else if (data[i]==0x21) {
                data[i]='!';
            } else if (data[i]==0x22) {
                data[i]='"';
            } else if (data[i]==0x23) {
                data[i]='#';
            } else if (data[i]==0x24) {
                data[i]='¤';
            } else if (data[i]==0x25) {
                data[i]='%';
            } else if (data[i]==0x26) {
                data[i]='&';
            } else if (data[i]==0x27) {
                data[i]='\'';
            } else if (data[i]==0x28) {
                data[i]='(';
            } else if (data[i]==0x29) {
                data[i]=')';
            } else if (data[i]==0x2A) {
                data[i]='*';
            } else if (data[i]==0x2B) {
                data[i]='+';
            } else if (data[i]==0x2C) {
                data[i]=',';
            } else if (data[i]==0x2D) {
                data[i]='-';
            } else if (data[i]==0x2E) {
                data[i]='.';
            } else if (data[i]==0x2F) {
                data[i]='/';
            } else if (data[i]==0x30) {
                data[i]='0';
            } else if (data[i]==0x31) {
                data[i]='1';
            } else if (data[i]==0x32) {
                data[i]='2';
            } else if (data[i]==0x33) {
                data[i]='3';
            } else if (data[i]==0x34) {
                data[i]='4';
            } else if (data[i]==0x35) {
                data[i]='5';
            } else if (data[i]==0x36) {
                data[i]='6';
            } else if (data[i]==0x37) {
                data[i]='7';
            } else if (data[i]==0x38) {
                data[i]='8';
            } else if (data[i]==0x39) {
                data[i]='9';
            } else if (data[i]==0x3A) {
                data[i]=':';
            } else if (data[i]==0x3B) {
                data[i]=';';
            } else if (data[i]==0x3C) {
                data[i]='<';
            } else if (data[i]==0x3D) {
                data[i]='=';
            } else if (data[i]==0x3E) {
                data[i]='>';
            } else if (data[i]==0x3F) {
                data[i]='?';
            } else if (data[i]==0x40) {
                data[i]='¡';
            } else if (data[i]==0x41) {
                data[i]='A';
            } else if (data[i]==0x42) {
                data[i]='B';
            } else if (data[i]==0x43) {
                data[i]='C';
            } else if (data[i]==0x44) {
                data[i]='D';
            } else if (data[i]==0x45) {
                data[i]='E';
            } else if (data[i]==0x46) {
                data[i]='F';
            } else if (data[i]==0x47) {
                data[i]='G';
            } else if (data[i]==0x48) {
                data[i]='H';
            } else if (data[i]==0x49) {
                data[i]='I';
            } else if (data[i]==0x4A) {
                data[i]='J';
            } else if (data[i]==0x4B) {
                data[i]='K';
            } else if (data[i]==0x4C) {
                data[i]='L';
            } else if (data[i]==0x4D) {
                data[i]='M';
            } else if (data[i]==0x4E) {
                data[i]='N';
            } else if (data[i]==0x4F) {
                data[i]='O';
            } else if (data[i]==0x50) {
                data[i]='P';
            } else if (data[i]==0x51) {
                data[i]='Q';
            } else if (data[i]==0x52) {
                data[i]='R';
            } else if (data[i]==0x53) {
                data[i]='S';
            } else if (data[i]==0x54) {
                data[i]='T';
            } else if (data[i]==0x55) {
                data[i]='U';
            } else if (data[i]==0x56) {
                data[i]='V';
            } else if (data[i]==0x57) {
                data[i]='W';
            } else if (data[i]==0x58) {
                data[i]='X';
            } else if (data[i]==0x59) {
                data[i]='Y';
            } else if (data[i]==0x5A) {
                data[i]='Z';
            } else if (data[i]==0x5B) {
                data[i]='Ä';
            } else if (data[i]==0x5C) {
                data[i]='Ö';
            } else if (data[i]==0x5D) {
                data[i]='Ñ';
            } else if (data[i]==0x5E) {
                data[i]='Ü';
            } else if (data[i]==0x5F) {
                data[i]='_';
            } else if (data[i]==0x60) {

                // data[i]='';//?
            } else if (data[i]==0x61) {
                data[i]='a';
            } else if (data[i]==0x62) {
                data[i]='b';
            } else if (data[i]==0x63) {
                data[i]='c';
            } else if (data[i]==0x64) {
                data[i]='d';
            } else if (data[i]==0x65) {
                data[i]='e';
            } else if (data[i]==0x66) {
                data[i]='f';
            } else if (data[i]==0x67) {
                data[i]='g';
            } else if (data[i]==0x68) {
                data[i]='h';
            } else if (data[i]==0x69) {
                data[i]='i';
            } else if (data[i]==0x6A) {
                data[i]='j';
            } else if (data[i]==0x6B) {
                data[i]='k';
            } else if (data[i]==0x6C) {
                data[i]='l';
            } else if (data[i]==0x6D) {
                data[i]='m';
            } else if (data[i]==0x6E) {
                data[i]='n';
            } else if (data[i]==0x6F) {
                data[i]='o';
            } else if (data[i]==0x70) {
                data[i]='p';
            } else if (data[i]==0x71) {
                data[i]='q';
            } else if (data[i]==0x72) {
                data[i]='r';
            } else if (data[i]==0x73) {
                data[i]='s';
            } else if (data[i]==0x74) {
                data[i]='t';
            } else if (data[i]==0x75) {
                data[i]='u';
            } else if (data[i]==0x76) {
                data[i]='v';
            } else if (data[i]==0x77) {
                data[i]='w';
            } else if (data[i]==0x78) {
                data[i]='x';
            } else if (data[i]==0x79) {
                data[i]='y';
            } else if (data[i]==0x7A) {
                data[i]='z';
            } else if (data[i]==0x7B) {
                data[i]='ä';
            } else if (data[i]==0x7C) {
                data[i]='ö';
            } else if (data[i]==0x7D) {
                data[i]='ñ';
            } else if (data[i]==0x7E) {
                data[i]='ü';
            } else if (data[i]==0xA3) {
                data[i]='£';
            } else if (data[i]==0xA4) {
                data[i]='¤';
            } else if (data[i]==0xA5) {
                data[i]='¥';
            } else if (data[i]==0xA7) {
                data[i]='§';
            } else if (data[i]==0x9B) {
                data[i]='›';
            } else if (data[i]==0x86) {
                data[i]='†';
            } else if (data[i]==0x97) {
                data[i]='—';
            } else if (data[i]==0x95) {
                data[i]='•';
            } else if (data[i]==0x91) {
                data[i]='‘';
            } else if (data[i]==0x94) {
                data[i]='”';
            } else if (data[i]==0xA1) {
                data[i]='΅';
            } else if (data[i]=='?') {}
            else if (data[i]=='¨') {}
            else if (data[i]=='¦') {}
            else if (data[i]=='±') {}
            else if (data[i]=='½') {}
            else if (data[i]=='»') {}
            else if (data[i]=='·') {}
            else if (data[i]=='¶') {}
            else if (data[i]=='©') {}
            else if (data[i]=='?') {}
            else if (data[i]=='µ') {}
            else if (data[i]=='¬') {}
            else if (data[i]==0x8C) {}
            else if (data[i]==0x9F) {}
            else if (data[i]==0x9A) {}
            else if (data[i]==0x8f) {}
            else {}
        }

//      byte[] d=new byte[data.length];
//         for (int i=0; i<data.length; i++) {
//             d[i]=(byte) data[i];
//             }

        return new String(data);
    }

    /**
     * Method description
     *
     *
     * @param data
     *
     * @return
     */
    public static String UCS2toISO(char[] data) {
        if (data!=null){
        byte[] d=new byte[data.length];

        for (int i=0; i<data.length; i++) {
            d[i]=(byte) data[i];
        }

        String text=null;

        try {
            text=new String(d, "UNICODE");
            return text;
        } catch (Exception error) {
            error.printStackTrace();
            return "ERROR";
        }}
            return "ERROR";
    }

    /**
     * Method description
     *
     *
     * @param c
     *
     * @return
     */
    private static String Gsm2Iso(char c) {
        char[] ch=new char[1];

        ch[0]=c;

        return Gsm2Iso.Gsm2Iso(ch);
    }
}
