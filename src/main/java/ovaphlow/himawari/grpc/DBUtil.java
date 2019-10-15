package ovaphlow.himawari.grpc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
    public static Map<String, Object> getMap(ResultSet rs) throws Exception {
        Map<String, Object> result = new HashMap<>();
        rs.next();
        if (rs.getRow() == 0) {
            return result;
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        for (int i = 1; i <= count; i++) {
            result.put(rsmd.getColumnLabel(i), rs.getObject(i));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getList(ResultSet rs) throws Exception {
        List<Map<String, Object>> result = new ArrayList();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<>();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            for (int i = 1; i <= count; i++) {
                map.put(rsmd.getColumnLabel(i), rs.getObject(i));
            }
            result.add(map);
        }
        return result;
    }
}
