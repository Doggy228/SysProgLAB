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
	push ebp
	mov ebp,esp
	sub esp,8
L1:
	push dword ptr 55
	pop eax
	mov [ebp-4],eax
	push edx
	mov eax,[ebp-4]
	push eax
	push dword ptr 54
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
	pop eax
	cmp eax,0
	je L5
	push dword ptr 3
	jmp L6
L5:
	push edx
	mov eax,[ebp-4]
	push eax
	push dword ptr 52
	pop edx
	pop eax
	add eax,edx
	pop edx
	push eax
L6:
	pop eax
	mov [ebp-8],eax
	push edx
	push dword ptr 100
	mov eax,[ebp-8]
	push eax
	pop edx
	pop eax
	cmp eax,edx
	pop edx
	jl L7
	push dword ptr 0
	jmp L8
L7:
	push dword ptr 1
L8:
	pop eax
	cmp eax,0
	je L9
	push edx
	mov eax,[ebp-8]
	push eax
	push dword ptr 47
	pop edx
	pop eax
	sub eax,edx
	pop edx
	push eax
	jmp L10
L9:
	mov eax,[ebp-8]
	push eax
	pop eax
	cmp eax,0
	je L11
	push dword ptr 0
	jmp L12
L11:
	push dword ptr 1
L12:
L10:
	pop eax
	mov [ebp-8],eax
	mov eax,[ebp-8]
	push eax
	pop ebx
	mov esp,ebp
	pop ebp
	ret
L2:
	mov esp,ebp
	pop ebp
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
