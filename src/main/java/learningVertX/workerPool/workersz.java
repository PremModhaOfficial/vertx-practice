package learningVertX.workerPool;

import java.util.Random;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class workersz extends AbstractVerticle
{

  @Override
  public void start(Promise<Void> startPromise) throws Exception
  {

    startPromise.complete();
    vertx.executeBlocking(event -> {
      try
      {
        Thread.sleep(3000);

        if (new Random(System.nanoTime()).nextInt(5) < 2)
        {
          throw new InterruptedException("RANDOM");

        }
        event.complete();
      }
      catch (InterruptedException interruptedException)
      {
        interruptedException.printStackTrace();
        event.fail(interruptedException);
      }
    }, results ->

    {
      if (results.succeeded())
      {
        System.out.println("succeeded");
      }
      else
      {
        System.out.println("result had problems %s".formatted(results.cause()));
      }
    });


  }
}
