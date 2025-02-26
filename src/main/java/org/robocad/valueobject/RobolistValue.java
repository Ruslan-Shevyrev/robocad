/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.robocad.valueobject;

/**
 *
 * @author shrv
 */
public class RobolistValue {
    private Long nRn;
    private Long nCompany;
    private Long nSend_wait;
    private Long nMsg_status;
    private String sErr_msg;
    private String sCadnumb;
    private String sRegion_code;
    private String sTK_Region;

    public String getsTK_Region() {
        return sTK_Region;
    }

    public void setsTK_Region(String sTK_Region) {
        this.sTK_Region = sTK_Region;
    }

    public Long getnRn() {
        return nRn;
    }

    public void setnRn(Long nRn) {
        this.nRn = nRn;
    }

    public Long getnCompany() {
        return nCompany;
    }

    public void setnCompany(Long nCompany) {
        this.nCompany = nCompany;
    }

    public Long getnSend_wait() {
        return nSend_wait;
    }

    public void setnSend_wait(Long nSend_wait) {
        this.nSend_wait = nSend_wait;
    }

    public Long getnMsg_status() {
        return nMsg_status;
    }

    public void setnMsg_status(Long nMsg_status) {
        this.nMsg_status = nMsg_status;
    }

    public String getsErr_msg() {
        return sErr_msg;
    }

    public void setsErr_msg(String sErr_msg) {
        this.sErr_msg = sErr_msg;
    }

    public String getsCadnumb() {
        return sCadnumb;
    }

    public void setsCadnumb(String sCadnumb) {
        this.sCadnumb = sCadnumb;
    }

    public String getsRegion_code() {
        return sRegion_code;
    }

    public void setsRegion_code(String sRegion_code) {
        this.sRegion_code = sRegion_code;
    }


}
