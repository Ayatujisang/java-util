从spring-util中复制出来

1.spring-uitl:
import org.springframework.beans.BeanUtils;
BeanUtils.copyProperties(src, dest)    字段名称和类型一致的字段可以复制过去，
