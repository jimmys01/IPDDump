package ipddump.data.Records;

/**
 *
 * @author Jimmys Daskalakis - jimdaskalakis01@gmail.com
 * @created Jun 20, 2009
 */
public class Task extends Record implements Comparable<Task> {
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
  public Task(int dbID, int dbVersion, int uid, int recordLength) {
    super(dbID, dbVersion, uid, recordLength);
  }

  
  public void addField(int type, char[] data) {
    switch (type) {
    case 1:
      // ignore, always 't'
      break;

    case 2:
      fields.put("Name", makeStringCropLast(data));
      break;

    case 3:
      fields.put("Notes", makeStringCropLast(data));
      break;

    case 5:
    case 6:
      fields.put("Due", makeDate(data).toString());
      break;

    case 8:
      boolean due = data[0] > 0;
      fields.put("Due", due ? "true" : "None");
      break;

    case 10:
      // ignore, 0 for first, 1 for the rest
      break;

    // Status
    case 9:
      int status =makeInt(data);
      switch (status) {
      case 0:
        fields.put("Status", "Not Started");
        break;

      case 1:
        fields.put("Status", "In Progress");
        break;

      case 2:
        fields.put("Status", "Completed");
        break;

      case 3:
        fields.put("Status", "Waiting");
        break;

      case 4:
        fields.put("Status", "Deferred");
        break;
      }
      break;

    // Recur
    case 12:
      // ignore for now
      break;

    // Priority
    case 14:
      int priority = makeInt(data);
      switch (priority) {
      case 0:
        fields.put("Priority", "Low");
        break;

      case 1:
        fields.put("Priority", "Normal");
        break;

      case 2:
        fields.put("Priority", "High");
        break;
      }
      break;

    case 15:
      fields.put("Reminder", makeDate(data).toString());
      break;

    // Timezone
    case 16:
      int timezone = makeInt(data);
        fields.put("TimeZone", String.valueOf(timezone));
      switch (timezone) {}
      
      break;

    case 17:
      String existing = fields.get("Categories");
      if (existing == null) {
        existing = "";
      } else {
        // Microsoft Outlook Style. If sepated by commas then the CSV brakes.
        existing += ";";
      }

      fields.put("Categories", existing + makeStringCropLast(data));
      break;

    case 18:
      // unknown
      break;

    case 31:
      // unknown
      break;
    }
  }

  @Override
  public int compareTo(Task o) {
    return getTask().compareTo(o.getTask());
  }

  @Override
  public String toString() {
    return fields.toString();
  }

 

  public String getTask() {
    return getField("Name");
  }

  public String getCategories() {
    return getField("Categories");
  }

  public String getDue() {
    return getField("Due");
  }

  public String getPriority() {
    if (getField("Priority").equals(""))
      return "Normal";
    return getField("Priority");
  }

  public String getNotes() {
    return getField("Notes");
  }

  public String getStatus() {
    return getField("Status");
  }

  public String getReminder() {
    if (getField("Reminder").equals(""))
      return "None";
    return getField("Reminder");
  }

  public String getTimeZone() {
    return getField("TimeZone");
  }
  
  public void setTimeZoneName(String name){
    fields.put("TimeZone", name);
  }
}