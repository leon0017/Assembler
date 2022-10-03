bits 16
org 0x7c00

start:
    mov sp, 0x7c00
    call clearScreen

    xor bx, bx
    xor dx, dx
    mov dh, -1
    mov bp, testString
    mov cx, 12
loop:
    inc bx
    inc dh
    call printStr
    cmp bx, 0xf
    je done
    jmp loop

# bp = string
# cx = string length
# bx = color
# dh = row
# dl = column
printStr:
    mov ah, 0x13
    mov al, 1
    int 0x10
    ret

clearScreen:
    mov ax, 0x3
    int 0x10
    ret

done:
    hlt
    jmp done

testString:
    db "Hello World!"

setpos 510
dw 0xaa55