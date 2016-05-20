package controllers

import javax.inject.{Inject, Singleton}

import models.{Task, TaskForm}
import play.api.mvc.{Action, Controller}
import services.TaskService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author Ondřej Kratochvíl
  */
@Singleton
class TaskController @Inject() extends Controller {

  def index = Action.async { implicit request =>
    TaskService.listAll map { tasks =>
      Ok(views.html.task.list(tasks))
    }
  }

  def newTask = Action { implicit request =>
    Ok(views.html.task.add(TaskForm.form))
  }

  def addTask() = Action.async { implicit request =>
    TaskForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => Future.successful(Ok(views.html.task.add(errorForm))),
      data => {
        val newTask = Task(data.id, data.text, data.finished)
        TaskService.saveOrUpdate(newTask).map(res =>
          Redirect(routes.TaskController.index())
        )
      })
  }
}
