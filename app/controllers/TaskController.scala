package controllers

import javax.inject.{Inject, Singleton}

import models.{Task, TaskForm}
import play.api.mvc.{Action, Controller}
import services.{CategoryService, TaskService}

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

  def newTask = Action.async { implicit request =>
    CategoryService.listAll map { categories =>
      Ok(views.html.task.add(TaskForm.form, categories))
    }
  }

  def addTask() = Action.async { implicit request =>
    TaskForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => {
        Future.successful(BadRequest(views.html.task.add(errorForm, null))) // toDo: fix
      },
      data => {
        val newTask = Task(data.id, data.text, data.finished, data.categoryId)
        TaskService.saveOrUpdate(newTask).map(res =>
          Redirect(routes.TaskController.index())
        )
      })
  }
}
