package com.example.emos.wx.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.task.EmailTask;
import com.example.emos.wx.util.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * @author: renke
 * @description: 监控IP是否正常
 * @date: 2023/2/21 8:45
 * @version: 1.0
 */
@Component
@Slf4j
public class  monitorJob {

    @Resource
    EmailTask emailTask;


    @Value("${emos.email.system}")
    private String sysEmail;

    @Value("${emos.info-addrees}")
    private String addrees;

    /**
     * 每隔10分钟刷新一次查询是否下载
     */
    @Scheduled(cron = "0 0/60 * * * ?")
    public void execute() {
        String[] aryy=addrees.split(",");
        for(int i=0;i<aryy.length;i++){
            this.monitorPost(aryy[i]);
        }
    }

    public void monitorPost(String checkinUrl){

        HttpRequest request = HttpUtil.createPost(checkinUrl);
        request.setConnectionTimeout(20000);
        HttpResponse response = request.execute();
        String body = response.body();
        if (response.getStatus() != 200) {
            log.error("请求异常服务异常");
            log.info(checkinUrl + DateUtil.format(new Date(), "yyyy年MM月dd日 HH:mm:ss") +" 连接失败");
            SimpleMailMessage message=new SimpleMailMessage();
            message.setTo(sysEmail);
            message.setSubject("IP:" + checkinUrl + "网络异常警告！");
            message.setText("IP:" + checkinUrl + "，" + DateUtil.format(new Date(), "yyyy年MM月dd日 HH:mm:ss") + "网络访问异常请注意！");
            emailTask.sendAsync(message);
        }
    }

}
