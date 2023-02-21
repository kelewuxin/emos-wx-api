package com.example.emos.wx.db.mapper;

import com.example.emos.wx.db.pojo.TbUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.Set;

/**
 * @Entity com.example.emos.wx.db.pojo.TbUser
 */
public interface TbUserMapper extends BaseMapper<TbUser> {

    public boolean haveRootUser();

    public int insert(HashMap param);

    public Integer searchIdByOpenId(String openId);

    public Set<String> searchUserPermissions(int userId);

    public TbUser searchById(int userId);

    public HashMap searchNameAndDept(int userId);


}




