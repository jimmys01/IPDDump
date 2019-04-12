package ipddump.tools;

//~--- non-JDK imports --------------------------------------------------------

import ipddump.data.InteractivePagerBackup;
import ipddump.data.Records.*;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@gmail.com
 */
public class Finder {
    private final InteractivePagerBackup database;
    private boolean useAdrrBookAll=false;

    //~--- constructors -------------------------------------------------------

    public Finder(InteractivePagerBackup database) {     
        this.database=database;
    }

    //~--- methods ------------------------------------------------------------

    public int[] findCallLogByNumber(String phNumber) {
        int i        =0;
        int callIndex=0;

        for (CallLog recordCallLog : database.getCallLogs()) {
            String CallLognumber=recordCallLog.getNumber();

            if (CallLognumber.replaceAll(" ", "").replaceAll("!",
                                         "").equals(phNumber.replaceAll(" ", "").replaceAll("!", ""))) {
                i++;
            }
        }

        int selectedCalls[]=new int[i];
        int pointer        =0;

        for (CallLog recordCallLog : database.getCallLogs()) {
            String CallNumber=recordCallLog.getNumber();

            if (CallNumber.replaceAll(" ", "").replaceAll("!", "").equals(phNumber.replaceAll(" ", ""))) {
                selectedCalls[pointer++]=callIndex;
            }

            callIndex++;
        }

        return selectedCalls;
    }

    public int[] findCallLogsByContacts(int[] selectedContacts) {
        int RecordIndex=0;
        int j          =0;
        int i          =0;

        // This first loop counts the Call Logs to be found
        for (Contact record : database.contacts()) {
            if ((RecordIndex==selectedContacts[j]) && (selectedContacts[j]<database.contacts().size())) {
                for (CallLog recordCallLog : database.getCallLogs()) {
                    String CallLogNumber=recordCallLog.getNumber().replaceAll(" ", "").replaceAll("!", "");

                    if  (CallLogNumber.equals(record.getMobilePhone().replaceAll(" ", "").replaceAll("!", ""))
                            ||CallLogNumber.equals(record.getMobilePhone2().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getPager().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getHomePhone().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getHomePhone2().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getWorkPhone().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getWorkPhone2().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getOtherNumber().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getWorkFax().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getHomeFax().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getPIN().replaceAll(" ", "").replaceAll("!", ""))) 
                    {
                        i++;
                    }
                }

                j++;
            }

            if (j>=selectedContacts.length) {
                break;
            }

            RecordIndex++;
        }

        int selectedCallLogs[]=new int[i];
        int pointer           =0;

        RecordIndex=0;
        j          =0;

        int index=0;

        for (Contact record : database.contacts()) {
            if ((RecordIndex==selectedContacts[j]) && (selectedContacts[j]<database.contacts().size())) {
                index=0;

                for (CallLog recordCallLog : database.getCallLogs()) {
                    String CallLogNumber=recordCallLog.getNumber().replaceAll(" ", "").replaceAll("!", "");

                    if (CallLogNumber.equals(record.getMobilePhone().replaceAll(" ", "").replaceAll("!", ""))
                            ||CallLogNumber.equals(record.getMobilePhone2().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getPager().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getHomePhone().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getHomePhone2().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getWorkPhone().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getWorkPhone2().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getOtherNumber().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getWorkFax().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getHomeFax().replaceAll(" ", "").replaceAll("!", ""))
                            || CallLogNumber.equals(record.getPIN().replaceAll(" ", "").replaceAll("!", ""))) {
                        selectedCallLogs[pointer++]=index;
                    }

                    index++;
                }

                j++;
            }

            if (j>=selectedContacts.length) {
                break;
            }

            RecordIndex++;
        }

        return selectedCallLogs;
    }

    public void useAdrrBookAll(boolean state){
            this.useAdrrBookAll = state;
    }

    /**
     *  You can find the contact via home
     *  work, mobile, pager or PIN number
     *
     *
     * @param phoneNumber
     *
     * @return The name of the contact if found, otherwise the phone number
     */
    public String findContactByPhoneNumber(String phNumber) {
        String phoneNumber=phNumber.replaceAll(" ", "");

        for (Contact record : database.contacts()) {
            if (useAdrrBookAll==record.isAdrrBookAllDB()){
            String name=record.getName();

            if (phoneNumber.equalsIgnoreCase(record.getMobilePhone().replaceAll(" ", "").replaceAll("!", ""))) {
                return name+" (M)";
            } else if (phoneNumber.equalsIgnoreCase(record.getHomePhone().replaceAll(" ", "").replaceAll("!", ""))) {
                return name+" (H)";
            } else if (phoneNumber.equalsIgnoreCase(record.getWorkPhone().replaceAll(" ", "").replaceAll("!", ""))) {
                return name+" (W)";
            } else if (phoneNumber.equalsIgnoreCase(record.getOtherNumber().replaceAll(" ", "").replaceAll("!", ""))) {
                return name+" (O)";
            } else if (phoneNumber.equalsIgnoreCase(record.getPager().replaceAll(" ", "").replaceAll("!", ""))) {
                return name+" (P)";
            } else if (phoneNumber.equalsIgnoreCase(record.getPIN().replaceAll(" ", "").replaceAll("!", ""))) {
                return name+" (Pin)";
            } else if (phoneNumber.equalsIgnoreCase(record.getWorkFax().replaceAll(" ", "").replaceAll("!", ""))) {
                return name+" (Work Fax)";
            } else if (phoneNumber.equalsIgnoreCase(record.getHomeFax().replaceAll(" ", "").replaceAll("!", ""))) {
                return name+" (HomeFax)";
            } else if (phoneNumber.equalsIgnoreCase(record.getMobilePhone2().replaceAll(" ", "").replaceAll("!", ""))) {
                return name+" (M2)";
            } else if (phoneNumber.equalsIgnoreCase(record.getHomePhone2().replaceAll(" ", "").replaceAll("!", ""))) {
                return name+" (H2)";
            } else if (phoneNumber.equalsIgnoreCase(record.getWorkPhone2().replaceAll(" ", "").replaceAll("!", ""))) {
                return name+" (W2)";
            } else if (phoneNumber==null) {
                return "";
            }
        }
        }

        return phNumber;
    }

    public int[] findSmsByContacts(int[] selectedContacts) {
        int RecordIndex=0;
        int j          =0;
        int i          =0;

        for (Contact record : database.contacts()) {
            if ((RecordIndex==selectedContacts[j]) && (selectedContacts[j]<database.contacts().size())) {
                for (SMSMessage recordsms : database.getSMSRecords()) {
                    String SMSnumber=recordsms.getNumber().replaceAll(" ", "").replaceAll("!", "");

                    if (SMSnumber.equals(record.getMobilePhone().replaceAll(" ", "").replaceAll("!", ""))
                            ||SMSnumber.equals(record.getMobilePhone2().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getPager().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getHomePhone().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getHomePhone2().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getWorkPhone().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getWorkPhone2().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getOtherNumber().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getWorkFax().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getHomeFax().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getPIN().replaceAll(" ", "").replaceAll("!", ""))) {
                        i++;
                    }
                }

                j++;
            }

            if (j>=selectedContacts.length) {
                break;
            }

            RecordIndex++;
        }

        int selectedSMS[]=new int[i];
        int pointer      =0;

        RecordIndex=0;
        j          =0;

        for (Contact record : database.contacts()) {
            if ((RecordIndex==selectedContacts[j]) && (selectedContacts[j]<database.contacts().size())) {
                int smsindex=0;

                for (SMSMessage recordsms : database.getSMSRecords()) {
                    String SMSnumber=recordsms.getNumber().replaceAll(" ", "").replaceAll("!", "");

                    if (SMSnumber.equals(record.getMobilePhone().replaceAll(" ", "").replaceAll("!", ""))
                            ||SMSnumber.equals(record.getMobilePhone2().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getPager().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getHomePhone().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getHomePhone2().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getWorkPhone().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getWorkPhone2().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getOtherNumber().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getWorkFax().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getHomeFax().replaceAll(" ", "").replaceAll("!", ""))
                            || SMSnumber.equals(record.getPIN().replaceAll(" ", "").replaceAll("!", ""))) {
                        selectedSMS[pointer++]=smsindex;
                    }

                    smsindex++;
                }

                j++;
            }

            if (j>=selectedContacts.length) {
                break;
            }

            RecordIndex++;
        }

        return selectedSMS;
    }

    public int[] findSmsByNumber(String phNumber) {
        int i       =0;
        int smsindex=0;

        for (SMSMessage recordsms : database.getSMSRecords()) {
            String SMSnumber=recordsms.getNumber();

            if (SMSnumber.replaceAll(" ", "").replaceAll("!",
                                     "").equals(phNumber.replaceAll(" ", "").replaceAll("!", ""))) {
                i++;
            }
        }

        int selectedSMS[]=new int[i];
        int pointer      =0;

        for (SMSMessage recordsms : database.getSMSRecords()) {
            String SMSnumber=recordsms.getNumber();

            if (SMSnumber.replaceAll(" ", "").replaceAll("!", "").equals(phNumber.replaceAll(" ", ""))) {
                selectedSMS[pointer++]=smsindex;
            }

            smsindex++;
        }

        return selectedSMS;
    }

    public CallLog findSpesificCallLog(int number) {
        if ((number<0) || (number>database.getCallLogs().size())) {
            return null;

//          throw new Exception("Out of Bounds : "+number);
        }

        int i=0;

        for (CallLog calllog : database.getCallLogs()) {
            if (i++==number) {
                return calllog;
            }
        }

        return null;

//      throw new Exception("Out of Bounds");
    }

    public SMSMessage findSpesificSms(int number) {
        if ((number<0) || (number>database.getSMSRecords().size())) {
            return null;
        }

        int i=0;

        for (SMSMessage sms : database.getSMSRecords()) {
            if (i++==number) {
                return sms;
            }
        }

        return null;

//      throw new Exception("Out of Bounds");
    }

    public String findTimeZoneByID(String idNumber) {
        for (BBTimeZone record : database.getTimeZones()) {
            String id=record.getTimeZoneID();

            if (idNumber.equals(id)) {
                return record.getPlaceNameWithOffset();
            }
        }

        return "TimeZ db not present ID:"+idNumber;
    }
}
