package learningVertX.Verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class CustomVerticle extends AbstractVerticle
{


  @Override
  public void start(Promise<Void> startPromise) throws Exception
  {
    System.out.println("[%s]-[%s]-[%s]".formatted(
        getClass(), Thread.currentThread().getName(), getClass().getCanonicalName()));
    startPromise.complete();
  }

}

