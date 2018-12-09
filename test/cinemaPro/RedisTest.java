package cinemaPro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import com.oristartech.cinema.utils.ApplicationContextUtil;
import com.oristartech.cinema.utils.RedisUtil;

import redis.clients.jedis.JedisPool;

public class RedisTest{


    /**
     * 测试插入与获取Redis的数据
     * @Title: testPutAndGet 
     * @Description: TODO
     * @throws
     */
//    @Test
//    public void testPutAndGet() {
//        redisTemplate.opsForHash().put("user", "name", "rhwayfun");
//        Object object = redisTemplate.opsForHash().get("user", "name");
//        System.out.println(object);
//    }
    
    public static void main(String[] args){
//    	ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-cache.xml");
//    	JedisPool jedis = (JedisPool)context.getBean("jedisPool");
//    	System.out.println(jedis.getResource().get("name"));
    	
    	/*ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-application.xml");
    	RedisUtil redisUtil = (RedisUtil)context.getBean("redisUtil");
    	System.out.println(redisUtil.hasKey("name"));*/
    	RedisUtil redisUtil = (RedisUtil)ApplicationContextUtil.getBean("redisUtil");
    	System.out.println(redisUtil.hasKey("name"));
    }

    /**
     * 测试Redis作为缓存的例子
     * @Title: testCache 
     * @Description: TODO
     * @throws InterruptedException
     * @throws
     */
//    @Test
//    public void testCache() throws InterruptedException {
//        // 插入一条数据
//        redisTemplate.opsForHash().put("user", "name", "rhwayfun");
//        // 设置失效时间为2秒
//        redisTemplate.expire("user", 2, TimeUnit.SECONDS);
//        Thread.sleep(1000);
//        // 1秒后获取
//        Object object = redisTemplate.opsForHash().get("user", "name");
//        System.out.println("1秒后：" + object);
//        Thread.sleep(1000);
//        // 2秒后获取
//        object = redisTemplate.opsForHash().get("user", "name");
//        System.out.println("2秒后：" + object);
//    }
}
