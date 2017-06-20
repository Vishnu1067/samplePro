package com.mobile.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Command Prompt - this class contains method to run windows and mac commands
 */
public class CommandPrompt {

    private Process process;
    private BufferedReader bufferedReader;

    String line;
    String allLine = "";
    int i = 1;

    public String runCommand(String command) {

        try {
            process = Runtime.getRuntime().exec(command);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while ((line = bufferedReader.readLine()) != null) {

                allLine = allLine + "" + line + "\n";

                if (line.contains("Console LogLevel: debug") && line.contains("Complete")) {
                    break;
                }
                i++;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return allLine;

    }

    public String runCommandThruProcessBuilder(String command) {

        try {

            bufferedReader = getBufferedReader(command);

            String ff = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {

                allLine = allLine + "" + line + "\n";
                System.out.println(allLine);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return allLine.split(":")[1].replace("\n", "").trim();
    }

    public String runProcessCommandToGetDeviceID(String command) {

        try {

            bufferedReader = getBufferedReader(command);
            while ((line = bufferedReader.readLine()) != null) {
                allLine = allLine.trim() + "" + line.trim() + "\n";
                System.out.println(allLine);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return allLine.trim();
    }

    public void runCommandThruProcess(String command) {

        try {

            bufferedReader = getBufferedReader(command);

            while ((line = bufferedReader.readLine()) != null) {

                allLine = allLine + "" + line + "\n";
                System.out.println(allLine);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private BufferedReader getBufferedReader(String command) {

        InputStream is;
        InputStreamReader isr = null;

        try {

            List<String> commands = new ArrayList<>();
            commands.add("/bin/sh");
            commands.add("-c");
            commands.add(command);
            ProcessBuilder builder = new ProcessBuilder(commands);
            Map<String, String> environ = builder.environment();

            final Process process = builder.start();
            is = process.getInputStream();
            isr = new InputStreamReader(is);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return new BufferedReader(isr);
    }

}
