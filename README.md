JTechLog Wait4Signal
====================

Ez a program a JTechLog (<http://jtechlog.blogspot.com>) blog "Instrumentation Javassist-tal" cikkéhez készült 
példaprogram. Prezentálja az instrumentációt, valamint a Javassist keretrendszer használatát. 

A program egy Java agent, melynek segítségével egy Java program 
adott szálának futása a megadott metódusnál felfüggeszthető, és vagy konzolon bevitt, vagy JMX-en
feladott jelre vár. Timeout-ot is lehet megadni, ekkor a megadott idő elteltével jel nélkül is továbbfut a 
program. Különösen hasznos lehet, ha pl. VisualVM-mel akarunk az alkalmazásunkhoz csatlakozni, és már az alkalmazás
futásának elejétől szeretnénk monitorozni. Ekkor annak main metódusa előtt kell a jelre várni.

Maven-nel build-elhető, és a letöltést követően a 'mvn package assembly:single' parancs kiadásával a
target könyvtárban létrejön egy jtechlog-wait4signal-1.0-SNAPSHOT-bin.zip és egy 
jtechlog-wait4signal-1.0-SNAPSHOT-tar.gz állomány. Valamelyik lib könyvtárában lévő két jar fájlt kell felhasználni.

A következő parancs kiadásával lehet az agent-et aktiválni:

    java -javaagent:jtechlog-wait4signal-1.0-SNAPSHOT.jar=entryPoint=java2d.Java2Demo.main -jar Java2Demo.jar

Ekkor a konzolon vár egy Enter lenyomásáig, vagy 5 másodperc múlva mindenképp lefuttatja az alkalmazást.

A következő parancs kiadásával lehet JMX-en értesíteni:

    java -javaagent:jtechlog-wait4signal-1.0-SNAPSHOT.jar=entryPoint=java2d.Java2Demo.main,mode=JMX,timeout=30 -jar Java2Demo.jar  

Ekkor a <code>jtechlog/SignalMBean signal()</code> operációjával lehet a futtatást továbbengedni, vagy 30
másodperc múltán timeout.

Felhasznált technológiák: Javassist 3.15, Maven 3.0.3

viczian.istvan a gmail-en