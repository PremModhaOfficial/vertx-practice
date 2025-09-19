package learningVertX.Verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class HierchyVerticle extends AbstractVerticle
{
  public static void main(String[] args)
  {
    var vertx = Vertx.vertx();

    vertx.deployVerticle(new HierchyVerticle());
  }

  @Override
  public void start() throws Exception
  {
    System.out.println("MainVerticle.start()");


    vertx.deployVerticle(new childA());
    vertx.deployVerticle(new childB());
  }
}
