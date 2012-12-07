reset; set term win
load 'gen3008.log'
set title "analysis #3008"
set xlabel "Q (Angs.^-1)"
set ylabel "dS/dO(Q)"
set log y ; p  'gen300801.fit' u 1:2:($2*10**(-rappm($6/$2)/log(10))):($2*10**(rappm($6/$2)/log(10))) t 'Ne=  1'  w err, 'gen300801.pq' u 1:(k01*$2+b01) notit w l lt 3 lw 3; pa -1 
