package org.logstashplugins.utils;


import java.util.ArrayList;
import java.util.List;

/**
 * SlicerUtils
 *
 * @author weixubin
 * @date 2020-12-08
 */
public class Slicer {

    public static <T> List<List<T>> fixedGrouping(List<T> source, int n) {
        if (null == source || source.size() == 0 || n <= 0)
            return null;
        List<List<T>> result = new ArrayList<List<T>>();
        int sourceSize = source.size();
        int size = (source.size() / n) + 1;
        for (int i = 0; i < size; i++) {
            List<T> subset = new ArrayList<T>();
            for (int j = i * n; j < (i + 1) * n; j++) {
                if (j < sourceSize) {
                    subset.add(source.get(j));
                }
            }
            if (subset.size() > 0) {
                result.add(subset);
            }
        }
        return result;
    }

}
