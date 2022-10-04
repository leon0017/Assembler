bits 64

test:
	call test2
    ret

times 10 nop

test2:
	call test
	ret
