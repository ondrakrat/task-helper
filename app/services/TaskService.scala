package services

import models.{DatabaseConnection, Task, TaskTable}
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * @author Ondřej Kratochvíl
  */
object TaskService {

  val db = DatabaseConnection.connection
  val tasks = TableQuery[TaskTable]

  def add(task: Task): Future[String] = {
    db.run(tasks += task)
      .map(res => "Task successfully added")
      .recover {
        case ex: Exception => ex.getCause.getMessage
      }
  }

  def saveOrUpdate(task: Task): Future[String] = {
    if (task.id == 0) {
      add(task)
    } else {
      db.run(tasks
        .filter(_.id === task.id)
        .update(task)
      )
        .map(res => s"Task $task.id successfully updated")
        .recover {
          case ex: Exception => ex.getCause.getMessage
        }
    }
  }

  def delete(id: Long): Future[Int] = {
    db.run(tasks.filter(_.id === id).delete)
  }

  def deleteForCategory(categoryId: Long): Future[String] = {
    db.run(tasks
      .filter(_.categoryId === categoryId)
      .delete)
      .map(res => s"Tasks in category $categoryId were successfully deleted")
      .recover {
        case ex: Exception => ex.getCause.getMessage
      }
  }

  def get(id: Long): Future[Option[Task]] = {
    db.run(tasks
      .filter(_.id === id)
      .result.headOption)
  }

  def listAll: Future[Seq[Task]] = {
    db.run(tasks
      .sortBy(_.id).result)
  }

  def listUnfinished: Future[Seq[Task]] = {
    db.run(tasks
      .filter(!_.finished)
      .sortBy(_.id)
      .result)
  }
}
