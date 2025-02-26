/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.robocad.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.robocad.connect.ConnectToOracle;
import org.robocad.valueobject.RobolistValue;

/**
 *
 * @author shrv
 */
public class select {
    private RobolistValue RoboValue;
    private List <RobolistValue> RoboValues;
    //private final String SQLSELECT ="select * from parus.test_technocad where (nsend_wait = ? and (nmsg_status = ? or nmsg_status = ?))";
    private final String SQLSELECT = "select * from parus.V_FANO_ROBOLIST v1, parus.v_fano_regobjects  v2 where (v1.nsend_wait = ? and (v1.nmsg_status = ? or v1.nmsg_status = ?)AND v1.SCADNUMB = v2.CADNUMB)";


    public List<RobolistValue> getRoboValues() {
        return RoboValues;
    }

    public void setRoboValues(List<RobolistValue> RoboValues) {
        this.RoboValues = RoboValues;
    }
    
    public List<RobolistValue> sqlSelect(long nSend_wait, long nMsg_status1, long nMsg_status2)
    {
        try (Connection con = ConnectToOracle.getConnection()){
            PreparedStatement ps = con.prepareStatement(SQLSELECT);
            ps.setLong(1, nSend_wait);
            ps.setLong(2, nMsg_status1);
            ps.setLong(3, nMsg_status2);
            ResultSet rs = ps.executeQuery();
            RoboValues = new ArrayList<RobolistValue>();
            while (rs.next())
            {
                RoboValue = new RobolistValue();
                RoboValue.setnRn(rs.getLong("nRn"));
                RoboValue.setnCompany(rs.getLong("nCompany"));
                RoboValue.setnSend_wait(rs.getLong("nSend_Wait"));
                RoboValue.setnMsg_status(rs.getLong("nMsg_status"));
                RoboValue.setsErr_msg(rs.getString("sErr_msg"));
                RoboValue.setsCadnumb(rs.getString("sCadnumb"));
                RoboValue.setsRegion_code(rs.getString("sRegion_code"));
                RoboValue.setsTK_Region(rs.getString("tk_region"));
                RoboValues.add(RoboValue);
            };
                    } catch (SQLException ex) {
            Logger.getLogger(select.class.getName()).log(Level.SEVERE, null, ex);
        }
        return RoboValues;
    }
}
