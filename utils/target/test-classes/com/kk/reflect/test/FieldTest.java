package com.kk.reflect.test;

import com.kk.utils.log.ConsoleLogger;
import com.kk.utils.reflect.Reflections;
import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.Test;

import java.lang.reflect.Field;

public class FieldTest {
    ConsoleLogger logger = new ConsoleLogger();

    @Test
    public void testField() throws Exception {
        User user = new User();
        {
            Field field = FieldUtils.getField(user.getClass(), "id", true);
            logger.info(field);

            field.set(user, 1);// yes
//            field.set(user, "1"); // error�����Ͳ�ƥ��
            Reflections.setFieldValue(user, "id", 1); // yes
//            Reflections.setFieldValue(user, "id", "1"); // error ,���Ͳ�ƥ��

            FieldUtils.writeField(user, "id", 1, true);// yes
//            FieldUtils.writeField(user, "id", "1", true);// error,���Ͳ�ƥ��

            Reflections.setProperty(user, "id", 1);// yes
//            Reflections.setProperty(user, "id", "1");// error,���Ͳ�ƥ��

            Reflections.setPropertyByIntrospector(user,"id",1);

            logger.info(field.get(user));
        }
    }
}
