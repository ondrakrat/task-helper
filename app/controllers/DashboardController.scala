package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{Action, Controller}
import services.{CategoryService, TaskService}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * @author Ondřej Kratochvíl
  */
@Singleton
class DashboardController @Inject() extends Controller with Timeout {

  def index(showFinished: Option[Boolean] = Some(false)) = Action { implicit request =>
    val categories = Await.result(CategoryService.listAll, timeoutInterval)
    val tasks = Await.result(if (showFinished.getOrElse(false)) TaskService.listAll else TaskService.listUnfinished, timeoutInterval)
    Ok(views.html.dashboard(categories, tasks))
  }
}
