package learningVertX;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import learningVertX.Verticles.childA;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class MainVerticle extends AbstractVerticle
{

  public static void main(String[] args)
  {

    var vertx = Vertx.vertx(new VertxOptions().setEventLoopPoolSize(10));


    vertx.deployVerticle(
        MainVerticle.class.getName(), new DeploymentOptions()
            .setClassLoader(null));

  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception
  {
    // Deploy EBC_PUB_SUB and wait for it to complete


    vertx.deployVerticle(new childA())
        .onSuccess(id -> {
          System.out.println("EBC_PUB_SUB deployed successfully");
          startPromise.complete();
        })
        .onFailure(err -> {
          System.err.println("Failed to deploy EBC_PUB_SUB: " + err
              .getMessage());
          startPromise.fail(err);
        });
  }
}
