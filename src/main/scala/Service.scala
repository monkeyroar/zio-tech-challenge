package com.daniel

import Domain.Data

object Service {

  private val windowWidth = 5 //seconds

  def updateState(newData: Data, prevState: Seq[Data]): Seq[Data] = filterWindow(prevState :+ newData)

  def calculateFrequency(state: Seq[Data]): Map[String, Map[String, Int]] =
    filterWindow(state).groupBy(_.eventType).mapValues(_.groupBy(_.data).mapValues(_.size))

  private def filterWindow(state: Seq[Data]): Seq[Data] = {
    val currentTimestamp = System.currentTimeMillis() / 1000
    state.dropWhile(currentTimestamp - _.timestamp > windowWidth)
  }
}
