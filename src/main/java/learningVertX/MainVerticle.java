package learningVertX;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import learningVertX.eventBusCommunication.EBC_PING_PONG;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class MainVerticle extends AbstractVerticle
{
  @Override
  public void start(Promise<Void> startPromise) throws Exception
  {
    // Deploy EBC_PUB_SUB and wait for it to complete
    vertx.deployVerticle(new EBC_PING_PONG())

         .onSuccess(id -> {
           System.out.println("EBC_PUB_SUB deployed successfully");
           startPromise.complete();
         })
         .onFailure(err -> {
           System.err.println("Failed to deploy EBC_PUB_SUB: " + err.getMessage());
           startPromise.fail(err);
         });
  }
}
