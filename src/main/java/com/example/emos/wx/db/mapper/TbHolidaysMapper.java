package com.example.emos.wx.db.mapper;

import com.example.emos.wx.db.pojo.TbHolidays;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity com.example.emos.wx.db.pojo.TbHolidays
 */
public interface TbHolidaysMapper extends BaseMapper<TbHolidays> {

    public Integer searchTodayIsHolidays();
    
}




