package learningVertX.eventLoopz;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class loopzz extends AbstractVerticle
{
  public static void main(String[] args)
  {

    var vertx = Vertx.vertx(
        new VertxOptions().setMaxEventLoopExecuteTime(500)
                          .setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS)
                          .setBlockedThreadCheckInterval(1)
                          .setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS)
    );

    vertx.deployVerticle(new loopzz(), new DeploymentOptions().setInstances(4));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception
  {
    System.out.println("startPromise = " + startPromise);
    System.out.println("started %s".formatted(getClass().getName()));

    Thread.sleep(new Random().nextInt(40000));

    startPromise.complete();
  }

}
