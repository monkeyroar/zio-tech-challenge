package com.daniel

import Domain.{Data, State}

object Service {

  private val windowWidth = 5 //seconds

  def updateState(newData: Data, prevState: State): State = {
    val currentTimestamp = System.currentTimeMillis() / 1000
    prevState.copy(window = (prevState.window :+ newData).filter(currentTimestamp - _.timestamp < windowWidth))
  }

  def calculateFrequency(state: State): Map[String, Map[String, Int]] = {
    state.window.groupBy(_.eventType).mapValues(_.groupBy(_.data).mapValues(_.size))
  }
}
