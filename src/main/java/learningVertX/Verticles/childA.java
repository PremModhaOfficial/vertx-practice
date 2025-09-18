package learningVertX.Verticles;

import io.vertx.core.Promise;

public class childA extends CustomVerticle
{

  @Override
  public void start(Promise<Void> startPromise) throws Exception
  {
    super.start();
    vertx.deployVerticle(new childAA());
    vertx.deployVerticle(new childAB());

  }

}
