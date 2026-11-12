@echo off
if not defined JAVA_HOME if exist "C:\Program Files\Android\Android Studio\jbr\bin\java.exe" set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
if not defined JAVA_HOME (
    echo No se encontro Java. Instala Android Studio o define JAVA_HOME.
    exit /b 1
)
echo Iniciando usuarios-service en http://localhost:8082 ... (para detenerlo: Ctrl+C)
cd /d "%~dp0usuarios-service"
call .\mvnw.cmd spring-boot:run
