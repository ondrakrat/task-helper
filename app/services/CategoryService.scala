package services

import models.{Category, CategoryTable, DatabaseConnection}
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author Ondřej Kratochvíl
  */
object CategoryService {

  val db = DatabaseConnection.connection
  val categories = TableQuery[CategoryTable]

  def add(category: Category): Future[String] = {
    db.run(categories += category).map(res => "Category successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def saveOrUpdate(category: Category): Future[String] = {
    if (category.id == 0) {
      add(category)
    } else {
      db.run(categories.filter(_.id === category.id).update(category))
        .map(res => s"Category $category.id successfully updated")
        .recover {
          case ex: Exception => ex.getCause.getMessage
        }
    }
  }

  def delete(id: Long): Future[Int] = {
    db.run(categories.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[Category]] = {
    db.run(categories.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Category]] = {
    db.run(categories.result)
  }
}
