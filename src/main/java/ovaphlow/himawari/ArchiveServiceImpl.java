package ovaphlow.himawari;

import com.google.gson.Gson;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchiveServiceImpl extends ArchiveGrpc.ArchiveImplBase {
    private static final Logger logger = LoggerFactory.getLogger(ArchiveServiceImpl.class);

    @Override
    @SuppressWarnings("unchecked")
    public void search(ArchiveRequest req, StreamObserver<ArchiveReply> responseObserver) {
        String resp = "";
        Gson gson = new Gson();

        try {
            Map<String, Object> body = gson.fromJson(req.getData(), Map.class);
            Connection conn = DBUtil.getConn();
            String sql = "select * from himawari.archive " +
                    "where sn = ? or identity = ? " +
                    "limit 2";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, body.get("keyword").toString());
            ps.setString(2, body.get("keyword").toString());
            ResultSet rs = ps.executeQuery();
            List<Map<String, Object>> r = DBUtil.getList(rs);
            Map<String, Object> map = new HashMap<>();
            if (r.size() == 0) {
                map.put("message", "未找到指定档案号/身份证的档案");
                map.put("content", body.get("keyword").toString());
            } else if (r.size() == 1) {
                map.put("message", "");
                map.put("content", r.get(0));
            } else {
                map.put("message", "您查询的档案号/身份证不是唯一数据，请联系系统管理员。");
                map.put("content", body.get("keyword").toString());
            }
            resp = gson.toJson(map);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> map = new HashMap<>();
            map.put("message", "gRPC服务器错误");
            resp = gson.toJson(map);
        }

        ArchiveReply reply = ArchiveReply.newBuilder().setData(resp).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void filter(ArchiveRequest req, StreamObserver<ArchiveReply> responseObserver) {
        String resp = "";
        Gson gson = new Gson();

        try {
            Map<String, Object> body = gson.fromJson(req.getData(), Map.class);
            Connection conn = DBUtil.getConn();
            String sql = "select * from himawari.archive " +
                    "where position(? in sn) > 0 " +
                    "and position(? in identity) > 0 " +
                    "and position(? in name) > 0 " +
                    "limit 2000";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, body.get("sn").toString());
            ps.setString(2, body.get("identity").toString());
            ps.setString(3, body.get("name").toString());
            ResultSet rs = ps.executeQuery();
            List<Map<String, Object>> r = DBUtil.getList(rs);
            Map<String, Object> map = new HashMap<>();
            map.put("message", "");
            map.put("content", r);
            resp = gson.toJson(map);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> map = new HashMap<>();
            map.put("message", "gRPC服务器错误");
            resp = gson.toJson(map);
        }

        ArchiveReply reply = ArchiveReply.newBuilder().setData(resp).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void list(ArchiveRequest req, StreamObserver<ArchiveReply> responseObserver) {
        String resp = "";
        Gson gson = new Gson();
        logger.info("list archives");

        try {
            Connection conn = DBUtil.getConn();
            String sql = "select * from himawari.archive order by id desc limit 200";
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

        ArchiveReply reply = ArchiveReply.newBuilder().setData(resp).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save(ArchiveRequest req, StreamObserver<ArchiveReply> responseObserver) {
        String resp = "";
        Gson gson = new Gson();

        try {
            Map<String, Object> body = gson.fromJson(req.getData(), Map.class);
            Connection conn = DBUtil.getConn();
            String sql = "insert into himawari.archive " +
                    "(sn, sn_alt, identity, name, birthday, " +
                    "cangongshijian, zhicheng, gongling, yutuixiuriqi, tuixiuriqi, vault_id, " +
                    "remark) " +
                    "values " +
                    "(?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, " +
                    "?)" +
                    " returning id";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, body.get("sn").toString());
            ps.setString(2, body.get("sn_alt").toString());
            ps.setString(3, body.get("identity").toString());
            ps.setString(4, body.get("name").toString());
            ps.setString(5, body.get("birthday").toString());
            ps.setString(6, body.get("cangongshijian").toString());
            ps.setString(7, body.get("zhicheng").toString());
            ps.setString(8, body.get("gongling").toString());
            ps.setString(9, body.get("yutuixiuriqi").toString());
            ps.setString(10, body.get("tuixiuriqi").toString());
            Double vaultId = Double.parseDouble(body.get("vault_id").toString());
            ps.setInt(11, vaultId.intValue());
            ps.setString(12, body.get("remark").toString());
            ResultSet rs = ps.executeQuery();
            Map<String, Object> r = DBUtil.getMap(rs);
            Map<String, Object> map = new HashMap<>();
            map.put("message", "");
            map.put("content", Integer.parseInt(r.get("id").toString()));
            resp = gson.toJson(map);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> map = new HashMap<>();
            map.put("message", "gRPC服务器错误");
            resp = gson.toJson(map);
        }

        ArchiveReply reply = ArchiveReply.newBuilder().setData(resp).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void get(ArchiveRequest req, StreamObserver<ArchiveReply> responseObserver) {
        String resp = "";
        Gson gson = new Gson();

        try {
            Map<String, Object> body = gson.fromJson(req.getData(), Map.class);
            Connection conn = DBUtil.getConn();
            String sql = "select * from himawari.archive where id = ? limit 1";
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

        ArchiveReply reply = ArchiveReply.newBuilder().setData(resp).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(ArchiveRequest req, StreamObserver<ArchiveReply> responseObserver) {
        String resp = "";
        Gson gson = new Gson();

        try {
            Map<String, Object> body = gson.fromJson(req.getData(), Map.class);
            Double d = Double.parseDouble(body.get("id").toString());
            Connection conn = DBUtil.getConn();
            String sql = "update himawari.archive " +
                    "set sn = ?, sn_alt = ?, " +
                    "identity = ?, name = ?, birthday = ?, " +
                    "cangongshijian = ?, zhicheng = ?, gongling = ?, " +
                    "yutuixiuriqi = ?, tuixiuriqi = ?, vault_id = ?, " +
                    "remark = ? " +
                    "where id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, body.get("sn").toString());
            ps.setString(2, body.get("sn_alt").toString());
            ps.setString(3, body.get("identity").toString());
            ps.setString(4, body.get("name").toString());
            ps.setString(5, body.get("birthday").toString());
            ps.setString(6, body.get("cangongshijian").toString());
            ps.setString(7, body.get("zhicheng").toString());
            ps.setString(8, body.get("gongling").toString());
            ps.setString(9, body.get("yutuixiuriqi").toString());
            ps.setString(10, body.get("tuixiuriqi").toString());
            Double vaultId = Double.parseDouble(body.get("vault_id").toString());
            ps.setInt(11, vaultId.intValue());
            ps.setString(12, body.get("remark").toString());
            ps.setInt(13, d.intValue());
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

        ArchiveReply reply = ArchiveReply.newBuilder().setData(resp).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void remove(ArchiveRequest req, StreamObserver<ArchiveReply> responseObserver) {
        String resp = "";
        Gson gson = new Gson();

        try {
            Map<String, Object> body = gson.fromJson(req.getData(), Map.class);
            Connection conn = DBUtil.getConn();
            String sql = "delete from himawari.archive where id = ?";
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

        ArchiveReply reply = ArchiveReply.newBuilder().setData(resp).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
