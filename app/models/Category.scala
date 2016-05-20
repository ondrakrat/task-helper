package models

import play.api.data.Form
import play.api.data.Forms._
import slick.driver.PostgresDriver.api._
import slick.lifted.Tag

/**
  * @author Ondřej Kratochvíl
  */
case class Category(id: Long, name: String)

case class CategoryFormData(id: Long, name: String)

object CategoryForm {

  val form = Form(
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText
    )(CategoryFormData.apply)(CategoryFormData.unapply)
  )
}

class CategoryTable(tag: Tag) extends Table[Category](tag, "category") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def * = (id, name) <>(Category.tupled, Category.unapply)
}