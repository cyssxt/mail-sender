package com.cyssxt.bean;

import lombok.Data;

@Data
public class SenderReq {
    String subject;
    String content;
    String filePath;
    MailUserInfo to;
    MailUserInfo from;

    public static void main(String[] args) {

    }
}
