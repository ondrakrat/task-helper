package models

import play.api.data.Form
import play.api.data.Forms._
import slick.lifted.Tag
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * @author Ondřej Kratochvíl
  */
case class Task(id: Long, text: String, finished: Boolean)

case class TaskFormData(id: Long, text: String, finished: Boolean)

object TaskForm {

  val form = Form(
    mapping(
      "id" -> longNumber,
      "text" -> nonEmptyText,
      "finished" -> boolean
    )(TaskFormData.apply)(TaskFormData.unapply)
  )
}

class TaskTable(tag: Tag) extends Table[Task](tag, "task") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("text")

  def finished = column[Boolean]("finished")

  def * = (id, name, finished) <>(Task.tupled, Task.unapply)
}