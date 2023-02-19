package com.example.emos.wx.db.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emos.wx.config.SystemConstants;
import com.example.emos.wx.controller.form.CheckinForm;
import com.example.emos.wx.db.mapper.TbFaceModelMapper;
import com.example.emos.wx.db.pojo.TbCheckin;
import com.example.emos.wx.db.service.TbCheckinService;
import com.example.emos.wx.db.mapper.TbCheckinMapper;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.service.CheckinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 *
 */
@Service
@Scope("prototype")
@Slf4j
public class TbCheckinServiceImpl implements TbCheckinService {
    @Resource
    TbFaceModelMapper tbFaceModelMapper;
    @Resource
    TbCheckinMapper tbCheckinMapper;

    @Value("${emos.face.checkinUrl}")
    private String checkinUrl;
    @Autowired
    private SystemConstants constants;

    @Override
    public void checkin(CheckinForm form, int userId, String path) {
        //判断签到
        Date d1 = DateUtil.date();  //当前时间
        Date d2 = DateUtil.parse(DateUtil.today() + " " + constants.attendanceTime);  //上班时间
        Date d3 = DateUtil.parse(DateUtil.today() + " " + constants.attendanceEndTime); //签到结束时间
        int status = 1;
        if (d1.compareTo(d2) <= 0) {
            status = 1; // 正常签到
        } else if (d1.compareTo(d2) > 0 && d1.compareTo(d3) < 0) {
            status = 2; //迟到
        }
        //查询签到人的人脸模型数据
        String faceModel=tbFaceModelMapper.searchFaceModel(userId);
        if (faceModel == null) {
            throw new EmosException("不存在人脸模型");
        } else {
            HttpRequest request = HttpUtil.createPost(checkinUrl);
            request.form("photo", FileUtil.file(path), "targetModel", faceModel);
            HttpResponse response = request.execute();
            if (response.getStatus() != 200) {
                log.error("人脸识别服务异常");
                throw new EmosException("人脸识别服务异常");
            }
            String body = response.body();
            if ("无法识别出人脸".equals(body) || "照片中存在多张人脸".equals(body)) {
                throw new EmosException(body);
            } else if ("False".equals(body)) {
                throw new EmosException("签到无效，非本人签到");
            } else if ("True".equals(body)) {
                //TODO 这里要获取签到地区新冠疫情风险等级
                //TODO 保存签到记录
                int risk = 1;
                //查询城市简称
                if (form.getCity() != null && form.getCity().length() > 0 && form.getDistrict() != null && form.getDistrict().length() > 0) {

                    //保存签到记录
                    TbCheckin entity=new TbCheckin();
                    entity.setUserId(userId);
                    entity.setAddress("");
                    entity.setCountry("");
                    entity.setProvince("");
                    entity.setCity("");
                    entity.setDistrict("");
                    entity.setStatus((byte) status);
                    entity.setRisk(risk);
                    entity.setDate(DateUtil.today());
                    entity.setCreateTime(d1);
                    tbCheckinMapper.insert(entity);
                }
            }
        }
    }
}





