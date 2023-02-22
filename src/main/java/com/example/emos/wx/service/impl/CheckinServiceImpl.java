package com.example.emos.wx.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.example.emos.wx.config.SystemConstants;
import com.example.emos.wx.controller.form.CheckinForm;
import com.example.emos.wx.db.mapper.*;
import com.example.emos.wx.db.pojo.TbCheckin;
import com.example.emos.wx.db.pojo.TbFaceModel;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.service.CheckinService;
import com.example.emos.wx.task.EmailTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
@Scope("prototype")
@Slf4j
public class CheckinServiceImpl implements CheckinService {
    @Autowired
    private SystemConstants systemConstants;

    @Resource
    TbHolidaysMapper holidaysMapper;

    @Resource
    TbWorkdayMapper workdayMapper;

    @Resource
    TbCheckinMapper checkinMapper;

    @Resource
    TbFaceModelMapper tbFaceModelMapper;
    @Resource
    TbUserMapper tbUserMapper;

    @Resource
    TbFaceModelMapper faceModelMapper;

    @Resource
    EmailTask emailTask;

    @Value("${emos.face.checkinUrl}")
    private String checkinUrl;

    @Value("${emos.email.system}")
    private String sysEmail;

    @Value("${emos.face.createFaceModelUrl}")
    private String createFaceModelUrl;

    @Value("${emos.code}")
    private String code;

    @Override
    public String validCanCheckIn(int userId, String date) {
        boolean bool_1 = holidaysMapper.searchTodayIsHolidays() != null ? true : false;
        boolean bool_2 = workdayMapper.searchTodayIsWorkday() != null ? true : false;
        String type = "工作日";
        if (DateUtil.date().isWeekend()) {
            type = "节假日";
        }
        if (bool_1) {
            type = "节假日";
        } else if (bool_2) {
            type = "工作日";
        }

        if (type.equals("节假日")) {
            return "节假日不需要考勤";
        } else {
            DateTime now = DateUtil.date();
            String start = DateUtil.today() + " " + systemConstants.attendanceStartTime;
            String end = DateUtil.today() + " " + systemConstants.attendanceEndTime;
            DateTime attendanceStart = DateUtil.parse(start);
            DateTime attendanceEnd = DateUtil.parse(end);
            if (now.isBefore(attendanceStart)) {
                return "没有到上班考勤开始时间";
            } else if (now.isAfter(attendanceEnd)) {
                return "超过了上班考勤结束时间";
            } else {
                HashMap map = new HashMap();
                map.put("userId", userId);
                map.put("date", date);
                map.put("start", start);
                map.put("end", end);
                boolean bool = checkinMapper.haveCheckin(map) != null ? true : false;
                return bool ? "今日已经考勤，不用重复考勤" : "可以考勤";
            }
        }
    }

    @Override
    public void checkin(HashMap param) {
        //判断签到
        Date d1 = DateUtil.date();  //当前时间
        Date d2 = DateUtil.parse(DateUtil.today() + " " + systemConstants.attendanceTime);  //上班时间
        Date d3 = DateUtil.parse(DateUtil.today() + " " + systemConstants.attendanceEndTime); //签到结束时间
        int status = 1;
        if (d1.compareTo(d2) <= 0) {
            status = 1; // 正常签到
        } else if (d1.compareTo(d2) > 0 && d1.compareTo(d3) < 0) {
            status = 2; //迟到
        }
        //查询签到人的人脸模型数据
        int userId= (Integer) param.get("userId");
        String faceModel=tbFaceModelMapper.searchFaceModel(userId);
        if (faceModel == null) {
            throw new EmosException("不存在人脸模型");
        } else {
            String path=(String)param.get("path");
            HttpRequest request = HttpUtil.createPost(checkinUrl);
            request.form("photo", FileUtil.file(path), "targetModel", faceModel);
            request.form("code",code);
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
                //发送告警邮件
                HashMap<String,String> map=tbUserMapper.searchNameAndDept(userId);
                String name = map.get("name");
                String deptName = map.get("dept_name");
                deptName = deptName != null ? deptName : "";
                SimpleMailMessage message=new SimpleMailMessage();
                message.setTo(sysEmail);
                message.setSubject("员工" + name + "身处高风险疫情地区警告");
                message.setText(deptName + "员工" + name + "，" + DateUtil.format(new Date(), "yyyy年MM月dd日") + "处于长沙，属于新冠疫情高风险地区，请及时与该员工联系，核实情况！");
                emailTask.sendAsync(message);
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
                checkinMapper.insert(entity);

            }
        }
    }

    public void createFaceModel(int userId, String path) {
        HttpRequest request = HttpUtil.createPost(createFaceModelUrl);
        request.form("photo", FileUtil.file(path));
        request.form("code",code);
        HttpResponse response = request.execute();
        String body = response.body();
        if ("无法识别出人脸".equals(body) || "照片中存在多张人脸".equals(body)) {
            throw new EmosException(body);
        } else {
            TbFaceModel entity = new TbFaceModel();
            entity.setUserId(userId);
            entity.setFaceModel(body);
            faceModelMapper.insert(entity);
        }
    }

    @Override
    public HashMap searchTodayCheckin(int userId) {
        HashMap map = checkinMapper.searchTodayCheckin(userId);
        return map;
    }

    @Override
    public long searchCheckinDays(int userId) {
        long days = checkinMapper.searchCheckinDays(userId);
        return days;
    }

    @Override
    public ArrayList<HashMap> searchWeekCheckin(HashMap param) {
        ArrayList<HashMap> checkinList = checkinMapper.searchWeekCheckin(param);
        ArrayList<String> holidaysList = holidaysMapper.searchHolidaysInRange(param);
        ArrayList<String> workdayList = workdayMapper.searchWorkdayInRange(param);

        DateTime startDate = DateUtil.parseDate(param.get("startDate").toString());
        DateTime endDate = DateUtil.parseDate(param.get("endDate").toString());
        DateRange range = DateUtil.range(startDate, endDate, DateField.DAY_OF_MONTH);
        ArrayList list = new ArrayList();
        range.forEach(one -> {
            String date = one.toString("yyyy-MM-dd");
            //查看今天是不是假期或者工作日
            String type = "工作日";
            if (one.isWeekend()) {
                type = "节假日";
            }
            if (holidaysList != null && holidaysList.contains(date)) {
                type = "节假日";
            } else if (workdayList != null && workdayList.contains(date)) {
                type = "工作日";
            }

            String status = "";
            if (type.equals("工作日") && DateUtil.compare(one, DateUtil.date()) <= 0) {
                status = "缺勤";
                boolean flag=false;
                for (HashMap<String, String> map : checkinList) {
                    if (map.containsValue(date)) {
                        status = map.get("status");
                        flag=true;
                        break;
                    }
                }
                DateTime endTime=DateUtil.parse(DateUtil.today()+" "+systemConstants.attendanceEndTime);
                String today=DateUtil.today();
                if(date.equals(today)&&DateUtil.date().isBefore(endTime)&&flag==false){
                    status="";
                }
            }

            HashMap map = new HashMap();
            map.put("date", date);
            map.put("status", status);
            map.put("type", type);
            map.put("day", one.dayOfWeekEnum().toChinese("周"));
            list.add(map);
        });
        return list;
    }

}

