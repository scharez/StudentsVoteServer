import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import utils.EVSBridge;


public class Main {

    public static final String BASE_URI = "http://localhost:8080/rest";

    public static org.glassfish.grizzly.http.server.HttpServer startServer() {

        final ResourceConfig rc = new ResourceConfig().packages("service","filter");

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        EVSBridge.getInstance().login("it150184","youneverknow");
        final org.glassfish.grizzly.http.server.HttpServer server = startServer();
        server.getServerConfiguration().addHttpHandler(new StaticHttpHandler("public"), "/");

        System.out.println(String.format("Server startet at %s\nHit enter to stop ...", BASE_URI));
        System.in.read();
        server.shutdown();
    }
}