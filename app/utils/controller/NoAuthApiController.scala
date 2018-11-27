package utils.controller

import play.api.mvc.ControllerComponents

abstract class NoAuthApiController(val controllerComponents: ControllerComponents) extends AbstractApiController(controllerComponents.actionBuilder)
