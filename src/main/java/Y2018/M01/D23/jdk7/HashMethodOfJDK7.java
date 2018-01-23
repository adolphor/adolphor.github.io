package Y2018.M01.D23.jdk7;

public class HashMethodOfJDK7 {

	transient int hashSeed = 0;

	final int hash(Object k) {
		int h = hashSeed;
		if (0 != h && k instanceof String) {
			return Hashing.stringHash32((String) k);
		}
		h ^= k.hashCode();
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}

}

// 伪码示例：实际实现方式参考源码
class Hashing {
	public static int stringHash32(String key) {
		return 0;
	}
}