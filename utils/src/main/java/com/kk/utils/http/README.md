
```
获取原生post内容:

import org.apache.commons.io.IOUtils;

// 获取post内容

InputStream in = request.getInputStream();

String res = IOUtils.toString(in, "UTF-8");



或者：

private String getRequestData(InputStream inputStream) {
    try {
        StringBuffer answer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            answer.append(line);
        }
        reader.close();

        logger.info("notify data is " + answer.toString());
        return answer.toString();

    } catch (Exception e) {
        logger.error(e.getCause());
    }

    return null;
}



或者：

在spring-mvc框架中，使用 @RequestBody 注解。

@RequestMapping(value = "/postdata2", method = RequestMethod.POST)
public String postdata(
        HttpServletRequest request,
        HttpServletResponse response, Model model,
        @RequestBody String cont
        ) throws IOException {
    System.out.println("data=" + cont);
    return "postdata:" + cont;
}
```