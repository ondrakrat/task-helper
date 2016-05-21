package models

import org.joda.time.{DateTime, LocalDate}
import play.api.data.Form
import play.api.data.Forms._
import services.CategoryService
import slick.lifted.Tag
import slick.driver.PostgresDriver.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * @author Ondřej Kratochvíl
  */
case class Task(id: Long, text: String, finished: Boolean, categoryId: Long) {

  lazy val category = {
    Await.result(CategoryService.get(categoryId), Duration(1000, "millis"))
      .getOrElse(throw new IllegalArgumentException(s"Category $categoryId was not found"))
  }
}

case class TaskFormData(id: Long, text: String, finished: Boolean, categoryId: Long)

object TaskForm {

  val form = Form(
    mapping(
      "id" -> longNumber,
      "text" -> nonEmptyText,
      "finished" -> boolean,
      "categoryId" -> longNumber
    )(TaskFormData.apply)(TaskFormData.unapply)
  )
}

class TaskTable(tag: Tag) extends Table[Task](tag, "task") {

  val categories = TableQuery[CategoryTable]

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("text")

  def finished = column[Boolean]("finished")

  def categoryId = column[Long]("category")

  def category = foreignKey("category_fk", categoryId, categories)(_.id)

  def * = (id, name, finished, categoryId) <>(Task.tupled, Task.unapply)
}