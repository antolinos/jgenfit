reset; set term win
load 'gensi07.log'
set title "analysis #si07"
set xlabel "Q (Angs.^-1)"
set ylabel "dS/dO(Q)"
set log y ; p  'gensi0701.fit' u 1:2:($2*10**(-rappm($6/$2)/log(10))):($2*10**(rappm($6/$2)/log(10))) t 'Ne=  1'  w err, 'gensi0701.pq' u 1:(k01*$2+b01) notit w l lt 3 lw 3; pa -1 
