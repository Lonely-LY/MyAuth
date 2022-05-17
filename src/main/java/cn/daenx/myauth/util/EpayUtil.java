package cn.daenx.myauth.util;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 * @since 2022-05-13
 */
public class EpayUtil {
    /**
     * 排序
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K extends Comparable<? super K>, V > Map<K, V> sortByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        map.entrySet().stream()
                .sorted(Map.Entry.<K, V>comparingByKey()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    /**
     * 生成订单号末尾拼接adminID
     *
     * @param id
     * @return
     */
    public static String getOrderIdByTime(Integer id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for(int i=0;i<3;i++){
            result+=random.nextInt(10);
        }
        return newDate+result+id.toString();
    }
}
