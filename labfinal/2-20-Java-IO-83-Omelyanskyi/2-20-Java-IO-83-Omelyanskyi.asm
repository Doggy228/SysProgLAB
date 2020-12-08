.586
.model flat, stdcall
option casemap:none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\masm32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib
NumbToStr PROTO :DWORD,:DWORD
main PROTO
.data
	buff db 11 dup(?)
.code
start:
	invoke main
	invoke NumbToStr, ebx, ADDR buff
	invoke StdOut,eax
	invoke ExitProcess,0
main PROC
	push edx
	push edx
	push edx
	push dword ptr 47
	push dword ptr 66
	pop edx
	pop eax
	add eax,edx
	pop edx
	push eax
	push edx
	push dword ptr 25
	push dword ptr 3
	pop edx
	pop eax
	add eax,edx
	pop edx
	push eax
	pop edx
	pop eax
	add eax,edx
	pop edx
	push eax
	push dword ptr 12
	pop edx
	pop eax
	add eax,edx
	pop edx
	push eax
	pop ebx
	ret
main ENDP
NumbToStr PROC uses ebx x:DWORD,buffer:DWORD
	mov eax,x
	test eax,80000000h
	jz LL2
	push dword ptr 2Dh
	neg eax
	jmp LL3
LL2:
	push dword ptr 20h
LL3:
	mov ecx,buffer
	mov ebx,10
	add ecx,ebx
LL1:
	xor edx,edx
	div ebx
	add edx,48
	mov BYTE PTR [ecx],dl
	dec ecx
	test eax,eax
	jnz LL1
	pop edx
	mov BYTE PTR [ecx],dl
	mov eax,ecx
	ret
NumbToStr ENDP
end start
