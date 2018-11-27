package utils

import java.security.SecureRandom

import model.internal.HashedPassword
import org.bouncycastle.crypto.generators.OpenBSDBCrypt
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
}
