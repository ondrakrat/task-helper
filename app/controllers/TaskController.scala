package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{Action, Controller}

/**
  * @author Ondřej Kratochvíl
  */
@Singleton
class TaskController @Inject() extends Controller {

  def index = Action {
    Ok(views.html.task())
  }
}
