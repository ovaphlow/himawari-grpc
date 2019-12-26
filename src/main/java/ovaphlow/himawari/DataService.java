package ovaphlow.himawari;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DataService {
    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    private Server server;

    private void start() throws IOException {
        int port = 5001;
        server = ServerBuilder.forPort(port)
                .maxInboundMessageSize(1024 * 1024 * 256)
                .addService(new ArchiveServiceImpl())
                .addService(new DeptServiceImpl())
                .addService(new UserServiceImpl())
                .addService(new VaultServiceImpl())
                .build()
                .start();
        logger.info("服务启动于端口 " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                DataService.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final DataService server = new DataService();
        server.start();
        server.blockUntilShutdown();
    }
}