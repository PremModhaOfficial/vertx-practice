package learningVertX.workerPool;

import java.util.Random;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class workersz extends AbstractVerticle
{

  @Override
  public void start(Promise<Void> startPromise) throws Exception
  {
    log.debug("started {}", getClass().getName());


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
