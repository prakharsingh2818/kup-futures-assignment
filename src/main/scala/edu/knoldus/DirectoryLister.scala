package edu.knoldus

import java.io.File
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps

class DirectoryLister {

  def listDirectories(path: String): Future[List[String]] = {
    def listDirectoriesHelper(path: String): List[String] = {
      new File(path).listFiles.flatMap(file => {
        {
          if (file.isDirectory) file.getPath +: listDirectoriesHelper(file.getPath)
          else List(file.getPath)
        }
      }).toList
    }

    Future {
      if (new File(path).exists()) listDirectoriesHelper(path)
      else List()
    }
  }
}

