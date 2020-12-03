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
L1:
	push edx
	push edx
	push dword ptr 8
	push dword ptr 1
	pop edx
	pop eax
	add eax,edx
	pop edx
	push eax
	push edx
	push edx
	push dword ptr 3
	push dword ptr 2
	pop edx
	pop eax
	sub eax,edx
	pop edx
	push eax
	push dword ptr 1
	pop edx
	pop eax
	sub eax,edx
	pop edx
	push eax
	pop edx
	pop eax
	cmp eax,edx
	pop edx
	jl L3
	push dword ptr 0
	jmp L4
L3:
	push dword ptr 1
L4:
	pop ebx
	ret
L2:
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
