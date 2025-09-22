package learningVertX.Verticles;

import java.util.UUID;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

public class childA extends CustomVerticle
{

  @Override
  public void start(Promise<Void> startPromise) throws Exception
  {
    super.start();
    startPromise.complete();
    var bulk  = vertx.deployVerticle(
        childAB.class.getName(), new DeploymentOptions()
            .setInstances(1)
            .setConfig(
                new JsonObject()
                    .put(
                        "id", UUID.randomUUID().toString()
                    )
            )
    );
    var bulk2 = vertx.deployVerticle(
        childAB.class.getName(), new DeploymentOptions()
            .setInstances(1)
            .setConfig(new JsonObject().put("id", UUID.randomUUID()
                .toString()))
    );
    var chAA  = vertx.deployVerticle(new childAA());
    var chAB  = vertx.deployVerticle(new childAB());
    var chAB2 = vertx.deployVerticle(new childAB());

    Future.all(bulk, bulk2, chAA, chAB, chAB2)
        .onFailure(err -> {
          err.printStackTrace();
        })
        .onSuccess(s -> {
          System.out.println(s);
        })

    ;

  }

}
