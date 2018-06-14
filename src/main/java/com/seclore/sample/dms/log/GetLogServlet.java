package com.seclore.sample.dms.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seclore.sample.dms.util.Global;

/**
 * Servlet implementation class GetLogServlet
 */
@WebServlet(name = "getLogServlet", urlPatterns = { "/getlog.do" }) public class GetLogServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetLogServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        InputStream fileInputStream = null;
        String logType = request.getParameter("type");
        String logFileName = "SecloreSampleApp.log";
        if ("1".equals(logType)) {
            logFileName = "WSClient.log";
        } else if ("2".equals(logType)) {
            logFileName = "SecloreSampleApp.log";
        }

        try {
            String logFilePath = Global.getAppRealPath() + File.separator + "logs" + File.separator + logFileName;
            // Get file data as ByteStream
            fileInputStream = new FileInputStream(logFilePath);
            response.setContentType("text/x-log");
            response.setContentLength(fileInputStream.available());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + logFileName + "\"");

            ServletOutputStream servletOutputStream = response.getOutputStream();
            byte[] bufferData = new byte[1024];
            int lRead = 0;

            while ((lRead = fileInputStream.read(bufferData)) != -1) {
                servletOutputStream.write(bufferData, 0, lRead);
            }

            try {
                servletOutputStream.flush();
            } catch (IOException ioExp) {
            }

        } catch (Exception lEx) {

        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                    fileInputStream = null;
                } catch (IOException ioExp) {
                    // Ignore
                }
            }
        }
    }

}
