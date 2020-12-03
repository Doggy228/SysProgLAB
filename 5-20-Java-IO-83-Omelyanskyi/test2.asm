.586
.model flat, stdcall
option casemap:none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\masm32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib
NumbToStr PROTO :DWORD,:DWORD
func_add PROTO :DWORD,:DWORD
func_sub PROTO :DWORD,:DWORD,:DWORD
main PROTO
.data
	buff db 11 dup(?)
.code
start:
	invoke main
	invoke NumbToStr, ebx, ADDR buff
	invoke StdOut,eax
	invoke ExitProcess,0
func_add PROC p_a:DWORD,p_b:DWORD
L1:
	push edx
	mov eax,p_a
	push eax
	mov eax,p_b
	push eax
	pop edx
	pop eax
	add eax,edx
	pop edx
	push eax
	pop ebx
	ret
L2:
func_add ENDP
func_sub PROC p_a:DWORD,p_b:DWORD,p_c:DWORD
L3:
	push edx
	push edx
	mov eax,p_a
	push eax
	mov eax,p_b
	push eax
	pop edx
	pop eax
	sub eax,edx
	pop edx
	push eax
	mov eax,p_c
	push eax
	pop edx
	pop eax
	sub eax,edx
	pop edx
	push eax
	pop ebx
	ret
L4:
func_sub ENDP
main PROC
L5:
	push edx
	mov edx,777
	push ebx
	push ecx
	push edx
	push dword ptr 20
	push dword ptr 10
	call func_add
	mov eax,ebx
	pop edx
	pop ecx
	pop ebx
	push eax
	mov edx,777
	push ebx
	push ecx
	push edx
	push dword ptr 2
	push dword ptr 3
	push dword ptr 8
	call func_sub
	mov eax,ebx
	pop edx
	pop ecx
	pop ebx
	push eax
	pop edx
	pop eax
	add eax,edx
	pop edx
	push eax
	pop ebx
	ret
L6:
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
