

package learningVertX.Verticles;

import io.vertx.core.DeploymentOptions;

public class childAB extends CustomVerticle
{


  @Override
  public void start() throws Exception
  {
    super.start();

    vertx.deployVerticle(childN.class.getName(), new DeploymentOptions().setInstances(4));
  }
}
