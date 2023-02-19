package com.example.emos.wx.db.service;

import com.example.emos.wx.controller.form.CheckinForm;
import com.example.emos.wx.db.pojo.TbCheckin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;

/**
 *
 */
public interface TbCheckinService  {

    public void checkin(CheckinForm form, int userId, String path);

}
