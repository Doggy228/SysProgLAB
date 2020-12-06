@java -jar 1-20-Java-IO-83-Omelyansky.jar 1-20-Java-IO-83-Omelyansky.py
@if %errorlevel% neq 0 exit /B %errorlevel%
@echo.
@\masm32\bin\ml /c /coff /Cp 1-20-Java-IO-83-Omelyansky.asm
@if %errorlevel% neq 0 exit /B %errorlevel%
@\masm32\bin\link /subsystem:console 1-20-Java-IO-83-Omelyansky.obj
@if %errorlevel% neq 0 exit /B %errorlevel%
@echo.
@echo ===========================================================
@echo === Execution 1-20-Java-IO-83-Omelyansky.exe
@echo ===========================================================
@1-20-Java-IO-83-Omelyansky.exe
@if %errorlevel% neq 0 exit /B %errorlevel%
@echo.
@echo.
@echo ===========================================================
@echo === Successful completion:
@echo ===========================================================
@echo     source file: 1-20-Java-IO-83-Omelyansky.py
@echo     asm file:    1-20-Java-IO-83-Omelyansky.asm
@echo     exe file:    1-20-Java-IO-83-Omelyansky.exe