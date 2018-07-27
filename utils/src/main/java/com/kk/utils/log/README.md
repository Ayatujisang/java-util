

### 记录执行时间 StopWatch
```
import org.springframework.util.StopWatch;

StopWatch sw = new StopWatch();
sw.start();

sw.stop();
logger.info("测试1,lastTime={}, totalTime:{}", sw.getLastTaskTimeMillis(), sw.getTotalTimeMillis());

```
