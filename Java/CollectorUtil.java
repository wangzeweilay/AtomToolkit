
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @program: AtomToolKit
 * @description: collerctor 工具类
 * @author: zewei.wang
 * @create: 2018-06-28 12:59
 **/
public class CollectorUtil {
    /** 
    * @Description: 在列表中找到一个类
    * @Param: [] 
    * @return: java.util.stream.Collector<T,?,T> 
    * @Author: xiaojun.cheng 
    * @Date: 28/06/2018 
    */ 
    public static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

}
