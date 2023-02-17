package com.example.emos.wx.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emos.wx.db.pojo.SysConfig;
import com.example.emos.wx.db.service.SysConfigService;
import com.example.emos.wx.db.mapper.SysConfigMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig>
    implements SysConfigService{
    @Resource
    SysConfigMapper sysConfigMapper;
    public  SysConfig sysconfig(){
        return sysConfigMapper.selectById("1");
    }
}




