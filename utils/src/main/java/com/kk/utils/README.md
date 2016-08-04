
apache-common 包中常见util

IOUtils
StringUtils
RandomStringUtils
FileUtils
BeanUtils
ObjectUtils
PropertyUtils
ClassUtils
SerializationUtils
NumberUtils
MethodUtils

DateUtils

### StringEscapeUtils
* escapeSql 防SQL注入
* escapeHtml，unescapeHtml 转义/反转义 HTML,注意汉字 如 &--&amp;
* escapeJava，unescapeJava 转义成Unicode编码， 如 陈--\u9648
* escapeXml，unescapeXml 转义xml  如 &--&amp;