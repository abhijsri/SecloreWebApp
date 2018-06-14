package com.seclore.sample.dms.config;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Node;

import com.seclore.fs.helper.exception.FSHelperException;
import com.seclore.fs.helper.library.FSHelperLibrary;
import com.seclore.sample.dms.util.XMLUtil;

/**
 * A utility class used to initialize and terminate WSClient Library
 *
 * @author harindra.chaudhary
 */
public class SecloreWSClientConfig {
    private static final String FSCONFIG_PATH = "config" + File.separator + "config.xml";

    /**
     * Used to connect at time of File upload using WebConnect Integration
     */
    private static String sstrProxyServerName = null;

    private static Logger logger = Logger.getLogger(SecloreWSClientConfig.class);

    public static void main(String[] args) throws IOException {
        String file = "/Users/abhijsri/tools/tomcat9/" + FSCONFIG_PATH;
        System.out.println("Reading file " + file);
        String content = getConfigFileContent(file);
        System.out.println("Content of File " + file );
    }
    /**
     * Used to connect at time of File upload using WebConnect Integration
     */
    private static int siProxyServerPort = -1;

    public static void initializeWSClient(String appPath) {

        String log4jConfPath = "/Users/abhijsri/tools/tomcat9/config/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        logger.debug("Logs initialized with path " + log4jConfPath);
        String wsClientConfigFilePath = appPath + FSCONFIG_PATH;
        System.out.println("File path is (wsClientConfigFilePath) " + wsClientConfigFilePath);


        String configContentXMLString = null;
        try {

            configContentXMLString = getConfigFileContent(wsClientConfigFilePath);

            // Initialize method takes WSClient config content not the config file path.
            FSHelperLibrary.initialize(configContentXMLString);
            logger.debug("Seclore WSClient Library has been initialized successfully");

            // Check the connection
            // PSConnection lPsConnection = FSHelperLibrary.getPSConnection();
            // logger.debug("PS Session Id "+lPsConnection.getSessionId());

            // Release PSConnection to connection pool
            // FSHelperLibrary.releasePSConnection(lPsConnection);
        } catch (FSHelperException e) {
            logger.error("Seclore WSClient Library has not been Initialized!");
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("Seclore WSClient Library has not been Initialized!");
            e.printStackTrace();
        }

        // -------- Extract proxy-details for WebConnect Integration START --------------- //
        // proxy-details from WSClient config will be used to connect Policy Server.
        // If WebConnect Integration will not be required then no need to do this.
        initializeProxyDetails(configContentXMLString);
        // -------- Extract proxy-details for WebConnect Integration END --------------- //

    }

    private static String getConfigFileContent(String configLocation, boolean var) throws IOException {
        ClassLoader classLoader = SecloreWSClientConfig.class.getClassLoader();
        String path = configLocation + "/config/config.xml";
        logger.debug("Path for config is " + path);
        InputStream inputStream = classLoader.getResourceAsStream(path);
        logger.error(" Input Stream " + inputStream);
        return readFromInputStream(inputStream);
    }

    private static String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
    /**
     * @param pConfigFilePath - Absolute path of the WSClient config file.
     * @return String - WSClient config file content.
     * @throws Exception
     */
    private static String getConfigFileContent(String pConfigFilePath) throws IOException {
        String fileContent = Files.lines(Paths.get(pConfigFilePath),
                Charset.forName("UTF-16")).collect(Collectors.joining(" "));
        System.out.println("File Content " + fileContent);
        return fileContent;

        /*File file = new File(pConfigFilePath);

        //Checking whether the file exist or not
        if (file.exists() == false || file.isDirectory()) {
            throw new Exception("Configuration File does not exist at '" + file.getAbsolutePath() + "'.");
        }

        String lstrReturn = "";
        InputStream lFileIOS = null;
        BufferedReader lReader = null;
        try {
            lFileIOS = new FileInputStream(file);
            lReader = new BufferedReader(new InputStreamReader(lFileIOS, Charset.forName("UTF-16")));

            StringBuilder out = new StringBuilder();
            String line = "";

            while ((line = lReader.readLine()) != null) {
                out.append(line);
            }
            lstrReturn = out.toString();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (lFileIOS != null) {
                lFileIOS.close();
            }

            if (lReader != null) {
                lReader.close();
            }

        }

        return lstrReturn;*/
    }

    private static void initializeProxyDetails(String xmlString) {
        Node rootNode = XMLUtil.getRootNode(xmlString);
        Node proxyDetailsNode = XMLUtil.parseNode("proxy-details", rootNode);
        if (proxyDetailsNode == null) {
            return;
        }

        String lstrProxySerevrName = XMLUtil.parseString("server", proxyDetailsNode);
        if (lstrProxySerevrName == null || lstrProxySerevrName.trim().isEmpty()) {
            return;
        }
        String lstrProxySerevrPort = XMLUtil.parseString("port", proxyDetailsNode);
        if (lstrProxySerevrPort == null || lstrProxySerevrPort.trim().isEmpty()) {
            return;
        }

        try {
            siProxyServerPort = Integer.parseInt(lstrProxySerevrPort);
        } catch (NumberFormatException nfe) {
            return;
        }
        sstrProxyServerName = lstrProxySerevrName;
    }

    public static void terminateWSClient() {
        try {
            FSHelperLibrary.logInfo("Terminating FSHelper Library");
            if (FSHelperLibrary.isTerminated() == false) {
                logger.debug("FSHelperLibrary.isTerminated(): " + FSHelperLibrary.isTerminated());
                FSHelperLibrary.terminate();
            }
        } catch (FSHelperException e) {
            FSHelperLibrary.logError(e.getMessage(), e);
        }
    }

    public static String getProxyServerName() {
        return sstrProxyServerName;
    }

    public static int getProxyServerPort() {
        return siProxyServerPort;
    }

}
