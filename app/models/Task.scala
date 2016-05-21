package models

import play.api.data.Form
import play.api.data.Forms._
import slick.lifted.Tag
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * @author Ondřej Kratochvíl
  */
case class Task(id: Long, text: String, finished: Boolean, categoryId: Long)

case class TaskFormData(id: Long, text: String, finished: Boolean, categoryId: Long)

object TaskForm {

  val form = Form(
    mapping(
      "id" -> longNumber,
      "text" -> nonEmptyText,
      "finished" -> boolean,
      "category" -> longNumber
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