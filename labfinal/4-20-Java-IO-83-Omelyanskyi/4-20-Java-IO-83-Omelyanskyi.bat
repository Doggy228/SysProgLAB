@java -jar 4-20-Java-IO-83-Omelyanskyi.jar 4-20-Java-IO-83-Omelyanskyi.py
@if %errorlevel% neq 0 exit /B %errorlevel%
@echo.
@\masm32\bin\ml /c /coff /Cp 4-20-Java-IO-83-Omelyanskyi.asm
@if %errorlevel% neq 0 exit /B %errorlevel%
@\masm32\bin\link /subsystem:console 4-20-Java-IO-83-Omelyanskyi.obj
@if %errorlevel% neq 0 exit /B %errorlevel%
@echo.
@echo ===========================================================
@echo === Execution %1.exe
@echo ===========================================================
@4-20-Java-IO-83-Omelyanskyi.exe
@if %errorlevel% neq 0 exit /B %errorlevel%
@echo.
@echo.
@echo ===========================================================
@echo === Successful completion:
@echo ===========================================================
@echo     source file: 4-20-Java-IO-83-Omelyanskyi.py
@echo     asm file:    4-20-Java-IO-83-Omelyanskyi.asm
@echo     exe file:    4-20-Java-IO-83-Omelyanskyi.exe