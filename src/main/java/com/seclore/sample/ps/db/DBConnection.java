package com.seclore.sample.ps.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.seclore.sample.dms.core.FileActivityLog;
import com.seclore.sample.dms.util.LoggerUtil;

public class DBConnection {

    private static final String mstrDATASOURCE_CONTEXT_NAME = "java:comp/env/jdbc/filesecure";

    private static Connection getDBConnection() throws NamingException {
        String mstrDataSourceContext = mstrDATASOURCE_CONTEXT_NAME;

        Connection conn = null;
        try {
            Context initialContext = new InitialContext();
            DataSource datasource = (DataSource) initialContext.lookup(mstrDataSourceContext);
            if (datasource != null) {
                conn = datasource.getConnection();
            } else {
                //log
            }
        } catch (NamingException ex) {
            throw ex;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }

        return conn;
    }

    public static List<FileActivityLog> getFileLogsByFileId(String pFileId) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<FileActivityLog> lliFileActivityLog = null;
        try {
            conn = getDBConnection();
            DatabaseMetaData lMetaData = conn.getMetaData();
            String lstrDBUrl = lMetaData.getURL();

            String mstrQuery;

            if (lstrDBUrl.contains("jdbc:sqlserver:")) {
                //For MSSQL
                mstrQuery = "SELECT TOP 20 FILE_EXT_REF_ID, ID, USER_QID, ACTIVITY_DATE, ACTIVITY, AUTHORIZED, ONLINE_MODE, CLID_DESCRIPTION, CURRENT_FILE_NAME FROM EXTFILEUSERACTIVITYVIEW WHERE FILE_EXT_REF_ID =? ORDER BY ID DESC";
            } else {
                //For ORACLE
                mstrQuery = "SELECT * FROM ( SELECT FILE_EXT_REF_ID, ID, USER_QID, ACTIVITY_DATE, ACTIVITY, AUTHORIZED, ONLINE_MODE, CLID_DESCRIPTION, CURRENT_FILE_NAME FROM EXTFILEUSERACTIVITYVIEW WHERE FILE_EXT_REF_ID =? ORDER BY ID DESC) WHERE rownum <= 20";
            }
            //String mstrQuery = "SELECT FILE_EXT_REF_ID, ID, USER_QID, ACTIVITY_DATE, ACTIVITY, AUTHORIZED, ONLINE_MODE, CLID_DESCRIPTION, CURRENT_FILE_NAME FROM EXTFILEUSERACTIVITYVIEW WHERE FILE_EXT_REF_ID =? AND ROWNUM <= 20 ORDER BY ID DESC";
            st = conn.prepareStatement(mstrQuery);

            st.setString(1, pFileId);

            rs = st.executeQuery();

            lliFileActivityLog = new ArrayList<FileActivityLog>();
            FileActivityLog mFileActivityLog;
            while (rs.next()) {
                mFileActivityLog = new FileActivityLog();

                long activityId = rs.getLong("ID");
                mFileActivityLog.setActivityId(activityId);

                String userQid = rs.getString("USER_QID");
                userQid = getUserNameByUserQid(userQid);
                mFileActivityLog.setUser(userQid);

                Timestamp timestamp = rs.getTimestamp("ACTIVITY_DATE");
                String accessTime = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss").format(timestamp);
                mFileActivityLog.setTimestamp(accessTime);

                int activity = rs.getInt("ACTIVITY");
                mFileActivityLog.setActivity(activity);

                int authorized = rs.getInt("AUTHORIZED");
                mFileActivityLog.setAuthorized(authorized);

                int mode = rs.getInt("ONLINE_MODE");
                mFileActivityLog.setMode(mode);

                String client = rs.getString("CLID_DESCRIPTION");
                mFileActivityLog.setClient(client);

                String currentFileName = rs.getString("CURRENT_FILE_NAME");
                mFileActivityLog.setCurrentFileName(currentFileName);

                lliFileActivityLog.add(mFileActivityLog);
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error while fetching File Logs", e);
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            LoggerUtil.logError("Error while loading db resource", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lliFileActivityLog;

    }

    public static String getUserNameByUserQid(String pstrUserQid) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String lstrUserName = null;
        try {
            conn = getDBConnection();

            String mstrQuery = "SELECT NAME, OBJECT_QID FROM PSREPOSITORYOBJECTS  WHERE OBJECT_QID = ?";

            st = conn.prepareStatement(mstrQuery);

            st.setString(1, pstrUserQid);

            rs = st.executeQuery();

            if (rs.next()) {
                lstrUserName = rs.getString(1);
            }
        } catch (SQLException e) {
            LoggerUtil.logError("Error while fetching User's Name", e);
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            LoggerUtil.logError("Error while loading db resource", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lstrUserName;
    }
}
