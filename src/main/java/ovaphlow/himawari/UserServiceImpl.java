package ovaphlow.himawari;

import com.google.gson.Gson;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl extends UserGrpc.UserImplBase {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @SuppressWarnings("unchecked")
    public void list(UserRequest req, StreamObserver<UserReply> responseObserver) {
        String resp = "";
        Gson gson = new Gson();

        try {
            Connection conn = DBUtil.getConn();
            String sql = "select u.id, u.dept_id, u.username, u.name, " +
                    "(select super from himawari.auth where user_id = u.id) as super, " +
                    "(select v from public.common where id = u.dept_id) as dept " +
                    "from public.user as u " +
                    "order by id desc limit 200";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            Map<String, Object> map = new HashMap<>();
            map.put("message", "");
            map.put("content", DBUtil.getList(rs));
            resp = gson.toJson(map);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> map = new HashMap<>();
            map.put("message", "gRPC服务器错误");
            resp = gson.toJson(map);
        }

        UserReply reply = UserReply.newBuilder().setData(resp).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save(UserRequest req, StreamObserver<UserReply> responseObserver) {
        String resp = "";
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<>();
        map.put("message", "");
        map.put("content", "");

        try {
            Map<String, Object> body = gson.fromJson(req.getData(), Map.class);
            logger.info("{}", body);
            Double d = Double.parseDouble(body.get("dept_id").toString());
            Connection conn = DBUtil.getConn();
            String sql = "insert into public.user " +
                    "(username, password, name, dept_id, remark) " +
                    "values (?, ?, ?, ?, ?) " +
                    "returning id";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, body.get("username").toString());
            ps.setString(2, body.get("password").toString());
            ps.setString(3, body.get("name").toString());
            ps.setInt(4, d.intValue());
            ps.setString(5, body.get("remark").toString());
            ResultSet rs = ps.executeQuery();
            map.put("content", DBUtil.getMap(rs));
            resp = gson.toJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "gRPC服务器错误");
            resp = gson.toJson(map);
        }

        UserReply reply = UserReply.newBuilder().setData(resp).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
