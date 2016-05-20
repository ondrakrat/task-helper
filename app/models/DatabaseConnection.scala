package models

import slick.driver.PostgresDriver.api._

/**
  * @author Ondřej Kratochvíl
  */
object DatabaseConnection {
  val connection = Database.forURL("jdbc:postgresql://localhost:5432/taskhelper", "postgres", "postgres")
}
