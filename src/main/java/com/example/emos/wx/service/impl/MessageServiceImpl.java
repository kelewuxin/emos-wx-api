package com.example.emos.wx.service.impl;

import com.example.emos.wx.db.mapper.MessageMapper;
import com.example.emos.wx.db.mapper.MessageRefMapper;
import com.example.emos.wx.db.pojo.MessageEntity;
import com.example.emos.wx.db.pojo.MessageRefEntity;
import com.example.emos.wx.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author: renke
 * @description: TODO
 * @date: 2023/2/26 15:13
 * @version: 1.0
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private MessageRefMapper messageRefMapper;

    @Override
    public String insertMessage(MessageEntity entity) {
        String id = messageMapper.insert(entity);
        return id;
    }

    @Override
    public String insertRef(MessageRefEntity entity) {
        String id = messageRefMapper.insert(entity);
        return id;
    }

    @Override
    public long searchUnreadCount(int userId) {
        long count = messageRefMapper.searchUnreadCount(userId);
        return count;
    }

    @Override
    public long searchLastCount(int userId) {
        long count = messageRefMapper.searchLastCount(userId);
        return count;
    }

    @Override
    public List<HashMap> searchMessageByPage(int userId, long start, int length) {
        List<HashMap> list = messageMapper.searchMessageByPage(userId, start, length);
        return list;
    }

    @Override
    public HashMap searchMessageById(String id) {
        HashMap map = messageMapper.searchMessageById(id);
        return map;
    }

    @Override
    public long updateUnreadMessage(String id) {
        long rows = messageRefMapper.updateUnreadMessage(id);
        return rows;
    }

    @Override
    public long deleteMessageRefById(String id) {
        long rows = messageRefMapper.deleteMessageRefById(id);
        return rows;
    }

    @Override
    public long deleteUserMessageRef(int userId) {
        long rows=messageRefMapper.deleteUserMessageRef(userId);
        return rows;
    }
}
