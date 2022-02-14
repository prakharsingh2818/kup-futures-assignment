package edu.knoldus

import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps


class DirectoryListerTest extends AnyFlatSpec {

  val directoryLister = new DirectoryLister

  val pathOne = "src"
  val pathTwo = "src/main"
  val pathThree = "src/test"
  val invalidPath = "invalid"

  val resultOne = List("src/test", "src/test/scala", "src/test/scala/edu", "src/test/scala/edu/knoldus", "src/test/scala/edu/knoldus/DirectoryListerTest.scala", "src/main", "src/main/scala", "src/main/scala/edu", "src/main/scala/edu/knoldus", "src/main/scala/edu/knoldus/DirectoryLister.scala")
  val resultTwo: List[String] = List("src/main/scala", "src/main/scala/edu", "src/main/scala/edu/knoldus", "src/main/scala/edu/knoldus/DirectoryLister.scala")
  val resultThree: List[String] = List("src/test/scala", "src/test/scala/edu", "src/test/scala/edu/knoldus", "src/test/scala/edu/knoldus/DirectoryListerTest.scala")

  "listDirectories" should "list directories of src" in {
    val actualResultOne: List[String] = Await.result(directoryLister.listDirectories(pathOne), 3 seconds)
    assertResult(resultOne)(actualResultOne)

  }
  it should "list directories of src/main" in {
    val actualResultTwo: List[String] = Await.result(directoryLister.listDirectories(pathTwo), 3 seconds)
    assertResult(resultTwo)(actualResultTwo)
  }

  it should "list directories of src/test" in {
    val actualResultThree: List[String] = Await.result(directoryLister.listDirectories(pathThree), 3 seconds)
    assertResult(resultThree)(actualResultThree)
  }

  it should "return an empty list if path is invalid" in {
    val actualResultFour: List[String] = Await.result(directoryLister.listDirectories(invalidPath), 3 seconds)
    assert(actualResultFour.isEmpty)
  }

}

