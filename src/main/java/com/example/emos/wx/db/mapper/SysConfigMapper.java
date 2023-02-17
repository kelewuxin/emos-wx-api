package com.example.emos.wx.db.mapper;

import com.example.emos.wx.db.pojo.SysConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Entity com.example.emos.wx.db.pojo.SysConfig
 */

public interface SysConfigMapper extends BaseMapper<SysConfig> {
    public List<SysConfig> selectAllParam();
}




