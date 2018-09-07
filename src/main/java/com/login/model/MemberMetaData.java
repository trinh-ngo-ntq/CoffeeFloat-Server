package com.login.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Description: MemberMetaData.java <br>
 * This class is used to store external member data. 1 member should only have 1 and only 1 this
 * class instance
 * 
 * @author TrinhNX
 * @create Sep 5, 2018
 */
public class MemberMetaData {
    private static Log log = LogFactory.getLog(MemberMetaData.class);

    private int id; // This is as an index
    private int ownerCode;
    private int memberCode;
    private String avatarUrl;
    private int gender;
    private String country;



}
