<CsoundSynthesizer>
<CsInstruments>
sr = 44100
kr = 1470
ksmps = 30
nchnls = 1
0dbfs = 1

gi_sin ftgen 1, 0, 8192, 10, 1

chn_k "slider_1", 1
chn_k "slider_2", 1

instr 1
    idur = p3
    iamp = p4
    
    k1 chnget "slider_1"    
    k2 chnget "slider_2"

    a1 foscil iamp, 440, 1, k1, k2, gi_sin
    
    out a1
endin

</CsInstruments>
<CsScore>
i 1 0 [60 * 60 * 24] 0.707
</CsScore>
</CsoundSynthesizer>
