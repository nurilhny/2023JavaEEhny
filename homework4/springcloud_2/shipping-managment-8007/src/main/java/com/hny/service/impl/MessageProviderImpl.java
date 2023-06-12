package com.hny.service.impl;

import com.hny.service.IMessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

@EnableBinding(Source.class) //定义消息推送管道
public class MessageProviderImpl implements IMessageProvider {
    @Autowired
    private MessageChannel output; //消息发送管道

    @Override
    public String send(Integer userId) {
        String mes = "用户"+userId+":新增新的订单!";
        output.send(MessageBuilder.withPayload(mes).build());
        System.out.println(mes);
        return null;
    }
}
