package com.kk.test;

import com.alibaba.fastjson.JSON;
import com.kk.model.User;
import com.kk.netty4.serial.HessianUtil;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * @auther zhihui.kzh
 * @create 9/9/1716:34
 */
public class HessianTest {

    @Test
    public void testHessian() throws IOException {
        User user = new User();
        user.setId(1);
        user.setName("kk");

        byte[] bytes = HessianUtil.serialize(user);
        System.out.println(bytes.length);


        User u = (User) HessianUtil.deserialize(bytes);
        System.out.println(JSON.toJSON(u));
    }

    @Test
    public void testJdk() throws IOException {
        User user = new User();
        user.setId(1);
        user.setName("kk");

        byte[] bytes = SerializationUtils.serialize(user);
        System.out.println(bytes.length);


        User u = (User) SerializationUtils.deserialize(bytes);
        System.out.println(JSON.toJSON(u));
    }
}
