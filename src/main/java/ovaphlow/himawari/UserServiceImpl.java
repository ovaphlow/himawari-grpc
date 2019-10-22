package ovaphlow.himawari;

import com.google.gson.Gson;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            Map<String, Object> body = gson.fromJson(req.getData(), Map.class);
            Map<String, Object> map = new HashMap<>();
            map.put("message", "");
            map.put("content", body.get("keyword").toString());
            resp = gson.toJson(map);
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
        logger.info("{}", req.getData());
        logger.info("{}", gson.toJson(req.getData()));

        try {
            Map<String, Object> body = gson.fromJson(req.getData(), Map.class);
            Map<String, Object> map = new HashMap<>();
            map.put("message", "");
            map.put("content", body.get("keyword").toString());
            resp = gson.toJson(map);
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
}
