<CsoundSynthesizer>
<CsOptions>
-g -odac /Users/clu/Documents/workspace/Csoundo/examples/playMessage/temp.csd
</CsOptions>
<CsInstruments>
sr = 44100
kr = 44100
ksmps = 1
nchnls = 2
0dbfs = 1

; Shape of cycling envelope
gi_env ftgen 0, 0, 32768, 7, 0, 2048, 1, 30720, 0

instr M
    iamp = p4    ; Amplitude
    iratio = p5  ; Frequency ratio
    icycle = p6  ; Rate of cycling envelope
    
    iamp_mod = ((iratio + 1) / iratio) * 0.5
    ifreq = 440 * 2 ^ (-9 / 12) * iratio
    
    ; Cycling envelope
    k2 expseg 0.0625, 15, 0.0625, 113, 3.75, 113, 0.03125, 15, 0.0625
    k1 phasor icycle * k2
    k1 tablei ftlen(gi_env) * k1, gi_env, 0, 0, 1

    ; Random panner
    krand randi 0.5, icycle * 5, rnd(1), 1, 0.5

    ; Sine wave oscillator
    a1 oscils iamp * iamp_mod, ifreq, 0, 2
    a1 = a1 * k1
    
    ; Output to chn bus
    chnmix a1 * sqrt(krand), "left"
    chnmix a1 * (sqrt(1 - krand)), "right"
endin

instr Mixer
    iamp = p4  ; Amplitude
    
    aleft chnget "left"
    aright chnget "right"    
    aenv linseg iamp, 240, iamp, 16, 0
 
    outs aleft * aenv, aright * aenv
    
    chnclear "left"
    chnclear "right"
endin

</CsInstruments>
<CsScore>
i "M" 0 256 1       1       1.24
i "M" 0 .   .       1.0666  1.23
i "M" 0 .   .       1.125   1.22
i "M" 0 .   .       1.14285 1.21
i "M" 0 .   .       1.23076 1.20
i "M" 0 .   .       1.28571 1.19
i "M" 0 .   .       1.333   1.18
i "M" 0 .   .       1.4545  1.17
i "M" 0 .   .       1.5     1.16
i "M" 0 .   .       1.6     1.15
i "M" 0 .   .       1.777   1.14
i "M" 0 .   .       1.8     1.13
i "M" 0 .   .       2       1.12
i "M" 0 .   .       2.25    1.10
i "M" 0 .   .       2.28571 1.09
i "M" 0 .   .       2.666   1.08
i "M" 0 .   .       3       1.07
i "M" 0 .   .       3.2     1.06
i "M" 0 .   .       4       1.05
i "M" 0 .   .       4.5     1.04
i "M" 0 .   .       5.333   1.03
i "M" 0 .   .       8       1.02
i "M" 0 .   [1 / 3] 9       1.01
i "M" 0 .   [1 / 6] 16      1.00

i "Mixer" 0 256 0.1058362578313075

e 256

</CsScore>
</CsoundSynthesizer>
