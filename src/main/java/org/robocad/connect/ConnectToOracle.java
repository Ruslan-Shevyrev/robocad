/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.robocad.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.robocad.login.OracleLogin.oraclePassword;
import static org.robocad.login.OracleLogin.oracleURL;
import static org.robocad.login.OracleLogin.oracleUser;
import org.robocad.settings.setting;

/**
 *
 * @author shrv
 */
public class ConnectToOracle {

    public static Connection getConnection() {
        Connection con = null;
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            con = DriverManager.getConnection(oracleURL, oracleUser, oraclePassword);
            return con;
        } catch (SQLException ex) {
            if (setting.isLogging) {
                Logger.getLogger(ConnectToOracle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return con;
    }
}
