/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.robocad;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.robocad.login.OracleLogin;
import org.robocad.login.TechnocadLogin;
import org.robocad.robot.StartRobot;
import org.robocad.settings.setting;

/**
 *
 * @author shrv
 */
public class RoboMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, InterruptedException, IOException, URISyntaxException {
        int RepeatCount = 0;
        int RepeatToResponce;
        long startTime;
        long timeSpent;
        String myJarPath = RoboMain.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        setting.pathToJar = new File(myJarPath).getParent();
        //System.out.println(setting.pathToJar);
        if (args.length != 0) {
            TechnocadLogin.technocadUser = args[0];
            TechnocadLogin.technocadPassword = args[1];
            OracleLogin.oracleURL = args[2];
            OracleLogin.oracleUser = args[3];
            OracleLogin.oraclePassword = args[4];
            setting.pathToFile = args[5];
            setting.timeToRequest = Integer.parseInt(args[6]);
            setting.timeToResponce = Integer.parseInt(args[7]);
            setting.isLogging = Boolean.valueOf(args[8]);
        }
        StartRobot startRobot = new StartRobot();
        FirefoxDriver driver = startRobot.start();
        RepeatToResponce = setting.timeToResponce / setting.timeToRequest;
        while (true) {
            if (RepeatCount == RepeatToResponce) {
                RepeatCount=0;
                startRobot.findResponce(driver);
            }
            startTime = System.currentTimeMillis();
            startRobot.sendRequest(driver);
            timeSpent = System.currentTimeMillis() - startTime;
            RepeatCount++;
            if (timeSpent < setting.timeToRequest) {
                Thread.sleep(setting.timeToRequest - timeSpent);
                System.out.println(timeSpent);
            }
        }

//        Thread.sleep(30000);
///////////////////////////////////////////////////////////////////
//Не забыть удалить логин а пароль из setting.xml от Oracle////////
///////////////////////////////////////////////////////////////////
    }

}
