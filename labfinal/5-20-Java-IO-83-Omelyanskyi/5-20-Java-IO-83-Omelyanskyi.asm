.586
.model flat, stdcall
option casemap:none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\masm32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib
NumbToStr PROTO :DWORD,:DWORD
func_a PROTO
func_b PROTO :DWORD,:DWORD
main PROTO
.data
	buff db 11 dup(?)
.code
start:
	invoke main
	invoke NumbToStr, ebx, ADDR buff
	invoke StdOut,eax
	invoke ExitProcess,0
func_a PROC
L1:
	push dword ptr 10
	pop ebx
	ret
L2:
func_a ENDP
func_b PROC p_a:DWORD,p_b:DWORD
L3:
	push edx
	push edx
	push ebx
	push ecx
	push edx
	call func_a
	mov eax,ebx
	pop edx
	pop ecx
	pop ebx
	push eax
	mov eax,p_a
	push eax
	pop edx
	pop eax
	add eax,edx
	pop edx
	push eax
	mov eax,p_b
	push eax
	pop edx
	pop eax
	sub eax,edx
	pop edx
	push eax
	pop ebx
	ret
L4:
func_b ENDP
main PROC
	push ebp
	mov ebp,esp
	sub esp,8
L5:
	push ebx
	push ecx
	push edx
	call func_a
	mov eax,ebx
	pop edx
	pop ecx
	pop ebx
	push eax
	pop eax
	mov [ebp-4],eax
	push dword ptr 20
	pop eax
	mov [ebp-8],eax
	push edx
	mov eax,[ebp-8]
	push eax
	push edx
	push ebx
	push ecx
	push edx
	call func_a
	mov eax,ebx
	pop edx
	pop ecx
	pop ebx
	push eax
	push ebx
	push ecx
	push edx
	mov eax,[ebp-4]
	push eax
	mov eax,[ebp-8]
	push eax
	call func_b
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
	pop edx
	pop eax
	add eax,edx
	pop edx
	push eax
	pop eax
	mov [ebp-8],eax
	mov eax,[ebp-8]
	push eax
	pop ebx
	mov esp,ebp
	pop ebp
	ret
L6:
	mov esp,ebp
	pop ebp
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
