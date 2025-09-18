package learningVertX;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import learningVertX.eventBusCommunication.EBC_P_TO_P;

public class MainVerticle extends AbstractVerticle
{
  public static void main(String[] args)
  {
    var vertx = Vertx.vertx();
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception
  {
    // vertx.deployVerticle(new learningVertX.Verticles.MainVerticle());
    vertx.deployVerticle(new EBC_P_TO_P());
  }
}
