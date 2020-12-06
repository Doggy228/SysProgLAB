@echo ===========================================================
@echo 6-20-Java-IO-83-Omelyanskyi.py
@echo ===========================================================
@type 6-20-Java-IO-83-Omelyanskyi.py
@echo.
@echo ===========================================================
@java -jar 6-20-Java-IO-83-Omelyanskyi.jar 6-20-Java-IO-83-Omelyanskyi.py
@if %errorlevel% neq 0 exit /B %errorlevel%
@echo ===========================================================
@echo 6-20-Java-IO-83-Omelyanskyi.asm
@echo ===========================================================
@type 6-20-Java-IO-83-Omelyanskyi.asm
@echo.
@echo ===========================================================
@\masm32\bin\ml /c /coff /Cp 6-20-Java-IO-83-Omelyanskyi.asm
@if %errorlevel% neq 0 exit /B %errorlevel%
@\masm32\bin\link /subsystem:console 6-20-Java-IO-83-Omelyanskyi.obj
@if %errorlevel% neq 0 exit /B %errorlevel%
@echo.
@echo ===========================================================
@echo === Execution 6-20-Java-IO-83-Omelyanskyi.exe
@echo ===========================================================
@6-20-Java-IO-83-Omelyanskyi.exe
@if %errorlevel% neq 0 exit /B %errorlevel%
@echo.
