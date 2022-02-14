package edu.knoldus

import java.io.File
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success}

class DirectoryLister {

  /**
   * Working (Without Futures)
   */
  /*  def listDirectories(path: String): List[String] = {
      new File(path).listFiles.flatMap(file => {
        file.getPath +: {
          if (file.isDirectory) listDirectories(file.getPath)
          else List(file.getPath)
        }
      }).toList
    }*/

  /*   def listDirectories(path: String): Future[List[List[String]]] = {
       val finalResult: Future[List[List[String]]] = Future.sequence(new File(path).listFiles.map(file => {
         val futureResult: Future[List[String]] = Future {
           val result: List[String] = file.getPath +: {
             if (file.isDirectory) listDirectories(file.getPath).value.get.get.flatten
             else List(file.getPath)
           }
           result
           }
         futureResult
       }).toList)
       finalResult
     }*/
  //println(listDirectories("src"))

  /*def listDirectories(path: String): Future[List[String]] = {
    val result: Future[List[List[String]]] = Future.sequence(new File(path).listFiles.map(file => {
      val futureResult: Future[List[String]] = Future {
        val result: List[String] = file.getPath +: {
          if (file.isDirectory) listDirectories(file.getPath).value.get.get
          else List(file.getPath)
        }
        result
      }
      futureResult
    }).toList)
    for {
      response: List[List[String]] <- result
    } yield {
      println("RESULT: "+ response.flatten)
      response.flatten
    }
  }

  println(listDirectories("src"))*/

  /*def listDirectories(path: String): List[String] = {
    val result: Future[List[List[String]]] = Future.sequence(new File(path).listFiles.map(file => {
      val futureResult: Future[List[String]] = Future {
        val result: List[String] = file.getPath +: {
          if (file.isDirectory) listDirectories(file.getPath)
          else List(file.getPath)
        }
        result
      }
      futureResult
    }).toList)
    val finalResponse: Future[List[List[String]]] = for {
      response <- result
    } yield {
      response
    }
  finalResponse.value.get.get.head
  }*/
  /*def listDirectories(path: String): List[String] = {
    new File(path).listFiles.flatMap(file => {
      val result: Future[List[String]] = Future {
        file.getPath +: {
          if (file.isDirectory) listDirectories(file.getPath)
          else List(file.getPath)
        }
      }
      result.value match {
        case Some(value) => value.get
        case None => throw new Exception("Invalid Path")
      }
    }).toList

  }*/

  /*def listDirectories(path: String): Future[List[String]] = {
    new File(path).listFiles.flatMap(file => Future {
      val result: List[String] = {
        val superList: List[String] = List(file.getPath)
        val subList: Future[List[String]] = {
          if (file.isDirectory) listDirectories(file.getPath)
          else Future(List(file.getPath))
        }
        {
          for {
            innerList <- subList
          } yield (superList ::: innerList)
        }.value.get.get
      }
      result.value match {
        case Some(value) => value.get
        case None => throw new Exception("Invalid Path")
      }
    }).toList

  }*/

  //  println(listDirectories("src"))

  /*def listDirectories(path: String): List[String] = {
    val result: List[List[String]] = new File(path).listFiles.map(file => {
      val futureResult: List[String] =  {
        val result: List[String] = file.getPath +: {
          if (file.isDirectory) listDirectories(file.getPath)
          else List(file.getPath)
        }
        result
      }
      futureResult
    }).toList
    val finalResponse: List[List[String]] = for {
      response: List[String] <- result
    } yield {
       response
    }
    finalResponse.flatten
  }
  println(listDirectories("src"))*/

  /*def listDirectories_v1(path: String): List[String] = {
    val result: Future[List[List[String]]] = Future.sequence(new File(path).listFiles.map(file => {
      val futureResult: Future[List[String]] = Future {
        val result: List[String] = file.getPath +: {
          if (file.isDirectory) listDirectories_v1(file.getPath)
          else List(file.getPath)
        }
        result
      }
      futureResult
    }).toList)
  }*/

  /*def listDirectories_v2(path: String): List[String] = {
    new File(path).listFiles.flatMap(file => {
      Await.result(Future {
        //println(Thread.currentThread().getName)
        file.getPath +: {
          if (file.isDirectory) listDirectories_v2(file.getPath)
          else List(file.getPath)
        }
      }, 1 seconds)
    }).toList
  }*/
  //println(listDirectories_v1("src/main/scala").mkString("\n"))
  //println(listDirectories_v2("src").mkString("\n"))

  /*def listDirectories(path: String): List[String] = {
    new File(path).listFiles.flatMap(file => {
      file.getPath +: {
        if (file.isDirectory) listDirectories(file.getPath)
        else List(file.getPath)
      }
    }).toList
  }*/

  /* def listContents(path: String): Future[List[String]] = {
     new File(path).listFiles().flatMap(file => {
       val contentList: List[String] = {
         //println(file.getName + " is a directory: "+file.isDirectory)
         if (file.isDirectory) {
           //println("Inside "+file.getName)
           val subDirsList =  {
             for {
               list <- Future(listContents(file.getPath))
             } yield {list :+ file.getPath}
           }
           subDirsList
         }
         else {
           Future(List(file.getPath))
         }
       }
       contentList
     }).toList
   }*/
  //listContents("src/main/scala/")
  //  println(listContents("src").mkString("\n"))

  def listContentsFuture(path: String): List[String] = {
    new File(path).listFiles().flatMap(file => {
      val contentList: List[String] = {
        //println(file.getName + " is a directory: "+file.isDirectory)
        if (file.isDirectory) {
          //println("Inside "+file.getName)
          val subDirsList = Future {
            listContentsFuture(file.getPath)
          }
          //file.getPath +: Await.result(subDirsList, 1 seconds)
          //Await.result(subDirsList.map(s => file.getPath +: s), 1 seconds)
          val result: Future[List[String]] = for {
            list <- subDirsList
          } yield {
            list :+ file.getPath
          }
          result.onComplete {
            case Failure(_) => throw new Exception("Invalid Path!!!")
            case Success(value) => //println(value)

          }
          //subDirsList.value.get.get :+ file.getPath
          Await.result(subDirsList.map(s => file.getPath +: s), 1 seconds)
          //Thread.sleep(3000)
          //subDirsList.map(s => file.getPath +: s).value.get.get
        }
        else {
          List(file.getPath)
        }
      }
      contentList
    }).toList
  }

  /* def listContentsFutureResult(path: String): Future[List[List[String]]] = {
     val finalResult: List[Future[List[String]]] = new File(path).listFiles().map(file => {
       val contentList: Future[List[List[String]]] = Future {
         //println(file.getName + " is a directory: "+file.isDirectory)
         if (file.isDirectory) {
           //println("Inside "+file.getName)
           val subDirsListFuture = listContentsFutureResult(file.getPath)
           val check: Future[List[List[String]]] = for {
             subDirsList <- subDirsListFuture
           } yield {
             subDirsList.map(s => s :+ file.getPath)
           }
          Await.result(check, 3 seconds)
         }
         else {
           (List(List(file.getPath)))
         }
       }
       contentList
       //Await.result(contentList, 1 seconds)
     }).toList
     Future.sequence(finalResult)
   }*/

  //println(listContentsFutureResult("src"))

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
      if(new File(path).exists()) listDirectoriesHelper(path)
      else List()
    }
  }

  //println(Await.result(listDirectories("cr"), 2 seconds).mkString("\n"))

  /*def listDirectoriesFuture(path: String): Future[List[String]] = {

    def listDirs(file: File): List[String] = {
      if (file.isDirectory) {
        val subFiles: List[File] = file.listFiles().toList
        subFiles.flatMap(subFile => subFile.getPath +: listDirs(subFile))
      }
      else List(file.getPath)
    }

    Future(new File(path).listFiles.toList.flatMap(file => {
      List(file.getPath) ::: listDirs(file)
    }))
  }*/
  //println(Await.result(listDirectoriesFuture("src"), 2 seconds).mkString("\n"))
}

