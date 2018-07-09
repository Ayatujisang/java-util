
apache-common 包中常见util

BeanUtils
PropertyUtils
ClassUtils
MethodUtils
ObjectUtils

spring 包中创建util
ObjectUtils
ReflectionUtils


## properties赋值到bean，可级联赋值
```
package com.kk.bean;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.validation.DataBinder;

import java.util.Properties;

/**
 * @author zhihui.kzh
 * @create 9/7/1820:56
 */
public class BeanProTest {
    public static void main(String[] args) {

        {
            WUrl bean = new WUrl();
            bean.setwPort(new WPort());

            Properties properties = new Properties();
            properties.setProperty("id", "11");
            properties.setProperty("wPort.port", "88");


            // 把properties的值注入到 diamondValue 里
//        new RelaxedDataBinder(bean).bind(new MutablePropertyValues(properties));
            new DataBinder(bean).bind(new MutablePropertyValues(properties));

            System.out.println(bean.getwPort().getPort());
            System.out.println(JSON.toJSONString(bean));
        }

    }
}

```
