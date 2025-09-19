
package learningVertX.eventBusCommunication;

import java.util.Random;

import io.vertx.core.Promise;
import learningVertX.Verticles.CustomVerticle;

public class EBC_P_TO_P extends CustomVerticle
{

  private String senderVerticleId;
  private String receiverVerticleId;

  private static final String STOP     = "stop";
  private static final String CONTINUE = "continue";

  @Override
  public void start(Promise<Void> startPromise) throws Exception
  {
    super.start();
    vertx.deployVerticle(new SENDER()).onSuccess(id -> senderVerticleId = id);
    // vertx.deployVerticle(new RECEIVER()).onSuccess(id -> receiverVerticleId = id);
    vertx.eventBus().consumer("shutdown", msg -> {
      System.out.println("Undeploying verticles");
      vertx.undeploy(senderVerticleId);
      vertx.undeploy(receiverVerticleId);
    });
  }


  private static class SENDER extends CustomVerticle
  {
    @Override
    public void start(Promise<Void> startPromise) throws Exception
    {
      start();
      startPromise.complete();
      vertx.setPeriodic(new Random().nextInt(100), id -> {
        var msg = ("id: %d: hellow ....".formatted(++id));
        vertx.eventBus().send(SENDER.class.getName(), msg);
      });
    }
  }

  private static class COUNTER extends CustomVerticle
  {
    private int counter = 0;
    private int limit;

    public COUNTER(int limit)
    {
      this.limit = limit;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception
    {
      startPromise.complete();
      start();

      var rec = vertx.eventBus();
      rec.consumer(COUNTER.class.getName(), msg -> {
        if (counter++ >= limit)
        {
          msg.reply(String.valueOf(STOP));
        }
        else
        {
          msg.reply(String.valueOf(CONTINUE));
        }
      });
    }
  }

  private static class RECEIVER extends CustomVerticle
  {
    private String counterVerticleId;

    @Override
    public void start(Promise<Void> startPromise) throws Exception
    {
      start();
      startPromise.complete();
      final int limit = 100;
      vertx.deployVerticle(new COUNTER(limit)).onSuccess(id -> counterVerticleId = id);
      var ebus = vertx.eventBus();
      ebus.<String>consumer(SENDER.class.getName(), msg -> {
        System.out.println("recieved: `%s`".formatted(msg.body()));
        ebus.<String>request(COUNTER.class.getName(), "", (reply) -> {
          if (reply.succeeded())
          {
            var body = reply.result().body();
            System.out.println("body = " + body);
            if (Integer.parseInt(body) >= limit)

            {
              System.out.println("SUTTING DOWN");
              vertx.undeploy(counterVerticleId);
              vertx.eventBus().publish("shutdown", "now");
            }
            System.out.println(body);
          }
        });
      });
    }
  }

}
