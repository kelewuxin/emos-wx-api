package com.example.emos.wx.config;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author: renke
 * @description: TODO
 * @date: 2023/2/16 21:02
 * @version: 1.0
 */
@Data
@Component
public class SystemConstants {
    public String attendanceStartTime;
    public String attendanceTime;
    public String attendanceEndTime;
    public String closingStartTime;
    public String closingTime;
    public String closingEndTime;
}

