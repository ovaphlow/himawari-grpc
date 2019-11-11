package ovaphlow.himawari;

import com.google.gson.Gson;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PictureServiceImpl extends PictureGrpc.PictureImplBase {
    private final static Logger logger = LoggerFactory.getLogger(PictureServiceImpl.class);

    @Override
    @SuppressWarnings("unchecked")
    public void list(PictureRequest req, StreamObserver<PictureReply> responseObserver) {
        Gson gson = new Gson();
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "");
        resp.put("content", "");

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

        PictureReply reply = PictureReply.newBuilder().setData(gson.toJson(resp)).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
