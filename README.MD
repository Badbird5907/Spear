# Spear
Spear is an annotation based wrapper around Javalin that allows you to effortlessly create and manage a number of routes.

Spear is inspired by the Spring framework, and aims to provide an easy way to create and manage routes in a Javalin application. 

Example Usage:
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