# Book-Service

Eine Anwendung um Bücher durch eine REST-API zu verwalten.

##  API Beschreibung:
<b>POST  /books</b>
<p>
erstellt ein neues Buch durch posten eines Buch-Objekts im Body Bspw. : {"name" : "theName", "title" : "theTitle", price : "10.00" } <br>
Alle 3 Parameter dürfen nicht Null sein, damit der Eintrag gespeichert wird.
</p>
<br>
<b>GET /books</b>
<p>
 Gibt alle Bücher zurück.
</p>
<br>
<b> GET  /books/{id}</b>
<p>
Gibt das Buch mit der angegebenen Id zurück, falls vorhanden. Sonst null.  
</p>
<br>
<b> GET  /books/title?title={Wort}</b>
<p>
Gibt alle gespeicherten Bücher zurück, deren Titel das gegebene Wort enthalten ( title LIKE %Wort% ).  
</p>
<br>
<b> DELETE /books/{id}</b>
<p>
Löscht das Buch mit der angegebenen Id.  
</p>

## Starten

<b>Zum Starten mit JDK 21 :</b><br>
$ mvn clean package<br>
$ docker-compose up 

Bei dem ersten Start wird die Spring-Anwendung mehrmals erfolglos starten, bis die Datenbank fertig initialisiert ist.  

<b>Zum Entfernen : </b><br>
$ docker-compose down -v <br>
$ docker rmi bookservice-backend mysql

## Funktionalität
Die spezifizierten Anforderungen können über die Unit-Tests eingesehen werden. Siehe BookServiceApplicationTests.
Es wurde zusätzlich mit Postman getestet.

Folgende Annahmen wurden getroffen, welche in den Anforderungen nicht näher spezifiziert wurden:
- Alle Preise sind in der gleichen Währung.
- "Ein Buch hat mindestens folgende Attribute : Titel, Autor, Preis", bedeutet diese Attribute müssen unbedingt vorhanden sein.

Da der Service eine relationale DatenBank nutzen soll, wurde diese in Docker Compose zu Ansichtszwecken bereitgestellt.
