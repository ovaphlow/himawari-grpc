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

public class DeptServiceImpl extends DeptGrpc.DeptImplBase {
    Logger logger = LoggerFactory.getLogger(DeptServiceImpl.class);

    @Override
    @SuppressWarnings("unchecked")
    public void list(DeptRequest req, StreamObserver<DeptReply> responseObserver) {
        String resp = "";
        Gson gson = new Gson();

        try {
            Connection conn = DBUtil.getConn();
            String sql = "select * from public.common where k = '部门' order by id desc";
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

        DeptReply reply = DeptReply.newBuilder().setData(resp).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save(DeptRequest req, StreamObserver<DeptReply> responseObserver) {
        String resp = "";
        Gson gson = new Gson();

        try {
            Map<String, Object> body = gson.fromJson(req.getData(), Map.class);
            Connection conn = DBUtil.getConn();
            String sql = "insert into public.common " +
                    "(master_id, k, v, remark) " +
                    "values (0, '部门', ?, ?) " +
                    "returning id";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, body.get("name").toString());
            ps.setString(2, body.get("remark").toString());
            ResultSet rs = ps.executeQuery();

            Map<String, Object> map = new HashMap<>();
            map.put("message", "");
            map.put("content", DBUtil.getMap(rs));
            resp = gson.toJson(map);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> map = new HashMap<>();
            map.put("message", "gRPC服务器错误");
            resp = gson.toJson(map);
        }

        DeptReply reply = DeptReply.newBuilder().setData(resp).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void get(DeptRequest req, StreamObserver<DeptReply> responseObserver) {
        String resp = "";
        Gson gson = new Gson();

        try {
            Map<String, Object> body = gson.fromJson(req.getData(), Map.class);
            Connection conn = DBUtil.getConn();
            String sql = "select * from public.common where id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(body.get("id").toString()));
            ResultSet rs = ps.executeQuery();

            Map<String, Object> map = new HashMap<>();
            map.put("message", "");
            map.put("content", DBUtil.getMap(rs));
            resp = gson.toJson(map);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> map = new HashMap<>();
            map.put("message", "gRPC服务器错误");
            resp = gson.toJson(map);
        }

        DeptReply reply = DeptReply.newBuilder().setData(resp).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(DeptRequest req, StreamObserver<DeptReply> responseObserver) {
        String resp = "";
        Gson gson = new Gson();

        try {
            Map<String, Object> body = gson.fromJson(req.getData(), Map.class);
            Double d = Double.parseDouble(body.get("id").toString());
            Connection conn = DBUtil.getConn();
            String sql = "update public.common " +
                    "set v = ?, remark = ? " +
                    "where id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, body.get("v").toString());
            ps.setString(2, body.get("remark").toString());
            ps.setInt(3, d.intValue());
            ps.execute();

            Map<String, Object> map = new HashMap<>();
            map.put("message", "");
            map.put("content", "");
            resp = gson.toJson(map);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> map = new HashMap<>();
            map.put("message", "gRPC服务器错误");
            resp = gson.toJson(map);
        }

        DeptReply reply = DeptReply.newBuilder().setData(resp).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void remove(DeptRequest req, StreamObserver<DeptReply> responseObserver) {
        String resp = "";
        Gson gson = new Gson();

        try {
            Map<String, Object> body = gson.fromJson(req.getData(), Map.class);
            Connection conn = DBUtil.getConn();
            String sql = "delete from public.common where id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(body.get("id").toString()));
            ps.execute();

            Map<String, Object> map = new HashMap<>();
            map.put("message", "");
            map.put("content", "");
            resp = gson.toJson(map);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> map = new HashMap<>();
            map.put("message", "gRPC服务器错误");
            resp = gson.toJson(map);
        }

        DeptReply reply = DeptReply.newBuilder().setData(resp).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
