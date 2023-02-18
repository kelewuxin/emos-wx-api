package com.example.emos.wx.db.mapper;

import com.example.emos.wx.db.pojo.TbCheckin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

/**
 * @Entity com.example.emos.wx.db.pojo.TbCheckin
 */

public interface TbCheckinMapper extends BaseMapper<TbCheckin> {

    public Integer haveCheckin(HashMap param);

}




