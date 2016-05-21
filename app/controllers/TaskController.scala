package controllers

import javax.inject.{Inject, Singleton}

import models.{Task, TaskForm, TaskFormData}
import play.api.mvc.{Action, Controller}
import services.{CategoryService, TaskService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * @author Ondřej Kratochvíl
  */
@Singleton
class TaskController @Inject() extends Controller {

  def index = Action.async { implicit request =>
    TaskService.listAll map { tasks =>
      Ok(views.html.task.list(tasks)) // toDo: display category names
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
        Future.successful(BadRequest(views.html.task.add(errorForm,
          Await.result(CategoryService.listAll, Duration(1000, "millis")))))
      },
      data => {
        val newTask = Task(data.id, data.text, data.finished, data.categoryId)
        TaskService.saveOrUpdate(newTask).map(res =>
          Redirect(routes.TaskController.index())
        )
      })
  }

  def editTask(id: Long) = Action.async { implicit  request =>
    TaskService.get(id) map { res =>
      val task = res.getOrElse(throw new IllegalArgumentException(s"Entity $id was not found."))
      val taskForm = TaskForm.form.fill(new TaskFormData(task.id, task.text, task.finished, task.categoryId))
      Ok(views.html.task.add(taskForm, Await.result(CategoryService.listAll, Duration(1000, "millis"))))
    }
  }
}
