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
func_not PROTO :DWORD
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
func_not PROC p_a:DWORD
L5:
	mov eax,p_a
	push eax
	pop eax
	cmp eax,0
	je L7
	push dword ptr 0
	jmp L8
L7:
	push dword ptr 1
L8:
	pop ebx
	ret
L6:
func_not ENDP
main PROC
	push ebp
	mov ebp,esp
	sub esp,8
L9:
	push dword ptr 12
	pop eax
	mov [ebp-4],eax
	push edx
	mov eax,[ebp-4]
	push eax
	push dword ptr 124
	pop edx
	pop eax
	cmp eax,edx
	pop edx
	jl L11
	push dword ptr 0
	jmp L12
L11:
	push dword ptr 1
L12:
	pop eax
	cmp eax,0
	je L13
	push dword ptr 49
	jmp L14
L13:
	push dword ptr 50
L14:
	pop eax
	mov [ebp-8],eax
	push edx
	mov eax,[ebp-8]
	push eax
	push edx
	push edx
	push ebx
	push ecx
	push edx
	push dword ptr 214
	mov eax,[ebp-4]
	push eax
	call func_add
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
	push dword ptr 51
	push dword ptr 255
	call func_sub
	mov eax,ebx
	pop edx
	pop ecx
	pop ebx
	push eax
	pop edx
	pop eax
	sub eax,edx
	pop edx
	push eax
	push ebx
	push ecx
	push edx
	push dword ptr 0
	call func_not
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
L10:
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
