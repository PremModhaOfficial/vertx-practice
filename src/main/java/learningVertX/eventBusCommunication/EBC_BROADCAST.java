
package learningVertX.eventBusCommunication;

import io.vertx.core.Promise;
import learningVertX.Verticles.CustomVerticle;

public class EBC_BROADCAST extends CustomVerticle
{
  private final static String EBUS_ADDR = "learningVertX.eventBusCommunication.EBC";

  @Override
  public void start(Promise<Void> startPromise) throws Exception
  {
    super.start();
    startPromise.complete();


    vertx.deployVerticle(new REQ());
    vertx.deployVerticle(new REP());
  }


  private static class REP extends CustomVerticle
  {

    @Override
    public void start(Promise<Void> startPromise) throws Exception
    {
      super.start();
      startPromise.complete();

      var eBus = vertx.eventBus();


      eBus.request(EBUS_ADDR, "HI! I am prem", event -> {
        System.out.println("reply came with status: %s".formatted(event.succeeded()));

        if (event.succeeded())
        {
          System.out.println(event.result().body());
        }
        else
        {
          throw new RuntimeException(event.cause());
        }

      });
    }
  }

  private static class REQ extends CustomVerticle
  {
    @Override
    public void start(Promise<Void> startPromise) throws Exception
    {
      super.start();
      startPromise.complete();


      vertx.eventBus().consumer(EBUS_ADDR, msg -> {


        if ((msg.body() != null))
        {
          System.out.println(msg.body());

          msg.reply("hi %s".formatted(msg.body().toString().substring("HI! I am ".length())));
        }
        else
        {
          throw new RuntimeException("request had problem: request: \n`%S`".formatted(msg));
        }
      });
    }
  }
}
