package com.example.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author: renke
 * @description: TODO
 * @date: 2023/2/23 20:23
 * @version: 1.0
 */
@Data
@ApiModel
public class SearchMonthCheckinForm {
    @NotNull
    @Range(min = 2000, max = 3000)
    private Integer year;

    @NotNull
    @Range(min = 1, max = 12)
    private Integer month;
}

