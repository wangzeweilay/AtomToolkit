
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * list相关的工具类
 * @author zewei.wang
 * @date 2018/7/9.
 */
public class ListUtils {
    /**
     * 将list分页操作
     * @param list
     * @param pageSize
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> list, int pageSize) {
        int listSize = list.size();
        int page = (listSize + (pageSize - 1)) / pageSize;

        List<List<T>> listArray = new ArrayList<List<T>>();
        int fromIndex = 0;
        int toIndex = 0;
        for (int i = 1; i < page; i++) {
            toIndex = i * pageSize;
            List<T> subList = subList(list,fromIndex,toIndex);
            fromIndex = toIndex;
            listArray.add(subList);
        }
        listArray.add(subList(list,fromIndex,listSize));
        return listArray;
    }

    /**
     * 深度拷贝一个list
     * @param src
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    private static <T> List<T> subList(List<T> list, int fromIndex, int toIndex) {
        List<T> subList = new ArrayList<T>();
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(list.get(i));
        }
        return subList;
    }
}