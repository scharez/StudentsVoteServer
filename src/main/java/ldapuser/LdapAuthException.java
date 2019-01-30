package ldapuser;

/**
 * Esception, die anzeigt, dass die Authorisierung über LDAP fehlgeschlagen ist.
 */
public class LdapAuthException extends Exception {

  /**
   * Constructs an instance of <code>LdapUserPasswordException</code> with the
   * specified detail message.
   *
   * @param msg the detail message.
   */
  public LdapAuthException(String msg) {
    super(msg);
  }
}
