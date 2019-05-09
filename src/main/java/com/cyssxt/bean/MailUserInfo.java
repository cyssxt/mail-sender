package com.cyssxt.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailUserInfo {
    String address;
    String name;
    String password;

    public MailUserInfo(String address, String name) {
        this.address = address;
        this.name = name;
    }
}
