@echo off & setlocal enabledelayedexpansion

echo ==================== Cleaning workspace to build ====================
    call .\gradlew.bat clean --no-daemon

@rem Loop trough everything in the version properties folder
for %%f in (versionProperties\*) do (
    @rem Get the name of the version that is going to be compiled
    set version=%%~nf

    echo ==================== Building !version! ====================
    call .\gradlew.bat build -PmcVer="!version!" --no-daemon
)

endlocal
