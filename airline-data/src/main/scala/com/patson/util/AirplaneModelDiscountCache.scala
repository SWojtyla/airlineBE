package com.patson.util

import com.patson.data.airplane.ModelSource
import com.patson.model.airplane.{Model, ModelDiscount}


object AirplaneModelDiscountCache {
  import com.google.common.cache.{CacheBuilder, CacheLoader, LoadingCache}
  import scala.jdk.CollectionConverters._

  val simpleCache: LoadingCache[Int, List[ModelDiscount]] = CacheBuilder.newBuilder.maximumSize(1000).build(new SimpleLoader())

  simpleCache.putAll(ModelSource.loadAllModelDiscounts().groupBy(_.modelId).asJava) //load everything once


  def getAllModelDiscounts() = {
    simpleCache.asMap().asScala
  }

  def getModelDiscount(modelId : Int) = {
    simpleCache.get(modelId)
  }

  class SimpleLoader() extends CacheLoader[Int, List[ModelDiscount]] {
    override def load(modelId : Int) : List[ModelDiscount] = {
      ModelSource.loadModelDiscountsByModelId(modelId)
    }
  }

  def updateModelDiscounts(modelDiscounts : List[ModelDiscount]) = {
    modelDiscounts.groupBy(_.modelId).foreach {
      case (modelId, discounts) => simpleCache.put(modelId, discounts)
    }

  }

}



