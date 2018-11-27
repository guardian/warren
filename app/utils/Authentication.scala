package utils

import java.security.SecureRandom

import utils.attempt.{Attempt, LoginFailure}

object Authentication {
  private val COST_FACTOR = 14  // this seems reasonable for now (1-2 seconds to run)
  private val SALT_LENGTH = 16

  private val secureRandom = new SecureRandom()

  private def genSalt(): Array[Byte] = {
    val salt = new Array[Byte](SALT_LENGTH)
    secureRandom.nextBytes(salt)
    salt
  }

  def hashPassword(password: String): HashedPassword = {
    val salt = genSalt()
    val hash = OpenBSDBCrypt.generate(password.toCharArray, salt, COST_FACTOR)
    HashedPassword(hash)
  }

  def checkLogin(providedPassword: String, user: DBUser): Attempt[Unit] = {
    if (user.registered) {
      user.password.map { dbPassword =>
        if (OpenBSDBCrypt.checkPassword(dbPassword.hash, providedPassword.toCharArray)) {
          Attempt.Right(())
        } else {
          println(dbPassword)
          Attempt.Left[Unit](LoginFailure("Incorrect password"))
        }
      }.getOrElse(Attempt.Left[Unit](LoginFailure(s"Attempt to log in as ${user.username} but that user is not managed by laundrette")))
    } else {
      Attempt.Left[Unit](LoginFailure("User not registered"))
    }
  }
}
