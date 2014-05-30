package com.company.product.project

import com.wordnik.swagger.annotations.{ApiModelProperty, ApiModel}
import scala.annotation.meta.field

@ApiModel(description = "Class Description")
case class MyAnnotClass(@(ApiModelProperty @field)(value="Member Description") myMember: List[String])
