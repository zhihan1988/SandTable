package com.rathink.ie.user.service;


/**
 * Created with IntelliJ IDEA.
 * User: ming
 * Date: 12-12-11
 * Time: 下午12:19
 * To change this template use File | Settings | File Templates.
 */
public interface SmsCheckManager {

     String createCheckCode();

  //  public Boolean validate(String phone, String code);

     String send(String phone, String content, String tpl_id, Integer company);

//    public void send(String phone, String content, String branchName,String tpl_id) throws Exception;

//     Boolean checkPhoneRegistered(String phone);

}
