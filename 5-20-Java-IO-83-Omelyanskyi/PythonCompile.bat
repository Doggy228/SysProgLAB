@rem ************************************************************
@rem ***  PythonCompile by Omelyanskyi Anton, IO-83
@rem ***  Usage: PythonCompile <file_name_without_extension>
@rem ************************************************************

@java -jar ../build/libs/io8322-sysprog-lab-1.0.0.jar %1.py
@if %errorlevel% neq 0 exit /B %errorlevel%
@echo.
@\masm32\bin\ml /c /coff /Cp %1.asm
@if %errorlevel% neq 0 exit /B %errorlevel%
@\masm32\bin\link /subsystem:console %1.obj
@if %errorlevel% neq 0 exit /B %errorlevel%
@echo.
@echo ===========================================================
@echo === Execution %1.exe
@echo ===========================================================
@%1.exe
@if %errorlevel% neq 0 exit /B %errorlevel%
@echo.
@echo.
@echo ===========================================================
@echo === Successful completion:
@echo ===========================================================
@echo     source file: %1.py
@echo     asm file:    %1.asm
@echo     exe file:    %1.exe