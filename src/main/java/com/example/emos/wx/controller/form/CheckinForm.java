package com.example.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author: renke
 * @description: TODO
 * @date: 2023/2/18 16:48
 * @version: 1.0
 */
@Data
@ApiModel
public class CheckinForm {
    private String address;
    private String country;
    private String province;
    private String city;
    private String district;
}
