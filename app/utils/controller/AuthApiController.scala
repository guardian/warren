package utils.controller

import play.api.mvc.ControllerComponents

abstract class AuthApiController(authActionBuilder: AuthActionBuilder, val controllerComponents: ControllerComponents) extends AbstractApiController(authActionBuilder)
