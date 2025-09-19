
package learningVertX.eventBusCommunication;

import java.util.Random;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import learningVertX.eventBusCommunication.codec.JacksonPojoCodec;
import learningVertX.Verticles.CustomVerticle;
import learningVertX.eventBusCommunication.pojo.PING;
import learningVertX.eventBusCommunication.pojo.PONG;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class EBC_PING_PONG extends CustomVerticle
{

  public static Handler<AsyncResult<String>> errorHandler()
  {
    return async_result -> {
      if (!async_result.succeeded()) log.error("Error: {}", async_result.cause());
    };
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception
  {
    startPromise.complete();
    vertx.eventBus().registerDefaultCodec(PING.class, new JacksonPojoCodec<>(PING.class));
    vertx.eventBus().registerDefaultCodec(PONG.class, new JacksonPojoCodec<>(PONG.class));

    vertx.deployVerticle(new Server());
    vertx.deployVerticle(new Client());
  }

  public static class Server extends CustomVerticle
  {
    private EventBus EBUS;

    @Override
    public void start(Promise<Void> startPromise) throws Exception
    {
      EBUS = vertx.eventBus();
      EBUS.consumer(Server.class.getName(), MessageHandler());
    }

    private Handler<Message<PING>> MessageHandler()
    {
      return msg -> {
        if (msg.body() != null)
        {
          log.info("Sending {}", msg);
          msg.reply(new PONG(msg.body().getMessages(), !msg.body().isEnabled()));
        }
        else
        {
        }
      };
    }
  }

  public static class Client extends CustomVerticle
  {
    private EventBus EBUS;

    @Override
    public void start(Promise<Void> startPromise) throws Exception
    {
      EBUS = vertx.eventBus();
      vertx.setPeriodic(1000, id -> pingTo(Server.class));
    }

    final static private Random RANDOM = new Random(System.nanoTime());

    private void pingTo(Class<?> clazz)
    {
      PING message = new PING("hi+" + RANDOM.nextInt(100), false);

      log.info("message: {}", message);
      EBUS.request(clazz.getName(), message, new DeliveryOptions());
    }
  }

}
