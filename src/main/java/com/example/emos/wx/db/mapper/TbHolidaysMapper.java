package com.example.emos.wx.db.mapper;

import com.example.emos.wx.db.pojo.TbHolidays;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Entity com.example.emos.wx.db.pojo.TbHolidays
 */
public interface TbHolidaysMapper extends BaseMapper<TbHolidays> {

    public Integer searchTodayIsHolidays();

    public ArrayList<String> searchHolidaysInRange(HashMap param);

}




