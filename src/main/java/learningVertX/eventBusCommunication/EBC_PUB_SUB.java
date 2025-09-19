package learningVertX.eventBusCommunication;

import java.util.Random;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;

public class EBC_PUB_SUB extends AbstractVerticle
{

  @Override
  public void start(Promise<Void> startPromise) throws Exception
  {

    vertx.deployVerticle(new PUB());
    vertx.deployVerticle(SUB_1.class.getName(), new DeploymentOptions().setInstances(2), res -> {
      if (res.succeeded())
      {

      }
      else
      {
        res.cause().printStackTrace();
      }
    }
    );

    vertx.deployVerticle(new SUB_2(), new DeploymentOptions().setInstances(2));

  }


  private static class PUB extends AbstractVerticle
  {
    private static Random random = new Random(System.nanoTime());

    @Override
    public void start(Promise<Void> startPromise) throws Exception
    {
      startPromise.complete();

      System.out.println("SEND");
      vertx.setPeriodic(1000, (id) -> {
        String msg = "" + random.nextInt(100);
        System.out.println("Publishing: " + msg);  // Added for debugging
        vertx.eventBus().publish(PUB.class.getName(), msg);
      });
    }
  }

  public static class SUB_2 extends AbstractVerticle
  {
    @Override
    public void start(Promise<Void> startPromise) throws Exception
    {
      System.out.println("SUB_2 starting...");  // Added for debugging
      // Set up the consumer before completing the promise
      vertx.eventBus().<String>consumer(PUB.class.getName(), (message) -> {
        System.out.println("SUB_2 instance " + context.deploymentID() + " received data: " + message.body());
      });

      // Complete the promise after setting up the consumer
      startPromise.complete();
      System.out.println("SUB_2 started successfully.");  // Added for debugging
    }
  }

  public static class SUB_1 extends AbstractVerticle
  {

    @Override
    public void start(Promise<Void> startPromise) throws Exception
    {
      System.out.println("SUB_1 starting...");  // Added for debugging
      startPromise.complete();

      vertx.eventBus().<String>consumer(PUB.class.getName(), (message) -> {
        System.out.println("SUB_1 received data: " + message.body());
      });
      System.out.println("SUB_1 started successfully.");  // Added for debugging

    }

  }


}
