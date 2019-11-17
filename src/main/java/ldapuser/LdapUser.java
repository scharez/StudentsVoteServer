package ldapuser;

import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;
import static javax.naming.directory.SearchControls.*;

/**
 * LDAP-User.
 * <br>
 * Die Attributtswerte der Objekte werden allesamt aus LDAP abgeleitet und sind
 * hier unveränderbar. Der Zugriff erfolgt verschlüssel, weshalb vorab einige
 * Tätigkeiten durchzuführen sind.
 * <code>
 * <br> 1.a Standard-Keystore in c://yourfullpathtoJRE/lib/security/cacerts
 * verwenden
 * <br> 1.b ODER eigenen Keystore erstellen
 * <br> keytool -genkey -keystore S:\0pgm\res\keystore.bin
 * <br> 2. Zertifikat in den Keystore importieren
 * <br> keytool -import -keystore S:\0pgm\res\keystore.bin -alias htlleonding
 * -file S:\0pgm\res\certs\htledvoca16.cer
 * <br> 3. kontrollieren, ob das Zertifikat jetzt im Keystore vorhenden ist
 * <br> keytool -list -keystore S:\0pgm\res
 * <br>
 * <br> Und dann in jedem Projekt:
 * <br> 4.a Keystore dem Projekt bekanntgeben
 * <br> Projekt ... re. Maus ... Properties ... Run
 * <br> -Djavax.net.ssl.keyStore="S:\0pgm\res\keystore.bin"
 * -Djavax.net.ssl.keyStorePassword=yourpassword
 * <br> bzw
 * <br> -Djavax.net.ssl.keyStore="c:\\yourfullpathtoJR\lib\security\cacerts"
 * -Djavax.net.ssl.keyStorePassword=changeit
 * <br> 4.b ODER Keystore zur Laufzeit inkludieren (funktionierte zuletzt
 * nicht!)
 * <br> System.setProperty("javax.net.ssl.trustStore",
 * "S:\\0pgm\\res\\keystore.bin");
 * <br> bzw
 * <br> System.setProperty("javax.net.ssl.trustStore",
 * "c:\\\\yourfullpathtoJRE\\lib\\security\\cacerts");
 * </code>
 */
public final class LdapUser {

  private String userId;
  //private String password; NICHT speichern!!!
  private String firstname;
  private String lastname;
  private String classId;

  public final String domain = "edu.htl-leonding.ac.at";
  public final String server = "addc01.edu.htl-leonding.ac.at";
  public final String baseDN = "ou=HTL,dc=edu,dc=htl-leonding,dc=ac,dc=at";
  public final int port = 636;

  /**
   * Konstruktor.
   * <br>
   * Initialisiert das Objekt, in dem es die Information aus LDAP lädt.
   *
   * @param userId LDAP-UserId
   * @param password LDAP-Passwort als char[], weil Strings im Hauptspeicher
   * gecachet werden un damit ausgelesen werden könnten. Bitte achten sie auch
   * darauf, dass sie in der restlichen Anwendung Passwörter nie als Strings
   * speichern.
   *
   * @throws LdapException wenn der Zugriff auf LDAP nicht klappt
   * @throws LdapAuthException wenn User/Password in LDAP nicht existiert
   */

  public LdapUser(String userId, char[] password) throws LdapException, LdapAuthException {
    this.setUserId(userId);

    // Zertifikat 
    System.setProperty("javax.net.ssl.trustStore", "/home/scharinger/keystore/keystore.bin");

    // LDAP-Properties
    Hashtable props = new Hashtable();
    props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    props.put(Context.PROVIDER_URL, "ldaps://" + server + ":" + port);
    props.put(Context.SECURITY_PRINCIPAL, userId + "@" + domain);
    props.put(Context.SECURITY_CREDENTIALS, password);

    try {
      // Authentifizierungsversuch
      DirContext context = new InitialDirContext(props);

      // LDAP-Tree durchsuchen
      SearchControls controls = new SearchControls();
      controls.setSearchScope(SUBTREE_SCOPE);
      NamingEnumeration<SearchResult> renum = context.search(baseDN,
              "cn=" + userId, controls);

      // ...wenn User nicht gefunden, dann...
      if (!renum.hasMore()) {
        throw new LdapException("Cannot locate user information for " + userId);
      }

      // Einige Userinfos ermitteln und ausgeben
      SearchResult result = renum.next();

      // Vor- und Nachname
      // displayName: Widmann Manfred
      String displayname[] = result.getAttributes().get("displayname").toString().split(" ");
      this.setFirstname(displayname[2]);
      this.setLastname(displayname[1]);

      // User, Group, etc...
      //Schüler: distinguishedName: CN=it150178,OU=4AHITM,OU=IT,OU=Students,OU=HTL,DC=EDU,DC=HTL-LEONDING,DC=AC,DC=AT
      //Lehrer:  distinguishedName: CN=m.widmann,OU=Teachers,OU=HTL,DC=EDU,DC=HTL-LEONDING,DC=AC,DC=AT
      String distinguishedname[] = result.getAttributes().get("distinguishedname").toString().split(" ");
      if (distinguishedname[1].contains("Teacher")) {
        // Teacher
        this.setClassId(null);
      }
      else {
        String attributes[] = distinguishedname[1].split(",");

        for (String attribute : attributes) {
          if (attribute.startsWith("OU")) {
            String keyValue[] = attribute.split("=");
            if (keyValue[1].charAt(0) - (char) '0' >= 0
                    && keyValue[1].charAt(0) - (char) '0' <= 9) {
              this.setClassId(keyValue[1]);
            }
          }
        }
      }

      // LDAB-Abfrage schließen
      context.close();
    }
    catch (AuthenticationException a) {
      throw new LdapAuthException("Authentication failed for " + userId);
    }
    catch (NamingException e) {
      throw new LdapException("Failed to bind to LDAP / get account information: " + e);
    }
  }

  /**
   * Ist der User ein Schüler?
   *
   * @return true, wenn Schüler
   */
  public boolean isStudent() {
    return classId != null;
  }

  /**
   * Ist der User ein Lehrer?
   *
   * @return true, wenn Lehrer
   */
  public boolean isTeacher() {
    return !isStudent();
  }

  private void setUserId(String userId) {
    this.userId = userId;
  }

  private void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  private void setLastname(String lastname) {
    this.lastname = lastname;
  }

  private void setClassId(String classId) {
    this.classId = classId;
  }

  /**
   * Liefert die LDAP-UserId.
   *
   * @return LDAP-UserId
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Liefert den Vornamen aus LDAP.
   *
   * @return Vornamen aus LDAP
   */
  public String getFirstname() {
    return firstname;
  }

  /**
   * Liefert den Nachnamen aus LDAP.
   *
   * @return Nachnamen aus LDAP
   */
  public String getLastname() {
    return lastname;
  }

  /**
   * Liefert für Schüler den Klassennamen aus LDAP; für Lehrer ist diese Info
   * null.
   *
   * @return Klassennamen aus LDAP, Schüler; null, sonst.
   */
  public String getClassId() {
    return classId;
  }

}
