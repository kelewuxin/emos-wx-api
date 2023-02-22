package com.example.emos.wx.db.mapper;

import com.example.emos.wx.db.pojo.TbWorkday;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Entity com.example.emos.wx.db.pojo.TbWorkday
 */
public interface TbWorkdayMapper extends BaseMapper<TbWorkday> {

    public Integer searchTodayIsWorkday();

    public ArrayList<String> searchWorkdayInRange(HashMap param);


}




