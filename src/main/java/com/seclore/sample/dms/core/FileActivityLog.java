package com.seclore.sample.dms.core;

import java.sql.Date;
import java.sql.Timestamp;

public class FileActivityLog {

    private static final long serialVersionUID = 1L;

    private long ActivityId;
    private String User;
    private int Activity;
    private String Timestamp;

    /**
     * @return the timestamp
     */
    public String getTimestamp() {
        return Timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    private int Mode;
    private int Authorized;
    private String Client;
    private String CurrentFileName;

    /**
     * @return the currentFileName
     */
    public String getCurrentFileName() {
        return CurrentFileName;
    }

    /**
     * @param currentFileName the currentFileName to set
     */
    public void setCurrentFileName(String currentFileName) {
        CurrentFileName = currentFileName;
    }

    /**
     * @return the client
     */
    public String getClient() {
        return Client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(String client) {
        Client = client;
    }

    /**
     * @return the activityId
     */
    public long getActivityId() {
        return ActivityId;
    }

    /**
     * @param activityId the activityId to set
     */
    public void setActivityId(long activityId) {
        ActivityId = activityId;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return User;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        User = user;
    }

    /**
     * @return the activity
     */
    public int getActivity() {
        return Activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(int activity) {
        Activity = activity;
    }

    /**
     * @return the mode
     */
    public int getMode() {
        return Mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(int mode) {
        Mode = mode;
    }

    /**
     * @return the authorized
     */
    public int getAuthorized() {
        return Authorized;
    }

    /**
     * @param authorized the authorized to set
     */
    public void setAuthorized(int authorized) {
        Authorized = authorized;
    }

}
