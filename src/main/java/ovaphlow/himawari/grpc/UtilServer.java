package ovaphlow.himawari.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

public class UtilServer {
    private static final Logger logger = Logger.getLogger(UtilServer.class.getName());

    private Server server;

    private void start() throws IOException {
        int port = 5001;
        server = ServerBuilder.forPort(port)
                .addService(new TestServiceImpl())
                .addService(new ArchiveServiceImpl())
                .build()
                .start();
        logger.info("服务启动于端口 " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                UtilServer.this.stop();
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
        final UtilServer server = new UtilServer();
        server.start();
        server.blockUntilShutdown();
    }
}