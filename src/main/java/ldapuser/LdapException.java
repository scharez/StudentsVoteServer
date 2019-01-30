package ldapuser;

/**
 * Exception, die einen Fehler bei der Verbindung mit oder beim Zugriff auF LDAP
 * anzeigt.
 */
public class LdapException extends Exception {

  /**
   * Constructs an instance of <code>LdapUserException</code> with the specified
   * detail message.
   *
   * @param msg the detail message.
   */
  public LdapException(String msg) {
    super(msg);
  }
}
