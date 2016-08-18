package carec2.camel.routes;

import org.apache.camel.spring.boot.FatJarRouter;
import org.apache.camel.spring.boot.FatWarInitializer;

public class RouterWarInitializer extends FatWarInitializer {

    @Override
    protected Class<? extends FatJarRouter> routerClass() {
        return Router.class;
    }
}
