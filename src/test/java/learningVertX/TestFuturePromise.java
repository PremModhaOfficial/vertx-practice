package learningVertX;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.vertx.core.Future;
// import io.vertx.core.Promise;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(VertxExtension.class)
public class TestFuturePromise
{


  @Test
  void promise_success_test(Vertx vertx, VertxTestContext context)
  {

    var promise = Promise.promise();

    vertx.setTimer(500, id -> {
      log.debug("Start");
      promise.complete("Success");
      log.debug("Success");
      context.completeNow();
    });


    log.debug("End");
  }

  @Test
  void promise_fail_test(Vertx vertx, VertxTestContext context)
  {

    var promise = Promise.promise();

    vertx.setTimer(500, id -> {
      log.debug("Start");
      promise.fail(new RuntimeException("FAILD"));
      log.debug("FAILED");
      context.completeNow();
    });


    log.debug("End");
  }


  @Test
  void future_succeeds(Vertx vertx, VertxTestContext context)
  {
    var promise = Promise.promise();
    log.debug("start");

    vertx.setTimer(1000, id -> {
      promise.complete("success");
    });
    log.debug("end");

    final var future = promise.future();

    future.onSuccess(result -> {
      log.debug("success");

      context.completeNow();
      System.out.println(result);
    });

    future.onFailure(context::failNow);


  }

  @Test
  void future_fails(Vertx vertx, VertxTestContext context)
  {
    var promise = Promise.promise();
    log.debug("start");

    vertx.setTimer(1000, id -> {
      promise.fail("FAILED");
    });
    log.debug("end");

    final var future = promise.future();


    future
        .onFailure(result -> {
          System.out.println(result);
          context.completeNow();
        });

    log.debug("success");


  }

  @Test
  void future_map(Vertx vertx, VertxTestContext context)
  {
    Promise<String> promise = Promise.promise();

    vertx.setTimer(100, id -> {
      promise.complete("PREM");
      log.debug("timer done");
    });

    var future = promise.future();

    future
        .map(element -> new JsonObject().put("key", element))
        .map(json_object -> new JsonArray().add(json_object))
        .onSuccess(suc -> {
          context.completeNow();

          System.out.println(suc);
        });
  }

  @Test
  void future_coordination(Vertx vertx, VertxTestContext context)
  {
    vertx.createHttpServer()
        .requestHandler(_ -> System.out.println("HAHAH"))
        .listen(10000)
        .compose(server -> {
          return Future.succeededFuture(server);
        })
        .compose(sever -> {
          return Future.succeededFuture(sever);
        })
        .onFailure(context::failNow)
        .onSuccess(_ -> {
          context.completeNow();
        });


  }

  void future_composition_all(Vertx vertx, VertxTestContext context)
  {
    var one = Promise.<Void>promise();
    var two = Promise.<Void>promise();
    var thr = Promise.<Void>promise();

    var f1 = one.future();
    var f2 = two.future();
    var f3 = thr.future();


    Future.all(f1, f2, f3)
        .onFailure(context::failNow)
        .onSuccess((_) -> context.completeNow());

    vertx.setTimer(1000, (_) -> {
      one.complete();
      two.complete();
      thr.complete();
      // thr.fail("LOL");
    });

  }

  @Test
  void future_composition_any(Vertx vertx, VertxTestContext context)
  {
    var one = Promise.<Void>promise();
    var two = Promise.<Void>promise();
    var thr = Promise.<Void>promise();

    var f1 = one.future();
    var f2 = two.future();
    var f3 = thr.future();


    Future.any(f1, f2, f3)
        .onFailure(context::failNow)
        .onSuccess((_) -> context.completeNow());

    vertx.setTimer(1000, (_) -> {
      one.complete();
      two.fail("FAIL");
      thr.fail("FAIL");
    });

  }

}
