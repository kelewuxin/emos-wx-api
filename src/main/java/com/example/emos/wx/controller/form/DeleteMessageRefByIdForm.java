package com.example.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: renke
 * @description: TODO
 * @date: 2023/2/26 15:20
 * @version: 1.0
 */
@Data
@ApiModel
public class DeleteMessageRefByIdForm {
    @NotBlank
    private String id;
}