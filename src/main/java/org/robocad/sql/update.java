/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.robocad.sql;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.robocad.connect.ConnectToOracle;

/**
 *
 * @author shrv
 */
public class update {
    private final String SQLUPDATE = "begin parus.procedure_insert_tech(?, ?, ?, ?); end;";
    private final String SQLANSWER = "begin parus.procedure_answer_tech(?, ?, ?, ?); end;";
   // private final String SQLUPDATE = "update parus.test_technocad set nSend_wait = ?, nmsg_status = ?, sErr_msg = ? where nRn = ?";
   // private final String SQLANSWER = "update parus.test_technocad set nmsg_status = 11, answer_blob= ? where nRn = ?";

    public void updateSql(long nSend_wait, long nMsg_status, String sErr_msg, long nRn) {
        try (Connection con = ConnectToOracle.getConnection()) {
            PreparedStatement ps = con.prepareStatement(SQLUPDATE);
            ps.setLong(1, nSend_wait);
            ps.setLong(2, nMsg_status);
            ps.setString(3, sErr_msg);
            ps.setLong(4, nRn);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(update.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean SendBlob(InputStream in, long nRn, String fileName, long nCompany) {
        boolean flag = false;
        try (Connection con = ConnectToOracle.getConnection()) {
            PreparedStatement ps = con.prepareStatement(SQLANSWER);
            ps.setBinaryStream(1, in);
            ps.setLong(2, nRn);
            ps.setString(3, fileName);
            ps.setLong(4, nCompany);
            ps.execute();
            flag = true;
        } catch (SQLException ex) {
            Logger.getLogger(update.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }
}
