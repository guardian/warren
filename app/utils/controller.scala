import play.api.mvc.Security.AuthenticatedRequest

package object utils {
  type AuthRequest[A] = AuthenticatedRequest[A, User]
}
