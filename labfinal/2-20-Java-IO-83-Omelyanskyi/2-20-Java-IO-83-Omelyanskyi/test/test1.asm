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
	push dword ptr 12345
	pop eax
	cmp eax,0
	je L1
	push dword ptr 0
	jmp L2
L1:
	push dword ptr 1
L2:
	pop ebx
	ret
main ENDP
NumbToStr PROC uses ebx x:DWORD,buffer:DWORD
	mov ecx,buffer
	mov eax,x
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
	inc ecx
	mov eax,ecx
	ret
NumbToStr ENDP
end start
