package com.awarepoint.androidaccuracytest.LocationEngine.Domain;

import android.os.Environment;
import android.util.Log;

import org.apache.log4j.Level;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by ureyes on 2/16/2016.
 */
public class Log4jHelper {
    static File storagePublicDirectory = new File(Environment.getExternalStorageDirectory(), "Log");
    static File fileLog = new File(storagePublicDirectory + File.separator + "AwpLog4j.log");


    private final static LogConfigurator mLogConfigrator = new LogConfigurator();

    static {
        configureLog4j();
    }

    private static void configureLog4j() {
        createFile();

        String fileName = fileLog.getPath();
        String filePattern = "%d - [%c] - %p : %m%n";
        int maxBackupSize = 10;
        long maxFileSize = 1024 * 1024 * 5;

        configure(fileName, filePattern, maxBackupSize, maxFileSize);
    }

    public static void createFile() {
        try {
            if (!storagePublicDirectory.exists()) {
                storagePublicDirectory.mkdirs();
            }

            if (!fileLog.exists()) {
                boolean created = fileLog.createNewFile();
            }

        } catch (IOException ioerr) {
            Log.e("IO createFile", ioerr.getMessage());
        } catch (Exception err) {
            Log.e("createFile", err.getMessage());
        }
    }


    public static StringBuilder openFile() {
        StringBuilder logContent = null;
        try {
            if (fileLog.exists()) {
                //Read text from file
                logContent = new StringBuilder();

                BufferedReader br = new BufferedReader(new FileReader(fileLog));
                String line;

                while ((line = br.readLine()) != null) {
                    logContent.append(line);
                    logContent.append("\n\r");
                }
                br.close();

            }
        } catch (IOException ioerr) {
            Log.e("Error IO openFile", ioerr.getMessage());
        } catch (Exception err) {
            Log.e("Error openFile", err.getMessage());
        }

        return logContent;
    }

    private static void configure(String fileName, String filePattern, int maxBackupSize, long maxFileSize) {
        try {
            mLogConfigrator.setFileName(fileName);
            mLogConfigrator.setRootLevel(Level.ALL);
            mLogConfigrator.setUseFileAppender(true);
            mLogConfigrator.setImmediateFlush(true);
            mLogConfigrator.setMaxFileSize(maxFileSize);
            mLogConfigrator.setFilePattern(filePattern);
            mLogConfigrator.setMaxBackupSize(maxBackupSize);
            mLogConfigrator.setUseLogCatAppender(true);
            mLogConfigrator.configure();

        } catch (Exception err) {
            Log.e("configure", err.getMessage());
        }

    }

    public static org.apache.log4j.Logger getLogger(String name) {
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(name);
        return logger;
    }
}
