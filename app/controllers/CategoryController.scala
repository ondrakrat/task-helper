package controllers

import javax.inject.{Inject, Singleton}

import models.{Categories, Category, CategoryForm, CategoryFormData}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author Ondřej Kratochvíl
  */
@Singleton
class CategoryController @Inject() extends Controller {

  def index = Action.async { implicit request =>
    Categories.listAll map { categories =>
      Ok(views.html.category.list(categories))
    }
  }

  def newCategory = Action { implicit request =>
    Ok(views.html.category.add(CategoryForm.form))
  }

  def editCategory(id: Long) = Action.async { implicit  request =>
    Categories.get(id) map { res =>
      val category = res.getOrElse(throw new IllegalArgumentException(s"Entity $id was not found."))
      val categoryForm = CategoryForm.form.fill(new CategoryFormData(category.id, category.name))
      Ok(views.html.category.add(categoryForm))
    }
  }

  def addCategory() = Action.async { implicit request =>
    CategoryForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => Future.successful(Ok(views.html.category.add(errorForm))),
      data => {
        val newCategory = Category(data.id, data.name)
        Categories.saveOrUpdate(newCategory).map(res =>
          Redirect(routes.CategoryController.index())
        )
      })
  }

  def delete(id: Long) = Action.async { implicit  request =>
    Categories.delete(id) map { res =>
      Redirect(routes.CategoryController.index())
    }
  }
}
