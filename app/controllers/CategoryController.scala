package controllers

import javax.inject.{Inject, Singleton}

import services.{CategoryService, TaskService}
import models.{Category, CategoryForm, CategoryFormData}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * @author Ondřej Kratochvíl
  */
@Singleton
class CategoryController @Inject() extends Controller with Timeout {

  def index = Action.async { implicit request =>
    CategoryService.listAll map { categories =>
      Ok(views.html.category.list(categories))
    }
  }

  def newCategory = Action { implicit request =>
    Ok(views.html.category.add(CategoryForm.form))
  }

  def editCategory(id: Long) = Action.async { implicit  request =>
    CategoryService.get(id) map { res =>
      val category = res.getOrElse(throw new IllegalArgumentException(s"Entity $id was not found."))
      val categoryForm = CategoryForm.form.fill(new CategoryFormData(category.id, category.name))
      Ok(views.html.category.add(categoryForm))
    }
  }

  def addCategory() = Action.async { implicit request =>
    CategoryForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => Future.successful(BadRequest(views.html.category.add(errorForm))),
      data => {
        val newCategory = Category(data.id, data.name)
        CategoryService.saveOrUpdate(newCategory).map(res =>
          Redirect(routes.CategoryController.index())
        )
      })
  }

  def delete(id: Long) = Action.async { implicit  request =>
    async(TaskService.deleteForCategory(id))
    CategoryService.delete(id) map { res =>
      Redirect(routes.CategoryController.index())
    }
  }
}
