
package learningVertX.Verticles;

public class childB extends CustomVerticle
{

  @Override
  public void start() throws Exception
  {
    super.start();

    vertx.deployVerticle(new childBA());


  }

}
