package ovaphlow.himawari.grpc;

import com.google.gson.Gson;
import io.grpc.stub.StreamObserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TestServiceImpl extends TestGrpc.TestImplBase {
    private static final Logger logger = Logger.getLogger(TestServiceImpl.class.getName());

    @Override
    public void save(SaveRequest req, StreamObserver<SaveReply> responseObserver) {
        System.out.println(req);
        System.out.println(req.getName());

        SaveReply reply = SaveReply.newBuilder().setMessage("Save " + req.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void list(TestRequest req, StreamObserver<TestReply> responseObserver) {
        String result = "";
        Gson gson = new Gson();
        try {
            Connection conn = DBHandler.getConn();
            String sql = "select * from public.user limit 20";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Map<String, Object> map = new HashMap<>();
            map.put("message", "");
            map.put("content", DBUtil.getList(rs));
            result = gson.toJson(map);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> map = new HashMap<>();
            map.put("message", "gRPC服务器错误");
            result = gson.toJson(map);
        }
        TestReply reply = TestReply.newBuilder().setData(result).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void get(TestRequest req, StreamObserver<TestReply> responseObserver) {
        String result = "";
        Gson gson = new Gson();
        try {
            Map<String, Object> map = gson.fromJson(req.getData(), Map.class);
            Connection conn = DBHandler.getConn();
            String sql = "select * from public.user where id = ? limit 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(map.get("id").toString()));
            ResultSet rs = ps.executeQuery();
            map.clear();
            map.put("message", "");
            map.put("content", DBUtil.getMap(rs));
            result = gson.toJson(map);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> map = new HashMap<>();
            map.put("message", "gRPC服务器错误");
            result = gson.toJson(map);
        }
        TestReply reply = TestReply.newBuilder().setData(result).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
