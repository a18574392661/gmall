
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.atguigu.gmall.UserServiceApplication;
import com.atguigu.gmall.util.RedisUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class) // 这里的Application是springboot的启动类名
@WebAppConfiguration
public class Test {

	@Autowired
	RedisUtil redisUtil;

	@org.junit.Test
	public void test() throws Exception {
		// System.out.println("测试"+redisUtil.getJedis());
		Jedis jedis = redisUtil.getJedis();
		/*
		 * jedis.setex("name", 5, "张三");//-2已经过期了 System.out.println(jedis.ttl("name"));
		 * //递增 jedis.set("num",1+""); System.out.println(jedis.get("num"));
		 * jedis.decr("num"); System.out.println(jedis.get("num")); jedis.incr("num");
		 * jedis.incr("num"); System.out.println(jedis.get("num"));
		 */
		/*
		 * Long num = jedis.hset("hash1", "username", "caopengfei");
		 * System.out.println(num); String hget = jedis.hget("hash1", "username");
		 * System.out.println(hget);
		 */
		/*
		 * Map<String, String> map = new HashMap<String, String>(); map.put("username",
		 * "caopengfei"); map.put("age", "25"); map.put("sex", "男"); String res =
		 * jedis.hmset("hash2", map); System.out.println(res);//ok
		 * System.out.println(jedis.hmget("hash2", "age","username"));
		 * 
		 * 
		 * Map<String, String> map2 = new HashMap<String, String>(); map2 =
		 * jedis.hgetAll("hash2"); System.out.println(map2);
		 * 
		 * 
		 * Long num = jedis.hdel("hash2", "username", "age"); System.out.println(num);
		 * jedis.hincrBy("hash2", "age", 10);
		 * 
		 * Map<String, String> map2 = new HashMap<String, String>(); map2 =
		 * jedis.hgetAll("hash2"); System.out.println(map2);
		 * 
		 * System.out.println(jedis.exists("name")+"//"+jedis.hexists("hash2", "age"));
		 * System.out.println(jedis.hlen("hash2")); Set<String> hash2 =
		 * jedis.hkeys("hash2"); System.out.println(hash2);
		 */
		
		
		// jedis.flushDB();
		/*
		 * System.out.println("===========添加一个list===========");
		 * jedis.lpush("collections", "ArrayList", "Vector", "Stack", "HashMap",
		 * "WeakHashMap", "LinkedHashMap"); jedis.lpush("collections", "HashSet");
		 * jedis.lpush("collections", "TreeSet"); jedis.lpush("collections", "TreeMap");
		 * System.out.println("collections的内容："+jedis.lrange("collections", 0,
		 * -1));//-1代表倒数第一个元素，-2代表倒数第二个元素
		 * System.out.println("collections区间0-3的元素："+jedis.lrange("collections",0,3));
		 * System.out.println("==============================="); // 删除列表指定的值
		 * ，第二个参数为删除的个数（有重复时），后add进去的值先被删，类似于出栈
		 * System.out.println("删除指定元素个数："+jedis.lrem("collections", 2, "HashMap"));
		 * System.out.println("collections的内容："+jedis.lrange("collections", 0, -1));
		 * System.out.println("删除下表0-3区间之外的元素："+jedis.ltrim("collections", 0, 3));
		 * System.out.println("collections的内容："+jedis.lrange("collections", 0, -1));
		 * System.out.println("collections列表出栈（左端）："+jedis.lpop("collections"));
		 * System.out.println("collections的内容："+jedis.lrange("collections", 0, -1));
		 * System.out.println("collections添加元素，从列表右端，与lpush相对应："+jedis.rpush(
		 * "collections", "EnumMap"));
		 * System.out.println("collections的内容："+jedis.lrange("collections", 0, -1));
		 * System.out.println("collections列表出栈（右端）："+jedis.rpop("collections"));
		 * System.out.println("collections的内容："+jedis.lrange("collections", 0, -1));
		 * System.out.println("修改collections指定下标1的内容："+jedis.lset("collections", 1,
		 * "LinkedArrayList"));
		 * System.out.println("collections的内容："+jedis.lrange("collections", 0, -1));
		 * System.out.println("===============================");
		 * System.out.println("collections的长度："+jedis.llen("collections"));
		 * System.out.println("获取collections下标为2的元素："+jedis.lindex("collections", 2));
		 */
		/*
		 * System.out.println("===============================");
		 * jedis.lpush("sortedList", "3","6","2","0","7","4");
		 * System.out.println("sortedList排序前："+jedis.lrange("sortedList", 0, -1));
		 * 
		 * System.out.println(jedis.sort("sortedList"));
		 * System.out.println("sortedList排序后："+jedis.lrange("sortedList", 0, -1));
		 */
		/*
		 * Long num = jedis.sadd("myset", "a", "a", "b","abc"); System.out.println(num);
		 * 
		 * System.out.println(jedis.smembers("myset"));
		 * System.out.println(jedis.srem("myset", "a")); Boolean sismember =
		 * jedis.sismember("myset", "a");
		 * 
		 * 
		 * jedis.sadd("myset1","123","32","abc","def","123456","sdfasd");
		 * jedis.sadd("myset2","abc","345","123","fda");
		 * 
		 * Set<String> sdiff = jedis.sdiff("myset1", "myset2");
		 * System.out.println(sdiff); Set<String> sinter = jedis.sinter("myset1",
		 * "myset2"); System.out.println(sinter); Set<String> sunion =
		 * jedis.sunion("myset1", "myset2"); System.out.println(sunion);
		 */
		
		

	        


	        
	}

	@org.junit.Test
	public void d() {
		Jedis jedis=redisUtil.getJedis();
		 jedis.zadd("mysort",100.0, "zhangsan");
	        jedis.zadd("mysort",200.0,"lisi");
	        jedis.zadd("mysort",50.0,"wangwu");
	        Map<String ,Double>map = new HashMap<String ,Double>();
	      //  map.put("mutouliu",70.0);​
	      //  jedis.zadd("mysort",map);
	        Set<String> mysort = jedis.zrange("mysort", 0, -1);
	        System.out.println(mysort);
	        Set<String> mysort1 = jedis.zrange("mysort", 1, 2);
	        System.out.println(mysort1);
	        
	}
	/*
	 * @org.junit.Test public void test2() { Jedis jedis=redisUtil.getJedis(); Long
	 * num = jedis.incrBy("num", 3); System.out.println(num);
	 * jedis.decrBy("num",10l);​ System.out.println(jedis.get("num"));
	 * 
	 * }
	 */

	
	
	
}
