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
	sub esp,4
L1:
	push dword ptr 5
	pop eax
	mov [ebp-4],eax
L3:
	push edx
	mov eax,[ebp-4]
	push eax
	push dword ptr 10
	pop edx
	pop eax
	cmp eax,edx
	pop edx
	jl L5
	push dword ptr 0
	jmp L6
L5:
	push dword ptr 1
L6:
	pop eax
	cmp eax,0
	je L4
L7:
	push edx
	mov eax,[ebp-4]
	push eax
	push dword ptr 3
	pop edx
	pop eax
	add eax,edx
	pop edx
	push eax
	pop eax
	mov [ebp-4],eax
	jmp L3
	push dword ptr 99
	pop eax
	mov [ebp-4],eax
L8:
	jmp L3
L4:
L9:
	push edx
	mov eax,[ebp-4]
	push eax
	push dword ptr 30
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
	je L10
L13:
	push edx
	mov eax,[ebp-4]
	push eax
	push dword ptr 3
	pop edx
	pop eax
	add eax,edx
	pop edx
	push eax
	pop eax
	mov [ebp-4],eax
	jmp L10
	push dword ptr 99
	pop eax
	mov [ebp-4],eax
L14:
	jmp L9
L10:
	mov eax,[ebp-4]
	push eax
	pop eax
	not eax
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
