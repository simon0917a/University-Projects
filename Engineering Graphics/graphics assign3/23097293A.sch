*version 9.1 1662688836
u 61
V? 2
U? 2
L? 2
C? 2
R? 2
@libraries
@analysis
.TRAN 1 0 0 0
+0 20ns
+1 10ms
+3 0.01us
@targets
@attributes
@translators
a 0 u 13 0 0 0 hln 100 PCBOARDS=PCB
a 0 u 13 0 0 0 hln 100 PSPICE=PSPICE
a 0 u 13 0 0 0 hln 100 XILINX=XILINX
@setup
unconnectedPins 0
connectViaLabel 0
connectViaLocalLabels 0
NoStim4ExtIFPortsWarnings 1
AutoGenStim4ExtIFPorts 1
@index
pageloc 1 0 2424 
@status
n 0 124:10:14:11:12:19;1731553939 e 
s 2832 124:10:14:13:41:04;1731562864 e 
c 124:10:14:11:12:04;1731553924
*page 1 0 970 720 iA
@ports
port 7 AGND 460 360 h
@parts
part 4 L 360 280 h
a 0 ap 9 0 13 2 hln 100 REFDES=L1
a 0 sp 0 0 0 10 hlb 100 PART=L
a 0 s 0:13 0 0 0 hln 100 PKGTYPE=L2012C
a 0 s 0:13 0 0 0 hln 100 GATE=
a 0 a 0:13 0 0 0 hln 100 PKGREF=L1
a 0 u 13 0 13 19 hln 100 VALUE=3m
part 5 C 380 320 h
a 0 u 13 0 9 23 hln 100 VALUE=0.21u
a 0 sp 0 0 0 10 hlb 100 PART=C
a 0 s 0:13 0 0 0 hln 100 PKGTYPE=CK05
a 0 s 0:13 0 0 0 hln 100 GATE=
a 0 a 0:13 0 0 0 hln 100 PKGREF=C1
a 0 ap 9 0 11 2 hln 100 REFDES=C1
part 6 R 460 350 v
a 0 u 13 0 23 1 hln 100 VALUE=0.1k
a 0 sp 0 0 0 10 hlb 100 PART=R
a 0 s 0:13 0 0 0 hln 100 PKGTYPE=RC05
a 0 s 0:13 0 0 0 hln 100 GATE=
a 0 a 0:13 0 0 0 hln 100 PKGREF=R1
a 0 ap 9 0 23 30 hln 100 REFDES=R1
part 2 VSIN 280 310 h
a 0 a 0:13 0 0 0 hln 100 PKGREF=V1
a 1 ap 9 0 20 10 hcn 100 REFDES=V1
a 1 u 0 0 0 0 hcn 100 VAMPL=10
a 1 u 0 0 0 0 hcn 100 FREQ=5.3k
a 1 u 0 0 0 0 hcn 100 VOFF=0
part 3 Sw_tClose 310 290 h
a 0 u 0 0 0 50 hln 100 Rclosed=10
a 0 sp 0 0 0 24 hln 100 PART=Sw_tClose
a 0 s 0:13 0 0 0 hln 100 PKGTYPE=
a 0 a 0:13 0 0 0 hln 100 PKGREF=U1
a 0 ap 9 0 0 20 hln 100 REFDES=U1
part 1 titleblk 970 720 h
a 1 s 13 0 350 10 hcn 100 PAGESIZE=A
a 1 s 13 0 180 60 hcn 100 PAGETITLE=
a 1 s 13 0 340 95 hrn 100 PAGECOUNT=1
a 1 s 13 0 300 95 hrn 100 PAGENO=1
@conn
w 15
a 0 up 0:33 0 0 0 hln 100 V=
s 350 300 360 300 14
s 360 300 360 280 16
s 360 300 360 320 18
s 360 320 380 320 20
a 0 up 33 0 370 319 hct 100 V=
w 23
a 0 up 0:33 0 0 0 hln 100 V=
s 420 280 420 300 22
s 420 320 410 320 24
s 420 300 420 320 28
s 420 300 460 300 26
a 0 up 33 0 440 299 hct 100 V=
s 460 300 460 310 29
w 34
s 460 360 280 360 33
s 280 360 280 350 35
s 460 350 460 360 37
w 45
a 0 up 0:33 0 0 0 hln 100 V=
s 280 310 280 300 44
s 280 300 310 300 46
a 0 up 33 0 295 299 hct 100 V=
@junction
j 350 300
+ p 3 2
+ w 15
j 310 300
+ p 3 1
+ w 45
j 280 350
+ p 2 -
+ w 34
j 280 310
+ p 2 +
+ w 45
j 460 310
+ p 6 2
+ w 23
j 460 350
+ p 6 1
+ w 34
j 380 320
+ p 5 1
+ w 15
j 410 320
+ p 5 2
+ w 23
j 360 280
+ p 4 1
+ w 15
j 420 280
+ p 4 2
+ w 23
j 460 360
+ s 7
+ w 34
j 360 300
+ w 15
+ w 15
j 420 300
+ w 23
+ w 23
@attributes
a 0 s 0:13 0 0 0 hln 100 PAGETITLE=
a 0 s 0:13 0 0 0 hln 100 PAGENO=1
a 0 s 0:13 0 0 0 hln 100 PAGESIZE=A
a 0 s 0:13 0 0 0 hln 100 PAGECOUNT=1
@graphics
t 48 t 5 400 386 520 400 0 22
    LI MING CHUN SIMON
t 42 t 5 460 406 500 420 0 10
      102D
t 41 t 5 440 396 510 410 0 14
     23097293A
