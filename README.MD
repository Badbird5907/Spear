# Spear
Spear is an annotation-based wrapper around Javalin that allows you to create and manage a number of routes effortlessly.

Spear also provides an optional end-to-end typesafe API client and server.

Spear is inspired by the Spring framework, and aims to provide an easy way to create and manage routes in a Javalin application. 

Example "route-only" Usage:
```java
public class TestMain {
    public static void main(String[] args) {
        Spear.bind(Javalin.create())
                .register(new TestController())
                .start(8080);
    }
}

@Route("/test")
public class TestController implements SpearHandler {
    @Route(value = "/", method = HttpMethod.GET) // hit localhost:8080/test/
    public void test(Context ctx) {
        System.out.println("Hello, World!");
        ctx.result("HW1");
    }
    @Route(value = "/{pathparam}", method = HttpMethod.GET) // hit localhost:8080/test/paramnamehere
    public String test2(Context ctx, @PathParam("pathparam") String param) {
        return param;
    }
}
```

Example Client & Server:
```java
public class ServerMain {
    public static void main(String[] args) {
        Spear.bind(Javalin.create())
                .register(new TestHandler())
                .start(8080);
    }
}


public record TestResponse(String response) {}

@Route("/test")
public record TestRequest(String str) implements APIRequest<TestResponse> {}

public class TestHandler implements APIHandler<TestResponse, TestRequest> {
    @Override
    public TestResponse handle(TestAPIRequest request, Context ctx) {
        System.out.println("Received request: " + request.str());
        return new TestResponse("Hello world!");
    }
}

public class ClientMain {
    public static void main(String[] args) {
        new SpearCommons(new Gson(), "http://localhost:8089", builder -> builder);
        new TestRequest("Hi").send().thenAccept(System.out::println);
    }
}
```

See the [example](https://github.com/Badbird5907/Spear/tree/master/example) module for more examples.

## Sponsors
This project is proudly brought to you by *jank*
