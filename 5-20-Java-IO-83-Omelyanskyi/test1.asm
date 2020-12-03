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
func_add PROC uses a:DWORD,b:DWORD
L1:
	push edx
	mov eax,a
	push eax
	mov eax,b
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
func_sub PROC uses a:DWORD,b:DWORD,c:DWORD
L3:
	push edx
	mov eax,a
	push eax
	push edx
	mov eax,b
	push eax
	mov eax,c
	push eax
	pop edx
	pop eax
	sub eax,edx
	pop edx
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
	push ebp
	mov ebp,esp
	sub esp,8
L5:
	push dword ptr 5
	pop eax
	mov [ebp-4],eax
	push dword ptr 1
	pop eax
	mov [ebp-8],eax
	push edx
	mov eax,[ebp-8]
	push eax
	push edx
	push ebx
	push ecx
	push edx
	push dword ptr 4
	mov eax,[ebp-4]
	push eax
	call func_add
	add esp,8
	pop edx
	pop ecx
	pop ebx
	push eax
	push ebx
	push ecx
	push edx
	mov eax,[ebp-4]
	push eax
	push dword ptr 1
	push dword ptr 8
	call func_sub
	add esp,12
	pop edx
	pop ecx
	pop ebx
	push eax
	pop edx
	pop eax
	sub eax,edx
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
